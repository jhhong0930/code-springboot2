package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    /*
    - 키 생성 전략
    - AUTO(default) - JPA 구현체(스프링 부트에서는 Hibernate)가 생성 방식을 결정
    - IDENTITY - 사용하는 데이터베이스가 키 생성을 결정, MySQL의 경우 auto increment
    - SEQUENCE - 데이터베이스의 sequence를 이용해서 키를 생성, @SequenceGenerator와 같이 사용
    - TABLE - 키 생성 전용 테이블을 생성하여 키 생성, @TableGenerator와 함께 사용
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
