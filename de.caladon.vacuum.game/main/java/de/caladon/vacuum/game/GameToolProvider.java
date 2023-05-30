package de.caladon.vacuum.game;

import java.io.PrintWriter;
import java.util.spi.ToolProvider;

public record GameToolProvider(String name) implements ToolProvider {
  public GameToolProvider() {
    this("vacuum");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    Game.main(args);
    return 0;
  }
}
