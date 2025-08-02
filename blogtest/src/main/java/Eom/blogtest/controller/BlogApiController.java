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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class BlogApiController {

    private final BlogService blogService;

    // ✅ 여러 장 이미지 업로드 + 게시글 등록
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ArticleResponse> addArticle(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            Principal principal) throws IOException {

        Article savedArticle = blogService.saveWithImages(title, content, principal.getName(), imageFiles);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
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
    @PatchMapping("/{id}")
    public ResponseEntity<ArticleResponse> patchArticle(@PathVariable("id") long id,
                                                        @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.patch(id, request);
        return ResponseEntity.ok().body(new ArticleResponse(updatedArticle));
    }



}
