package org.komamitsu.spring.data.sqlite.domain.model;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class SinglePKModel {

  @Id public final Integer accountId;

  public final Integer accountType;

  public final Integer balance;

  public final String name;

  public SinglePKModel(Integer accountId, Integer accountType, Integer balance, String name) {
    this.accountId = accountId;
    this.accountType = accountType;
    this.balance = balance;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SinglePKModel)) {
      return false;
    }
    SinglePKModel that = (SinglePKModel) o;
    return Objects.equals(accountId, that.accountId)
        && Objects.equals(accountType, that.accountType)
        && Objects.equals(balance, that.balance)
        && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountType, balance, name);
  }

  @Override
  public String toString() {
    return "SinglePKModel{"
        + "accountId="
        + accountId
        + ", accountType="
        + accountType
        + ", balance="
        + balance
        + ", name="
        + name
        + '}';
  }
}
