package co.com.autentication.model.gateways;

import co.com.autentication.model.user.User;
import reactor.core.publisher.Mono;

/**
 * Repository interface for User operations.
 */
public interface UserRepository {

  /**
   * Saves a User.
   *
   * @param user the User to save
   * @return a Mono containing the saved User
   */
  Mono<User> save(User user);
}
