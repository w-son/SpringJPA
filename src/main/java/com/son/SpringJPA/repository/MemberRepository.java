package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
        하지만 namedQuery는 컴파일 할 때 쿼리문을 파싱하여 문법오류를 찾아 에러를 발생시킨다
     */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

}
