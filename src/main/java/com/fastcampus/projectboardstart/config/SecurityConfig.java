package com.fastcampus.projectboardstart.config;

import com.fastcampus.projectboardstart.dto.UserAccountDto;
import com.fastcampus.projectboardstart.dto.security.BoardPrincipal;
import com.fastcampus.projectboardstart.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // TODO: 모든 부분에서 인증 열어 놨지만 시큐리티 버전 문제로 해당 코드 작성시 /login, /loginout 경로 요청에서 Error 발생
    // Boot 2.7 Security 5.7 이상 SecurityFilterChain 사용
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // spring security 관리 하에 여러가지 보안적인 요소의 방어나 서비스를 받을 수 있음 csrf
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                HttpMethod.GET, "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
