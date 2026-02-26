package com.example.knowledge.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Data
public class AiImproveRequest {

    private String currentTitle;

    @NotBlank
    private String content;

    public AiImproveRequest() {
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

