package de.caladon.vacuum.game;

import java.awt.*;
import javax.swing.*;

class CommandPanel extends JPanel {
  CommandPanel(JFrame frame, Game game) {
    var summaryButton = new JButton("Print Summary");
    summaryButton.addActionListener(event -> game.printSummary());

    var commandField = new JTextField("<enter command here>");
    commandField.setMinimumSize(new Dimension(200, 20));
    commandField.addActionListener(
        event -> {
          var command = commandField.getText();
          commandField.setText("");
          game.handleCommand(frame, game.loop(), command);
        });

    add(summaryButton);
    add(commandField);
  }
}
