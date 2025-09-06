package co.com.autentication.usecase.user;

import co.com.autentication.model.auth.Login;
import co.com.autentication.model.auth.Token;
import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.gateways.AuthGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Use case for authentication operations.
 */
@RequiredArgsConstructor
public class AuthUseCase {

  private final AuthGateway authGateway;
  private final UserUseCase useCase;

  /**
   * Authenticates a user and returns a JWT token if credentials are valid.
   *
   * @param data the login data containing email and password
   * @return a Mono emitting the generated Token
   */
  public Mono<Token> login(Login data) {
    return useCase.getUserWithRoleByEmail(data.email())
        .flatMap(user -> {
          if (!authGateway.matches(data.password(), user.getPassword())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS));
          }
          return Mono.fromSupplier(() -> {
            String jwt = authGateway.generateToken(user);
            return new Token(jwt);
          });
        });
  }
}
