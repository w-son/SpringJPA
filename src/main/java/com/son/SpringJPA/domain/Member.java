package com.son.SpringJPA.domain;

import lombok.*;

import javax.persistence.*;

@Entity @Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본적으로 파라미터가 없는 생성자를 사용해야할 수 있어야 한다
@AllArgsConstructor
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(
        name = "Member.all",
        attributeNodes = @NamedAttributeNode("team")
)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id") // 데이터베이스에서 FK 매핑할 때 쓸 것
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
