package de.caladon.vacuum.game;

import java.util.Random;
import java.util.UUID;

record Unit(String name, UUID id, int x, int y, int spawnTick, int deathTick)
    implements Comparable<Unit> {
  static Unit generateRandomUnit() {
    var random = new Random();
    var x = random.nextInt(Game.WIDTH);
    var y = random.nextInt(Game.HEIGHT);
    return new Unit("Name", UUID.randomUUID(), x, y, 0, 0);
  }

  @Override
  public int compareTo(Unit o) {
    return id.compareTo(o.id);
  }
}
