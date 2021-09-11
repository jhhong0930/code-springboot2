package org.zerock.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService service;

    @Test
    @DisplayName("게시물 등록 테스트")
    void testRegister() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test Title")
                .content("Test Content")
                .writerEmail("user55@aaa.com")
                .build();

        Long bno = service.register(dto);
    }

    @Test
    @DisplayName("게시물 목록 처리 테스트")
    void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = service.getList(pageRequestDTO);

        for (Object boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    @Test
    @DisplayName("게시물 조회 테스트")
    void testGet() {

        Long bno = 100L;

        BoardDTO boardDTO = service.get(bno);

        System.out.println(boardDTO);
    }

    @Test
    @DisplayName("게시물 삭제 테스트(댓글 먼저 삭제)")
    void testRemove() {

        Long bno = 1L;

        service.removeWithReplies(bno);
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    void testModify() {

        BoardDTO dto = BoardDTO.builder()
                .bno(2L)
                .title("변경된 제목")
                .content("변경된 내용")
                .build();

        service.modify(dto);
    }

}