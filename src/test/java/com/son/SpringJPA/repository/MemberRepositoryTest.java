package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import com.son.SpringJPA.domain.Team;
import com.son.SpringJPA.dto.MemberDto;
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
    @Autowired
    private TeamRepository teamRepository;

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

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        // given
        Member member = new Member("memberA");
        memberRepository.save(member);
        // when
        List<Member> members = memberRepository.findByUsername("memberA");
        // then
        assertThat(members.get(0)).isEqualTo(member);
    }

    @Test
    public void testQueryAnnotation() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        // when
        List<Member> result = memberRepository.findUser("AAA", 10);
        // then
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findMemberDto() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        Team t1 = new Team("team1");
        teamRepository.save(t1);
        m1.setTeam(t1);

        List<MemberDto> findMember = memberRepository.findMemberDto();
        assertThat(findMember.get(0).getId()).isEqualTo(m1.getId());
        assertThat(findMember.get(0).getUsername()).isEqualTo(m1.getUsername());
        assertThat(findMember.get(0).getTeamName()).isEqualTo(t1.getName());
    }

    @Test
    public void TypeChecking() throws Exception {
        // given
        Member member = new Member("AAA", 10);
        memberRepository.save(member);
        // when
        List<Member> find1 = memberRepository.findListByUsername("AAA");
        Member find2 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> find3 = memberRepository.findOptionalByUsername("AAA");
        // then
        assertThat(find1.get(0)).isEqualTo(member);
        assertThat(find2).isEqualTo(member);
        assertThat(find3.get()).isEqualTo(member);
    }

}