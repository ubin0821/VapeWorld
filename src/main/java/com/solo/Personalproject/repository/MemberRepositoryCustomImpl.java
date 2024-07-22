package com.solo.Personalproject.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.entity.QMember;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{


    private final JPAQueryFactory queryFactory;

    // EntityManager를 사용하여 JPAQueryFactory를 초기화하는 생성자 주입
    public MemberRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Member> memberSearch(Long id, String name) {
        QMember member = QMember.member;
        String strId = String.valueOf(id);
        // 쿼리 생성
        JPAQuery<Member> query = queryFactory.selectFrom(member)
                .where((strId.equals("0")) ? member.name.like("%" + name + "%") : member.id.stringValue().like("%" + strId + "%"),
                        member.name.like("%" + name + "%"))
                .orderBy(member.id.desc());
        return query.fetch();
    }
}
