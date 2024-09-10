package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.ItemDto;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.service.ItemService;
import com.solo.Personalproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InventoryController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "/liquid_phase")
    public String liList(ItemDto itemDto, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }

        // 액상 카테고리의 모든 상품 가져오기
        List<ItemDto> liquidItems = itemService.getLiquidItems();
        model.addAttribute("items", liquidItems); // 뷰로 전달

        return "inventory/liquid_phase"; // 해당 페이지로 리턴
    }

    @GetMapping(value = "/machine")
    public String maList(ItemDto itemDto, Model model,Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }
        return "inventory/machine";
    }
    @GetMapping(value = "/consumables")
    public String coList(ItemDto itemDto, Model model,Principal principal) {
        String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
        Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

        if (member != null && member.getName() != null) {
            model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
        } else {
            model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
        }
        return "inventory/consumables";
    }
}
