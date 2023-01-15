package org.komamitsu.spring.data.sqlite;

import java.lang.annotation.Annotation;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * {@link RepositoryBeanDefinitionRegistrarSupport} extending the repository registration for SQLite
 * DB.
 */
public class SqliteRepositoryRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

  @Override
  protected Class<? extends Annotation> getAnnotation() {
    return EnableSqliteRepositories.class;
  }

  @Override
  protected RepositoryConfigurationExtension getExtension() {
    return new SqliteRepositoryConfigurationExtension();
  }
}
