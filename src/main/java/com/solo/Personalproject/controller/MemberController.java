package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.MemberFormDto;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.service.MailService;
import com.solo.Personalproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    @Value("${admin.code}")
    private String adminCode;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    String confirm = "";
    boolean confirmCheck = false;

    @GetMapping(value = "/login")
    public String loginMember() {
        return "members/memberLoginForm";
    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요!");
        return "members/memberLoginForm";
    }
    @GetMapping(value = "/consent")
    public String consent() {
        return "members/consent";
    }


    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "members/memberForm";
    }
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "members/memberForm";
        }
        try {
            // 어드민 코드 비교하여 역할 설정
            Member member = Member.createMember(memberFormDto, passwordEncoder, adminCode);

            // 회원 저장
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }
        return "members/anno";
    }
    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfrim(@PathVariable("email") String email)
            throws Exception{
        System.out.println("email");

        confirm = mailService.sendSimpleMessage(email);
        System.out.println("인증코드 : "+ confirm);
        return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
    }
    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code")String code)
            throws Exception {
        if (confirm.equals(code)) {
            confirmCheck = true;
            return new ResponseEntity<String>("인증 성공하였습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
    }
}
