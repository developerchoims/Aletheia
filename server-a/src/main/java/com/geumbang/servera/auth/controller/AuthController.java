package com.geumbang.servera.auth.controller;

import com.geumbang.servera.auth.dto.UserLoginDto;
import com.geumbang.servera.auth.service.AuthServiceClient;
import com.geumbang.servera.common.Constants;
import com.geumbang.servera.entity.User;
import com.geumbang.servera.repository.UserRepository;
import com.geumbang.servera.grpc.auth.*;
import com.geumbang.servera.auth.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "로그인", description = "로그인 컨트롤러에 대한 설명입니다.")
public class AuthController {

    private final AuthServiceClient authServiceClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Operation(summary = "로그인 요청", description = "server-a에서 아이디, 비밀번호 확인 후 server-b로 JWT Token 발급을 요청합니다.")
    @Parameter(name = "userId", description = "유저 아이디")
    @Parameter(name = "password", description = "유저 비밀번호")
    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.")
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
            return ResponseEntity.ok(Constants.LOGIN_SUCCESS);
        } catch (EntityNotFoundException | BadCredentialsException e){
            log.error(e.getMessage());
            throw e;
        } catch (Exception e){
            log.error(Constants.JWT_EXCEPTION);
            throw new JwtException(Constants.JWT_EXCEPTION);
        }
    }

}
