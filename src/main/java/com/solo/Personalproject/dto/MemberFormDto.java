package com.solo.Personalproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
    private Long id;
    private String detailAddress;
    private String postcode;

    @NotBlank(message = "이름을 입력 해 주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력 해 주세요.")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력 해 주세요.")
    private String password;

    @NotEmpty(message = "주소를 입력 해 주세요.")
    private String address;

    @NotBlank(message = "전화번호를 입력 해 주세요.")
    private String tel;

    private String adminCode;
}
