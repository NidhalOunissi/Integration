package tn.esprit.clubconnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static tn.esprit.clubconnect.entities.Permissions.*;
import static tn.esprit.clubconnect.entities.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authentificatinProvider;
    private final LogoutHandler logoutHandler;

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",
                                "/api/v1/auth/**",
                                "/api/v1/auth/refresh-token",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())

                        .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())


                        .requestMatchers("/api/v1/clubadmin/**").hasAnyRole(ADMIN.name(), CLUBADMIN.name())

                        .requestMatchers(GET, "/api/v1/clubadmin/**").hasAnyAuthority(ADMIN_READ.name(), CLUBADMIN_READ.name())
                        .requestMatchers(POST, "/api/v1/clubadmin/**").hasAnyAuthority(ADMIN_CREATE.name(), CLUBADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/clubadmin/**").hasAnyAuthority(ADMIN_UPDATE.name(), CLUBADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/clubadmin/**").hasAnyAuthority(ADMIN_DELETE.name(), CLUBADMIN_DELETE.name())

                        .requestMatchers("/api/v1/member/**").hasAnyRole(ADMIN.name(), CLUBADMIN.name(), MEMBER.name())

                        .requestMatchers(GET, "/api/v1/member/**").hasAnyAuthority(ADMIN_READ.name(), CLUBADMIN_READ.name(), MEMBER_READ.name())
                        .requestMatchers(POST, "/api/v1/member/**").hasAnyAuthority(ADMIN_CREATE.name(), CLUBADMIN_CREATE.name(), MEMBER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/member/**").hasAnyAuthority(ADMIN_UPDATE.name(), CLUBADMIN_UPDATE.name(), MEMBER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/member/**").hasAnyAuthority(ADMIN_DELETE.name(), CLUBADMIN_DELETE.name(), MEMBER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/member/**").hasAnyAuthority(ADMIN_PATCH.name(), CLUBADMIN_PATCH.name(), MEMBER_PATCH.name())


                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authentificatinProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        l -> {
                            l.addLogoutHandler(logoutHandler)
                            .logoutUrl("/api/v1/auth/logout")
                            .logoutSuccessHandler(
                                    (request, response, authentication) -> SecurityContextHolder.clearContext()
                            );
                            }
                );





        return http.build();
    }





}
