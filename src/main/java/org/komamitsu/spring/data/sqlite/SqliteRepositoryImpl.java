package org.komamitsu.spring.data.sqlite;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;

/**
 * Default implementation of {@link org.springframework.data.repository.CrudRepository} and ${@link
 * SqliteHelperRepository} interface in the integration of Spring Data and SQLite.
 */
public class SqliteRepositoryImpl<T, ID> extends SimpleJdbcRepository<T, ID>
    implements SqliteHelperRepository<T> {
  private final JdbcAggregateOperations entityOperation;

  public SqliteRepositoryImpl(
      JdbcAggregateOperations entityOperations,
      PersistentEntity<T, ?> entity,
      JdbcConverter converter) {
    super(entityOperations, entity, converter);
    this.entityOperation = entityOperations;
  }

  @Override
  public T insert(T instance) {
    return entityOperation.insert(instance);
  }

  @Override
  public T update(T instance) {
    return entityOperation.update(instance);
  }
}
