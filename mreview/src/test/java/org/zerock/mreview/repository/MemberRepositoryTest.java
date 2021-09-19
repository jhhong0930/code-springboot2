package org.zerock.mreview.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

//    @Test
    @DisplayName("테스트 데이터 추가")
    void insertMembers() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder()
                    .email("r" + i + "@zerock.org")
                    .pw("1111")
                    .nickname("reviewer" + i)
                    .build();

            memberRepository.save(member);
        });
    }

    @Commit
    @Transactional
    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMember() {

        Long mid = 1L; // Member의 mid

        Member member = Member.builder().mid(mid).build();

//        memberRepository.deleteById(mid);
//        reviewRepository.deleteByMember(member);

        // 순서 주의
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }

}