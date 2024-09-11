package com.geumbang.servera.auth.filter;

import com.geumbang.servera.auth.dto.UserCustom;
import com.geumbang.servera.auth.service.AuthServiceClient;
import com.geumbang.servera.auth.util.JwtUtil;
import com.geumbang.servera.common.Constants;
import com.geumbang.servera.entity.User;
import com.geumbang.servera.grpc.auth.TokenResponse;
import com.geumbang.servera.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인이나 회원가입 경로는 JWT 검증을 건너뛰기
        String requestURI = request.getRequestURI();
        // AntPathMatcher 객체 생성
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // 허용할 URI 목록
        List<String> allowedURIs = Arrays.asList(
                "/api/login",
                "/api/join"
//                "/swagger-ui/**",
//                "/swagger-resources/**",
//                "/v3/api-docs/**",
//                "/webjars/**",
//                "/swagger-ui.html",
//                "/swagger.json",
//                "/configuration/**",
//                "/v3/api-docs/swagger-config",
//                "/**"
        );

        // 허용된 URI인지 확인
        boolean isAllowedURI = allowedURIs.stream()
                .anyMatch(allowedURI -> pathMatcher.match(allowedURI, requestURI));

        if (isAllowedURI) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Server B에 acs token 토큰 검증 요청
            String acsToken = jwtUtil.getAcsToken(request);
            boolean acsIsValid = false;
            if(acsToken != null) {
                acsIsValid = authServiceClient.validateAcsToken(acsToken).getIsValid();
            }

            // Server B에 rfr token 토큰 검증 요청
            String rfrToken = jwtUtil.getRfrToken(request);
            boolean rfrIsValid = false;
            if(rfrToken != null) {
                rfrIsValid = authServiceClient.validateRfrToken(rfrToken).getIsValid();
            }

            //1. acs Token 유효성 검사
            if (acsIsValid) {
                //유효할 경우
                //1) 아이디, 역할 추출
                String id = jwtUtil.getIdFromToken(acsToken);
                String role = jwtUtil.getRoleFromToken(acsToken);
                //2) 인증 객체 생성
                setSecurity(id, role);
            } else {
                // 유효하지 않을 경우
                //1) rfr token 만료 검사
                if (rfrIsValid) {
                    // rfr token이 유효할 경우
                    String rfrId = jwtUtil.getIdFromToken(rfrToken);
                    User user = userRepository.findByUserId(rfrId)
                                                .orElseThrow(() -> new EntityNotFoundException(Constants.USER_NOT_FOUND));

                    TokenResponse tokenResponse = authServiceClient.generateToken(user.getUserId(), user.getRole().toString());
                    String newAcsToken = tokenResponse.getAccessToken();
                    String newRfrToken = tokenResponse.getRefreshToken();

                    //3) 헤더와 쿠키에 각각 저장
                    jwtUtil.addAcsToken(response, newAcsToken);
                    jwtUtil.addRfrToken(response, newRfrToken);

                    //4) 인증 객체 저장
                    setSecurity(rfrId, jwtUtil.getRoleFromToken(newAcsToken));

                } else {
                    // rfr token이 유효하지 않을 경우
                    // 1) acs token, rfr token 삭제
                    jwtUtil.deleteAcsToken(response);
                    jwtUtil.deleteRfrToken(response);
                    log.info("Jwt token 모두 유효하지 않아 Exception 발생시킴");
                    throw new JwtException(Constants.JWT_EXPIRED);
                }
            }
        } catch (Exception e) {
            log.info("Jwt token Filter에서 Exception 발생 - jwt 만료 Exception 아님");
            throw new JwtException(Constants.JWT_EXCEPTION);
        }
            filterChain.doFilter(request, response);
    }

    private void setSecurity(String id, String role){
        UserCustom userCustom = new UserCustom(id, role);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userCustom, null, userCustom.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
