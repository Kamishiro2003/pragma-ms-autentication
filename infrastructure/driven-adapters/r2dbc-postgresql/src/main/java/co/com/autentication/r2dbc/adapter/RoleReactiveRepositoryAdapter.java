package co.com.autentication.r2dbc.adapter;

import co.com.autentication.model.gateways.RoleRepository;
import co.com.autentication.model.role.Role;
import co.com.autentication.r2dbc.entity.RoleEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import co.com.autentication.r2dbc.repository.RoleReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<Role, RoleEntity, String, RoleReactiveRepository> implements
    RoleRepository {

  /**
   * Constructor to initialize the repository, mapper, and entity conversion function.
   *
   * @param repository the reactive CRUD repository
   * @param mapper     the object mapper for entity-data conversion
   */
  protected RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Role.class));
  }

  @Override
  public Mono<Boolean> existsById(String id) {
    return super.repository.existsById(id);
  }
}
