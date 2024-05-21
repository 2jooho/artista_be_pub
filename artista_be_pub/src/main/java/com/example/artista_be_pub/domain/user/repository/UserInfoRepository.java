package com.artista.main.domain.user.repository;

import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserEntity, String> {

    /**
     * 아이디 조회
     * @return
     */
    Optional<UserEntity> findByUserId(String userId);

    /**
     * 기본 회원 정보 조회
     * @return
     */
    Optional<UserEntity> findByUserIdAndLoginDvsn(String userId, String loginDvsn);

    /**
     * 회원 정보 조회(이름, 핸드폰)
     * @param userName
     * @param phoneNumber
     * @return
     */
    Optional<UserEntity> findByNameAndPhone(String userName, String phoneNumber);

    /**
     * 유저 개수 조회
     * @return
     */
    Optional<Integer> countByUserIdAndLoginDvsn(String userId, String loginDvsn);

    /**
     * 유저 개수 조회
     * @return
     */
    Optional<Integer> countByNickName(String userId);

}
