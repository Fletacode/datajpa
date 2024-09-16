package com.jpadata.repository;

import com.jpadata.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findAllMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
