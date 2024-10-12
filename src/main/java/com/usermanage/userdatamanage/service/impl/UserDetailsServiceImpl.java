package com.usermanage.userdatamanage.service.impl;

import com.usermanage.userdatamanage.Exception.PassNotValidException;
import com.usermanage.userdatamanage.entity.UserDetails;
import com.usermanage.userdatamanage.repository.UserDetailsRepo;
import com.usermanage.userdatamanage.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Override
    public String saveUserCredentials(String userName, String passWord) {
        if(userName != null && passWord != null){
            Boolean validation=passWordValidation(passWord);
            if(!validation){
                throw new PassNotValidException("password is not valid");
            }
            UserDetails userDetail = new UserDetails();
            userDetail.setUserName(userName);
            userDetail.setPassWord(passWord);
            userDetailsRepo.save(userDetail);
            return "User Credentials Saved Successfully";
        }
        return "User Credentials Not Saved";
    }

    @Override
    public String getUserCredentials(String userName, String passWord) {
        UserDetails userDetails = userDetailsRepo.findByUserName(userName);
        if(userDetails != null){
            if(userDetails.getPassWord().equals(passWord)){
                return "User Credentials Matched";
            }
            return "User Credentials Not Matched";
        }
        return "User Credentials Not Found";
    }

    @Override
    public String updateUserCredentials(String userName, String passWord) {
        UserDetails userDetails = userDetailsRepo.findByUserName(userName);
        if(userDetails != null){
            userDetails.setPassWord(passWord);
            userDetailsRepo.save(userDetails);
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
        if (hasDigit && hasLowerCase && hasLowerCase && hasSpecialChar) {
            return true;
        }
        return false;
    }

}
