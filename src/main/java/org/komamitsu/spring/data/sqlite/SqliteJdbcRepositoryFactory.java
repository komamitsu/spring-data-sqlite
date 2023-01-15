package org.komamitsu.spring.data.sqlite;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * An extended ${@link JdbcRepositoryFactory} to make ${@link
 * org.springframework.data.repository.CrudRepository} internally use ${@link
 * SqliteJdbcAggregateTemplate}.
 */
public class SqliteJdbcRepositoryFactory extends JdbcRepositoryFactory {
  // These fields are copied from JdbcRepositoryFactory since the parent's fields are `private` and
  // this class can't access them. This might be a bit tricky.
  private final ApplicationEventPublisher publisher;
  private final RelationalMappingContext context;
  private final JdbcConverter converter;
  private final DataAccessStrategy accessStrategy;
  private EntityCallbacks entityCallbacks;

  /**
   * Creates a new {@link JdbcRepositoryFactory} for the given {@link DataAccessStrategy}, {@link
   * RelationalMappingContext} and {@link ApplicationEventPublisher}.
   *
   * @param dataAccessStrategy must not be {@literal null}.
   * @param context must not be {@literal null}.
   * @param converter must not be {@literal null}.
   * @param dialect must not be {@literal null}.
   * @param publisher must not be {@literal null}.
   * @param operations must not be {@literal null}.
   */
  public SqliteJdbcRepositoryFactory(
      DataAccessStrategy dataAccessStrategy,
      RelationalMappingContext context,
      JdbcConverter converter,
      Dialect dialect,
      ApplicationEventPublisher publisher,
      NamedParameterJdbcOperations operations) {
    super(dataAccessStrategy, context, converter, dialect, publisher, operations);
    this.publisher = publisher;
    this.context = context;
    this.converter = converter;
    this.accessStrategy = dataAccessStrategy;
  }

  /**
   * Returns a target repository that uses {@link SqliteJdbcAggregateTemplate} instead of {@link
   * JdbcAggregateTemplate}.
   *
   * @param repositoryInformation repositoryInformation
   * @return SqliteJdbcAggregateTemplate to use SQLite
   */
  @Override
  protected Object getTargetRepository(RepositoryInformation repositoryInformation) {

    JdbcAggregateTemplate template =
        new SqliteJdbcAggregateTemplate(publisher, context, converter, accessStrategy);

    if (entityCallbacks != null) {
      template.setEntityCallbacks(entityCallbacks);
    }

    RelationalPersistentEntity<?> persistentEntity =
        context.getRequiredPersistentEntity(repositoryInformation.getDomainType());

    return getTargetRepositoryViaReflection(
        repositoryInformation, template, persistentEntity, converter);
  }

  @Override
  public void setEntityCallbacks(EntityCallbacks entityCallbacks) {
    this.entityCallbacks = entityCallbacks;
    super.setEntityCallbacks(entityCallbacks);
  }
}
