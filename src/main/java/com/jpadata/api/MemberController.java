package com.jpadata.api;


import com.jpadata.dto.MemberDto;
import com.jpadata.entity.Member;
import com.jpadata.entity.Team;
import com.jpadata.repository.MemberRepository;
import com.jpadata.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/member/page")
    public Page<MemberDto> getMembers(@PageableDefault(
            size = 12,
            sort = "name",
            direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Member> members = memberRepository.findAllpage(pageable);

        //return members;
        return members.map(m -> new MemberDto(m.getName(),m.getAge(),null));
    }


    //@PostConstruct
    public void init() {


        for (int i = 0; i < 100; i++){
            memberRepository.save(new Member("kim",i,null));
        }
    }

}
