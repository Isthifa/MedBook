package com.usermanage.userdatamanage.jwt.Repo;


import com.usermanage.userdatamanage.entity.UserDetail;
import com.usermanage.userdatamanage.jwt.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserDetail(UserDetail userDetail);
    void deleteByUserDetail(UserDetail userDetail);
}