package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
