package com.shoppingmall.controller;

import com.shoppingmall.dto.CategoryDTO;
import com.shoppingmall.dto.ProductDTO;
import com.shoppingmall.entity.ProdImageEntity;
import com.shoppingmall.entity.ProductEntity;
import com.shoppingmall.service.CategoryService;
import com.shoppingmall.service.MemberService;
import com.shoppingmall.service.ProdImageService;
import com.shoppingmall.service.ProductService;
import com.shoppingmall.util.GptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private GptUtil gptUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProdImageService prodImageService;

    @Autowired
    private MemberService memberService;

    @PostMapping("/gptCategory")
    @ResponseBody
    public List<String> gptCategory(@RequestBody Map<String,List<String>> requestData){
        List<String> checkedLabels = requestData.get("checkedLabels");
        List<String> recommendations = gptUtil.categoryRecommendation(checkedLabels);
        System.out.println(recommendations);
        return recommendations;
    }

    @GetMapping("/dashboard/product")
    public String dashboardProduct(Model model,
                                   @PageableDefault(page = 0, sort = "productNo", direction = Sort.Direction.DESC) Pageable pageable,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "productName", required = false) String productName,
                                   @RequestParam(value = "productCode",required = false) String productCode,
                                   @RequestParam(value = "productBrand",required = false) String productBrand,
                                   @RequestParam(value = "manufacturer",required = false) String manufacturer,
                                   @RequestParam(value = "productState",required = false) List<String> productState,
                                   @RequestParam(value = "searchKeyword", required = false)String searchKeyword){
        Pageable changeSize = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
        Page<ProductEntity> productEntities = productService.searchList(changeSize,searchKeyword,productName,productCode,productBrand,manufacturer,productState);
        List<CategoryDTO> category = categoryService.getAllCategoryList();

        System.out.println("pageSize ::: " + pageSize);

        Long dataCount = productService.getProductCountByConditions(searchKeyword, productState, productName, productCode, productBrand, manufacturer);
        Long allCount = productService.countByState(null);
        Long saleCount = productService.countByState("판매중");
        Long soldOutCount = productService.countByState("품절");
        Long stopCount = productService.countByState("판매중지");

        int nowPage = productEntities.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, productEntities.getTotalPages());

        model.addAttribute("product", productEntities);
        model.addAttribute("category", category);
        model.addAttribute("dataCount", dataCount);
        model.addAttribute("allCount", allCount);
        model.addAttribute("saleCount", saleCount);
        model.addAttribute("soldOutCount", soldOutCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageSize", pageSize);

        return "dashboard/product";
    }

    @GetMapping("/dashboard/productInsert")
    public String dashboardProductInsert(Model model){
        List<CategoryDTO> category = categoryService.getAllCategoryList();
        model.addAttribute("category", category);
        return "dashboard/productInsert";
    }


    @PostMapping("/insertProduct")
    public String insertProduct(@ModelAttribute ProductDTO productDTO,
                                @RequestParam("mainImage") MultipartFile mainImage,
                                @RequestParam("subImages") List<MultipartFile> subImages) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        Long memberId = (Long)memberService.getMemberIdByEmail(userEmail);

        try{
            productService.saveProduct(productDTO, mainImage, subImages, memberId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/dashboard/product";
    }

    @PostMapping(value = "/uploadSummernoteImageFile", produces = "application/json")
    @ResponseBody
    public Map<String, Object> uploadSummernoteImageFile(MultipartHttpServletRequest multipartRequest){
        return prodImageService.saveSummernoteImage(multipartRequest);
    }


    @GetMapping("/dashboard/productDetail")
    public String dashboardProductDetail(@RequestParam(value = "productNo", required = false, defaultValue = "0")Long productNo, Model model){
        List<ProductEntity> product = productService.getProductByNo(productNo);
        ProductEntity productEntity = product.get(0);
        List<ProdImageEntity> image = productEntity.getProdImageEntities();
        List<CategoryDTO> category = categoryService.getAllCategoryList();

        model.addAttribute("product", product);
        model.addAttribute("prodImage", image);
        model.addAttribute("category", category);

        return "dashboard/productDetail";
    }

    @PostMapping("/modifyProduct/{productNo}")
    public String productModify(@PathVariable("productNo")Long productNo,
                                @ModelAttribute ProductDTO productDTO,
                                @RequestParam("mainImage") MultipartFile mainImage,
                                @RequestParam("subImages") List<MultipartFile> subImages){
        try{
            productService.modifyProduct(productNo, productDTO, mainImage, subImages);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/dashboard/productDetail?productNo=" + productNo;
    }


    @PostMapping("/deleteProduct")
    public String productDelete(@RequestParam(value = "productNo")Long[] productNo){
        productService.deleteProduct(productNo);
        return "redirect:/dashboard/product";
    }

    @PostMapping("/updateProductState")
    public String productStateUpdate(@RequestParam(value = "productNo")Long[] productNo,
                                     @RequestParam(value = "productState") String productState){
        productService.stateUpdate(productNo, productState);
        return "redirect:/dashboard/product";
    }

}
