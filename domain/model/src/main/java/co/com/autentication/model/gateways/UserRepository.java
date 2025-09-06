package co.com.autentication.model.gateways;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserWithRole;
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

  /**
   * Finds a User by their document ID.
   *
   * @param documentId the document ID of the User
   * @return a Mono emitting the User if found, empty otherwise
   */
  Mono<User> findByDocumentId(String documentId);

  /**
   * Finds a User along with their role information by email.
   *
   * @param email the email of the User
   * @return a Mono emitting a UserWithRole if found, empty otherwise
   */
  Mono<UserWithRole> findUserWithRoleByEmail(String email);

  /**
   * Finds a User by their email.
   *
   * @param email the email fo the user to find
   * @return a Mono emitting the User if found, empty otherwise
   */
  Mono<User> findByEmail(String email);
}
