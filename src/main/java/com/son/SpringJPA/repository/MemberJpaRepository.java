package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

}
