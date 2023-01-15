package org.komamitsu.springdata.sqlite.example.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteHelperRepository;
import org.komamitsu.springdata.sqlite.example.domain.model.Monster;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MonsterRepository
    extends PagingAndSortingRepository<Monster, String>, SqliteHelperRepository<Monster> {
  default void writeAndAbortBeforeCommit(Monster monster) {
    insert(monster);
    throw new RuntimeException("Intentional abort");
  }
}
