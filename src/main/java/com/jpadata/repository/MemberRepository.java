package com.jpadata.repository;


import com.jpadata.dto.MemberDto;
import com.jpadata.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

    public List<Member> findByNameAndAgeGreaterThan(String name, Integer age);

    @Query("select m from Member m where m.name = :name")
    public Optional<Member> findByName(@Param("name") String name);

    @Query("select new com.jpadata.dto.MemberDto(m.name, m.age, m.team) from Member m join m.team t")
    public Optional<MemberDto> findTeamMembers();

    @Query("select m from Member m where m.name in :names")
    public List<Member> findByNames(@Param("names") List<String> names);

    @Query("select m from Member m")
    public Page<Member> findAllpage(Pageable pageable);

    public Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m join m.team t",
            countQuery = "select count(m) from Member m")
    public Page<Member> findTeamMembersPage(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    public void updateAge(@Param("age") int age);


    @Query("select m from Member m left join m.team")
    public List<Member> findAllMemberFetchJoin();


    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph("Member.all")
    //@EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraphByname(@Param("name") String name);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByname(@Param("name") String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findRockByname(@Param("name") String name);

    @Query("select m.name from Member m where m.name = :name")
    String findOnlyOneName(@Param("name") String name);


}
