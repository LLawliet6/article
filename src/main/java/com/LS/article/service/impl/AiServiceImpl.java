package com.LS.article.service.impl;

import com.LS.article.service.AiService;
import com.LS.article.util.AiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class AiServiceImpl implements AiService{


    private final  OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)   // 连接超时
            .writeTimeout(30, TimeUnit.SECONDS)     // 写请求体超时
            .readTimeout(120, TimeUnit.SECONDS)     // 读取响应超时
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiUrl = AiConfig.get("silicon-flow.api.url");
    private final String apiKey = AiConfig.get("silicon-flow.api.key");

    /**
     * 调用 SiliconFlow Chat Completion 接口
     * @param userMessage 前端（或 Postman）传入的用户消息
     * @return AI 返回的内容
     */
    public String chat(List<ObjectNode> messages) {
        try {
            ObjectNode body = mapper.createObjectNode()
                    .put("model", "deepseek-ai/DeepSeek-V3")
                    .put("stream", false)
                    .put("max_tokens", 1024)
                    .put("enable_thinking", true)
                    .put("temperature", 0.7)
                    .put("top_p", 0.7)
                    .put("top_k", 50)
                    .put("frequency_penalty", 0.5)
                    .put("n", 1);

            body.set("stop", mapper.createArrayNode());

            // 加入所有上下文消息
            ArrayNode msgArray = mapper.createArrayNode();
            for (ObjectNode msg : messages) {
                msgArray.add(msg);
            }
            body.set("messages", msgArray);

            RequestBody requestBody = RequestBody.create(
                    mapper.writeValueAsString(body),
                    MediaType.get("application/json")
            );
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response resp = client.newCall(request).execute()) {
                String text = resp.body().string();
                if (!resp.isSuccessful()) {
                    throw new RuntimeException("调用 AI 接口失败，HTTP " + resp.code() + "，" + text);
                }
                JsonNode root = mapper.readTree(text);
                return root
                        .path("choices").get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }
        } catch (Exception e) {
            throw new RuntimeException("AI 调用异常：" + e.getMessage(), e);
        }
    }

}

