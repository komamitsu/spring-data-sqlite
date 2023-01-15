package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SqliteRepositoryRegistrarTest {
  private final SqliteRepositoryRegistrar registrar = new SqliteRepositoryRegistrar();

  @Test
  void getAnnotation() {
    assertThat(registrar.getAnnotation()).isEqualTo(EnableSqliteRepositories.class);
  }

  @Test
  void getExtension() {
    assertThat(registrar.getExtension()).isInstanceOf(SqliteRepositoryConfigurationExtension.class);
  }
}
