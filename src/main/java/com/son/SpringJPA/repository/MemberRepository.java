package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
     인터페이스 밖에 없는데 의존성 주입이 되고
     여러 CRUD 인터페이스 메서드들을 활용할 수 있다
     */

}
