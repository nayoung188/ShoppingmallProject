package com.shoppingmall.repository;

import com.shoppingmall.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> { // 어떤 entity class를 사용하느냐, entity class의 pk 타입
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    @Query("SELECT m from MemberEntity m order by m.memberNo DESC")
    List<MemberEntity> findTop5ByOrderByMemberNo();

    @Query("SELECT m.memberNo FROM MemberEntity m WHERE m.memberEmail = :memberEmail")
    Long findMemberNoByMemberEmail(String memberEmail);

}
