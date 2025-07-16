package Eom.blogtest.service;

import Eom.blogtest.domain.Article;
import Eom.blogtest.dto.request.AddArticleRequest;
import Eom.blogtest.dto.request.UpdateArticleRequest;
import Eom.blogtest.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(long id){

        return blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found: "+id));
    }

    public void delete(Long id){
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(),request.getContent());

        return article;
    }
}
