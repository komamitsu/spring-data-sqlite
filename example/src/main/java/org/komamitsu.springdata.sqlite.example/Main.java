package org.komamitsu.springdata.sqlite.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.komamitsu.spring.data.sqlite.EnableSqliteRepositories;
import org.komamitsu.springdata.sqlite.example.domain.model.Monster;
import org.komamitsu.springdata.sqlite.example.domain.model.Player;
import org.komamitsu.springdata.sqlite.example.domain.repository.MonsterRepository;
import org.komamitsu.springdata.sqlite.example.domain.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableSqliteRepositories
@EnableRetry
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Autowired JdbcTemplate template;

  @Autowired PlayerRepository playerRepository;

  @Autowired MonsterRepository monsterRepository;

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  private void setUpSchema() throws IOException {
    ClassPathResource resource = new ClassPathResource("sqlite-schema.sql");
    Files.readAllLines(Paths.get(resource.getURI())).forEach(ddl -> template.execute(ddl));
  }

  @Bean
  public CommandLineRunner run() throws Exception {
    return (String[] args) -> {
      setUpSchema();

      String playerId = "foo-bar";

      playerRepository
          .findById(playerId)
          .ifPresent(
              player -> {
                throw new AssertionError("The target entity shouldn't exist");
              });
      logger.info("Confirmed the player didn't exist expectedly");

      {
        Player player = new Player(playerId, 111, 11);
        playerRepository.insert(player);
        logger.info("Inserted {}", player);

        Player stored =
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new AssertionError("The stored entity isn't found"));
        if (!stored.equals(player)) {
          throw new AssertionError(
              String.format(
                  "The entity content isn't expected. expected=%s, actual=%s", player, stored));
        }
        logger.info("Confirmed {} was found expectedly", player);
      }

      {
        Player player = new Player(playerId, 222, 22);
        playerRepository.getAndUpdate(player);
        logger.info("Updated the player to {}", player);

        Player stored =
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new AssertionError("The stored entity isn't found"));
        if (!stored.equals(player)) {
          throw new AssertionError(
              String.format(
                  "The entity content isn't expected. expected=%s, actual=%s", player, stored));
        }
        logger.info("Confirmed the player was updated expectedly: {}", player);
      }

      {
        String enemyId = "will-o'-wisp";

        Monster monster = new Monster(enemyId, 99, 9);
        monsterRepository.insert(monster);
        logger.info("Inserted {}", monster);

        Monster stored =
            monsterRepository
                .findById(enemyId)
                .orElseThrow(() -> new AssertionError("The stored entity isn't found"));
        if (!stored.equals(monster)) {
          throw new AssertionError(
              String.format(
                  "The entity content isn't expected. expected=%s, actual=%s", monster, stored));
        }
        logger.info("Confirmed {} was found expectedly", monster);
      }

      {
        String enemyId = "greater demon";

        Monster monster = new Monster(enemyId, 888, 88);
        try {
          monsterRepository.writeAndAbortBeforeCommit(monster);
          throw new AssertionError("Shouldn't reach here");
        } catch (RuntimeException e) {
          // Expected
        }
        logger.info("Tried to insert {}, but it threw an exception before commit", monster);

        monsterRepository
            .findById(enemyId)
            .ifPresent(
                found -> {
                  throw new AssertionError("The target entity shouldn't exist");
                });
        logger.info(
            "Confirmed the partial write operation was aborted expectedly since {} was not found",
            monster);
      }

      logger.info("All the tests passed successfully!");
    };
  }
}
