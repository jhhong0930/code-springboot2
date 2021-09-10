package org.zerock.ex2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex2.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired MemoRepository memoRepository;

    @Test
    @DisplayName("의존성 주입 테스트")
    void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    @DisplayName("등록 작업 테스트")
    void testInsertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample" + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    @DisplayName("조회 작업 테스트")
    void testSelect() {

        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("===================================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    @DisplayName("수정 작업 테스트")
    void testUpdate() {

        Memo memo = Memo.builder()
                .mno(100L)
                .memoText("Updated Text")
                .build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    @DisplayName("삭제 작업 테스트")
    void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    @DisplayName("페이징 처리 테스트")
    void testPageDefault() {

        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("=======================================");

        // 총 페이지 개수
        System.out.println("Total Pages: " + result.getTotalPages());
        // 전체 게시글 개수
        System.out.println("Total Count: " + result.getTotalElements());
        // 현재 페이지 번호 0부터 시작
        System.out.println("Page Number: " + result.getNumber());
        // 페이지당 데이터 개수
        System.out.println("Page Size: " + result.getSize());
        // 다음 페이지 존재 여부
        System.out.println("has next page?: " + result.hasNext());
        // 시작 페이지(0) 여부
        System.out.println("first page?: " + result.isFirst());

        System.out.println("=======================================");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    @DisplayName("정렬 조건 테스트")
    void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

}