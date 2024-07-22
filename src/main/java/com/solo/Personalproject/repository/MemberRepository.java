package com.solo.Personalproject.repository;

import com.solo.Personalproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;



public interface  MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);
    Member findByTel(String tel);
}
