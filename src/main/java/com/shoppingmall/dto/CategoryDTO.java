package com.shoppingmall.dto;

import com.shoppingmall.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private int parentId;
    private int categoryId;
    private String categoryName;
    private int depth;
    private String parentName;
    private List<CategoryDTO> children;


    public static List<CategoryDTO> toDtoList(List<CategoryEntity> categories) {
        CategoryHelper helper = CategoryHelper.newInstance(
                categories,
                c -> {
                    CategoryEntity parent = c.getParent();
                    int parentId = (parent != null) ? parent.getCategoryId() : 0;
                    String parentName = (parent != null) ? parent.getCategoryName() : "";
                    CategoryDTO categoryDTO = new CategoryDTO(parentId, c.getCategoryId(), c.getCategoryName(), c.getDepth(), parentName, new ArrayList<>());
                    return categoryDTO;
                },
                CategoryEntity::getParent,
                CategoryEntity::getCategoryId,
                CategoryDTO::getChildren);
        return helper.convert();
    }

}


