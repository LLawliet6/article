package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.service.AiService;
import com.LS.article.service.impl.AiServiceImpl;
import com.LS.article.util.WebUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/api/ai/*")
public class AiController extends BaseController {
    private final AiServiceImpl aiService = new AiServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final int MAX_HISTORY = 20;
    // POST /api/ai/chat
    private final Map<String, List<ObjectNode>> sessionHistory = new ConcurrentHashMap<>();

    protected void chat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonNode reqJson = mapper.readTree(req.getInputStream());
        String userMsg = reqJson.path("message").asText("");
        String sessionId = req.getSession().getId(); // 可替换为登录用户 ID

        // 从 map 中取历史记录，没有就新建
        List<ObjectNode> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());

        // 添加用户消息到历史
        ObjectNode userMsgNode = mapper.createObjectNode()
                .put("role", "user")
                .put("content", userMsg);
        history.add(userMsgNode);
        // 裁剪：如果长度超过上限，就丢弃最早的多余部分
        if (history.size() > MAX_HISTORY) {
            // 只保留最后 MAX_HISTORY 条
            history = history.subList(history.size() - MAX_HISTORY, history.size());
            sessionHistory.put(sessionId, history);
        }

        // 调用 AI
        String answer = aiService.chat(history); // 传历史消息列表

        // 添加 AI 回复到历史
        ObjectNode aiMsgNode = mapper.createObjectNode()
                .put("role", "assistant")
                .put("content", answer);
        history.add(aiMsgNode);

        WebUtil.writeJson(resp, Result.ok(answer));
    }

}
