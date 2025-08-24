package co.com.autentication.r2dbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.com.autentication.model.exception.ConstraintException;
import co.com.autentication.model.user.User;
import co.com.autentication.r2dbc.adapter.UserReactiveRepositoryAdapter;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.repository.UserReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

  private final UserEntity userEntity = UserEntity.builder().id("1").build();
  private final User user = User.builder().id("1").build();
  @InjectMocks
  UserReactiveRepositoryAdapter repositoryAdapter;
  @Mock
  UserReactiveRepository repository;
  @Mock
  ObjectMapper mapper;

  @Test
  void mustFindValueById() {

    when(repository.findById("1")).thenReturn(Mono.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Mono<User> result = repositoryAdapter.findById("1");

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }

  @Test
  void mustFindAllValues() {

    when(repository.findAll()).thenReturn(Flux.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Flux<User> result = repositoryAdapter.findAll();

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }

  @Test
  void mustSaveValue() {

    when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
    when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Mono<User> result = repositoryAdapter.save(user);

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }

  @ParameterizedTest
  @CsvSource({"correo_electronico_unique_constraint, EMAIL-ALREADY-EXISTS",
      "documento_identidad_unique_constraint, DOCUMENT-ALREADY-EXISTS",
      "other constraint exception, CONSTRAINT-VIOLATION"}
  )
  void mustHandleConstraintViolations(String constraint, String expectedErrorCode) {
    DataIntegrityViolationException exception = new DataIntegrityViolationException(constraint);
    when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
    when(repository.save(userEntity)).thenReturn(Mono.error(exception));

    Mono<User> result = repositoryAdapter.save(user);

    StepVerifier.create(result).expectErrorSatisfies(error -> {
      assertThat(error).isInstanceOf(ConstraintException.class);
      assertThat(((ConstraintException) error).getFullErrorCode()).isEqualTo(expectedErrorCode);
    }).verify();
  }

  @Test
  void mustHandleOtherExceptions() {
    RuntimeException exception = new RuntimeException("Some other error");
    when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
    when(repository.save(userEntity)).thenReturn(Mono.error(exception));

    Mono<User> result = repositoryAdapter.save(user);

    StepVerifier.create(result).expectErrorSatisfies(error -> {
      assertThat(error).isInstanceOf(RuntimeException.class);
      assertThat(error.getMessage()).isEqualTo("Some other error");
    }).verify();
  }
}
