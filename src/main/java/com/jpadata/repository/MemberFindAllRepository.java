package com.jpadata.repository;


import com.jpadata.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class MemberFindAllRepository {

    private final EntityManager em;

    public List<Member> findAllFindAllRepo() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }


}
