package org.zerock.mreview.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

//    @Test
    @DisplayName("테스트 데이터 추가")
    void insertMovieReview() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            // Movie 번호
            Long mno = (long) (Math.random() * 100) + 1;

            // Reviewer 번호
            Long mid = ((long)(Math.random() * 100) + 1);

            Member member = Member.builder().mid(mid).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌" + i)
                    .build();

            reviewRepository.save(movieReview);
        });
    }

    @Test
    @DisplayName("특정 영화의 모든 리뷰와 회원의 닉네임 조회 테스트")
    void testGetMovieReviews() {

        Movie movie = Movie.builder().mno(92L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {

            System.out.print(movieReview.getReviewnum());
            System.out.print("\t" + movieReview.getGrade());
            System.out.print("\t" + movieReview.getText());
            System.out.print("\t" + movieReview.getMember().getEmail());
            System.out.println("-------------------------------------");
        });
    }



}