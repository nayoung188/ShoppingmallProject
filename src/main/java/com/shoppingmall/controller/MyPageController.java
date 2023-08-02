package com.shoppingmall.controller;

import com.shoppingmall.dto.DeliveryDTO;
import com.shoppingmall.entity.DeliveryEntity;
import com.shoppingmall.entity.MemberEntity;
import com.shoppingmall.service.MemberService;
import com.shoppingmall.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Controller
public class MyPageController {

    @Autowired
    private MyPageService myPageService;
    @Autowired
    private MemberService memberService;
    @Value("${open.api.encoding.key}")
    private String openApiEncodingKey;
    @Value("${open.api.decoding.key}")
    private String openApiDecodingKey;


    @GetMapping("/dashboard/myInformation")
    public String memberInformation(){
        return "/dashboard/mypage/memberInfo";
    }

    @GetMapping("/dashboard/sellerInformation")
    public String sellerInformation(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberEmail = authentication.getName();

        MemberEntity memberInfo = memberService.getMember(memberEmail);
        List<DeliveryEntity> delivery = myPageService.getDeliveryList();

        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("delivery", delivery);

        return "/dashboard/mypage/sellerInfo";
    }

    @GetMapping("/dashboard/sellerInformation/insert")
    public String insertSellerInformation(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberEmail = authentication.getName();

        MemberEntity memberInfo = memberService.getMember(memberEmail);

        model.addAttribute("memberInfo", memberInfo);
        return "/dashboard/mypage/sellerInfoInsert";
    }


    @PostMapping("/uploadLicense")
    @ResponseBody
    public void uploadLicenses(@RequestParam("file")MultipartFile multipartFile,
                               @RequestParam("licenseType") String licenseType){
        System.out.println(licenseType);
        try{
            memberService.saveLicenseImageFile(multipartFile, licenseType);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 판매자정보등록
    @PostMapping("/insertSellerInfo")
    public String insertSellerInfo(@RequestBody Map<String, String> sellerInfoMap){
        memberService.saveCompanyInfo(
                sellerInfoMap.get("companyCeo"),
                sellerInfoMap.get("companyCreateDate"),
                sellerInfoMap.get("companyType"),
                sellerInfoMap.get("companyAddress"),
                sellerInfoMap.get("companyBusinessType"),
                sellerInfoMap.get("companyBusinessCategory")
        );
        return "redirect:/dashboard/sellerInformation";
    }

    // 정산정보등록
    @PostMapping("/insertAccount")
    public String insertAccount(@RequestBody Map<String, String> bankInfoMap){
        memberService.saveBankInfo(
                bankInfoMap.get("bankName"),
                bankInfoMap.get("accountNo"),
                bankInfoMap.get("accountDepositor")
        );
        return "redirect:/dashboard/sellerInformation";
    }

    // 배송정책등록
    @PostMapping("/insertDelivery")
    public String insertDelivery(@ModelAttribute DeliveryDTO deliveryDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        Long memberId = (Long)memberService.getMemberIdByEmail(userEmail);
        myPageService.addDelivery(deliveryDTO, memberId);
        return "redirect:/dashboard/sellerInformation";
    }

    // 배송정책수정
    @PostMapping("/modifyDelivery")
    public String modifyDelivery(Long deliveryNo,
                                 @ModelAttribute DeliveryDTO deliveryDTO){
        myPageService.modifyDelivery(deliveryNo, deliveryDTO);
        return "redirect:/dashboard/sellerInformation";
    }

    // 배송정책삭제
    @PostMapping("/deleteDelivery")
    public String deleteDelivery(Long deliveryNo){
        myPageService.deleteDelivery(deliveryNo);
        return "redirect:/dashboard/sellerInformation";
    }


}
