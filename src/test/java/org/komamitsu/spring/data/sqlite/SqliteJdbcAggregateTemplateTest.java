package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.komamitsu.spring.data.sqlite.domain.model.Group;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.mapping.callback.EntityCallback;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

class SqliteJdbcAggregateTemplateTest {

  private SqliteJdbcAggregateTemplate template;
  private JdbcAggregateTemplate templateAsSuper;
  private final Group group = new Group(1L, "My group", 4.2);

  @BeforeEach
  void setUp() {
    ApplicationContext publisher = mock(ApplicationContext.class);
    when(publisher.getBeanProvider(EntityCallback.class)).thenReturn(mock(ObjectProvider.class));
    RelationalMappingContext context = mock(RelationalMappingContext.class);
    JdbcConverter converter = mock(JdbcConverter.class);
    DataAccessStrategy dataAccessStrategy = mock(DataAccessStrategy.class);
    template =
        spy(new SqliteJdbcAggregateTemplate(publisher, context, converter, dataAccessStrategy));
    templateAsSuper = template;
    doReturn(group).when(templateAsSuper).save(group);
    doReturn(group).when(templateAsSuper).insert(group);
    doReturn(group).when(templateAsSuper).update(group);
    doNothing().when(templateAsSuper).delete(group, Group.class);
    doNothing().when(templateAsSuper).deleteById(group.id, Group.class);
    doNothing().when(templateAsSuper).deleteAll(Group.class);
    doReturn(42L).when(templateAsSuper).count(Group.class);
    doReturn(group).when(templateAsSuper).findById(group.id, Group.class);
    doReturn(Arrays.asList(group)).when(templateAsSuper).findAll(Group.class);
    doReturn(true).when(templateAsSuper).existsById(group.id, Group.class);
    List<Group> groupList = Collections.singletonList(group);
    List<Long> groupIdList = groupList.stream().map(group -> group.id).collect(Collectors.toList());
    doReturn(groupList).when(templateAsSuper).findAllById(groupIdList, Group.class);
  }

  @Test
  void save() {
    assertThat(template.save(group)).isInstanceOf(Group.class);
    verify(templateAsSuper).save(group);
  }

  @Test
  void insert() {
    assertThat(template.insert(group)).isInstanceOf(Group.class);
    verify(templateAsSuper).insert(group);
  }

  @Test
  void update() {
    assertThat(template.update(group)).isInstanceOf(Group.class);
    verify(templateAsSuper).update(group);
  }

  @Test
  void count() {
    assertThat(template.count(Group.class)).isEqualTo(42);
    verify(templateAsSuper).count(Group.class);
  }

  @Test
  void findById() {
    assertThat(template.findById(group.id, Group.class)).isEqualTo(group);
    verify(templateAsSuper).findById(group.id, Group.class);
  }

  @Test
  void existsById() {
    assertThat(template.existsById(group.id, Group.class)).isTrue();
    verify(templateAsSuper).existsById(group.id, Group.class);
  }

  @Test
  void findAll() {
    assertThat(template.findAll(Group.class)).isEqualTo(Arrays.asList(group));
    verify(templateAsSuper).findAll(Group.class);
  }

  @Test
  void findAllById() {
    List<Long> groupIdList = Collections.singletonList(group.id);
    Iterable<Group> found = template.findAllById(groupIdList, Group.class);
    assertThat(found).size().isEqualTo(1);
    assertThat(found.iterator().next().id).isEqualTo(group.id);
    verify(templateAsSuper).findAllById(groupIdList, Group.class);
  }

  @Test
  void delete() {
    template.delete(group, Group.class);
    verify(templateAsSuper).delete(group, Group.class);
  }

  @Test
  void deleteById() {
    template.deleteById(group.id, Group.class);
    verify(templateAsSuper).deleteById(group.id, Group.class);
  }

  @Test
  void deleteAll() {
    template.deleteAll(Group.class);
    verify(templateAsSuper).deleteAll(Group.class);
  }
}
