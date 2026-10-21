package JumpAndRun.Entity.gegner;

import JumpAndRun.Entity.Entity;
import JumpAndRun.Main.Handler;
import JumpAndRun.Main.Id;
import JumpAndRun.Main.Main;
import JumpAndRun.tile.Tile;
import java.awt.*;

public class Player extends Entity {

  private int frame = 0;
  private int frameDelay = 0;

  private boolean animate = false;

  private Player(Builder builder) {
    super(builder);
    setVelX(5);
  }

  @Override
  public void render(Graphics g) {
    if (facing == 0) {
      g.drawImage(Main.player[frame + 5].getBufferedImage(), x, y, width, height, null);
    } else if (facing == 1) {
      g.drawImage(Main.player[frame].getBufferedImage(), x, y, width, height, null);
    }
  }

  @Override
  public void tick() {
    x += velX;
    y += velY;
    if (y + height >= 771) y = 771 - height;
    if (velX != 0) animate = true;
    else animate = false;
    boolean onGround = false;

    for (int i = 0; i < handler.tile.size(); i++) {
      Tile t = handler.tile.get(i);
      if (t.isSolid()) {
        if (getBoundsTop().intersects(t.getBounds())) {
          setVelY(0);
          if (jumping) {
            jumping = false;
            gravity = 0.8;
            falling = true;
          }
        }
        if (getBoundsBottom().intersects(t.getBounds())) {
          setVelY(0);
          onGround = true;
        }
        if (getBoundsLeft().intersects(t.getBounds())) {
          setVelX(0);
          x = t.getX() + t.width;
        }
        if (getBoundsRight().intersects(t.getBounds())) {
          setVelX(0);
          x = t.getX() - width;
        }
      }
    }

    if (onGround) {
      falling = false;
    } else if (!falling && !jumping) {
      falling = true;
      gravity = 0.8;
    }

    for (int i = 0; i < handler.entity.size(); i++) {
      Entity e = handler.entity.get(i);

      if (e.getId() == Id.mini) {
        if (getBoundsBottom().intersects(e.getBoundsTop())) {
          e.die();
        } else if (getBounds().intersects(e.getBounds())) {
          die();
          break;
        }
      }
    }

    if (jumping) {
      gravity -= 0.15;
      setVelY((int) -gravity);
      if (gravity <= 0.6) {
        jumping = false;
        falling = true;
      }
    }

    if (falling) {
      gravity += 0.15;
      setVelY((int) gravity);
    }

    if (velX != 0) {
      frameDelay++;
      if (frameDelay >= 3) {
        frame++;
        if (frame >= 5) {
          frame = 0;
        }
        frameDelay = 0;
      }
    }
  }

  public static class Builder extends Entity.Builder<Builder> {
    public Builder(int x, int y, int width, int height, Id id, Handler handler) {
      super(x, y, width, height, id, handler);
    }

    @Override
    public Player build() {
      return new Player(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
