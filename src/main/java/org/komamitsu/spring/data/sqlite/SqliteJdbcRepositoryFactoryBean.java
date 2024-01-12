package org.komamitsu.spring.data.sqlite;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

/**
 * Returns ${@link SqliteJdbcRepositoryFactory} to make ${@link
 * org.springframework.data.repository.CrudRepository} internally use ${@link
 * SqliteJdbcAggregateTemplate}.
 */
class SqliteJdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends JdbcRepositoryFactoryBean<T, S, ID> {
  // These fields are copied from JdbcRepositoryFactoryBean since the parent's fields are `private`
  // and this class can't access them. This might be a bit tricky.
  private JdbcConverter converter;
  private DataAccessStrategy dataAccessStrategy;
  private RelationalMappingContext context;
  private Dialect dialect;
  private ApplicationEventPublisher publisher;
  private NamedParameterJdbcOperations operations;

  /**
   * Creates a new {@link SqliteJdbcRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public SqliteJdbcRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport doCreateRepositoryFactory() {
    return new SqliteJdbcRepositoryFactory(
        dataAccessStrategy, context, converter, dialect, publisher, operations);
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
    super.setApplicationEventPublisher(publisher);
    this.publisher = publisher;
  }

  @Autowired
  @Override
  public void setMappingContext(RelationalMappingContext mappingContext) {
    Assert.notNull(mappingContext, "MappingContext must not be null");
    super.setMappingContext(mappingContext);
    this.context = mappingContext;
  }

  @Autowired
  @Override
  public void setDialect(Dialect dialect) {
    Assert.notNull(dialect, "Dialect must not be null");
    super.setDialect(dialect);
    this.dialect = dialect;
  }

  /**
   * @param dataAccessStrategy can be {@literal null}.
   */
  @Autowired
  @Override
  public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
    Assert.notNull(dataAccessStrategy, "DataAccessStrategy must not be null");
    super.setDataAccessStrategy(dataAccessStrategy);
    this.dataAccessStrategy = dataAccessStrategy;
  }

  @Autowired
  @Override
  public void setJdbcOperations(NamedParameterJdbcOperations operations) {
    Assert.notNull(operations, "NamedParameterJdbcOperations must not be null");
    super.setJdbcOperations(operations);
    this.operations = operations;
  }

  @Autowired
  @Override
  public void setConverter(JdbcConverter converter) {
    Assert.notNull(converter, "JdbcConverter must not be null");
    super.setConverter(converter);
    this.converter = converter;
  }
}
