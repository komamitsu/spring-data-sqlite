package org.komamitsu.spring.data.sqlite.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(schema = "public", value = "user")
public class User {
  @Id
  @Column("id")
  public final Long id;

  @Column("group_id")
  public final Long groupId;

  @Column("name")
  public final String name;

  @Column("point")
  public final Integer point;

  public User(Long id, Long groupId, String name, Integer point) {
    this.id = id;
    this.groupId = groupId;
    this.name = name;
    this.point = point;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", groupId="
        + groupId
        + ", name='"
        + name
        + '\''
        + ", point="
        + point
        + '}';
  }
}
