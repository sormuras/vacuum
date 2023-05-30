package de.caladon.vacuum.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel {

  private int squareX = 50;
  private int squareY = 50;
  private int squareW = 100;
  private int squareH = 100;

  GamePanel() {
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            moveSquare(e.getX(), e.getY());
          }
        });
  }

  private void moveSquare(int x, int y) {
    int OFFSET = 0;
    if ((squareX != x) || (squareY != y)) {
      var w2 = squareW / 2;
      var h2 = squareH / 2;
      repaint(squareX - w2, squareY - h2, squareW + OFFSET, squareH + OFFSET);
      squareX = x;
      squareY = y;
      repaint(squareX - w2, squareY - h2, squareW + OFFSET, squareH + OFFSET);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(Game.WIDTH, Game.HEIGHT);
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    var w2 = squareW / 2;
    var h2 = squareH / 2;
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect(0, 0, getWidth(), getHeight());
    graphics.drawString("This is my custom Panel!", 10, 20);
    graphics.setColor(Color.RED);
    graphics.fillRect(squareX - w2, squareY - h2, squareW, squareH);
  }
}
