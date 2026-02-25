package com.example.knowledge.ai;

import jakarta.validation.constraints.NotBlank;

public class AiSummaryRequest {

    @NotBlank
    private String content;

    public AiSummaryRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

