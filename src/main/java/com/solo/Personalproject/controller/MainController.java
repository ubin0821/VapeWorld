package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.ItemSearchDto;
import com.solo.Personalproject.dto.MainItemDto;
import com.solo.Personalproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        // 한 페이지에 20개의 상품을 표시하도록 수정
        Pageable pageable = PageRequest.of(page.orElse(0), 20); // 페이지당 20개씩
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);  // 페이지네이션의 최대 페이지
        return "main";
    }
}
