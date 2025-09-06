package co.com.autentication.usecase.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.exception.ObjectNotFoundException;
import co.com.autentication.model.gateways.AuthGateway;
import co.com.autentication.model.gateways.TransactionGateway;
import co.com.autentication.model.gateways.UserRepository;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.model.user.UserWithRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import util.UserTestUtil;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

  @Mock
  private UserRepository repository;

  @Mock
  private AuthGateway authGateway;

  @Mock
  private RoleUseCase roleUseCase;

  @Mock
  private TransactionGateway transactionGateway;

  @InjectMocks
  private UserUseCase useCase;

  @Test
  void shouldCreateUserSuccessfully() {
    // Arrange
    when(transactionGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(
        invocation -> invocation.getArgument(0));
    User user = UserTestUtil.getUser();
    UserCreate userCreate = UserTestUtil.getUserCreate();
    when(roleUseCase.validateRoleById(anyString())).thenReturn(Mono.empty());
    when(authGateway.encode(anyString())).thenAnswer(inv -> "encoded-" + inv.getArgument(0));
    when(repository.save(any(User.class))).thenReturn(Mono.just(user));

    // Act
    Mono<User> result = useCase.createUser(userCreate);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(created -> created
            .getName()
            .equals(user.getName()) && created
            .getLastName()
            .equals(user.getLastName()) && created
            .getEmail()
            .equals(user.getEmail()) && created
            .getDocumentId()
            .equals(user.getDocumentId()))
        .verifyComplete();

    verify(repository, times(1)).save(any(User.class));
    verify(roleUseCase, times(1)).validateRoleById(userCreate.roleId());
    verify(authGateway, times(1)).encode(userCreate.password());
  }

  @Test
  void shouldThrowExceptionWhenUserIsUnderAge() {
    // Arrange
    when(transactionGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(
        invocation -> invocation.getArgument(0));
    UserCreate underAgeUser = UserTestUtil.getUnderAgeUserCreate();

    // Act & Assert
    StepVerifier
        .create(useCase.createUser(underAgeUser))
        .expectErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.USER_CANNOT_BE_UNDER_AGE.getMessage()))
        .verify();

    verify(repository, never()).save(any(User.class));
  }

  @Test
  void shouldReturnUserByDocumentId() {
    // Arrange
    User user = UserTestUtil.getUser();
    when(repository.findByDocumentId(user.getDocumentId())).thenReturn(Mono.just(user));

    // Act
    Mono<User> result = useCase.getUserByDocumentId(user.getDocumentId());

    // Assert
    StepVerifier
        .create(result)
        .expectNext(user)
        .verifyComplete();

    verify(repository, times(1)).findByDocumentId(user.getDocumentId());
  }

  @Test
  void shouldThrowExceptionWhenUserByDocumentIdNotFound() {
    // Arrange
    String documentId = "12345";
    when(repository.findByDocumentId(documentId)).thenReturn(Mono.empty());

    // Act
    Mono<User> result = useCase.getUserByDocumentId(documentId);

    // Assert
    StepVerifier
        .create(result)
        .expectErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage()))
        .verify();
  }

  @Test
  void shouldReturnUserWithRoleByEmail() {
    // Arrange
    UserWithRole userWithRole = UserWithRole
        .builder()
        .email("test@example.com")
        .password("encoded-pass")
        .roleName("ADMIN")
        .build();
    when(repository.findUserWithRoleByEmail(userWithRole.getEmail())).thenReturn(
        Mono.just(userWithRole));

    // Act
    Mono<UserWithRole> result = useCase.getUserWithRoleByEmail(userWithRole.getEmail());

    // Assert
    StepVerifier
        .create(result)
        .expectNext(userWithRole)
        .verifyComplete();

    verify(repository, times(1)).findUserWithRoleByEmail(userWithRole.getEmail());
  }

  @Test
  void shouldThrowExceptionWhenUserWithRoleByEmailNotFound() {
    // Arrange
    String email = "missing@example.com";
    when(repository.findUserWithRoleByEmail(email)).thenReturn(Mono.empty());

    // Act
    Mono<UserWithRole> result = useCase.getUserWithRoleByEmail(email);

    // Assert
    StepVerifier
        .create(result)
        .expectErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining(ErrorCode.USER_NOT_FOUND_BY_EMAIL.getMessage()))
        .verify();
  }

  @Test
  void shouldReturnUserByEmail() {
    // Arrange
    User user = UserTestUtil.getUser();
    when(repository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

    // Act
    Mono<User> result = useCase.getUserByEmail(user.getEmail());

    // Assert
    StepVerifier
        .create(result)
        .expectNext(user)
        .verifyComplete();

    verify(repository, times(1)).findByEmail(user.getEmail());
  }

  @Test
  void shouldThrowExceptionWhenUserByEmailNotFound() {
    // Arrange
    String email = "example@gmail.com";
    when(repository.findByEmail(email)).thenReturn(Mono.empty());

    // Act
    Mono<User> result = useCase.getUserByEmail(email);

    // Assert
    StepVerifier
        .create(result)
        .expectErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining(ErrorCode.USER_NOT_FOUND_BY_EMAIL.getMessage() + email))
        .verify();
  }
}

