package com.shoppingmall.entity;

import com.shoppingmall.dto.DeliveryDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "delivery")
@DynamicInsert
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryNo;

    @Column
    private String deliveryName;

    @Column
    private String deliveryAddress;

    @Column
    private String deliveryTel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_no")
    private MemberEntity memberEntity;

    public static DeliveryEntity toDeliveryEntity(DeliveryDTO deliveryDTO){
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setDeliveryName(deliveryDTO.getDeliveryName());
        deliveryEntity.setDeliveryAddress(deliveryDTO.getDeliveryAddress());
        deliveryEntity.setDeliveryTel(deliveryDTO.getDeliveryTel());

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberNo(deliveryDTO.getMemberNo());
        deliveryEntity.setMemberEntity(memberEntity);

        return deliveryEntity;
    }

}
