package co.com.autentication.usecase.user;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.ObjectNotFoundException;
import co.com.autentication.model.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Use case for role-related operations.
 */
@RequiredArgsConstructor
public class RoleUseCase {

  private final RoleRepository repository;

  /**
   * Validates if a role exists by its ID.
   *
   * @param id the role ID to validate
   * @return a Mono that completes if the role exists or emits an error if not
   */
  public Mono<Void> validateRoleById(String id) {
    return repository.existsById(id)
        .flatMap(exists -> {
          if (exists) {
            return Mono.empty();
          } else {
            return Mono.error(new ObjectNotFoundException(ErrorCode.ROLE_NOT_FOUND, id));
          }
        });
  }
}
