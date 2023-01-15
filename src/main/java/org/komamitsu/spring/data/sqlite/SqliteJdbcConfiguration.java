package org.komamitsu.spring.data.sqlite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * An extended ${@link AbstractJdbcConfiguration} to inject some custom classes to support SQLite.
 */
@Configuration
@EnableSqliteRepositories
public class SqliteJdbcConfiguration extends AbstractJdbcConfiguration {
  /**
   * Return the customized SqliteJdbcAggregateTemplate to use it as {@link JdbcAggregateTemplate}
   * when it's required
   *
   * @param applicationContext {@link ApplicationContext} instance
   * @param mappingContext {@link JdbcMappingContext} instance
   * @param converter {@link JdbcConverter} instance
   * @param dataAccessStrategy {@link DataAccessStrategy} instance
   * @return {@link SqliteJdbcAggregateTemplate} instance
   */
  @Bean
  @Primary
  public JdbcAggregateTemplate createJdbcAggregateTemplate(
      ApplicationContext applicationContext,
      JdbcMappingContext mappingContext,
      JdbcConverter converter,
      DataAccessStrategy dataAccessStrategy) {
    return new SqliteJdbcAggregateTemplate(
        applicationContext, mappingContext, converter, dataAccessStrategy);
  }

  /**
   * Return the customized {@link SqliteJdbcRepositoryFactory} to use it when to create
   *
   * @param dataAccessStrategy {@link DataAccessStrategy} instance
   * @param context {@link RelationalMappingContext} instance
   * @param converter {@link JdbcConverter} instance
   * @param dialect {@link Dialect} instance
   * @param publisher {@link ApplicationEventPublisher} instance
   * @param operations {@link NamedParameterJdbcOperations} instance
   * @return {@link SqliteJdbcAggregateTemplate} instance
   */
  @Bean
  @Primary
  public JdbcRepositoryFactory createJdbcRepositoryFactory(
      DataAccessStrategy dataAccessStrategy,
      RelationalMappingContext context,
      JdbcConverter converter,
      Dialect dialect,
      ApplicationEventPublisher publisher,
      NamedParameterJdbcOperations operations) {
    return new SqliteJdbcRepositoryFactory(
        dataAccessStrategy, context, converter, dialect, publisher, operations);
  }
}
