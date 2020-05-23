package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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

}