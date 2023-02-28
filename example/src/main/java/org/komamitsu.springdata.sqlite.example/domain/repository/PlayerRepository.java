package org.komamitsu.springdata.sqlite.example.domain.repository;

import org.komamitsu.spring.data.sqlite.SqliteRepository;
import org.komamitsu.springdata.sqlite.example.domain.model.Player;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PlayerRepository extends SqliteRepository<Player, String> {
  default void getAndDelete(Player player) {
    findById(player.id).ifPresent(existing -> delete(player));
  }

  default void getAndUpdate(Player player) {
    findById(player.id).ifPresent(existing -> update(player));
  }
}
