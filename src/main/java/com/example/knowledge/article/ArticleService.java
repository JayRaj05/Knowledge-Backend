package com.example.knowledge.article;

import com.example.knowledge.ai.AiService;
import com.example.knowledge.user.User;
import com.example.knowledge.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ArticleService {

    private final ArticleRepository articleRepo;
    private final UserRepository userRepo;
    private final AiService aiService;

    public ArticleService(ArticleRepository articleRepo,
                          UserRepository userRepo,
                          AiService aiService) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
        this.aiService = aiService;
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request, String userEmail) {
        User author = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Instant now = Instant.now();
        String summary = aiService.generateSummary(request.getContent());

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory() == null ? ArticleCategory.OTHER : request.getCategory());
        article.setContent(request.getContent());
        article.setSummary(summary);
        article.setTags(request.getTags());
        article.setAuthor(author);
        article.setCreatedAt(now);
        article.setUpdatedAt(now);

        articleRepo.save(article);
        return toResponse(article);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleRequest request, String userEmail) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new SecurityException("You are not the author of this article");
        }

        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory() == null ? ArticleCategory.OTHER : request.getCategory());
        article.setContent(request.getContent());
        article.setTags(request.getTags());
        article.setUpdatedAt(Instant.now());
        article.setSummary(aiService.generateSummary(request.getContent()));

        return toResponse(article);
    }

    public void deleteArticle(Long id, String userEmail) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new SecurityException("You are not the author of this article");
        }
        articleRepo.delete(article);
    }

    public ArticleResponse getById(Long id) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        return toResponse(article);
    }

    public Page<ArticleResponse> search(String search, ArticleCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> result = articleRepo.search(
                (search == null || search.isBlank()) ? null : search,
                category,
                pageable
        );
        return result.map(this::toResponse);
    }

    public Page<ArticleResponse> listByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Article> spec = (root, query, cb) ->
                cb.equal(root.get("author").get("id"), authorId);
        Page<Article> pageResult = articleRepo.findAll(spec, pageable);
        return pageResult.map(this::toResponse);
    }

    public Page<ArticleResponse> listByAuthorEmail(String email, int page, int size) {
        User author = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return listByAuthor(author.getId(), page, size);
    }

    private ArticleResponse toResponse(Article article) {
        ArticleResponse resp = new ArticleResponse();
        resp.setId(article.getId());
        resp.setTitle(article.getTitle());
        resp.setSummary(article.getSummary());
        resp.setCategory(article.getCategory().name());
        resp.setContent(article.getContent());
        resp.setTags(article.getTags());
        resp.setAuthorUsername(article.getAuthor().getUsername());
        resp.setAuthorId(article.getAuthor().getId());
        resp.setCreatedAt(article.getCreatedAt());
        resp.setUpdatedAt(article.getUpdatedAt());
        return resp;
    }
}

