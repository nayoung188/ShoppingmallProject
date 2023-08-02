package com.shoppingmall.dto;

import com.shoppingmall.entity.ProdImageEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdImageDTO {
    private Long prodImageId;
    private String prodImageOriginName;
    private String prodImageName;
    private String prodImagePath;
    private String prodImageType;

    @Builder
    public ProdImageDTO(String prodImageOriginName, String prodImageName, String prodImagePath, String prodImageType){
        this.prodImageOriginName = prodImageOriginName;
        this.prodImageName = prodImageName;
        this.prodImagePath = prodImagePath;
        this.prodImageType = prodImageType;
    }

    public ProdImageEntity toEntity(ProdImageDTO dto){
        ProdImageEntity entity = ProdImageEntity.builder()
                .prodImageOriginName(dto.prodImageOriginName)
                .prodImageName(dto.prodImageName)
                .prodImagePath(dto.prodImagePath)
                .prodImageType(dto.prodImageType)
                .build();
        return entity;
    }

    public ProdImageDTO of(ProdImageEntity entity){
        ProdImageDTO dto = ProdImageDTO.builder()
                .prodImageOriginName(entity.getProdImageOriginName())
                .prodImageName(entity.getProdImageName())
                .prodImagePath(entity.getProdImagePath())
                .prodImageType(entity.getProdImageType())
                .build();
        return dto;
    }

}
