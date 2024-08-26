package com.solo.Personalproject.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solo.Personalproject.constant.ItemSellStatus;
import com.solo.Personalproject.dto.ItemSearchDto;
import com.solo.Personalproject.dto.MainItemDto;
import com.solo.Personalproject.dto.QMainItemDto;
import com.solo.Personalproject.entity.Item;
import com.solo.Personalproject.entity.QItem;
import com.solo.Personalproject.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체 생성
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //ItemSellStatus null  이면 null 리턴 / null 아니면 SELL , SOLD 둘중 하나 리턴
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(searchSellStatusEq(itemSearchDto.getSearchSellStatus())) // 판매 상태만 필터링
                .orderBy(QItem.item.id.desc()) // 아이템 ID 기준으로 내림차순 정렬
                .offset(pageable.getOffset()) // 페이징 오프셋 설정
                .limit(pageable.getPageSize()) // 페이지 크기 설정
                .fetchResults(); // 결과를 페치

        List<Item> content = results.getResults(); // 조회된 아이템 리스트
        long total = results.getTotal(); // 총 아이템 수

        return new PageImpl<>(content, pageable, total); // 페이지 객체 반환
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryProjection을 활용하면 Dto 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price))
                .from(itemImg).join(itemImg.item,item).where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
}
