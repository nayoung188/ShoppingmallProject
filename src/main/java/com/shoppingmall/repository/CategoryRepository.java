package com.shoppingmall.repository;

import com.shoppingmall.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Query("SELECT c FROM CategoryEntity c LEFT JOIN c.parent p order by p.categoryId asc nulls first, c.categoryId asc")
    List<CategoryEntity> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();

    List<CategoryEntity> findAllByOrderByCategoryIdDesc();

    @Query("select c from CategoryEntity c order by c.depth ASC, c.categoryId ASC ")
    List<CategoryEntity> findAllOrderByDepthAndCategoryId();

}
