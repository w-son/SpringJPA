package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    /*
     커스터마이징 한 리포지토리
     이 인터페이스의 구현체인 MemberRepositoryImpl을 구현해 놓고
     Spring Data JPA의 MemberRepository에 extend 시키면
     구현체를 쓸 수 있게 된다

     단 구현체의 이름은 "Spring Data JPA 인터페이스 이름 + Impl" 이 이름을 꼭 지켜줘야 한다
     이 예제의 경우 MemberRepository + Impl
     */
    List<Member> findMemberCustom();

}
