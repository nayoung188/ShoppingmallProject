package com.shoppingmall.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Valid
public class MemberDTO {
    private long memberNo;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message="올바른 형식의 이메일 주소를 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$~!@#$%^&*?]{8,}$",
            message = "영문, 숫자, 특수문자를 조합한 8자 이상")
    private String memberPw;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=1, max = 50)
    private String memberName;

    @NotBlank(message = "핸드폰번호를 입력해주세요.")
    @Pattern(regexp="^010[0-9]{7,8}$", message = "10~11자로 숫자만 입력해주세요")
    private String memberMobile;

    @NotBlank(message = "회사명을 입력해주세요.")
    @Size(min=1, max = 50)
    private String companyName;

    @NotBlank(message = "사업자번호를 입력해주세요.")
    @Size(min = 9, max = 11, message = "숫자만 입력해주세요")
    private String companyNumber;

    private String companyCreateDate;
    private String companyCeo;
    private String companyType;
    private String companyAddress;
    private String companyBusinessType;
    private String companyBusinessCategory;

    private String bankName;
    private String accountNo;
    private String accountDepositor;

    private String businessLicense;
    private String telemarketingLicense;
    private String accountLicense;

    private String useAgree;
    private String privacyAgree;
    private String marketingAgree;

    // role member=일반 가입, manager=결제회원, admin=관리자
    private String role;
}
