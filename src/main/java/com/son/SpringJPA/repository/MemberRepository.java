package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import com.son.SpringJPA.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

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


}
