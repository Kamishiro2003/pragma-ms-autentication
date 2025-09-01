package co.com.autentication.usecase.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.com.autentication.model.auth.Login;
import co.com.autentication.model.auth.Token;
import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.gateways.AuthGateway;
import co.com.autentication.model.user.UserWithRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

  Login login = new Login("email test", "email test");
  Token token = new Token("token test");
  UserWithRole userWithRole = UserWithRole.builder()
      .email(login.email())
      .password("hash password")
      .build();

  @Mock
  private AuthGateway authGateway;

  @Mock
  private UserUseCase useCase;

  @InjectMocks
  private AuthUseCase authUseCase;

  @Test
  void shouldReturnTokenSuccessfully() {
    // Arrange
    when(useCase.getUserWithRoleByEmail(login.email())).thenReturn(Mono.just(userWithRole));
    when(authGateway.matches(login.password(), userWithRole.getPassword())).thenReturn(true);
    when(authGateway.generateToken(userWithRole)).thenReturn(token.token());

    // Act
    var result = authUseCase.login(login);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(value -> token.token().equals("token test"))
        .verifyComplete();
  }

  @Test
  void shouldThrowBusinessException() {
    // Arrange
    when(useCase.getUserWithRoleByEmail(login.email())).thenReturn(Mono.just(userWithRole));
    when(authGateway.matches(login.password(), userWithRole.getPassword())).thenReturn(false);

    // Act
    var result = authUseCase.login(login);

    // Assert
    StepVerifier.create(result)
        .expectErrorSatisfies(throwable -> assertThat(throwable).isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.INVALID_CREDENTIALS.getMessage()))
        .verify();
  }
}