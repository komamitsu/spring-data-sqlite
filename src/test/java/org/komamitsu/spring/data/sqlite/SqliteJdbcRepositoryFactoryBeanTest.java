package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.komamitsu.spring.data.sqlite.domain.model.User;
import org.komamitsu.spring.data.sqlite.domain.repository.UserRepository;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.convert.BasicJdbcConverter;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

// This class uses
// https://github.com/spring-projects/spring-data-relational/blob/394b1cec31c5f6cbf528e973d629be4ac4b0fa61/spring-data-jdbc/src/test/java/org/springframework/data/jdbc/repository/support/JdbcRepositoryFactoryBeanUnitTests.java as a reference.
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class SqliteJdbcRepositoryFactoryBeanTest {
  @Mock DataAccessStrategy dataAccessStrategy;
  RelationalMappingContext mappingContext = new JdbcMappingContext();
  JdbcConverter converter;
  @Mock Dialect dialect;
  @Mock ApplicationEventPublisher publisher;
  @Mock NamedParameterJdbcOperations operations;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  ListableBeanFactory beanFactory;

  // If we use GroupRepository instead of UserRepository, setsUpInstanceCorrectly() test fails while
  // using GroupRepository as a part of Spring Data application works well. This means we need to
  // completely represent the same dependencies using mocks. But the implementation of Spring Data
  // would change and it's not worth making effort on it.
  // The unexpected thing happens at
  // https://github.com/spring-projects/spring-data-commons/blob/2.7.x/src/main/java/org/springframework/data/repository/core/support/DefaultRepositoryInformation.java#L147 where the injected APIs from the fragment repository are treated as non-custom methods.
  private SqliteJdbcRepositoryFactoryBean<UserRepository, User, Long> factoryBean;

  @BeforeEach
  void setUp() {
    converter = new BasicJdbcConverter(mappingContext, dataAccessStrategy);
    when(beanFactory.getBean(NamedParameterJdbcOperations.class)).thenReturn(operations);

    factoryBean = spy(new SqliteJdbcRepositoryFactoryBean<>(UserRepository.class));
  }

  @Test
  void setUpInstanceCorrectly() {
    factoryBean.setDataAccessStrategy(dataAccessStrategy);
    factoryBean.setMappingContext(mappingContext);
    factoryBean.setQueryMappingConfiguration(mock(QueryMappingConfiguration.class));

    factoryBean.setConverter(converter);
    factoryBean.setDialect(dialect);
    factoryBean.setApplicationEventPublisher(publisher);
    factoryBean.setJdbcOperations(operations);
    factoryBean.setBeanFactory(beanFactory);
    factoryBean.afterPropertiesSet();

    assertThat(factoryBean.getObject()).isNotNull();
  }
}
