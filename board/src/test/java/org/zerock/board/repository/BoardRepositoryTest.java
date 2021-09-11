package org.zerock.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository repository;

    @Test
    @DisplayName("테스트 데이터 추가")
    void insertBoard() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title" + i)
                    .content("Content" + i)
                    .writer(member)
                    .build();

            repository.save(board);
        });

    }

}