package com.solo.Personalproject.controller;

import com.solo.Personalproject.constant.Category;
import com.solo.Personalproject.dto.ItemDto;
import com.solo.Personalproject.dto.ItemSearchDto;
import com.solo.Personalproject.dto.MainItemDto;
import com.solo.Personalproject.entity.Item;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.service.ItemService;
import com.solo.Personalproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class InventoryController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value ={"/liquid_phase","/liquid_phase/{page}"} )
    public String liList(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }
        Pageable pageable = PageRequest.of(page.orElse(0), 20); // 페이지당 20개씩
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        Page<MainItemDto> getCategoryItemPage=itemService.getCategoryItemPage(itemSearchDto,pageable,Category.LIQUID_PHASE);
        model.addAttribute("items", getCategoryItemPage);
        model.addAttribute("totalCount", itemService.countItemsByCategory(Category.LIQUID_PHASE));

        return "inventory/liquid_phase"; // 해당 페이지로 리턴
    }

    @GetMapping(value = {"/machine","/machine/{page}"})
    public String maList(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }
        Pageable pageable = PageRequest.of(page.orElse(0), 20); // 페이지당 20개씩
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        Page<MainItemDto> getCategoryItemPage=itemService.getCategoryItemPage(itemSearchDto,pageable,Category.MACHINE);
        model.addAttribute("items", getCategoryItemPage);
        model.addAttribute("totalCount", items.getTotalElements());
        model.addAttribute("totalCount", itemService.countItemsByCategory(Category.MACHINE));
        return "inventory/machine";
    }
    @GetMapping(value ={"/consumables","/consumables/{page}"})
    public String coList(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }

        Pageable pageable = PageRequest.of(page.orElse(0), 20); // 페이지당 20개씩
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        Page<MainItemDto> getCategoryItemPage = itemService.getCategoryItemPage(itemSearchDto, pageable, Category.CONSUMABLES);
        model.addAttribute("items", getCategoryItemPage);
        model.addAttribute("totalCount", itemService.countItemsByCategory(Category.CONSUMABLES));

        return "inventory/consumables";
    }

}
