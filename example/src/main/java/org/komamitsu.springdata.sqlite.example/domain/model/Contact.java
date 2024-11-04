package org.komamitsu.springdata.sqlite.example.domain.model;

import org.springframework.data.annotation.Id;

public class Contact {
  @Id Long id;
  Boolean isOrganization;

  public Contact(Long id, Boolean isOrganization) {
    this.id = id;
    this.isOrganization = isOrganization;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "id=" + id +
        ", isOrganization=" + isOrganization +
        '}';
  }
}
