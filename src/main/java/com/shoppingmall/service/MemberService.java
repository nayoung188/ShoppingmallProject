package com.shoppingmall.service;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.entity.MemberEntity;
import com.shoppingmall.repository.MemberRepository;
import com.shoppingmall.util.FileUtil;
import com.shoppingmall.util.NaverSmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService{

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private NaverSmsUtil naverSmsUtil;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private FileUtil fileUtil;

    // 회원조회
    public List<MemberEntity> getAllMembers(){
        List<MemberEntity> members = memberRepository.findAll();
        return members;
    }

    public Page<MemberEntity> memberlist(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    public List<MemberEntity> getTop5Member(){
        List<MemberEntity> members = memberRepository.findTop5ByOrderByMemberNo();
        return members;
    }

    public Long getMemberIdByEmail(String memberEmail){
        return memberRepository.findMemberNoByMemberEmail(memberEmail);
    }

    public MemberEntity getMember(String memberEmail){
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(memberEmail);
        return member.orElse(null);
    }

    // 이메일 중복 확인
    public String checkSameEmail(String memberEmail){
        Optional<MemberEntity> result = memberRepository.findByMemberEmail(memberEmail);
        if(result.isPresent()){
            return "중복";
        } else {
            return "중복없음";
        }
    }

    // 핸드폰인증
    public String sendAuthCodeService(String memberMobile){
        Random randomNum = new Random();
        String authCode = "";
        for( int i = 0; i < 4; i++) {
            String random = Integer.toString(randomNum.nextInt(10));
            authCode += random;
        }
        System.out.println("회원가입 문자 인증번호 ::: " + authCode);
        naverSmsUtil.sendAuthCodeUtil(memberMobile, authCode);
        return authCode;
    }


    // 회원가입
    public void saveMember(MemberDTO memberDTO) {
        // 1. dto -> entity 변환
        // 2. repository의 saveMember 메서드 호출
        memberDTO.setRole("ROLE_MEMBER");
        String raqPassword = memberDTO.getMemberPw();
        String encPassword = bCryptPasswordEncoder.encode(raqPassword);
        memberDTO.setMemberPw(encPassword);
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);        // JPA가 제공하는 메서드 save
        // repository의 savemember메서드 호출 (조건 ; entity객체를 넘겨줘야함)
    }

    // 은행정보 저장
    public void saveBankInfo(String bankName, String accountNo, String accountDepositor){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println(userEmail);

        MemberEntity memberEntity = memberRepository.findByMemberEmail(userEmail).orElse(null);

        if(memberEntity != null){
            memberEntity.setBankName(bankName);
            memberEntity.setAccountNo(accountNo);
            memberEntity.setAccountDepositor(accountDepositor);
        }
    }

    // 판매자정보 저장
    public void saveCompanyInfo(String companyCeo,
                                String companyCreateDate,
                                String companyType,
                                String companyAddress,
                                String companyBusinessType,
                                String companyBusinessCategory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println(userEmail);

        MemberEntity memberEntity = memberRepository.findByMemberEmail(userEmail).orElse(null);

        if(memberEntity != null){
            memberEntity.setCompanyCeo(companyCeo);
            memberEntity.setCompanyCreateDate(companyCreateDate);
            memberEntity.setCompanyType(companyType);
            memberEntity.setCompanyAddress(companyAddress);
            memberEntity.setCompanyBusinessType(companyBusinessType);
            memberEntity.setCompanyBusinessCategory(companyBusinessCategory);
        }
    }

    // 사업자등록증, 통신판매업, 통장사본 저장
    public void saveLicenseImageFile(MultipartFile imageFile, String licenseType) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Long loginUserNo = getMemberIdByEmail(userEmail);
        MemberEntity memberEntity = memberRepository.findById(loginUserNo).orElse(null);
        if(memberEntity != null){
            if(licenseType.equals("business")){
                String licenseURL = fileUtil.saveLicenseImage(imageFile, loginUserNo,licenseType);
                memberEntity.setBusinessLicense(licenseURL);
                System.out.println("사업자등록증 등록 성공");
            }
            else if(licenseType.equals("telemarketing")){
                String licenseURL = fileUtil.saveLicenseImage(imageFile, loginUserNo,licenseType);
                memberEntity.setTelemarketingLicense(licenseURL);
                System.out.println("통신판매업등록증 등록 성공");
            }
            else if(licenseType.equals("account")){
                String licenseURL = fileUtil.saveLicenseImage(imageFile, loginUserNo,licenseType);
                memberEntity.setAccountLicense(licenseURL);
                System.out.println("통장사본 등록 성공");
            }
        }
    }




}
