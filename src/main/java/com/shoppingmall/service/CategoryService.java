package com.shoppingmall.service;

import com.shoppingmall.dto.CategoryCreateRequest;
import com.shoppingmall.dto.CategoryDTO;
import com.shoppingmall.entity.CategoryEntity;
import com.shoppingmall.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getCategoryList(){
        List<CategoryEntity> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDTO.toDtoList(categories);
    }

    // 카테고리 리스트 출력할때 상위 카테고리 삽입하기
    private List<CategoryDTO> convertCate(List<CategoryEntity> categories){
        List<CategoryDTO> list = new ArrayList<>();
        for( CategoryEntity category : categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(category.getCategoryId());
            categoryDTO.setCategoryName(category.getCategoryName());
            categoryDTO.setDepth(category.getDepth());
            CategoryEntity parent = category.getParent();
            if(parent != null){
                categoryDTO.setParentId(parent.getCategoryId());
                categoryDTO.setParentName(parent.getCategoryName());
                categoryDTO.setDepth(category.getDepth());
            }

            list.add(categoryDTO);
        }
        return list;
    }

    // 모든 카테고리 리스트 출력
    public List<CategoryDTO> getAllCategoryList(){
        List<CategoryEntity> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return convertCate(categories);
    }

    public List<CategoryDTO> getDecsCategoryList(){
        List<CategoryEntity> category = categoryRepository.findAllByOrderByCategoryIdDesc();
        return convertCate(category);
    }
    public List<CategoryDTO> getCategoryAscDepth(){
        List<CategoryEntity> category = categoryRepository.findAllOrderByDepthAndCategoryId();
        return convertCate(category);
    }


    // 카테고리 만들기
    @Transactional
    public void addCategory(CategoryCreateRequest req){
        int depth = req.getDepth();
        int DEFAULT_PARENT_ID = 0;
        CategoryEntity parent = Optional.ofNullable(req.getParentId())
                .flatMap(id -> categoryRepository.findById(id))
                .orElseGet(() -> categoryRepository.findById(DEFAULT_PARENT_ID).orElse(null));
        CategoryEntity category = new CategoryEntity(req.getName(), parent);
        category.setDepth(depth);
        categoryRepository.save(category);
    }



}
