package org.komamitsu.spring.data.sqlite.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteRepository;
import org.komamitsu.spring.data.sqlite.domain.model.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends SqliteRepository<Group, Long> {}
