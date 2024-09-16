package com.jpadata.repository;


import com.jpadata.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Long getCount(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public List<Member> findByPage(int age ,int offset, int limit){
        return em.createQuery("select m from Member m where age = :age" , Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long findCount(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }


    public void updateMemberAge(int age) {
        em.createQuery("update Member m" +
                " set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age",age)
                .executeUpdate();
    }


}
