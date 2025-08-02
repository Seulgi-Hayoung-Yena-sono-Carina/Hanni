package Eom.blogtest.dto.response;

import Eom.blogtest.domain.Article;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class ArticleListViewResponse {
    private final long id;
    private final String title;
    private final String content;
    private List<String> imagePaths; // ✅ 추가
    public ArticleListViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        // ✅ 이미지가 없으면 빈 리스트 반환
        this.imagePaths = Optional.ofNullable(article.getImages())
                .orElse(List.of())
                .stream()
                .map(img -> img.getImagePath())
                .toList();
    }
}