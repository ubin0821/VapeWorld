package com.solo.Personalproject.repository;

import com.solo.Personalproject.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> memberSearch(Long id, String name);
}
