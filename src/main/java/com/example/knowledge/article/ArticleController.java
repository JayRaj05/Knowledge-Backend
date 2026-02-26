package com.example.knowledge.article;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin
public class    ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public Page<ArticleResponse> listArticles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ArticleCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.search(search, category, page, size);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable Long id) {
        return articleService.getById(id);
    }

    @GetMapping("/me")
    public Page<ArticleResponse> myArticles(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.listByAuthorEmail(principal.getUsername(), page, size);
    }

    @PostMapping
    public ArticleResponse createArticle(
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return articleService.createArticle(request, principal.getUsername());
    }

    @PutMapping("/{id}")
    public ArticleResponse updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return articleService.updateArticle(id, request, principal.getUsername());
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        articleService.deleteArticle(id, principal.getUsername());
    }
}

