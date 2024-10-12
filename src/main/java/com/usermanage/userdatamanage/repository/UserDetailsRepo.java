package com.usermanage.userdatamanage.repository;

import com.usermanage.userdatamanage.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails,Long> {

    @Query("SELECT u FROM UserDetails u WHERE u.UserName = ?1")
    UserDetails findByUserName(String userName);
}
