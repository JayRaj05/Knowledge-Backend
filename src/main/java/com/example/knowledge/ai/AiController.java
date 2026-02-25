package com.example.knowledge.ai;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/improve")
    @PreAuthorize("isAuthenticated()")
    public AiImproveResponse improve(@Valid @RequestBody AiImproveRequest request) {
        AiImproveResponse resp = new AiImproveResponse();
        resp.setImprovedContent(aiService.improveContent(request.getContent()));
        resp.setImprovedTitle(aiService.suggestBetterTitle(
                request.getCurrentTitle(), request.getContent()));
        return resp;
    }

    @PostMapping("/summary")
    @PreAuthorize("isAuthenticated()")
    public AiSummaryResponse summary(@Valid @RequestBody AiSummaryRequest request) {
        AiSummaryResponse resp = new AiSummaryResponse();
        resp.setSummary(aiService.generateSummary(request.getContent()));
        return resp;
    }

    @PostMapping("/tags")
    @PreAuthorize("isAuthenticated()")
    public AiTagsResponse tags(@Valid @RequestBody AiSummaryRequest request) {
        AiTagsResponse resp = new AiTagsResponse();
        resp.setTags(aiService.suggestTags(request.getContent()));
        return resp;
    }
}

