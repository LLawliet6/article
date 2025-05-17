package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.service.AiService;
import com.LS.article.service.impl.AiServiceImpl;
import com.LS.article.util.WebUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/ai/*")
public class AiController extends BaseController {
    private final AiServiceImpl aiService = new AiServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    // POST /api/ai/chat
    protected void chat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 读取前端 JSON：{ "message":"你好 AI" }
        JsonNode reqJson = mapper.readTree(req.getInputStream());
        String userMsg = reqJson.path("message").asText("");

        // 直接调用 Service
        String answer = aiService.chat(userMsg);

        // 返回 { code:0, msg:"success", data:answer }
        WebUtil.writeJson(resp, Result.ok(answer));
    }
}
