package co.com.autentication.r2dbc;

import static org.mockito.Mockito.when;

import co.com.autentication.model.user.User;
import co.com.autentication.r2dbc.adapter.UserReactiveRepositoryAdapter;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.repository.UserReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

  @InjectMocks
  UserReactiveRepositoryAdapter repositoryAdapter;

  @Mock
  UserReactiveRepository repository;

  @Mock
  ObjectMapper mapper;

  @Test
  void mustFindValueById() {
    UserEntity userEntity = new UserEntity();
    userEntity.setId("1");
    User user = new User();
    user.setId("1");

    when(repository.findById("1")).thenReturn(Mono.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Mono<User> result = repositoryAdapter.findById("1");

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }

  @Test
  void mustFindAllValues() {
    UserEntity userEntity = new UserEntity();
    userEntity.setId("1");
    User user = new User();
    user.setId("1");

    when(repository.findAll()).thenReturn(Flux.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Flux<User> result = repositoryAdapter.findAll();

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }

  @Test
  void mustSaveValue() {
    UserEntity userEntity = new UserEntity();
    userEntity.setId("1");
    User user = new User();
    user.setId("1");

    when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
    when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
    when(mapper.map(userEntity, User.class)).thenReturn(user);

    Mono<User> result = repositoryAdapter.save(user);

    StepVerifier.create(result)
        .expectNextMatches(value -> value.getId().equals("1"))
        .verifyComplete();
  }
}
