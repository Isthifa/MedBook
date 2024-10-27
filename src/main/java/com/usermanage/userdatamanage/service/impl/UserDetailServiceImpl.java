package com.usermanage.userdatamanage.service.impl;

import com.usermanage.userdatamanage.Exception.PassNotValidException;
import com.usermanage.userdatamanage.entity.UserDetail;
import com.usermanage.userdatamanage.repository.UserDetailsRepo;
import com.usermanage.userdatamanage.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String saveUserCredentials(String userName, String passWord) {
        if(userName != null && passWord != null){
            Boolean validation=passWordValidation(passWord);
            if(!validation){
                throw new PassNotValidException("password is not valid");
            }
            UserDetail userDetail = new UserDetail();
            userDetail.setUserName(userName);
            userDetail.setPassWord(passwordEncoder.encode(passWord));
            userDetailsRepo.save(userDetail);
            return "User Credentials Saved Successfully";
        }
        return "User Credentials Not Saved";
    }

    @Override
    public String getUserCredentials(String userName, String passWord) {
        Optional<UserDetail> userDetail = userDetailsRepo.findByUserName(userName);
        if(userDetail != null){
            if(userDetail.get().getPassWord().equals(passwordEncoder.encode(passWord))){
                return "User Credentials Matched";
            }
            return "User Credentials Not Matched";
        }
        return "User Credentials Not Found";
    }

    @Override
    public String updateUserCredentials(String userName, String passWord) {
        Optional<UserDetail> userDetail = userDetailsRepo.findByUserName(userName);
        if(userDetail != null){
            userDetail.get().setPassWord(passWord);
            userDetailsRepo.save(userDetail.get());
            return "User Credentials Updated Successfully";
        }
        return "User Credentials Not Updated";
    }

    private boolean passWordValidation(String passWord) {

        final int MinSize = 8;

        if (passWord.length() < MinSize) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char c : passWord.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        if (hasDigit && hasLowerCase && hasUpperCase && hasSpecialChar) {
            return true;
        }
        return false;
    }

}
