package com.shoppingmall.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "prod_image")
public class ProdImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long prodImageId;
    @Column
    private String prodImageOriginName;
    @Column
    private String prodImageName;
    @Column
    private String prodImagePath;
    @Column
    private String prodImageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private ProductEntity productEntity;

    @Builder
    public ProdImageEntity(String prodImageOriginName, String prodImageName, String prodImagePath, String prodImageType){
        this.prodImageOriginName = prodImageOriginName;
        this.prodImageName = prodImageName;
        this.prodImagePath = prodImagePath;
        this.prodImageType = prodImageType;
    }


    public void updateItemImage(String prodImageOriginName, String prodImageName, String prodImagePath, String prodImageType){
        this.prodImageOriginName = prodImageOriginName;
        this.prodImageName = prodImageName;
        this.prodImagePath = prodImagePath;
        this.prodImageType = prodImageType;
    }
}
