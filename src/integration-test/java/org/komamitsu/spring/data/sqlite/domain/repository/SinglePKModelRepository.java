package org.komamitsu.spring.data.sqlite.domain.repository;

import java.util.List;
import java.util.Optional;
import org.komamitsu.spring.data.sqlite.SqliteHelperRepository;
import org.komamitsu.spring.data.sqlite.domain.model.SinglePKModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SinglePKModelRepository
    extends PagingAndSortingRepository<SinglePKModel, Integer>,
        SqliteHelperRepository<SinglePKModel> {

  Optional<SinglePKModel> findFirstByAccountId(int accountId);

  List<SinglePKModel> findByAccountType(int accountType);

  List<SinglePKModel> findTop2ByAccountType(int accountType);

  List<SinglePKModel> findAllByOrderByAccountId();

  @Transactional
  default void incrementBalance(int accountId, int value) {
    Optional<SinglePKModel> model = findById(accountId);
    if (model.isPresent()) {
      SinglePKModel existing = model.get();
      SinglePKModel updated =
          new SinglePKModel(
              existing.accountId, existing.accountType, existing.balance + value, existing.name);
      update(updated);
    }
  }

  @Transactional
  default void transfer(int accountIdFrom, int accountIdTo, int value) {
    incrementBalance(accountIdFrom, -value);
    incrementBalance(accountIdTo, value);
  }

  @Transactional
  default void deleteAfterSelect(int accountId) {
    Optional<SinglePKModel> testSinglePK = findById(accountId);
    testSinglePK.ifPresent(this::delete);
  }
}
