package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SqliteRepositoryConfigurationExtensionTest {
  private SqliteRepositoryConfigurationExtension extension =
      new SqliteRepositoryConfigurationExtension();

  @Test
  void getModulePrefix() {
    assertThat(extension.getModulePrefix()).isEqualTo("sqlite");
  }

  @Test
  void getRepositoryFactoryBeanClassName() {
    assertThat(extension.getRepositoryFactoryBeanClassName())
        .isEqualTo("org.komamitsu.spring.data.sqlite.SqliteJdbcRepositoryFactoryBean");
  }
}
