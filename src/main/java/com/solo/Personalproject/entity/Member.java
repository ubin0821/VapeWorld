package com.solo.Personalproject.entity;

import com.solo.Personalproject.constant.Role;
import com.solo.Personalproject.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String address;
    private String postcode;
    private String detailAddress;
    private String tel;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;
    // 소셜 로그인 사용자를 생성하는 메서드
    public static Member createSocialUser(String name, String email, String picture) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPicture(picture);
        member.setRole(Role.USER); // 기본적으로 소셜 로그인 사용자는 USER로 설정
        return member;
    }

    // 관리자 코드와 사용자를 생성하는 메서드
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder, String adminCodeFromConfig) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setTel(memberFormDto.getTel());
        member.setPostcode(memberFormDto.getPostcode());
        member.setDetailAddress(memberFormDto.getDetailAddress());

        // 비밀번호 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        // 입력된 adminCode와 설정된 adminCode를 비교하여 역할 설정
        if (adminCodeFromConfig.equals(memberFormDto.getAdminCode())) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.USER);
        }

        return member;
    }
}

