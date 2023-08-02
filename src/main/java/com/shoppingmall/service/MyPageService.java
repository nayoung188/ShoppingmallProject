package com.shoppingmall.service;

import com.shoppingmall.dto.DeliveryDTO;
import com.shoppingmall.entity.DeliveryEntity;
import com.shoppingmall.entity.MemberEntity;
import com.shoppingmall.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<DeliveryEntity> getDeliveryList(){
        return deliveryRepository.findAll();
    }

    public void addDelivery(DeliveryDTO deliveryDTO, Long memberId){
        DeliveryEntity deliveryEntity = DeliveryEntity.toDeliveryEntity(deliveryDTO);
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberNo(memberId);
        deliveryEntity.setMemberEntity(memberEntity);
        deliveryRepository.save(deliveryEntity);
    }

    public void deleteDelivery(Long deliveryNo){
        deliveryRepository.deleteById(deliveryNo);
    }

    public void modifyDelivery(Long deliveryNo, DeliveryDTO deliveryDTO){
        DeliveryEntity deliveryEntity = deliveryRepository.findById(deliveryNo).orElse(null);
        if(deliveryEntity != null){
            DeliveryEntity updateDelivery = DeliveryEntity.toDeliveryEntity(deliveryDTO);
            updateDelivery.setDeliveryNo(deliveryDTO.getDeliveryNo());
            deliveryRepository.save(updateDelivery);
        }
    }
}
