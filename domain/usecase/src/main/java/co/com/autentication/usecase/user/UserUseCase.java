package co.com.autentication.usecase.user;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.BusinessException;
import co.com.autentication.model.exception.ObjectNotFoundException;
import co.com.autentication.model.gateways.TransactionGateway;
import co.com.autentication.model.gateways.UserRepository;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import java.time.LocalDate;
import java.time.Period;
import reactor.core.publisher.Mono;

/**
 * Use case for creating a new user.
 */
public class UserUseCase {

  private final UserRepository repository;
  private final TransactionGateway transactionGateway;

  /**
   * Constructs a UserCreateUseCase with the given UserRepository.
   *
   * @param repository         the user repository
   * @param transactionGateway the transaction gateway
   */
  public UserUseCase(UserRepository repository, TransactionGateway transactionGateway) {
    this.repository = repository;
    this.transactionGateway = transactionGateway;
  }

  /**
   * Creates a new user.
   *
   * @param user the user creation data
   * @return a Mono containing the created User
   */
  public Mono<User> createUser(UserCreate user) {
    return transactionGateway.execute(Mono.fromCallable(() -> {
      validateUserAge(user.birthDate());
      return User.builder()
          .name(user.name())
          .lastName(user.lastName())
          .email(user.email())
          .documentId(user.documentId())
          .baseSalary(user.baseSalary())
          .phone(user.phone())
          .address(user.address())
          .birthDate(user.birthDate())
          .build();
    }).flatMap(repository::save));
  }

  public Mono<User> getUserByDocumentId(String documentId) {
    return repository.findByDocumentId(documentId)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.USER_NOT_FOUND,
            documentId)));
  }

  /**
   * Validates that the user is at least 18 years old.
   *
   * @param birthDate the user's birthdate
   * @throws BusinessException if the user is under 18
   */
  private void validateUserAge(LocalDate birthDate) {
    int age = Period.between(birthDate, LocalDate.now()).getYears();
    if (age < 18) {
      throw new BusinessException(ErrorCode.USER_CANNOT_BE_UNDER_AGE);
    }
  }
}
