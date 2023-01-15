package org.komamitsu.spring.data.sqlite.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(schema = "public", value = "group")
public class Group {
  @Id
  @Column("id")
  public final Long id;

  @Column("name")
  public final String name;

  @Column("score")
  public final double score;

  public Group(Long id, String name, double score) {
    this.id = id;
    this.name = name;
    this.score = score;
  }

  public Group withName(String name) {
    return new Group(this.id, name, this.score);
  }

  @Override
  public String toString() {
    return "Group{" + "id=" + id + ", name='" + name + '\'' + ", score=" + score + '}';
  }
}
