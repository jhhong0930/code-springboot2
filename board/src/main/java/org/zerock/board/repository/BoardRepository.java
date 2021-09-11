package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 한개의 행(Object) 내에 Object[] 로 나온다
    // 엔티티 클래스 내부에 연관관계가 있는 경우에는 join 뒤 on을 사용하지 않는다
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 연관관계가 없는 엔티티 조인 처리시에는 on 사용
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 목록 화면에 필요한 JPQL(게시물 번호/제목/작성시간, 회원 이름/이메일, 댓글 수)
    @Query(value = "select b, w, count(r) " +
            "from Board b " +
            "left join b.writer w " +
            "left join Reply r on r.board = b " +
            "group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    // 조회 화면에서 필요한 JPQL
    @Query("select b, w, count(r) " +
            "from Board b left join b.writer w " +
            "left outer join Reply r on r.board = b " +
            "where b.bno =:bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
