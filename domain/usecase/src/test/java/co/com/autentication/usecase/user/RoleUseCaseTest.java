package co.com.autentication.usecase.user;

import static org.mockito.Mockito.when;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.ObjectNotFoundException;
import co.com.autentication.model.gateways.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {

  private final String roleId = "test";

  @Mock
  private RoleRepository repository;

  @InjectMocks
  private RoleUseCase useCase;

  @Test
  void shouldValidateRoleByIdSuccessfully() {
    // Arrange
    when(repository.existsById(roleId)).thenReturn(Mono.just(true));

    // Act
    var result = useCase.validateRoleById(roleId);

    // Assert
    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void shouldThrowObjectNotFoundException() {
    // Arrange
    when(repository.existsById(roleId)).thenReturn(Mono.just(false));

    // Act
    var result = useCase.validateRoleById(roleId);

    // Assert
    StepVerifier.create(result)
        .expectErrorSatisfies(throwable -> Assertions.assertThat(throwable)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining(ErrorCode.ROLE_NOT_FOUND.getMessage() + roleId))
        .verify();
  }
}