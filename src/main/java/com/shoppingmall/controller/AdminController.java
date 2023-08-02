package com.shoppingmall.controller;

import com.shoppingmall.dto.CategoryCreateRequest;
import com.shoppingmall.dto.CategoryDTO;
import com.shoppingmall.entity.MemberEntity;
import com.shoppingmall.service.CategoryService;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MemberService memberService;

// 회원

    @GetMapping("/admin/main")
    public String memberCateList(Model model){
        List<MemberEntity> member = memberService.getTop5Member();
        List<CategoryDTO> category = categoryService.getDecsCategoryList();
        model.addAttribute("members", member);
        model.addAttribute("categories", category);
        return "/admin/main";
    }

    @GetMapping("/admin/memberList")
    public String memberList(Model model, @PageableDefault(page = 0, size = 10, sort = "memberNo", direction = Sort.Direction.DESC)Pageable pageable){
        Page<MemberEntity> list = memberService.memberlist(pageable);
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("memberList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/admin/memberList";
    }


// 카테고리

    @GetMapping("/admin/categoryList")
    public ResponseEntity<?> categoryList(){
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    // 카테고리 리스트 페이지 이동
    @GetMapping("/admin/category")
    public String categoryList(Model model){
        List<CategoryDTO> categories = categoryService.getCategoryAscDepth();
        model.addAttribute("categories", categories);
        return "/admin/category";
    }

    @GetMapping("/admin/addCategory")
    public String categoryAdd(Model model){
        List<CategoryDTO> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "/admin/addCategory";
    }

    @PostMapping("/category/add")
    public String addCategory(@RequestParam(value = "name") String categoryName, @RequestParam(value = "parentId")int parentId, @RequestParam(value = "depth")int depth) {
        CategoryCreateRequest req = new CategoryCreateRequest();
        req.setName(categoryName);
        req.setParentId(parentId);
        req.setDepth(depth);
        categoryService.addCategory(req);
        return "redirect:/admin/category";
    }
}
