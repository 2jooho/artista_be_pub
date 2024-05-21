package com.artista.main.global.jwt;

import com.artista.main.global.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = resolveToken(request);
        String refreshToken = resolveRefreshToken(request);
//        System.out.println("accessToken: "+ accessToken + "/refreshToken :"+ refreshToken);

        // 유효한 토큰인지 확인합니다.
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
            } // 어세스 토큰이 만료된 상황 | 리프레시 토큰 또한 존재하는 상황
            else if (refreshToken != null) {
                // 리프레시 토큰 검증
                boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
                // 리프레시 토큰 저장소 존재유무 확인
                boolean isRefreshToken = true; //= redisUtil.existsValue(refreshToken);
                if (validateRefreshToken && isRefreshToken) {
                    /// 리프레시 토큰으로 아이디 정보 가져오기
                    String userId = jwtTokenProvider.getUserId(refreshToken);

                    //토큰 발급
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    if(!userId.equals(authentication.getName())){
//                        throw new CustomException(RE_TOKEN_RESPONSE);
                    }
                    TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
                    /// 헤더에 어세스 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, tokenInfo.getAccessToken());
                    jwtTokenProvider.setHeaderRefreshToken(response, tokenInfo.getRefreshToken());

                    //레디스에 넣기
//                    redisUtil.setExpireValue("refresh"+userId, tokenInfo.getRefreshToken(), ((24 * 7) *  60 * 60 * 1000L));

                    // 컨텍스트에 넣기
                    this.setAuthentication(tokenInfo.getAccessToken());
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // Request Header 에서 액세스 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Request의 Header에서 RefreshToken 값을 가져옵니다. "authorization" : "token'
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("refreshToken");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
