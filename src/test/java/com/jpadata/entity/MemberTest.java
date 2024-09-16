package com.jpadata.entity;

import com.jpadata.repository.MemberJpaRepository;
import com.jpadata.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @Autowired
    private MemberJpaRepository memberRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Test
    public void MemberTest() throws Exception{
        //given
        Team teamA = new Team("A");
        teamRepo.save(teamA);
        Member member = new Member("kim", 22, teamA);
        memberRepo.save(member);

        //when
        Optional<Member> findMember = memberRepo.findById(member.getId());

        //then

        Assertions.assertThat(findMember.get().getTeam()).isEqualTo(teamA);

    }

}