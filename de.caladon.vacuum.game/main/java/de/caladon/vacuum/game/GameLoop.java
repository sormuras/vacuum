package de.caladon.vacuum.game;

class GameLoop implements Runnable {
  boolean alive = true;

  void exit() {
    alive = false;
  }

  @Override
  public void run() {
    long tick = 0;
    while (alive) {
      tick++;

      // create new random unit and add it
      var unit = Unit.generateRandomUnit();
      // TODO game.add(unit);

      try {
        Thread.sleep(1000);
      } catch (Exception exception) {
        break;
      }
      System.out.println(tick);
    }
  }
}
