### ORM과 JPA

- ORM(Object Relational Mapping) - 객체지향 패러다임을 관계형 데이터베이스에 보존하는 기술
- 객체지향의 구조가 관계형 데이터베이스와 유사하다
- 관계형 데이터베이스는 테이블 사이의 **관계** 를 통해서 구조적인 데이터를 표현하고, 객체지향에서는 **참조** 를 통해서 어떤 객체가 다른 객체들과 어떤 관계를 맺고 있는지를 표현한다
- 즉 ORM은 객체지향을 자동으로 관계형 데이터베이스에 맞게 처리해 주는 기법이다
- JPA(Java Persistence API)는 ORM을 Java언어에 맞게 사용하는 **스펙** 이다

### Spring Data JPA와 JPA

- 스프링 부트는 JPA의 구현체 중 **Hibernate** 라는 구현체를 이용한다
- Hibernate는 단독으로 프로젝트에 적용이 가능한 독립된 프레임워크이기 때문에 스프링 환경에서도 Hibernate와 연동해서 JPA를 사용할 수 있다
- Spring Data JPA <-> Hibernate <-> JDBC <-> DB

### 엔티티 클래스와 JpaRepository

- 엔티티 클래스 - JPA를 통해서 관리하게 되는 객체를 위한 클래스
- @Entity 엔티티 클래스는 반드시 @Entity 어노테이션을 추가해야 해당 클래스의 인스턴스들이 JPA로 관리되는 엔티티 객체라는 것을 의미한다
- 키 생성 전략
  - AUTO(default) - JPA 구현체(스프링 부트에서는 Hibernate)가 생성 방식을 결정
  - IDENTITY - 사용하는 데이터베이스가 키 생성을 결정, MySQL 등의 경우 auto increment 방식을 이용
  - SEQUENCE - 데이터베이스의 sequence를 이용하여 키를 생성, @SequenceGenerator와 같이 사용
  - TABLE - 키 생성 전용 테이블을 생성해서 키 생성, @TableGenerator와 함께 사용

- Spring Data JPA를 위한 스프링 부트 설정
  - spring.jpa.hibernate.ddl-auto: 프로젝트 실행 시에 자동으로 DDL 생성여부 설정
    - create: 매번 테이블을 새로 생성
    - update: 변경이 필요한 경우는 alter, 테이블이 없는 경우에는 create
  - spirng.jpa.hibernate.format_sql: SQL의 가독성을 높여주는 설정
  - spring.jpa.show_sql: JPA 처리 시에 발생하는 SQL을 보여줄 것인지 설정

### JpaRepository 인터페이스

- Repository <- CrudRepository <- PagingAndSortRepository <- JpaRepository (JpaRepository 상속 구조)

- JpaRepository를 사용하려면 각 인터페이스에 JpaRepository를 상속하는것 만으로 모든 처리가 끝난다

  ```java
  // 인터페이스 선언만으로도 자동으로 스프링 빈으로 등록된다
  public interface xxxRepository extends JpaRepository<클래스명, @Id의 타입> {}
  ```

- JpaRepository를 이용한 CRUD
  - insert - save(엔티티 객체)
  - select - findById(키 타입), getOne(키 타입)
  - update - save(엔티티 객체)
  - delete - deleteById(키 타입), delete(엔티티 객체)
- save()는 JPA의 구현체가 메모리상에서 객체를 비교한 후 존재하지 않으면 insert, 존재하면 update를 동작

### 페이징/정렬 처리

- Pageable 인터페이스 - 페이지 처리에 필요한 정보를 전달하는 용도, 구현체는 PageRequest 클래스를 사용
- PageRequest 클래스의 생성자는 protected로 선언되어 new를 이용할 수 없다, 객체를 생성하기 위해서 static한 of()를 이용하여 처리한다
- static 메서드 of()의 경우 페이지 처리에 필요한 정렬 조건을 같이 지정하기 위해 몇가지의 형태가 존재한다
  - of(int page, int size) - 0부터 시작하는 페이지 번호와 개수(size), 정렬이 지정되지 않음
  - of(int page, int size, Sort.Direction direction, String ...props) - 0부터 시작하는 페이지 번호와 개수, 정렬의 방향과 정렬 기준 필드들
  - of(int page, int size, Sort sort) - 페이지 번호와 개수, 정렬 관련 정보

### 쿼리 메서드와 @Query

- 다양한 검색 조건이 필요할 경우 Spring Data JPA가 다음과 같은 방법을 제공한다
  - 쿼리 메서드 - 메서드의 이름 자체가 쿼리의 구문으로 처리되는 기능
  - @Query - SQL과 유사하게 엔티티 클래스의 정보를 이용해서 쿼리를 작성하는 기능
  - Querydsl 등의 동적 쿼리 처리 기능
- 쿼리 메서드는 사용하는 키워드에 따라서 파라미터의 개수가 결정된다
- @Transactional과 @Commit
  - deleteBy의 경우 select문으로 해당 엔티티 객체들을 가져오는 작업과 각 엔티티를 삭제하는 작업이 같이 이루어지기 때문에 @Transactional이 없다면 TransactionRequiredException 에러가 발생한다
  - @Commit은 최종 결과를 커밋하기 위해 사용한다
- @Query 어노테이션
  - @Query의 값은 JPQL(Java Persistence Query Language)로 작성하는데 **객체지향 쿼리** 라고 불린다
  - 필요한 데이터만 선별적으로 추출하는 기능이 가능하다
  - 데이터베이스에 맞는 순수한 SQL을 사용하는 기능이 있다
  - insert, update, delete와 같은 DML등을 처리하는 기능이 있다(@Modifying과 함께 사용)
- @Query의 파라미터 바인딩
  - '?1, ?2' 와 1부턱 시작하는 파라미터의 순서를 이용하는 방식
  - ':xxx' 와 같이 ':파라미터 이름' 을 활용하는 방식
  - ':#{ }' 과 같이 자바 빈 스타일을 이용하는 방식

