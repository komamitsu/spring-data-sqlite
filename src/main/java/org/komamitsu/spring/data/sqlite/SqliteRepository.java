package org.komamitsu.spring.data.sqlite;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * A Spring Data fragment repository interface for SQLite. This interface has a feature that's not
 * SQLite specific but useful in some cases. {@link CrudRepository#save(Object)} depends on
 * auto-increment id columns and {@link CrudRepository} doesn't expose `insert()` and `update()`
 * methods. So, users always need to use SQLite's autoincrement feature on tables. For
 * non-autoincrement ID tables, indeed there are some workarounds
 * https://spring.io/blog/2021/09/09/spring-data-jdbc-how-to-use-custom-id-generation, but they are
 * not straightforward. This class provides `insert()` and `update()` instead of {@link
 * CrudRepository}.
 *
 * @param <T> a model class
 * @param <ID> an identifier class
 */
@NoRepositoryBean
public interface SqliteRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
  T insert(T instance);

  T update(T instance);
}
