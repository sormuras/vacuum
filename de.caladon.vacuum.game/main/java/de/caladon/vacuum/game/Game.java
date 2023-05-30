package de.caladon.vacuum.game;

import static javax.swing.JOptionPane.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;
import java.util.UUID;
import javax.swing.*;

record Game(World world, GameLoop loop) {

  static int WIDTH = 800;
  static int HEIGHT = 600;

  public static void main(String... args) {
    System.out.println(Thread.currentThread());

    var world = new World(new TreeSet<>());
    var loop = new GameLoop();
    var game = new Game(world, loop);
    game.printSummary();

    SwingUtilities.invokeLater(game::createAndShowGamePanel);
    SwingUtilities.invokeLater(game::createAndShowCommandPanel);

    loop.run();
    System.exit(0);
  }

  void createAndShowGamePanel() {
    JFrame frame = new JFrame("Vacuum Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new GamePanel());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  void createAndShowCommandPanel() {
    JFrame frame = new JFrame("Vacuum Commander");
    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    frame.add(new CommandPanel(frame, this));
    frame.pack();
    frame.setLocation(100, 100);
    frame.setVisible(true);
  }

  void handleCommand(JFrame frame, GameLoop loop, String command) {
    switch (command) {
      case "exit" -> loop.exit();
      case "add" -> {
        var name = showInputDialog(frame, "Add Unit. Name?");
        var unit = new Unit(name, UUID.randomUUID(), 0, 0, 0, 0);
        add(unit);
      }
      case "del", "delete", "kill", "remove" -> {
        var name = showInputDialog(frame, "Remove Unit. Name?");
        var unit = world.units().stream().filter(u -> u.name().equals(name)).findFirst();
        unit.ifPresentOrElse(this::remove, () -> System.out.println("Name unknown: " + name));
      }
      case "status" -> {
        System.out.println("Status");
        printSummary();
      }
      default -> System.out.println("Unknown command: " + command);
    }
  }

  void add(Unit unit) {
    world.units().add(unit);
  }

  void remove(Unit unit) {
    world.units().remove(unit);
  }

  void printSummary() {
    var timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toLocalTime();
    System.out.printf("[%s] World with %d unit(s)%n", timestamp, world.units().size());
    world.units().forEach(System.out::println);
  }
}
