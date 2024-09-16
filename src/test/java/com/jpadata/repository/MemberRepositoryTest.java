package com.jpadata.repository;

import com.jpadata.dto.MemberDto;
import com.jpadata.entity.Item;
import com.jpadata.entity.Member;
import com.jpadata.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberFindAllRepository memberFindAllRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;


    @Test
    public void 회원가입() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("kim",22,team);
        memberRepository.save(member);
        //when

        Optional<Member> findMember = memberRepository.findById(member.getId());

        //then
        assertThat(member).isEqualTo(findMember.get());


    }

    @Test
    public void 나이높은회원조회() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("kim",22, team);
        Member member2 = new Member("kim",23, team);

        memberRepository.save(member);
        memberRepository.save(member2);
        //when

        List<Member> findMember = memberRepository.findByNameAndAgeGreaterThan("kim",20);

        //then
        assertThat(findMember.size()).isEqualTo(2);

    }




    @Test
    public void datajpa쿼리테스트() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("김동훈", 10,team);
        Member member2 = new Member("김성자", 20,team);

        memberRepository.save(member);
        memberRepository.save(member2);

        //when

        List<String> names = new ArrayList<>();
        names.add("김동훈");
        names.add("김성자");

        Optional<Member> members = memberRepository.findByName("김동훈");
        List<Member> memberList = memberRepository.findByNames(Arrays.asList("김동훈,김성자"));

        //then

        if(members.isPresent()){
            assertThat(members.get().getName()).isEqualTo("김동훈");
        }else{
            fail();
        }



    }

    @Test
    public void 쿼리dto조회() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("김동훈", 10,team);
        memberRepository.save(member);

        //when
        Optional<MemberDto> memberDtos = memberRepository.findTeamMembers();


        //then
        if (memberDtos.isPresent()){
            assertThat(memberDtos.get().getName()).isEqualTo(member.getName());
        }else{
            fail();
        }

    }

    @Test
    public void datajpa페이징() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member1 = new Member("kim1",10,team);
        Member member2 = new Member("kim2",10,team);
        Member member3 = new Member("kim3",10,team);
        Member member4 = new Member("kim4",10,team);
        Member member5 = new Member("kim5",10,team);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"name"));

        Page<Member> memberPage =  memberRepository.findByAge(10,pageRequest);

        Page<Member> membersTeam = memberRepository.findTeamMembersPage(pageRequest);

        //List<Member> members = memberPage.getContent();


        Page<MemberDto> memberDtoPage = memberPage
                .map(m -> new MemberDto(m.getName(),m.getAge(),m.getTeam()));


        //then

        //assertThat(members.size()).isEqualTo(3);
        //assertThat(memberPage.getTotalElements()).isEqualTo(5);
        //assertThat(memberPage.getNumber()).isEqualTo(0); //현재 몇 페이지?
        //assertThat(memberPage.getTotalPages()).isEqualTo(2); //총 몇 페이지?
        //assertThat(memberPage.isFirst());
        //assertThat(memberPage.hasNext());

        //assertThat(members.get(0).getName()).isEqualTo("kim3");

    }

    @Test
    public void 멤버벌크업데이트() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member1 = new Member("kim1",10,team);
        Member member2 = new Member("kim2",20,team);
        Member member3 = new Member("kim3",30,team);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when

        memberRepository.updateAge(10);


        Optional<Member> findMember = memberRepository.findById(member1.getId());

        //then

        assertThat(findMember.get().getAge()).isEqualTo(11);


    }

    @Test
    public void 멤버페치조인() throws Exception{
        //given
        Team team = new Team("teamA");
        Team team2 = new Team("teamB");
        teamRepository.save(team);
        teamRepository.save(team2);
        Member member1 = new Member("kim1",10,team);
        Member member2 = new Member("kim2",20,team2);
        Member member3 = new Member("kim3",30,team2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when

        //List<Member> members = memberRepository.findAllMemberFetchJoin();
        //List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findMemberEntityGraph();
        //List<Member> members = memberRepository.findMemberEntityGraphByname("kim1");


        //then

        for (Member member : members){
            System.out.println(member.getName());
            System.out.println(member.getTeam().getName());
        }

    }

    @Test
    public void 쿼리힌트_락() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        memberRepository.save(new Member("kim",10, team));

        //쿼리 날리기
        em.flush();
        //영속성 컨텍스트 초기화
        em.clear();

        //읽기전용으로 가져옴
        Member member = memberRepository.findReadOnlyByname("kim");

        //읽기 전용이므로 update쿼리가 안감 영속성 컨텍스트에 스냅샷을 저장안함
        member.setName("kim2");
        em.flush();
    }

    @Test
    public void 커스텀레포() throws Exception{
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        memberRepository.save(new Member("kim",10, team));

        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findAllMemberCustom();
        List<Member> members = memberFindAllRepository.findAllFindAllRepo();


        //then

        assertThat(members.size()).isEqualTo(1);

    }






}