package org.zerock.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    @Test
    @DisplayName("Board 조회 테스트")
    void testRead1() {

        Optional<Board> result = repository.findById(100L);

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    @Test
    @DisplayName("연관관계가 있는 join 테스트")
    void testGetBoardWithWriter() {

        Object result = repository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println("================================");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    @DisplayName("연관관계가 없는 join 테스트")
    void testGetBoardWithReply() {

        List<Object[]> result = repository.getBoardWithReply(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    @DisplayName("목록 화면에 필요한 JPQL 테스트")
    void testGetBoardWithReplyCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = repository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {

            Object[] arr = (Object[]) row;

            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    @DisplayName("조회 화면에 필요한 JPQL 테스트")
    void testGetBoardByBno() {

        Object result = repository.getBoardByBno(100L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

}