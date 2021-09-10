package org.zerock.guestbook.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.entity.Guestbook;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    @DisplayName("셈플 데이터 추가 테스트")
    void insertDummies() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title" + i)
                    .content("Content" + i)
                    .writer("user" + (i % 10))
                    .build();

            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    @DisplayName("수정 시간 테스트")
    void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()) {

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title");
            guestbook.changeContent("Changed Content");

            guestbookRepository.save(guestbook);
        }

    }

}