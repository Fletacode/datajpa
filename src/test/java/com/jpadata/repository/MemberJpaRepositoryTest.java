package com.jpadata.repository;

import com.jpadata.entity.Member;

import com.jpadata.entity.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    EntityManager em;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void 회원저장() throws Exception{
        /*
        //given
        Member member = new Member("김동훈");

        //when
        Member saveMember = memberRepository.save(member);
        Member findMember =  memberRepository.findById(saveMember.getId());

        //then
        assertThat(findMember).isEqualTo(member);

        //assertEquals(saveMember,findMember);
        */

    }

    @Test
    public void basicCRUD() throws Exception{
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);


        Member member1 = new Member("kim",22,teamA);
        Member member2 = new Member("hun",23,teamB);

        //when
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Long cnt = memberJpaRepository.getCount();

        System.out.println(cnt);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        //then
        assertThat(memberJpaRepository.getCount()).isEqualTo(0);
    }

    @Test
    public void 기본jpa로페이징() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("james",10,team);
        Member member2 = new Member("jame2",20,team);
        Member member3 = new Member("jame3",20,team);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        //when

        List<Member> findMember = memberJpaRepository.findByPage(20,1,2);
        Long totalCnt = memberJpaRepository.getCount();

        //then
        assertThat(findMember.size()).isEqualTo(2);
        assertThat(totalCnt).isEqualTo(3);
    }

    @Test
    public void 멤버나이수정() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("james",30,team);
        Member member2 = new Member("james",40,team);
        Member member3 = new Member("james",50,team);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        //when

        memberJpaRepository.updateMemberAge(10);

        em.flush();
        em.clear();

        //then
        Optional<Member> findMember = memberJpaRepository.findById(1L);



        assertThat(findMember.get().getAge()).isEqualTo(31);

    }

    @Test
    public void auditing() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("james",30,team);

        //when
        //@PrePersist 작동
        memberJpaRepository.save(member);

        Thread.sleep(1000);

        member.setName("kim");

        //@PreUpdate 작동
        em.flush();
        em.clear();

        Optional<Member> findMember = memberJpaRepository.findById(1L);

        //then
        System.out.println(findMember.get().getCreatedDate());
        System.out.println(findMember.get().getLastModifiedBy());
    }

    @Test
    public void 간단하게_이름만조회() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("kim1",10,team);
        memberRepository.save(m1);

        em.flush();
        em.clear();

        //when

        String findMemberName = memberRepository.findOnlyOneName("kim1");


        //then

        assertThat(findMemberName).isEqualTo("kim1");

    }

}