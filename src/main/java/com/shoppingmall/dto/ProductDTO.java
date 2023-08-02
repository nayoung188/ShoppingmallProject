package com.shoppingmall.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {
    private long productNo;
    private String productName;
    private String productCode;
    private String productBrand;
    private String manufacturer;
    private int originalPrice;
    private int salePrice;
    private String productState;
    private int productStock;
    private String productContent;
    private int categoryId;
    private List<ProdImageDTO> prodImageDtoList = new ArrayList<>();
    private Long memberNo;

}
