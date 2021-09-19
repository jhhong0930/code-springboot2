package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 리뷰 조회
     * EntityGraph - 특정 기능을 수행할 때만 EAGER 로딩을 하도록 지정
     * FETCH - attributePaths에 명시한 속성은 EAGER, 나머지는 LAZY 처리
     * LOAD - 명시된 속성은 EAGER, 나머지는 엔티티 클래스에 명시되거나 기본 방식으로 처리
     */
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // 회원 삭제를 위한 리뷰 삭제
    // @Query로 update나 delete를 사용하려면 @Modifying 필요
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}
