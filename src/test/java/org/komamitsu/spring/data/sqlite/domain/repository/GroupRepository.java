package org.komamitsu.spring.data.sqlite.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteHelperRepository;
import org.komamitsu.spring.data.sqlite.domain.model.Group;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository
    extends PagingAndSortingRepository<Group, Long>, SqliteHelperRepository<Group> {}
