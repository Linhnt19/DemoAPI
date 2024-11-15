package com.tunlin.service.impl;

import com.tunlin.config.JwtProvider;
import com.tunlin.domain.USER_ROLE;
import com.tunlin.model.Cart;
import com.tunlin.model.Seller;
import com.tunlin.model.User;
import com.tunlin.model.VerificationCode;
import com.tunlin.repository.CartRepository;
import com.tunlin.repository.SellerRepository;
import com.tunlin.repository.UserRepository;
import com.tunlin.repository.VerificationCodeRepository;
import com.tunlin.request.LoginRequest;
import com.tunlin.response.AuthResponse;
import com.tunlin.response.SignupRequest;
import com.tunlin.service.AuthService;
import com.tunlin.service.EmailService;
import com.tunlin.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNING_PREFIX = "signing_";
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_SELLER)){
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null){
                    throw new Exception("Seller not found");
                }
            }else {
                System.out.println(email);
                User user = userRepository.findByEmail(email);
                if (user == null){
                    throw new Exception("User not exit with provided email");
                }
            }

        }
        VerificationCode isExit = verificationCodeRepository.findByEmail(email);

        if (isExit != null){
            verificationCodeRepository.delete(isExit);
        }

        String otp = OtpUtil.generateOtp();
        System.out.println(otp);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);
        String subject ="Lin login/signup otp";
        String text ="<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <title>Document</title>\n"
                + "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
                + "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
                + "  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@100..900&display=swap\" rel=\"stylesheet\">\n"
                + "  <style>\n"
                + "    body {\n"
                + "      font-family: 'Inter', sans-serif;\n"
                + "    }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div style=\"min-width: 1000px; overflow: auto; line-height: 2;\">\n"
                + "    <div style=\"margin: 50px auto; width: 70%; padding: 20px 0;\">\n"
                + "      <div style=\"border-bottom: 1px solid #eee;\">\n"
                + "        <a href=\"\" style=\"font-size: 1.2em; color: #2365d0; text-decoration: none; font-weight: bold;\">Quản lý tài khoản\n </a>\n"
                + "      </div>\n"
                + "      <p style=\"font-size: 1em;\">Xin chào, chúng tôi đã nhận được yêu cầu xác .</p>\n"
                + "      <p style=\"font-size: 1em;\">Your login/sigup otp is::</p>\n"
                + "      <h2\n"
                + "        style=\"background: #e1eefb; margin: 0 auto; width: max-content; padding: 0px 10px; color: #161616; border-radius: 5px; box-shadow: 0 0 0 1px #1877f2; font-size: 1.3em; font-weight: bold;\">\n"
                + otp
                + "      </h2>\n"
                + "      <br>\n"
                + "      <hr style=\"border: none; border-top: 1px solid #eee;\">\n"
                + "      <div style=\"float: right; padding: 8px 0; color: #aaa; font-size: 0.9em; line-height: 1; font-weight: 300;\">\n"
                + "        <p>Website ...</p>\n"
                + "      </div>\n"
                + "    </div>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>";

        emailService.sendVerificationOtpEmail(email,otp,subject,text);

    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("wrong otp ...");
        }
        User user = userRepository.findByEmail(req.getEmail());

        if (user == null){

            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER); // khach hang
            createdUser.setMobile("0398376053");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);

        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);

    }

    @Override
    public AuthResponse signing(LoginRequest loginRequest) throws Exception {
        
        String username =loginRequest.getEmail();
        String otp = loginRequest.getOtp();
        // Kiểm tra nếu username hoặc otp rỗng
        if (username == null || username.isEmpty() || otp == null || otp.isEmpty()) {
            throw new IllegalArgumentException("Email và OTP không được để trống");
        }
        System.out.println(username);
        System.out.println(otp);
        Authentication authentication = authenticate(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {

        UserDetails userDetails = customUserService.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)){
            username = username.substring(SELLER_PREFIX.length());
        }

        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
