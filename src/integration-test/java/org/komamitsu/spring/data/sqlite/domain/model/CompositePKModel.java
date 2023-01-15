package org.komamitsu.spring.data.sqlite.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class CompositePKModel {

  // This @Id annotation is a kind of dummy since Spring Data JDBC requires @Id annotation and
  // doesn't support multiple ID columns...
  @Id public final Integer accountId;

  // This column is a part of PRIMARY KEY
  public final Integer accountType;

  public final Integer balance;

  public final String name;

  public CompositePKModel(Integer accountId, Integer accountType, Integer balance, String name) {
    this.accountId = accountId;
    this.accountType = accountType;
    this.balance = balance;
    this.name = name;
  }
}
