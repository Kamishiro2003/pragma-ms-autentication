package co.com.autentication.model.gateways;

import reactor.core.publisher.Mono;

/**
 * Repository interface for role operations.
 */
public interface RoleRepository {

  /**
   * Checks if a role exists by its ID.
   *
   * @param id the role ID
   * @return a Mono emitting true if the role exists, false otherwise
   */
  Mono<Boolean> existsById(String id);
}
