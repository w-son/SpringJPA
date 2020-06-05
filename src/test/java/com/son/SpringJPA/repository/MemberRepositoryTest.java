package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import com.son.SpringJPA.domain.Team;
import com.son.SpringJPA.dto.MemberDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;
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
    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

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

    @Test
    public void testPaging() throws Exception {
        // given
        generateMember(10);

        // Pageable의 구현체 PageRequest
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "username"));
        Page<Member> pagedMembers = memberRepository.findByAge(10, pageRequest);
        Page<MemberDto> mappedMembers = pagedMembers.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<MemberDto> members = mappedMembers.getContent();
        long count = pagedMembers.getTotalElements();

        // then
        for(MemberDto m : members) {
            System.out.println(m.getUsername());
        }
        assertThat(members.size()).isEqualTo(3);
        assertThat(count).isEqualTo(10);
        assertThat(pagedMembers.getNumber()).isEqualTo(0);
        assertThat(pagedMembers.isFirst()).isTrue();
        assertThat(pagedMembers.hasNext()).isTrue();
    }

    @Test
    public void testBulkUpdate() throws Exception {
        // given
        generateMember(5);
        // when
        int count = memberRepository.bulkAgePlus(10);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findByUsername("0번째 멤버");
        Member findMember = result.get(0);
        System.out.println(findMember.getAge());

        // then
        assertThat(count).isEqualTo(5);
    }

    @Test
    public void findMemberLazy() throws Exception {
        // given
        // member1 -> teamA
        // member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findAll();
        for(Member member : members) {
            System.out.println(member.getUsername());
            System.out.println(member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() throws Exception {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); // 변경 감지
    }

    @Test
    public void customRepo() throws Exception {
        // given
        List<Member> members = memberRepository.findMemberCustom();
    }

    @Test
    public void projection() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnly> r1 = memberRepository.findProjectionsByUsername("m1");
        for(UsernameOnly username : r1) {
            System.out.println("UsernameOnly = " + username.getUsername());
        }

        List<UsernameOnlyDto> r2 = memberRepository.findProjectionsDtoByUsername("m1");
        for(UsernameOnlyDto dto : r2) {
            System.out.println("UsernameOnlyDto = " + dto.getUsername());
        }

        List<NestedClosedProjection> r3 = memberRepository.findProjectionsGenericByUsername("m1", NestedClosedProjection.class);
        for(NestedClosedProjection g : r3) {
            System.out.println("username = " + g.getUsername());
            System.out.println("teamname = " + g.getTeam().getName());
        }
    }

    public void generateMember(int n) {
        for(int i=0; i<n; i++) {
            Member member = new Member(Integer.toString(i) + "번째 멤버", 10);
            memberRepository.save(member);
        }
    }

}