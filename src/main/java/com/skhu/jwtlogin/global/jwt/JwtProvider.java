package com.skhu.jwtlogin.global.jwt;

import com.skhu.jwtlogin.common.error.ErrorCode;
import com.skhu.jwtlogin.common.exception.BusinessException;
import com.skhu.jwtlogin.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-time}")
    private long accessTokenExpireTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .subject(String.valueOf(member.getMemberId()))
                .claim(AUTHORITIES_KEY, "ROLE_" + member.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT가 유효하지 않습니다.");
        } catch (SignatureException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 서명 검증에 실패했습니다.");
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT가 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT가 비어있거나 잘못된 값입니다.");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 검증에 실패했습니다.");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String authority = claims.get(AUTHORITIES_KEY, String.class);

        if (authority == null) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "권한 정보가 없는 토큰입니다.");
        }

        return new UsernamePasswordAuthenticationToken(
                Long.valueOf(claims.getSubject()),
                null,
                List.of(new SimpleGrantedAuthority(authority))
        );
    }

    public Long getMemberId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}