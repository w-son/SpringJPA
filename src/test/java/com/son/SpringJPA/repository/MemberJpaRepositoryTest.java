package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {
    // junit5 에서는 @Runwith(SpringRunner.class) 가 없어도 된다

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception {
        // given
        Member member = Member.builder()
                .username("memberA")
                .build();
        // when
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.findOne(savedMember.getId());
        // then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testCRUD() throws Exception {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        // when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        // then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        // when
        List<Member> members = memberJpaRepository.findAll();
        // then
        assertThat(members.size()).isEqualTo(memberJpaRepository.count());
        // when
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long count = memberJpaRepository.count();
        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        // given
        Member member = new Member("memberA");
        memberJpaRepository.save(member);
        // when
        List<Member> members = memberJpaRepository.findByUsername("memberA");
        // then
        assertThat(members.get(0)).isEqualTo(member);
    }

}