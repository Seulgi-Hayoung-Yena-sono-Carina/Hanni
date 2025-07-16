package Eom.blogtest.dto.response;

import Eom.blogtest.domain.Article;
import lombok.Data;

@Data
public class ArticleResponse {
    private final String title;
    private final String content;

    //constructor
    public ArticleResponse(Article article){
        this.title=article.getTitle();
        this.content=article.getContent();
    }
}
