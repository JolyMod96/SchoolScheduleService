package org.itstep.schooltimetable.security.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(antMatcher("/css/**")).permitAll()
                        .requestMatchers(antMatcher("/icon/**")).permitAll()
                        .requestMatchers(antMatcher("/images/**")).permitAll()
                        .requestMatchers(antMatcher("/js/**")).permitAll()
                        .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(antMatcher("/student/**")).hasAnyAuthority("ROLE_STUDENT", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/teacher/**")).hasAnyAuthority("ROLE_TEACHER", "ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/h2-console/**")))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
