package co.com.autentication.usecase.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import util.UserTestUtil;

@ExtendWith(MockitoExtension.class)
class UserCreateUseCaseTest {

  @Mock
  private UserRepository repository;

  @InjectMocks
  private UserCreateUseCase useCase;

  @Test
  void shouldCreateUserSuccessfully() {
    // Arrange
    User user = UserTestUtil.getUser();
    UserCreate userCreate = UserTestUtil.getUserCreate();
    when(repository.save(any(User.class))).thenReturn(Mono.just(user));

    // Act
    Mono<User> result = useCase.createUser(userCreate);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(created ->
            created.getName()
                .equals(user.getName()) &&
                created.getLastName()
                    .equals(user.getLastName()) &&
                created.getEmail()
                    .equals(user.getEmail()) &&
                created.getDocumentId()
                    .equals(user.getDocumentId())
        )
        .verifyComplete();

    verify(repository, times(1)).save(any(User.class));
  }

  @Test
  void shouldThrowExceptionWhenUserIsUnderAge() {
    // Arrange
    UserCreate underAgeUser = UserTestUtil.getUnderAgeUserCreate();

    // Act & Assert
    StepVerifier.create(useCase.createUser(underAgeUser))
        .expectErrorSatisfies(throwable -> {
            assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_CANNOT_BE_UNDER_AGE.getMessage());
        })
        .verify();

    verify(repository, never()).save(any(User.class));
  }
}
