package com.tunlin.service;

import com.tunlin.domain.USER_ROLE;
import com.tunlin.request.LoginRequest;
import com.tunlin.response.AuthResponse;
import com.tunlin.response.SignupRequest;


public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse signing(LoginRequest loginRequest) throws Exception;

}
