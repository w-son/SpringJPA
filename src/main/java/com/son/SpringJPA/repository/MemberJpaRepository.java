package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em;
    /*
      Update는 주로 서비스 계층에서
      변경 감지를 통해 엔티티를 조회한 상태에서 수정을 하고
      이를 @Transactional이 끝날때 commit을 시키면서 적용시킨다
     */

    public Member save(Member member) { // Crete
        em.persist(member);
        return member;
    }

    public Member findOne(Long id) { // Read
        return em.find(Member.class, id);
    }

    public Optional<Member> findById(Long id) { // Read
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll() { // Read
        // jpql 사용
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public void delete(Member member) { // Delete
        em.remove(member);
    }

}
