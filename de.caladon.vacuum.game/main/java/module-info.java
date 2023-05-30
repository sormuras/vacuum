module de.caladon.vacuum.game {
  requires java.desktop;

  provides java.util.spi.ToolProvider with
      de.caladon.vacuum.game.GameToolProvider;
}
