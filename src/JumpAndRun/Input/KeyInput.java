package JumpAndRun.Input;

import JumpAndRun.Entity.Entity;
import JumpAndRun.Main.Id;
import JumpAndRun.Main.Main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
  private boolean left = false;
  private boolean right = false;

  public void keyTyped(KeyEvent e) {
    // wird nicht benutzt
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    for (int i = 0; i < Main.handler.entity.size(); i++) {
      Entity en = Main.handler.entity.get(i);
      if (en.getId() == Id.player) {
        switch (key) {
          case KeyEvent.VK_W:
            if (!en.jumping && !en.falling) {
              en.jumping = true;
              en.gravity = 10.0;
            }
            break;
          case KeyEvent.VK_A:
            left = true;
            break;
          case KeyEvent.VK_D:
            right = true;
            break;
        }
        if (left && !right) {
          en.setVelX(-5);
          en.facing = 0;
        } else if (right && !left) {
          en.setVelX(5);
          en.facing = 1;
        } else {
          en.setVelX(0);
        }
      }
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    for (int i = 0; i < Main.handler.entity.size(); i++) {
      Entity en = Main.handler.entity.get(i);
      if (en.getId() == Id.player) {
        switch (key) {
          case KeyEvent.VK_W:
            en.setVelY(0);
            break;
          case KeyEvent.VK_S:
            en.setVelY(0);
            break;
          case KeyEvent.VK_A:
            left = false;
            break;
          case KeyEvent.VK_D:
            right = false;
            break;
        }
        if (!left && !right) {
          en.setVelX(0);
        } else if (!left && right) {
          en.setVelX(5);
          en.facing = 1;
        } else if (!right && left) {
          en.setVelX(-5);
          en.facing = 0;
        }
      }
    }
  }
}
