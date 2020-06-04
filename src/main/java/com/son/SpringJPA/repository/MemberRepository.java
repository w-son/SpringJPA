package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import com.son.SpringJPA.dto.MemberDto;
import org.hibernate.LockMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    /*
     인터페이스 밖에 없는데 의존성 주입이 되고
     여러 CRUD 인터페이스 메서드들을 활용할 수 있다

     # NOTE
       Generic Type T : 엔티티
       save : 새로운 객체를 저장하거나 기존의 있던 객체는 ```변경사항을 적용```시켜준다

     1) count...By (long)
     2) exists...By (boolean)
     3) remove...By (long)
     4) find...DistinctBy (Object)
     5) findTop3 (Object, limit 조건이 들어간다)

     */

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /*
     Named Query 사용 시 @Query 와 @Param으로 쿼리와 파라미터 설정을 해줘야 한다 name에 해당하는 namedQuery를 찾아주게 된다
     Named Query 은 사실 잘 사용이 잘 안된다
     하지만 이런 장점이?
        em.createQuery 의 쿼리문은 오타가 있어도 실행에 문제가 없다 하지만 런타임 중 문제가 생길 것이다
        하지만 namedQuery는 컴파일 할 때 쿼리문을 파싱하여 문법 오류를 찾아 에러를 발생시킨다
     */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    /*
     Data JPA + Named Query의 장점
     왜?
     1) 쿼리 조건에 맞춘 메서드 이름 정의 -> 조건이 많아지면 많아질수록 메서드 이름이 길어진다..
        그렇다고 JpaRepository를 상속받는 구현체를 생성할 수도 없음.. 왜? 오버라이드 해야하는 메서드가 너무 많다!! Data JPA 없이 짜는거랑 다를 바 없음
        그래도 조건이 별로 없을 때는 아주 생산적
     2) Named Query
        컴파일 타임에 쿼리문의 오타를 잡을 수 있다는 강점이 있지만 엔티티에 쿼리가 덕지덕지 붙어있음..

     -> Data JPA가 제공하는 @Query에 원하는 jpql문을 설계, 컴파일 시점에 쿼리문의 문법 오류도 잡고
        복잡한 조건에도 메서드명을 간결하게 정의함으로써 생산성, 코드 간결성 더욱 증가 ㅎㅎ
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernames();

    /*
     엔티티 조회 + DTO 매핑을 한꺼번에 실행한다
     TODO ModelMapper와는 어떤 차이가 있는것일까?
     */
    @Query("select new com.son.SpringJPA.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // List 파라미터로 in 쿼리문 작성하기
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /*
     여러가지 반환 타입
     반환 값이 없는 경우
     1) 빈 리스트를 리턴한다
     2) 원래 JPA는 NoResultException을 발생시키지만 Data JPA는 try catch 후에 null 이 리턴된다
        -> 문제가 될 수 있음
     3) 2번 문제를 해결하기 위해서 java 8 에서 생긴 것이 Optional
        Optional에 여러개의 값이 반환되어서 들어가게 된다면 어떻게 될까
        -> NonUniqueResultException
     */
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    /*
     페이징 쿼리
     리턴되는 페이지에 age에 해당하는 전체 Member가 리턴되고
     여기서 getContent를 하면 페이징 조건에 맞는 객체들을 리스트 형태로 가져올 수 있다
     쉽게 얻을 수 있는 것
     1) getTotalElements : 조건에 알맞는 객체의 개수
     2) getContent : 페이지 조건으로 필터된 객체의 리스트
     3) getNumber : 현재 페이지의 번호
     4) getTotalPages : 페이지의 개수
     5) isFirst, isLast, hasNext 등등
     TODO 페이지 인터페이스는 어떤 구현체로 리턴 받을까? JpaRepository가 상속받는 PagingAndSortingRepository의 구현체가 무엇을 리턴하는지 알고 싶다
     -> JpaRepository의 구현체인 SimpleJpaRepository를 보면 확인할 수 있다 Page<>의 구현체는 PageImpl<>

     countQuery 분리
     왜?
     페이징을 통해서 left outer join 되는 경우를 가정
     페이지의 데이터를 조회하는 방식으로는 적합하지만
     데이터의 전체 개수를 구하는 경우에는 굳이 join해서 가져올 필요가 없음 .. 이건 왜? 모른다면 left outer join 다시 공부

     @Query 를 사용해서 countQuery에 해당하는 쿼리문을 따로 짤 수 있다
     */

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /*
     벌크형 수정 쿼리
     @Modifying 이 있어야 .executeUpdate와 같은 기능을 수행할 수 있다
     * 주의 : 영속성 컨텍스트에 반영하는 것이 아닌 데이터베이스에 바로 반영하는 것이다
       따라서 같은 transaction 내에서 벌크 연산 후 조회(=같은 엔티티 매니저를 쓴다는 말)를 할 시에는 다음과 같은 과정을 거쳐야 함
       1) em.flush() = 영속성 캐시에 아직 DB에 반영되지 않은 내용이 있다면 반영시킨다
       2) em.clear() = 이후에 엔티티 조회 시 (이미 캐시에 있는 내용이면 캐시의 내용을 불러오므로) 벌크가 반영된 새 엔티티를 불러오기 위해 비워준다

       위 과정을 수행 후 벌크연산을 수행한 엔티티를 조회해야 정상적인 조회가 이루어진다
       위 과정을 Spring Data JPA 가 해줌 ㅋㅋ
       = @Modifying(clearAutomarically = true)
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /*
     fetch join, paging을 통한 in Query 다시 정리해보기

     1) fetch join
     일대 다, 다대 일 관계에서 일(Team) 쪽에 mapped by => 다(Member) 쪽이 관계의 주인이자 LAZY fetch가 적용이 된다
     LAZY fetch 활용시 EAGER과는 다르게 매핑되는 객체가 프록시 타입의 빈 객체로 초기화 된다
     이를 조회하려고 할 시에(get 메서드를 활용하는 것이 일반적) 추가적인 쿼리가 나간다
     * 문제는, LAZY 초기화 후에 모든 프록시객체에 대해 이런 쿼리문을 날린다면 비요율적일 것 즉 N + 1 문제가 발생한다
     ==> join fetch 를 통해서 프록시 객체를 한꺼번에 조회
     ==> Spring Data JPA에서는 Entity Graph를 제공

     2) in Query
     다 입장에서 일을 호출할때는 join fetch가 적절하지만... 일 입장에서 다를 join fetch 할 경우에는 어떨까?
     A와 연관되어 있는 1, 2를 join fetch 한다고 가정하였을 때
     일 인 A를 기점으로 join fetch를 하게 된다면
     (A, 1), (A, 2) 식으로 A가 다 의 횟수만큼 중복되어 반횐되는 결과가 나올 것이다
     이 문제를 해결하기 위해 in Query를 사용하게 된다
     */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberByJoinFetch();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    /*
     JPA Query hint & lock
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
