package co.com.autentication.r2dbc.repository;

import co.com.autentication.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository interface for UserEntity, extending ReactiveCrudRepository and
 * ReactiveQueryByExampleExecutor.
 */
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>,
    ReactiveQueryByExampleExecutor<UserEntity> {

  Mono<UserEntity> findByDocumentId(String documentId);
}
