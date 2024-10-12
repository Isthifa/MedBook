package com.usermanage.userdatamanage.service;

public interface UserDetailsService {

     String saveUserCredentials(String userName, String passWord);
     String getUserCredentials(String userName, String passWord);
     String updateUserCredentials(String userName, String passWord);
}
