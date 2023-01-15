package org.komamitsu.spring.data.sqlite;

import org.springframework.data.jdbc.repository.config.JdbcRepositoryConfigExtension;

/**
 * {@link JdbcRepositoryConfigExtension} extending the repository registration process by
 * registering SQLite repositories.
 */
public class SqliteRepositoryConfigurationExtension extends JdbcRepositoryConfigExtension {

  @Override
  protected String getModulePrefix() {
    return "sqlite";
  }

  @Override
  public String getRepositoryFactoryBeanClassName() {
    return SqliteJdbcRepositoryFactoryBean.class.getName();
  }
}
