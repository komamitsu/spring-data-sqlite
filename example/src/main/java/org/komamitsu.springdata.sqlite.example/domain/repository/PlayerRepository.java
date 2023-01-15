package org.komamitsu.springdata.sqlite.example.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteHelperRepository;
import org.komamitsu.springdata.sqlite.example.domain.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PlayerRepository
    extends PagingAndSortingRepository<Player, String>, SqliteHelperRepository<Player> {
  Logger logger = LoggerFactory.getLogger(PlayerRepository.class);

  default void getAndDelete(Player player) {
    findById(player.id).ifPresent(existing -> delete(player));
  }

  default void getAndUpdate(Player player) {
    findById(player.id).ifPresent(existing -> update(player));
  }
}
