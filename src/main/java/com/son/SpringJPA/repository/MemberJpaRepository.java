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

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m" + " where m.username= :username" + " and m.age >:age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    // 리스트 10개라 가정하고 offset 3, limit 3이라면 ? 3, 4, 5 번째 페이지 리턴
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username asc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
    // 페이징

    public List<Member> findAll() { // Read
        // jpql 사용
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public int bulkAgePlus(int age) { // Update
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }

    public void delete(Member member) { // Delete
        em.remove(member);
    }

}
