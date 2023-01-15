package org.komamitsu.spring.data.sqlite;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.komamitsu.spring.data.sqlite.domain.model.Group;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.mapping.PersistentEntity;

@ExtendWith(MockitoExtension.class)
public class SqliteRepositoryImplTest {
  private SqliteRepositoryImpl<Group, Long> repository;
  @Mock private PersistentEntity<Group, ?> persistentEntity;
  @Mock private JdbcAggregateOperations operations;
  @Mock private Group group;

  @BeforeEach
  void setUp() {
    repository =
        new SqliteRepositoryImpl<>(operations, persistentEntity, mock(JdbcConverter.class));
  }

  @Test
  void insert() {
    repository.insert(group);
    verify(operations).insert(group);
  }

  @Test
  void update() {
    repository.update(group);
    verify(operations).update(group);
  }
}
