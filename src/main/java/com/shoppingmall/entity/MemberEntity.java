package com.shoppingmall.entity;

import com.shoppingmall.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "member")
@DynamicInsert
public class MemberEntity{
    @Id     //pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increament
    private Long memberNo;

    @Column(unique = true, name = "member_email")  // unique 제약 조건 추가
    private String memberEmail;

    @Column
    private String memberPw;

    @Column
    private String memberName;

    @Column
    private String memberMobile;

    @Column
    private String companyName;

    @Column(unique = true)
    private String companyNumber;

    @Column
    private String companyCreateDate;

    @Column
    private String companyCeo;

    @Column
    private String companyType;

    @Column
    private String companyAddress;

    @Column
    private String companyBusinessType;

    @Column
    private String companyBusinessCategory;

    @Column
    private String bankName;

    @Column
    private String accountNo;

    @Column
    private String accountDepositor;

    @Column
    private String businessLicense;

    @Column
    private String telemarketingLicense;

    @Column
    private String accountLicense;

    @Column
    @ColumnDefault("'N'")
    private String useAgree;

    @Column
    @ColumnDefault("'N'")
    private String privacyAgree;

    @Column
    @ColumnDefault("'N'")
    private String marketingAgree;

    @Column
    private String role;


    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPw(memberDTO.getMemberPw());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setCompanyName(memberDTO.getCompanyName());
        memberEntity.setCompanyNumber(memberDTO.getCompanyNumber());
        memberEntity.setUseAgree(memberDTO.getUseAgree());
        memberEntity.setPrivacyAgree(memberDTO.getPrivacyAgree());
        memberEntity.setMarketingAgree(memberDTO.getMarketingAgree());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setCompanyCeo(memberDTO.getCompanyCeo());
        memberEntity.setCompanyCreateDate(memberDTO.getCompanyCreateDate());
        memberEntity.setCompanyType(memberDTO.getCompanyType());
        memberEntity.setCompanyAddress(memberDTO.getCompanyAddress());
        memberEntity.setCompanyBusinessType(memberDTO.getCompanyBusinessType());
        memberEntity.setCompanyBusinessCategory(memberDTO.getCompanyBusinessCategory());
        memberEntity.setBankName(memberDTO.getBankName());
        memberEntity.setAccountNo(memberDTO.getAccountNo());
        memberEntity.setAccountDepositor(memberDTO.getAccountDepositor());
        memberEntity.setBusinessLicense(memberDTO.getBusinessLicense());
        memberEntity.setTelemarketingLicense(memberDTO.getTelemarketingLicense());
        memberEntity.setAccountLicense(memberDTO.getAccountLicense());
        return memberEntity;
    }
}
