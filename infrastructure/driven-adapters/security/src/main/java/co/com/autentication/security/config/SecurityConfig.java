package co.com.autentication.security.config;

import co.com.autentication.model.role.RoleEnum;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http,
      ReactiveJwtAuthenticationConverterAdapter jwtAuthConverter) {
    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchange -> exchange.pathMatchers("/api/v1/login")
            .permitAll()
            .pathMatchers(HttpMethod.POST, "/api/v1/usuarios")
            .hasAnyAuthority(RoleEnum.ADMIN.getName(), RoleEnum.ADVISER.getName())
            .anyExchange()
            .authenticated())
        .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(
            jwtAuthConverter)))
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .logout(ServerHttpSecurity.LogoutSpec::disable)
        .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new BearerTokenServerAuthenticationEntryPoint())
            .accessDeniedHandler(new BearerTokenServerAccessDeniedHandler()))
        .build();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder(
      @Value("${spring.security.oauth2.resourceserver.jwt.secret}") String secret) {
    byte[] keyBytes = Decoders.BASE64URL.decode(secret);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    return NimbusReactiveJwtDecoder.withSecretKey(key)
        .build();
  }


  @Bean
  public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Object rolesObj = jwt.getClaims()
          .get("roles");
      if (rolesObj instanceof Collection<?> roles) {
        return roles.stream()
            .filter(Map.class::isInstance)
            .map(r -> ((Map<?, ?>) r).get("authority"))
            .filter(Objects::nonNull)
            .map(Object::toString)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
      }
      return List.of();
    });
    return new ReactiveJwtAuthenticationConverterAdapter(converter);
  }
}
