package com.son.SpringJPA.controller;

import com.son.SpringJPA.domain.Member;
import com.son.SpringJPA.dto.MemberDto;
import com.son.SpringJPA.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        for (int i=0; i<10; i++) {
            memberRepository.save(new Member("userA" + i, i));
        }
    }

    @GetMapping("/members1/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /*
     도메인 클래스 컨버터
     위 메서드와는 다르게 스프링에서
     id (키값) 값을 보고 난 후에 자동으로 그에 해당하는 엔티티를 찾아서
     메서드의 파라미터로 넣어준다

     조회용으로만 쓰자...
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 3) Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberDto> memberDtos = members.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return memberDtos;
    }

}
