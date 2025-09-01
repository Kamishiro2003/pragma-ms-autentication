package co.com.autentication.security.adapter;

import static org.springframework.security.core.userdetails.User.withUsername;

import co.com.autentication.model.gateways.AuthGateway;
import co.com.autentication.model.user.UserWithRole;
import co.com.autentication.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthGatewayImpl implements AuthGateway {

  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  @Override
  public String encode(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  @Override
  public String generateToken(UserWithRole user) {
    return jwtProvider.generateToken(withUsername(user.getEmail()).password(user.getPassword())
        .authorities(user.getRoleName())
        .build());
  }
}
