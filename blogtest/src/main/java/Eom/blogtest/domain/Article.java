package Eom.blogtest.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor

public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",updatable=false)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @Builder
    public Article(String title, String content){
        this.title=title;
        this.content=content;
    }

    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }
}
