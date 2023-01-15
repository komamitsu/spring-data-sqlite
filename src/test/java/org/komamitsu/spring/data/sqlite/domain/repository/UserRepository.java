package org.komamitsu.spring.data.sqlite.domain.repository;

import org.komamitsu.spring.data.sqlite.domain.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {}
