package com.tunlin.service.impl;

import com.tunlin.config.JwtProvider;
import com.tunlin.model.User;
import com.tunlin.repository.UserRepository;
import com.tunlin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new Exception("User not found with email"+ email);
        }
        return user;

    }
}
