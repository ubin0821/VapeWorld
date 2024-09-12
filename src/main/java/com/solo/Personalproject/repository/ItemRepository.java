package com.solo.Personalproject.repository;

import com.solo.Personalproject.constant.Category;
import com.solo.Personalproject.dto.ItemDto;
import com.solo.Personalproject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Collectors;

public interface ItemRepository extends JpaRepository<Item,Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom{

    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    List<Item> findByPriceLessThan(Integer price);
    //select * from item where price < Integer price order by desc;
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail")String itemDetail);
    @Query(value = "select * from item i where i.item_Detail like %:itemDetail% order by i.price desc"
            ,nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail")String itemDetail);

    List<Item> findItemsByCategory(Category category);

    @Query("SELECT COUNT(i) FROM Item i WHERE i.category = :category")
    long countByCategory(@Param("category") Category category);
}
