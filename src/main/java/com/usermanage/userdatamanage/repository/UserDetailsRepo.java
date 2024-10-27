package com.usermanage.userdatamanage.repository;

import com.usermanage.userdatamanage.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetail,Long> {

    @Query("SELECT u FROM UserDetail u WHERE u.UserName = ?1")
    Optional<UserDetail> findByUserName(String userName);
}
