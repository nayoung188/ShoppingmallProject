package com.shoppingmall.entity;

import com.shoppingmall.dto.ProdImageDTO;
import com.shoppingmall.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="product")
@DynamicInsert
public class ProductEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Long productNo;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_brand")
    private String productBrand;

    @Column
    private String manufacturer;

    @Column
    private int originalPrice;

    @Column
    private int salePrice;

    @Column(name = "product_state")
    private String productState;

    @Column
    private int productStock;

    @Lob
    private String productContent;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private CategoryEntity CategoryEntity;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProdImageEntity> prodImageEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_no")
    private MemberEntity MemberEntity;

    public void addProdImage(ProdImageEntity prodImage){
        prodImageEntities.add(prodImage);
        prodImage.setProductEntity(this);
    }

    public static ProductEntity toProductEntity(ProductDTO productDTO){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductCode(productDTO.getProductCode());
        productEntity.setProductBrand(productDTO.getProductBrand());
        productEntity.setManufacturer(productDTO.getManufacturer());
        productEntity.setOriginalPrice(productDTO.getOriginalPrice());
        productEntity.setSalePrice(productDTO.getSalePrice());
        productEntity.setProductState(productDTO.getProductState());
        productEntity.setProductStock(productDTO.getProductStock());
        productEntity.setProductContent(productDTO.getProductContent());

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(productDTO.getCategoryId());
        productEntity.setCategoryEntity(categoryEntity);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberNo(productDTO.getMemberNo());
        productEntity.setMemberEntity(memberEntity);

        List<ProdImageEntity> prodImageEntities = new ArrayList<>();
        for(ProdImageDTO prodImageDTO : productDTO.getProdImageDtoList()) {
            ProdImageEntity prodImageEntity = new ProdImageEntity();
            prodImageEntity.setProdImageId(prodImageDTO.getProdImageId());
            prodImageEntity.setProductEntity(productEntity);
            prodImageEntities.add(prodImageEntity);
        }
        productEntity.setProdImageEntities(prodImageEntities);


        return productEntity;
    }


}
