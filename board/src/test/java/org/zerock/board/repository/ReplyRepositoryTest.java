package org.zerock.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository repository;

    @Test
    @DisplayName("테스트 데이터 추가")
    void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            long bno = (long) (Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply" + i)
                    .board(board)
                    .replyer("guest")
                    .build();

            repository.save(reply);
        });

    }

    @Test
    @DisplayName("Reply 조회 테스트")
    void readReply1() {

        Optional<Reply> result = repository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void testGetRepliesByBoardOrderByRno() {

        List<Reply> replyList = repository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());

        replyList.forEach(System.out::println);
    }

}