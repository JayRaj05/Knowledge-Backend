package com.example.knowledge.ai;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {

    public String improveContent(String content) {
        if (content == null) {
            return "";
        }
        return content.trim()
                .replaceAll("\\s+", " ")
                + " (Improved by AI mock)";
    }

    public String suggestBetterTitle(String currentTitle, String content) {
        if (currentTitle == null || currentTitle.isBlank()) {
            return "Improved Article Title (Mock)";
        }
        return "Improved: " + currentTitle;
    }

    public String generateSummary(String content) {
        if (content == null) {
            return "";
        }
        String plain = content.replaceAll("<[^>]*>", "");
        return plain.length() <= 150 ? plain : plain.substring(0, 147) + "...";
    }

    public List<String> suggestTags(String content) {
        if (content == null) {
            return List.of();
        }
        String lower = content.toLowerCase();
        return Arrays.stream(new String[]{"java", "react", "spring", "mysql", "backend", "frontend", "devops", "ai"})
                .filter(lower::contains)
                .collect(Collectors.toList());
    }
}

