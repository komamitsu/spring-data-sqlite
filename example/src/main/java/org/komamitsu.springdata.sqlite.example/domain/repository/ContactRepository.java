package org.komamitsu.springdata.sqlite.example.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteRepository;
import org.komamitsu.springdata.sqlite.example.domain.model.Contact;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends SqliteRepository<Contact, Long> {
}
