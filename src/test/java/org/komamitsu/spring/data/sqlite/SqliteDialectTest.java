package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.dialect.LimitClause;
import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.sql.From;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.core.sql.LockOptions;

class SqliteDialectTest {

  private final SqliteDialect dialect = new SqliteDialect();

  @Test
  void limit() {
    LimitClause limit = dialect.limit();
    assertThat(limit.getClausePosition()).isEqualTo(LimitClause.Position.AFTER_ORDER_BY);
    assertThat(limit.getLimit(42)).isEqualTo("LIMIT 42");
    assertThrows(UnsupportedOperationException.class, () -> limit.getOffset(10000));
    assertThat(limit.getLimitOffset(42, 24)).isEqualTo("LIMIT 42 OFFSET 24");
  }

  @Test
  void lock() {
    LockClause lock = dialect.lock();
    From from = mock(From.class);
    assertThat(lock.getClausePosition()).isEqualTo(LockClause.Position.AFTER_ORDER_BY);
    assertThrows(
        UnsupportedOperationException.class,
        () -> lock.getLock(new LockOptions(LockMode.PESSIMISTIC_READ, from)));
    assertThrows(
        UnsupportedOperationException.class,
        () -> lock.getLock(new LockOptions(LockMode.PESSIMISTIC_WRITE, from)));
  }
}
