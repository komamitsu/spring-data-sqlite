package org.komamitsu.spring.data.sqlite;

import org.springframework.data.relational.core.dialect.AbstractDialect;
import org.springframework.data.relational.core.dialect.LimitClause;
import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
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

  // Custom implementation to avoid binary incompatibility between Spring Data 3.x and 4.x.
  // The return type of IdentifierProcessing.create() changed from DefaultIdentifierProcessing to
  // IdentifierProcessing, which causes NoSuchMethodError when compiled against one version and
  // run against the other.
  private static final IdentifierProcessing IDENTIFIER_PROCESSING =
      new IdentifierProcessing() {
        @Override
        public String quote(String identifier) {
          return "\"" + identifier + "\"";
        }

        @Override
        public String standardizeLetterCase(String identifier) {
          return identifier.toLowerCase();
        }
      };

  @Override
  public IdentifierProcessing getIdentifierProcessing() {
    return IDENTIFIER_PROCESSING;
  }
}
