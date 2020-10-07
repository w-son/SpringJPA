package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {


    /*
     OneToMany 관계의 One 의 입장에서 EntityGraph 를 이용한 join fetch 시
     EntityGraphType 을 두 가지로 설정 할 수 있다
     FETCH(default) : attributePath 에 명시된 속성만 EAGER fetch, 나머지는 LAZY 로 초기화
     LOAD : attributePath 에 명시된 속성만 EAGER fetch, 나머지는 기본 fetch 전략으로 초기화
     */
    @Override
    @EntityGraph(attributePaths = {"members"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Team> findAll();


}
