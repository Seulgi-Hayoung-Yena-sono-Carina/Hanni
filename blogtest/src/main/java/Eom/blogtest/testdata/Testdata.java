package Eom.blogtest.testdata;

import Eom.blogtest.domain.Article;
import Eom.blogtest.repository.BlogRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Testdata {

    private final BlogRepository blogRepository;

    @PostConstruct //Spring Bean이 생성되고 의존성 주입이 완료된 후 자동으로 실행
    public void init() {
        blogRepository.save(new Article("제목 1", "내용 1","글쓴이 1"));
        blogRepository.save(new Article("제목 2", "내용 2","글쓴이 2"));
        blogRepository.save(new Article("제목 3", "내용 3","글쓴이 3"));
    }
}