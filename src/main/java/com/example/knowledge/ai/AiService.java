package com.example.knowledge.ai;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AiService {

    private final RestTemplate restTemplate;

    private static  final String OPENAPI_URI = "https://api.openai.com/v1/chat/completions";

    @Value("${app.openai.api-key}")
    private String apikey;


    public String improveContent(String content) {
        try{
            if (content == null) {
                return "";
            }
            String prompt = " Improve the following content : \n" + content ;
            return callOpenAI(prompt);
        } catch (Exception e) {
            return content.trim()
                    .replaceAll("\\s+", " ")
                    + " (Improved by AI mock)";
        }

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

