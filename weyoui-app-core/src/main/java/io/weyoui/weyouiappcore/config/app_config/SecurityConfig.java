package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.config.oauth2.CustomOauth2UserService;
import io.weyoui.weyouiappcore.config.oauth2.OAuth2LoginSuccessHandler;
import io.weyoui.weyouiappcore.user.command.application.UserTokenService;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.infrastructure.filter.CustomAccessDeniedHandler;
import io.weyoui.weyouiappcore.user.infrastructure.filter.EntryPointUnauthorizedHandler;
import io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final String allowOrigin;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserTokenService userTokenService;
    private final CustomOauth2UserService customOauth2UserService;


    public SecurityConfig(@Value("${api.client.allow-origin}")String allowOrigin, JwtTokenProvider jwtTokenProvider, UserTokenService userTokenService, CustomOauth2UserService customOauth2UserService ) {
        this.allowOrigin = allowOrigin;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userTokenService = userTokenService;
        this.customOauth2UserService = customOauth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .exceptionHandling(exceptionHandler -> {
                    exceptionHandler.accessDeniedHandler(accessDeniedHandler());
                    exceptionHandler.authenticationEntryPoint(authenticationEntryPoint());
                })
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        http.cors(cors -> corsConfigurationSource())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/guest/**").permitAll()
                        .anyRequest().permitAll()
        );

        http.oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2LoginSuccessHandler(jwtTokenProvider,userTokenService))
                        .userInfoEndpoint(endPoint -> endPoint.userService(customOauth2UserService)));


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(allowOrigin));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.PATCH.name(),HttpMethod.PUT.name(),HttpMethod.OPTIONS.name(),HttpMethod.DELETE.name()));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new EntryPointUnauthorizedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
