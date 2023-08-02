package com.shoppingmall.spec;

import com.shoppingmall.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Component
public class ProductSpecification {

    public static Specification<ProductEntity> containsSearchKeyword(String searchKeyword){
        return StringUtils.isEmpty(searchKeyword)
                ? null
                : (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("productName"), "%" + searchKeyword + "%"),
                criteriaBuilder.like(root.get("productCode"), "%" + searchKeyword + "%"),
                criteriaBuilder.like(root.get("productBrand"), "%" + searchKeyword + "%"),
                criteriaBuilder.like(root.get("manufacturer"), "%" + searchKeyword + "%")
        );
    }

    public static Specification<ProductEntity> equalsProductName(String productName){
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("productName"), "%" + productName + "%");
    }

    public static Specification<ProductEntity> equalsProductCode(String productCode){
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("productCode"), "%" + productCode + "%");
    }

    public static Specification<ProductEntity> equalsProductBrand(String productBrand){
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("productBrand"), "%" + productBrand + "%");
    }

    public static Specification<ProductEntity> equalsManufacturer(String manufacturer) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("manufacturer"), "%" + manufacturer + "%");
    }

    public static Specification<ProductEntity> equalsProductState(String productState) {
        return StringUtils.isEmpty(productState)
                ? null
                : (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productState"), productState);
    }

    public static Specification<ProductEntity> equalsCheckedProductState(List<String> productStates) {
        return StringUtils.isEmpty(productStates)
                ? null
                :(root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (String state : productStates) {
                    if (!StringUtils.isEmpty(state)) {
                        predicates.add(criteriaBuilder.equal(root.get("productState"), state));
                    }
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

}
