package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallback;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@ExtendWith(MockitoExtension.class)
public class SqliteJdbcConfigurationTest {
  private final SqliteJdbcConfiguration configuration = new SqliteJdbcConfiguration();

  @Test
  void createJdbcAggregateTemplate(
      @Mock ApplicationContext applicationContext,
      @Mock ObjectProvider<EntityCallback<?>> objectProvider) {
    doReturn(objectProvider).when(applicationContext).getBeanProvider(EntityCallback.class);
    JdbcAggregateTemplate aggregateTemplate =
        configuration.createJdbcAggregateTemplate(
            applicationContext,
            mock(JdbcMappingContext.class),
            mock(JdbcConverter.class),
            mock(DataAccessStrategy.class));
    assertThat(aggregateTemplate).isInstanceOf(SqliteJdbcAggregateTemplate.class);
  }

  @Test
  void createJdbcRepositoryFactory() {
    JdbcRepositoryFactory repositoryFactory =
        configuration.createJdbcRepositoryFactory(
            mock(DataAccessStrategy.class),
            mock(RelationalMappingContext.class),
            mock(JdbcConverter.class),
            mock(Dialect.class),
            mock(ApplicationEventPublisher.class),
            mock(NamedParameterJdbcOperations.class));
    assertThat(repositoryFactory).isInstanceOf(SqliteJdbcRepositoryFactory.class);
  }
}
