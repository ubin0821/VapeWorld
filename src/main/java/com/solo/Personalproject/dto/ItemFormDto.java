package com.solo.Personalproject.dto;

import com.solo.Personalproject.constant.ItemSellStatus;
import com.solo.Personalproject.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명 필수 입력")
    private String itemNm;

    @NotNull(message = "가격 필수 입력")
    private Integer price;

    @NotBlank(message = "상세설명 필수 입력")
    private String itemDetail;

    @NotNull(message = "재고 필수 입력")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); // 상품 이미지 아이디

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this,Item.class);
    }
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item,ItemFormDto.class);
    }

}
