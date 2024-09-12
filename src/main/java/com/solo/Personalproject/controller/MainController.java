package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.ItemSearchDto;
import com.solo.Personalproject.dto.MainItemDto;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {
        // 한 페이지에 20개의 상품을 표시하도록 수정
        Pageable pageable = PageRequest.of(page.orElse(0), 10); // 페이지당 20개씩
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);  // 페이지네이션의 최대 페이지
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }
        return "main";
    }
}
