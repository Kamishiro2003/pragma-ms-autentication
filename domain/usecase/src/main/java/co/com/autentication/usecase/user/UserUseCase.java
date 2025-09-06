package co.com.autentication.usecase.user;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.exception.ObjectNotFoundException;
import co.com.autentication.model.gateways.AuthGateway;
import co.com.autentication.model.gateways.TransactionGateway;
import co.com.autentication.model.gateways.UserRepository;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.model.user.UserWithRole;
import java.time.LocalDate;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Use case for managing user-related operations.
 */
@RequiredArgsConstructor
public class UserUseCase {

  private final RoleUseCase roleUseCase;
  private final UserRepository repository;
  private final AuthGateway authGateway;
  private final TransactionGateway transactionGateway;

  /**
   * Creates a new user.
   *
   * @param user the user creation data
   * @return a Mono containing the created User
   */
  public Mono<User> createUser(UserCreate user) {
    return transactionGateway.execute(validateUserAge(user.birthDate()).then(Mono.defer(() -> roleUseCase.validateRoleById(
            user.roleId())))
        .then(Mono.defer(() -> {
          User entity = User.builder()
              .name(user.name())
              .lastName(user.lastName())
              .email(user.email())
              .password(authGateway.encode(user.password()))
              .roleId(user.roleId())
              .documentId(user.documentId())
              .baseSalary(user.baseSalary())
              .phone(user.phone())
              .address(user.address())
              .birthDate(user.birthDate())
              .build();
          return repository.save(entity);
        })));
  }

  /**
   * Retrieves a user by their document ID.
   *
   * @param documentId the document ID of the user
   * @return a Mono containing the user or an error if not found
   */
  public Mono<User> getUserByDocumentId(String documentId) {
    return repository.findByDocumentId(documentId)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.USER_NOT_FOUND,
            documentId)));
  }

  /**
   * Retrieves a user along with their role by email.
   *
   * @param email the user's email
   * @return a Mono containing UserWithRole or an error if not found
   */
  public Mono<UserWithRole> getUserWithRoleByEmail(String email) {
    return repository.findUserWithRoleByEmail(email)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL,
            email)));
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to find
   * @return a Mono containing User or an error if not found
   */
  public Mono<User> getUserByEmail(String email) {
    return repository.findByEmail(email)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL,
            email)));
  }

  /**
   * Validates that the user is at least 18 years old.
   *
   * @param birthDate the user's birthdate
   * @throws BusinessException if the user is under 18
   */
  private Mono<Void> validateUserAge(LocalDate birthDate) {
    int age = Period.between(birthDate, LocalDate.now())
        .getYears();
    if (age < 18) {
      return Mono.error(new BusinessException(ErrorCode.USER_CANNOT_BE_UNDER_AGE));
    }
    return Mono.empty();
  }
}
