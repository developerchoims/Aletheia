package com.geumbang.servera.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserCustom implements UserDetails {

    private String userId;
    private String role;

    // 생성자 (JWT 필터에서 사용할 생성자)
    public UserCustom(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    // Getter 메서드
    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    // UserDetails 인터페이스 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // role을 GrantedAuthority로 변환하여 반환
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return null; // JWT에서는 비밀번호가 필요하지 않음
    }

    @Override
    public String getUsername() {
        return userId;
    }

}
