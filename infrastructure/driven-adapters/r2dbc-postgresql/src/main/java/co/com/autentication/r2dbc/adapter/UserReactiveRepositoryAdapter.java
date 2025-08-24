package co.com.autentication.r2dbc.adapter;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import co.com.autentication.r2dbc.repository.UserReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


/**
 * Adapter class that implements the UserRepository interface using a reactive repository.
 */
@Repository
public class UserReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<User, UserEntity, String, UserReactiveRepository> implements
    UserRepository {

  public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, User.class));
  }

  @Override
  @Transactional
  public Mono<User> save(User user) {
    return super.save(user);
  }
}
