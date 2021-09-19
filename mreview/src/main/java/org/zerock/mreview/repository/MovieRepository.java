package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * 목록 화면 조회 - 영화, 이미지, 리뷰의 수, 평점 평균
     * N+1 문제 발생 - max(mi)
     * 1페이지에 해당하는 10개의 데이터를 가져오는 쿼리 1번과
     * 각 영화의 모든 이미지를 가져오기 위한 10번의 추가적인 쿼리가 실행되어
     * 한 페이지를 볼 때마다 11번의 쿼리를 실행하기 때문에 성능저하의 원인이 된다
     * -> 중간의 이미지를 1개로 줄여서 처리
     * - max(mi) -> mi
     */
    @Query("select m, mi, (coalesce(r.grade, 0)), count(distinct r) " +
            " from Movie m " +
            " left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review r on r.movie = m " +
            " group by m")
    Page<Object[]> getListPage(Pageable pageable);

    // 특정 영화 조회
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(@Param("mno") Long mno);

}
