package com.LS.article.service.impl;

import com.LS.article.service.AiService;
import com.LS.article.util.AiConfig;
import com.LS.article.util.MD5Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AiServiceImpl implements AiService{

    // Redis 连接池
    private static final JedisPool jedisPool = new JedisPool(
            new JedisPoolConfig(),
            "192.168.40.133",
            6379,
            2000,
            "root",
            2
    );
    private static final int CACHE_TTL_SECONDS = 6 * 3600; // 缓存6小时

    // HTTP 客户端 & JSON 解析
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiUrl = AiConfig.get("silicon-flow.api.url");
    private final String apiKey = AiConfig.get("silicon-flow.api.key");

    /**
     * @param messages  本次请求要传给 AI 的上下文消息列表
     * @param userId    登录用户的唯一 ID，用于缓存隔离
     */
    public String chat(List<ObjectNode> messages, Long userId) {
        // 1. 序列化上下文并生成缓存 Key
        String messagesJson;
        try {
            messagesJson = mapper.writeValueAsString(messages);
        } catch (IOException e) {
            throw new RuntimeException("序列化消息列表异常", e);
        }
        String hash     = MD5Util.encrypt(messagesJson);
        String redisKey = String.format("ai:chat:%s:%s", userId, hash);

        // 2. 尝试从 Redis 缓存命中
        try (Jedis jedis = jedisPool.getResource()) {
            String cached = jedis.get(redisKey);
            if (cached != null) {
                return cached;
            }
        }

        // 3. 缓存未命中，构造请求体
        ObjectNode body = mapper.createObjectNode()
                .put("model", "deepseek-ai/DeepSeek-V3")
                .put("stream", false)
                .put("max_tokens", 1024)
                .put("enable_thinking", true)
                .put("temperature", 0.7)
                .put("top_p", 0.7)
                .put("top_k", 50)
                .put("frequency_penalty", 0.5)
                .put("n", 1)
                .set("stop", mapper.createArrayNode());

        ArrayNode arr = mapper.createArrayNode();
        for (ObjectNode msg : messages) {
            arr.add(msg);
        }
        body.set("messages", arr);

        // 4. 调用外部 AI 接口
        String answer;
        try {
            RequestBody reqBody = RequestBody.create(
                    mapper.writeValueAsString(body),
                    MediaType.get("application/json")
            );
            Request req = new Request.Builder()
                    .url(apiUrl)
                    .post(reqBody)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response resp = client.newCall(req).execute()) {
                String respText = resp.body().string();
                if (!resp.isSuccessful()) {
                    throw new RuntimeException("调用 AI 接口失败，HTTP " + resp.code() + "，" + respText);
                }
                JsonNode root = mapper.readTree(respText);
                answer = root.path("choices").get(0)
                        .path("message").path("content")
                        .asText();
            }
        } catch (IOException e) {
            throw new RuntimeException("AI 调用异常", e);
        }

        // 5. 将结果写入 Redis 并设置过期
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(redisKey, CACHE_TTL_SECONDS, answer);
        }

        return answer;
    }
    /**
     * 根据提示和可选的输入图片，调用 SiliconFlow 图片生成接口
     * @param prompt            文本提示
     * @param negativePrompt    反向提示（可传 null）
     * @param initImageBase64   用于图生图的 base64（可传 null）
     * @return 生成的图片 URL 列表
     */
    public List<String> generateImage(String prompt,
                                      String negativePrompt,
                                      String initImageBase64) throws JsonProcessingException {


        ObjectNode body = mapper.createObjectNode()
                .put("model", "Kwai-Kolors/Kolors")
                .put("prompt", prompt)
                .put("negative_prompt", negativePrompt == null ? "" : negativePrompt)
                .put("image_size", "1024x1024")
                .put("batch_size", 1)
                .put("seed", 4999999999L)
                .put("num_inference_steps", 20)
                .put("guidance_scale", 7.5);

        if (initImageBase64 != null) {
            body.put("image", initImageBase64);
        }

        RequestBody reqBody = RequestBody.create(
                mapper.writeValueAsString(body),
                MediaType.get("application/json")
        );
        Request req = new Request.Builder()
                .url("https://api.siliconflow.cn/v1/images/generations")
                .post(reqBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response resp = client.newCall(req).execute()) {
            String json = resp.body().string();
            if (!resp.isSuccessful()) {
                throw new RuntimeException("图片生成接口失败，HTTP " + resp.code() + "，" + json);
            }
            JsonNode root = mapper.readTree(json);
            List<String> urls = new ArrayList<>();
            for (JsonNode imgNode : root.path("images")) {
                urls.add(imgNode.path("url").asText());
            }
            return urls;
        } catch (IOException e) {
            throw new RuntimeException("图片生成异常", e);
        }
    }

}

