package Eom.blogtest.controller;

import Eom.blogtest.domain.Article;
import Eom.blogtest.dto.request.AddArticleRequest;
import Eom.blogtest.dto.request.UpdateArticleRequest;
import Eom.blogtest.dto.response.ArticleResponse;
import Eom.blogtest.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleResponse(savedArticle));
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") Long id){
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id){
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") long id,
                                                         @RequestBody UpdateArticleRequest request){
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok().body(new ArticleResponse(updatedArticle));
    }
}
