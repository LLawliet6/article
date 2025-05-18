package com.LS.article.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface AiService {
    String chat(List<ObjectNode> userMessage);
}
