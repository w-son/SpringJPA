package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 출력용() throws Exception {
        System.out.println("memberRepository = " + memberRepository.getClass());
    }

    @Test
    public void createMember() throws Exception {
        // given
        Member member = Member.builder()
                .username("memberA")
                .build();
        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testCRUD() throws Exception {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        // when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        // then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        // when
        List<Member> members = memberRepository.findAll();
        // then
        assertThat(members.size()).isEqualTo(memberRepository.count());
        // when
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count = memberRepository.count();
        // then
        assertThat(count).isEqualTo(0);
    }

}