package org.komamitsu.spring.data.sqlite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.jdbc.repository.config.DialectResolver.DefaultDialectProvider;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * An implementation of ${@link DialectResolver.JdbcDialectProvider} to return ${@link
 * SqliteDialect}.
 */
public class SqliteDialectProvider implements DialectResolver.JdbcDialectProvider {
  @Override
  public Optional<Dialect> getDialect(JdbcOperations operations) {
    Optional<Dialect> dialect =
        Optional.ofNullable(
            operations.execute((ConnectionCallback<Dialect>) SqliteDialectProvider::getDialect));
    if (dialect.isPresent()) {
      return dialect;
    } else {
      return new DefaultDialectProvider().getDialect(operations);
    }
  }

  private static Dialect getDialect(Connection connection) throws SQLException {
    DatabaseMetaData metaData = connection.getMetaData();
    String name = metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);

    if (name.contains("sqlite")) {
      return SqliteDialect.INSTANCE;
    }
    return null;
  }
}
