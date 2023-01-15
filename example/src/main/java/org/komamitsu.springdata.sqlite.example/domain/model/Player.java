package org.komamitsu.springdata.sqlite.example.domain.model;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Player {
  @Id public String id;
  public int hp;
  public int attack;

  public Player(String id, int hp, int attack) {
    this.id = id;
    this.hp = hp;
    this.attack = attack;
  }

  @Override
  public String toString() {
    return "Player{" + "id='" + id + '\'' + ", hp=" + hp + ", attack=" + attack + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return hp == player.hp && attack == player.attack && Objects.equals(id, player.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hp, attack);
  }
}
