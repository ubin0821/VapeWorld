package com.solo.Personalproject.repository;

import com.solo.Personalproject.dto.ItemSearchDto;
import com.solo.Personalproject.dto.MainItemDto;
import com.solo.Personalproject.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
