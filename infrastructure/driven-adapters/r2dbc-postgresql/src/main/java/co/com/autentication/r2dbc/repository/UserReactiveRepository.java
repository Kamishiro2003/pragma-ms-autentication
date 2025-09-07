package co.com.autentication.r2dbc.repository;

import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.projection.UserWithRolProjection;
import org.springframework.data.r2dbc.repository.Query;
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

  Mono<UserEntity> findByEmail(String email);

  @Query("""
      SELECT u.correo_electronico, u.contrasena, r.nombre
        FROM usuario u
        JOIN rol r ON u.id_role = r.id_role
        WHERE u.correo_electronico = :email;
      """
  )
  Mono<UserWithRolProjection> findUserWithRoleByEmail(String email);
}
