package com.jpadata.dto;


import com.jpadata.entity.Team;
import lombok.Data;

@Data
public class MemberDto {


    private String name;
    private Team team;
    private int age;

    public MemberDto(String name,int age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
    }

}
