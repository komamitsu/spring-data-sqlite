package org.komamitsu.spring.data.sqlite.domain.repository;

import java.util.List;
import java.util.Optional;
import org.komamitsu.spring.data.sqlite.SqliteHelperRepository;
import org.komamitsu.spring.data.sqlite.domain.model.CompositePKModel;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CompositePKModelRepository
    extends PagingAndSortingRepository<CompositePKModel, Integer>,
        SqliteHelperRepository<CompositePKModel> {

  Optional<CompositePKModel> findFirstByAccountIdAndAccountType(int accountId, int accountType);

  List<CompositePKModel> findByAccountIdAndAccountTypeBetween(
      int accountId, int accountTypeFrom, int accountTypeTo);

  List<CompositePKModel> findByAccountIdAndAccountTypeBetweenOrderByAccountTypeDesc(
      int accountId, int accountTypeFrom, int accountTypeTo);

  List<CompositePKModel> findTop2ByAccountIdAndAccountTypeBetween(
      int accountId, int accountTypeFrom, int accountTypeTo);

  List<CompositePKModel> findByName(String name);

  List<CompositePKModel> findAllByOrderByAccountIdAscAccountTypeAsc();

  /**
   * Update a record with attributes received as arguments
   *
   * @param accountId account ID to be updated
   * @param accountType account type to be updated
   * @param balance new balance value
   * @param someColumn new some column value
   * @return The number of updated records
   */
  @Modifying
  @Query(
      "UPDATE \"composite_pk_model\" SET \"balance\" = :balance, \"name\" = :name \n"
          + " WHERE \"account_id\" = :accountId \n"
          + " AND \"account_type\" = :accountType \n")
  int updateWithAttributes(
      @Param("accountId") int accountId,
      @Param("accountType") int accountType,
      @Param("balance") int balance,
      @Param("name") String name);

  @Transactional
  default void incrementBalance(int accountId, int accountType, int value) {
    Optional<CompositePKModel> model = findFirstByAccountIdAndAccountType(accountId, accountType);
    model.ifPresent(
        found ->
            updateWithAttributes(
                found.accountId, found.accountType, found.balance + value, found.name));
  }

  @Transactional
  default void transfer(
      int accountIdFrom, int accountTypeFrom, int accountIdTo, int accountTypeTo, int value) {
    incrementBalance(accountIdFrom, accountTypeFrom, -value);
    incrementBalance(accountIdTo, accountTypeTo, value);
  }

  // This method name and signature results in issuing a SELECT statement. It looks a bug of Spring
  // Data...
  // void deleteByAccountIdAndAccountType(int accountId, int accountType);
  /**
   * Delete a record by specifying attributes as conditions
   *
   * @param accountId account ID to be deleted
   * @param accountType account type to be deleted
   * @return The number of updated records
   */
  @Modifying
  @Query(
      "DELETE FROM \"composite_pk_model\" \n"
          + " WHERE \"account_id\" = :accountId \n"
          + " AND \"account_type\" = :accountType \n")
  @Transactional
  int deleteByAccountIdAndAccountType(
      @Param("accountId") int accountId, @Param("accountType") int accountType);

  @Transactional
  default void deleteByAccountIdAndAccountTypeAfterSelect(int accountId, int accountType) {
    Optional<CompositePKModel> entity = findFirstByAccountIdAndAccountType(accountId, accountType);
    entity.ifPresent(found -> deleteByAccountIdAndAccountType(accountId, accountType));
  }
}
