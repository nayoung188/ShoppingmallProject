package com.shoppingmall.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryDTO {
    private long deliveryNo;
    private String deliveryName;
    private String deliveryAddress;
    private String deliveryTel;
    private Long memberNo;
}
