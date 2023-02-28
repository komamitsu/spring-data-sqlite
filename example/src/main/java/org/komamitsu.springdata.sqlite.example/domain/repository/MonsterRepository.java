package org.komamitsu.springdata.sqlite.example.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteRepository;
import org.komamitsu.springdata.sqlite.example.domain.model.Monster;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MonsterRepository extends SqliteRepository<Monster, String> {
  default void writeAndAbortBeforeCommit(Monster monster) {
    insert(monster);
    throw new RuntimeException("Intentional abort");
  }
}
