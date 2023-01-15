package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;

class SqliteDialectProviderTest {
  private final SqliteDialectProvider sqliteDialectProvider = new SqliteDialectProvider();

  @Test
  void getDialect_ShouldReturnCorrectDialect() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:sqlite:my-app.db");
    DataSource dataSource = mock(DataSource.class);
    when(dataSource.getConnection()).thenReturn(conn);
    Optional<Dialect> dialect = sqliteDialectProvider.getDialect(new JdbcTemplate(dataSource));
    assertThat(dialect).isPresent().get().isInstanceOf(SqliteDialect.class);
  }

  @Test
  void getDialect_OtherDatabaseProductGiven_ShouldReturnTheDialect() throws SQLException {
    DatabaseMetaData metaData = mock(DatabaseMetaData.class);
    when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");
    Connection connection = mock(Connection.class);
    when(connection.getMetaData()).thenReturn(metaData);
    DataSource dataSource = mock(DataSource.class);
    when(dataSource.getConnection()).thenReturn(connection);
    Optional<Dialect> dialect = sqliteDialectProvider.getDialect(new JdbcTemplate(dataSource));
    assertThat(dialect).isPresent().get().isInstanceOf(JdbcPostgresDialect.class);
  }

  @Test
  void getDialect_UnknownDatabaseProductGiven_ShouldReturnEmpty() throws SQLException {
    DatabaseMetaData metaData = mock(DatabaseMetaData.class);
    when(metaData.getDatabaseProductName()).thenReturn("MonetDB");
    Connection connection = mock(Connection.class);
    when(connection.getMetaData()).thenReturn(metaData);
    DataSource dataSource = mock(DataSource.class);
    when(dataSource.getConnection()).thenReturn(connection);
    Optional<Dialect> dialect = sqliteDialectProvider.getDialect(new JdbcTemplate(dataSource));
    assertThat(dialect).isEmpty();
  }
}
