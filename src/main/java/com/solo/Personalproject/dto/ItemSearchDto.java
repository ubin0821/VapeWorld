package com.solo.Personalproject.dto;

import com.solo.Personalproject.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private ItemSellStatus searchSellStatus; // 상태

    private String searchBy; // 조회 유형

    private String searchQuery = ""; // 검색 단어
}
