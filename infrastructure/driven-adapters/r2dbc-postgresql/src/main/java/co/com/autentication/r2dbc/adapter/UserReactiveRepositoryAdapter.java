package co.com.autentication.r2dbc.adapter;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.exception.ConstraintException;
import co.com.autentication.model.gateways.UserRepository;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserWithRole;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import co.com.autentication.r2dbc.mapper.UserPersistenceMapper;
import co.com.autentication.r2dbc.repository.UserReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


/**
 * Adapter class that implements the UserRepository interface using a reactive repository.
 */
@Slf4j
@Repository
public class UserReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<User, UserEntity, String, UserReactiveRepository> implements
    UserRepository {

  private final UserPersistenceMapper userPersistenceMapper;

  /**
   * Constructor to initialize the adapter with the given repository and mapper.
   *
   * @param repository the reactive repository for UserEntity
   * @param mapper     the object mapper for converting between User and UserEntity
   */
  public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
      UserPersistenceMapper userPersistenceMapper) {
    super(repository, mapper, d -> mapper.map(d, User.class));
    this.userPersistenceMapper = userPersistenceMapper;
  }

  @Override
  public Mono<User> save(User user) {
    log.debug("Saving user: {}", user);
    return super.save(user)
        .onErrorMap(e -> {
          if (e instanceof DataIntegrityViolationException) {
            log.error("Data integrity violation: {}", e.getMessage());
            String message = e.getMessage();
            if (message.contains("correo_electronico_unique_constraint")) {
              return new ConstraintException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            if (message.contains("documento_identidad_unique_constraint")) {
              return new ConstraintException(ErrorCode.DOCUMENT_ALREADY_EXISTS);
            }
            return new ConstraintException(ErrorCode.CONSTRAINT_VIOLATION);
          }
          log.error("Unexpected error: {}", e.getMessage());
          return e;
        });
  }

  @Override
  public Mono<User> findByDocumentId(String documentId) {
    log.debug("Finding an user with document id: {}", documentId);
    return repository.findByDocumentId(documentId)
        .map(this::toEntity)
        .doOnNext(user -> log.info("User with documentId {} was found", documentId))
        .switchIfEmpty(Mono.defer(() -> {
          log.debug("User with documentId {} was not found", documentId);
          return Mono.empty();
        }));
  }

  @Override
  public Mono<UserWithRole> findUserWithRoleByEmail(String email) {
    log.debug("Finding userWithRole by email: {}", email);
    return repository.findUserWithRoleByEmail(email)
        .map(userPersistenceMapper::toUserWithRole)
        .doOnNext(userWithRole -> log.info("UserWithRole with email {} was found", email))
        .switchIfEmpty(Mono.defer(() -> {
          log.debug("UserWithRole with email {} was not found", email);
          return Mono.empty();
        }));
  }

  @Override
  public Mono<User> findByEmail(String email) {
    log.debug("Finding user entity by email: {}", email);
    return repository.findByEmail(email)
        .map(this::toEntity)
        .doOnNext(user -> log.info("User entity with email {} was found", email))
        .switchIfEmpty(Mono.defer(() -> {
          log.debug("User entity with email {} was not found", email);
          return Mono.empty();
        }));
  }
}
