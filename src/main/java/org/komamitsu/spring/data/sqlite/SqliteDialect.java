package org.komamitsu.spring.data.sqlite;

import org.springframework.data.relational.core.dialect.AbstractDialect;
import org.springframework.data.relational.core.dialect.LimitClause;
import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.IdentifierProcessing.LetterCasing;
import org.springframework.data.relational.core.sql.IdentifierProcessing.Quoting;
import org.springframework.data.relational.core.sql.LockOptions;

/**
 * A SQL dialect for SQLite. This class does nothing for pessimistic locking as SQLite doesn't
 * support the feature.
 */
public class SqliteDialect extends AbstractDialect {
  /** Singleton instance. */
  public static final SqliteDialect INSTANCE = new SqliteDialect();

  private static final LimitClause LIMIT_CLAUSE =
      new LimitClause() {

        @Override
        public String getLimit(long limit) {
          return "LIMIT " + limit;
        }

        @Override
        public String getOffset(long offset) {
          throw new UnsupportedOperationException("Only OFFSET isn't supported");
        }

        @Override
        public String getLimitOffset(long limit, long offset) {
          return String.format("LIMIT %d OFFSET %d", limit, offset);
        }

        @Override
        public Position getClausePosition() {
          return Position.AFTER_ORDER_BY;
        }
      };

  private final LockClause LOCK_CLAUSE =
      new LockClause() {
        @Override
        public String getLock(LockOptions lockOptions) {
          throw new UnsupportedOperationException("Pessimistic lock isn't supported");
        }

        @Override
        public Position getClausePosition() {
          return Position.AFTER_ORDER_BY;
        }
      };

  @Override
  public LimitClause limit() {
    return LIMIT_CLAUSE;
  }

  @Override
  public LockClause lock() {
    return LOCK_CLAUSE;
  }

  @Override
  public IdentifierProcessing getIdentifierProcessing() {
    return IdentifierProcessing.create(Quoting.ANSI, LetterCasing.LOWER_CASE);
  }
}
