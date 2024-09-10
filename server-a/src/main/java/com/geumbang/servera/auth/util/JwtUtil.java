package com.geumbang.servera.auth.util;

import com.geumbang.servera.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    private final long ACS_EXPIRATION_TIME = 1000 * 60 * 15; //15q분
    private final long RFR_EXPIRATION_TIME = 1000* 60 * 60 * 24 * 7; //일주일

    public final Key key;
    public JwtUtil(@Value("${secretKey}") String SECRETKEY) {
        this.key =new SecretKeySpec(SECRETKEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    // 헤더에 acs token 추가
    public void addAcsToken(HttpServletResponse response, String token) {
        response.setHeader("Authorization", "Bearer " + token);
    }

    // 헤더에서 acs token 추출
    public String getAcsToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            return null;
        }
    }

    //쿠키에 rfr token 추가
    public void addRfrToken(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("rfr_token", token)
                .httpOnly(true)
                .maxAge(RFR_EXPIRATION_TIME / 1000)
                .secure(true)
                .sameSite("Strict")
                .build();
        // 쿠키를 응답에 추가
        response.addHeader("Set-Cookie", cookie.toString());
    }

    //쿠키에서 rfr token 추출
    public String getRfrToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("rfr_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //토큰에서 id값 추출
    public String getIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // claims에서 subject 추출 (보통 id 값으로 설정됨)
            return Optional.of(claims.get("userId").toString())
                            .orElseThrow(()-> new JwtException(Constants.JWT_EXCEPTION));

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException(Constants.JWT_EXCEPTION);
        }
    }

    //토큰에서 role값 추출
    public String getRoleFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role").toString();
    }

    // 헤더에서 acs token 삭제
    public void deleteAcsToken(HttpServletResponse response) {
        response.setHeader("Authorization", null);  // 헤더에서 Authorization 삭제
    }

    // 쿠키에서 rfr token 삭제
    public void deleteRfrToken(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("rfr_token", "")
                .httpOnly(true)
                .maxAge(0)  // 쿠키 만료 설정
                .secure(true)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());  // 쿠키 삭제
    }
}
