package com.geumbang.serverb.auth.util;

import com.geumbang.serverb.common.Constants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class JwtUtil {

    private final long ACS_EXPIRATION_TIME = 1000 * 60 * 15; //15q분
    private final long RFR_EXPIRATION_TIME = 1000* 60 * 60 * 24 * 7; //일주일

    public final Key key;
    public JwtUtil(@Value("${secretKey}") String SECRETKEY) {
        this.key =new SecretKeySpec(SECRETKEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    // acs token 생성
    public String generateAcsToken(String userId, String role){
        return generateToken(userId, ACS_EXPIRATION_TIME, role);
    }

    // rfr token 생성
    public String generateRfrToken(String userId, String role){
        return generateToken(userId, RFR_EXPIRATION_TIME, role);
    }

    // token생성
    public String generateToken(String userId, long expirationTime, String role) {
        Map<String, String> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
                    .setSubject(userId)
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
    }

    // 유효성 검사
    public boolean isValidToken(String token) {
        try {
            // 토큰 클레임 검사
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("claims : {}", claims);
            // 만료 시간 확인
            return !claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            // 토큰이 만료된 경우에도 false 반환
            return false;
        } catch (Exception e) {
            throw new JwtException(Constants.JWT_EXCEPTION);
        }
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
            return Objects.requireNonNullElse(claims.get("userId").toString(), "");

        } catch (Exception e) {
            log.info("id추출 불가능");
            return "";
        }
    }

    //토큰에서 role값 추출
    public String getRoleFromToken(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Objects.requireNonNullElse(claims.get("role").toString(), "");
        }catch (Exception e){
            log.info("role 추출 불가능");
            return "";
        }
    }

}
