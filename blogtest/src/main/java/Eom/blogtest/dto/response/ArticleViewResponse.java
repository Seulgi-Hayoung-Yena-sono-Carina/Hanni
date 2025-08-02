package Eom.blogtest.dto.response;

import Eom.blogtest.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String author;
    private List<String> imagePaths; // ✅ 추가

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.author = article.getAuthor();

        // ✅ 이미지 경로 리스트를 초기화 (없으면 빈 리스트 반환)
        this.imagePaths = Optional.ofNullable(article.getImages())
                .orElse(List.of())
                .stream()
                .map(img -> img.getImagePath())
                .toList();
    }
}
