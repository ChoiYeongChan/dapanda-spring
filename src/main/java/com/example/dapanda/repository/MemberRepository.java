package com.example.dapanda.repository;

import com.example.dapanda.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //@Query("select m from Member m")
    //List<Member> findAllMembers();
    Member findByMemberId(int memberId);

    Member findByName(String name);

    @Query("select m from Member m where m.memberString = :memberString")
    Member findByMemberString(String memberString);
}
