package com.jpadata.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "age", "team"})
@Entity
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseJpaEntity {

    @Id @GeneratedValue
    @Column(name= "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        if (team != null){
            changeTeam(team);
        }

    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }




}
