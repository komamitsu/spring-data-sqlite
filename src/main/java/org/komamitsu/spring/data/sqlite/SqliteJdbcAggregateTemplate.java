package org.komamitsu.spring.data.sqlite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

/**
 * An extended ${@link JdbcAggregateTemplate} customized for SQLite. This prevents users from using
 * some APIs that are not supported in SQLite by throwing ${@link UnsupportedOperationException}.
 */
public class SqliteJdbcAggregateTemplate extends JdbcAggregateTemplate {
  public SqliteJdbcAggregateTemplate(
      ApplicationContext publisher,
      RelationalMappingContext context,
      JdbcConverter converter,
      DataAccessStrategy dataAccessStrategy) {
    super(publisher, context, converter, dataAccessStrategy);
  }

  public SqliteJdbcAggregateTemplate(
      ApplicationEventPublisher publisher,
      RelationalMappingContext context,
      JdbcConverter converter,
      DataAccessStrategy dataAccessStrategy) {
    super(publisher, context, converter, dataAccessStrategy);
  }
}
