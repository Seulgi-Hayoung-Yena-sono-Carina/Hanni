package Eom.blogtest.service;

import Eom.blogtest.domain.Article;
import Eom.blogtest.domain.Image;
import Eom.blogtest.dto.request.AddArticleRequest;
import Eom.blogtest.dto.request.UpdateArticleRequest;
import Eom.blogtest.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request,String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    // ✅ 여러 장 이미지 업로드 지원 메서드
    @Transactional
    public Article saveWithImages(String title, String content, String userName, List<MultipartFile> imageFiles) throws IOException {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .author(userName)
                .build();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String uploadDir = "C:/uploads/blog/";
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();

                    String originalFilename = file.getOriginalFilename();
                    String storeFileName = UUID.randomUUID() + "_" + originalFilename;

                    file.transferTo(new File(uploadDir + storeFileName));

                    Image image = Image.builder()
                            .imagePath("/uploads/blog/" + storeFileName)
                            .imageName(originalFilename)
                            .article(article)
                            .build();

                    article.getImages().add(image);
                }
            }
        }

        return blogRepository.save(article);
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(Long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    @Transactional
    public Article patch(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        authorizeArticleAuthor(article);
        article.patch(Optional.ofNullable(request.getTitle()), Optional.ofNullable(request.getContent()));
        return article;
    }

    private static void authorizeArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("not authorized");
        }
    }
}
