package procentaurus.projects.ReservationSystem.Configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import procentaurus.projects.ReservationSystem.Configuration.Auth.Jwt.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth

                    .requestMatchers("/api/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/slots/**").hasAnyAuthority("MANAGER", "ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/rooms/**", "/api/conferenceRooms/**",
                            "/api/parkingPlaces/**", "/api/guests/**", "/api/stuffMembers/**", "/api/slots/**")
                    .hasAnyAuthority("MANAGER", "ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/rooms/**", "/api/conferenceRooms/**",
                            "/api/parkingPlaces/**", "/api/slots/**")
                    .hasAnyAuthority("MANAGER", "ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/rooms/**", "/api/conferenceRooms/**", "/api/parkingPlaces/**")
                    .hasAnyAuthority("MANAGER", "ADMIN")

                    .anyRequest().authenticated()
                )
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
