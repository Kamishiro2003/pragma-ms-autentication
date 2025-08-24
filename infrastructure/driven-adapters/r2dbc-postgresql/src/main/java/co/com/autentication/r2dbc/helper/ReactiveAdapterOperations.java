package co.com.autentication.r2dbc.helper;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abstract class providing reactive CRUD operations with entity-data mapping.
 */
public abstract class ReactiveAdapterOperations
    <E, D, I, R extends ReactiveCrudRepository<D, I> & ReactiveQueryByExampleExecutor<D>> {

  private final Class<D> dataClass;
  private final Function<D, E> toEntityFn;
  protected R repository;
  protected ObjectMapper mapper;

  /**
   * Constructor to initialize the repository, mapper, and entity conversion function.
   *
   * @param repository the reactive CRUD repository
   * @param mapper     the object mapper for entity-data conversion
   * @param toEntityFn function to convert data objects to entity objects
   */
  @SuppressWarnings("unchecked")
  protected ReactiveAdapterOperations(R repository, ObjectMapper mapper,
      Function<D, E> toEntityFn) {
    this.repository = repository;
    this.mapper = mapper;
    ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
        .getGenericSuperclass();
    this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
    this.toEntityFn = toEntityFn;
  }

  /**
   * Converts an entity to its corresponding data object.
   *
   * @param entity the entity to convert
   * @return the converted data object
   */
  protected D toData(E entity) {
    return mapper.map(entity, dataClass);
  }

  /**
   * Converts a data object to its corresponding entity.
   *
   * @param data the data object to convert
   * @return the converted entity
   */
  protected E toEntity(D data) {
    return data != null ? toEntityFn.apply(data) : null;
  }

  /**
   * Saves an entity to the repository.
   *
   * @param entity the entity to save
   * @return a Mono emitting the saved entity
   */
  public Mono<E> save(E entity) {
    return saveData(toData(entity)).map(this::toEntity);
  }

  /**
   * Saves multiple entities to the repository.
   *
   * @param entities a Flux emitting the entities to save
   * @return a Flux emitting the saved entities
   */
  protected Flux<E> saveAllEntities(Flux<E> entities) {
    return saveData(entities.map(this::toData)).map(this::toEntity);
  }

  /**
   * Saves a data object to the repository.
   *
   * @param data the data object to save
   * @return a Mono emitting the saved data object
   */
  protected Mono<D> saveData(D data) {
    return repository.save(data);
  }

  /**
   * Saves multiple data objects to the repository.
   *
   * @param data a Flux emitting the data objects to save
   * @return a Flux emitting the saved data objects
   */
  protected Flux<D> saveData(Flux<D> data) {
    return repository.saveAll(data);
  }

  /**
   * Finds an entity by its identifier.
   *
   * @param id the identifier of the entity
   * @return a Mono emitting the found entity, or empty if not found
   */
  public Mono<E> findById(I id) {
    return repository.findById(id).map(this::toEntity);
  }

  /**
   * Finds entities matching the example entity.
   *
   * @param entity the example entity to match
   * @return a Flux emitting the matching entities
   */
  public Flux<E> findByExample(E entity) {
    return repository.findAll(Example.of(toData(entity))).map(this::toEntity);
  }

  /**
   * Finds all entities in the repository.
   *
   * @return a Flux emitting all entities
   */
  public Flux<E> findAll() {
    return repository.findAll().map(this::toEntity);
  }
}
