package com.geumbang.servera.auth.controller;

import com.geumbang.servera.auth.dto.UserLoginDto;
import com.geumbang.servera.auth.service.AuthServiceClient;
import com.geumbang.servera.common.Constants;
import com.geumbang.servera.entity.User;
import com.geumbang.servera.repository.UserRepository;
import com.geumbang.servera.grpc.auth.*;
import com.geumbang.servera.auth.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthServiceClient authServiceClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody UserLoginDto user) {
        // 1) Server A에서 User Login정보 확인
        User storedUser = userRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.USER_NOT_FOUND));

        // 2) 비밀번호 일치 여부 확인
        if (!storedUser.getUserPwd().equals(user.getPassword())) {
            throw new BadCredentialsException(Constants.PWD_DIFF);
        }

        try {
            // 3) 비밀번호 일치하는 경우, Server B에 및 JWT 발급요청
            TokenResponse tokenResponse = authServiceClient.generateToken(storedUser.getUserId(), storedUser.getRole().toString());

            // 4) tokenResponse에서 acs token, rfr token추출하기
            String acsToken = tokenResponse.getAccessToken();
            String rfrToken = tokenResponse.getRefreshToken();

            // 5) 각각 헤더와 cookie에 추가
            jwtUtil.addAcsToken(response, acsToken);
            jwtUtil.addRfrToken(response, rfrToken);


            // 발급받은 JWT 클라이언트에 반환
            return ResponseEntity.ok(Constants.JWT_SUCCESS);
        } catch (EntityNotFoundException | BadCredentialsException e){
            log.error(e.getMessage());
            throw e;
        } catch (Exception e){
            log.error(Constants.JWT_EXCEPTION);
            throw new JwtException(Constants.JWT_EXCEPTION);
        }
    }

}
