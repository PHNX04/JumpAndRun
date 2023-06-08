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

    public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
        setVelX(5);
    }

    public void render(Graphics g) {
        if (facing == 0) {
            g.drawImage(Main.player[frame+5].getBufferedImage(), x, y, width, height, null);
        } else  if (facing==1) {
            g.drawImage(Main.player[frame].getBufferedImage(), x, y, width, height, null);
        }
    }

    public void tick() {
        x += velX;
        y += velY;
        if (y+height>=771) y =771-height;
        if (velX!=0) animate = true;
        else animate = false;

        for (int i = 0; i<handler.tile.size();i++) {
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
                    if (falling) falling = false;
                } else {
                    if (!falling&&!jumping) {
                        gravity = 0.8;
                        falling = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX()+t.width;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX()-t.width;
                }
            }
        }

        for (int i=0;i<handler.entity.size();i++) {
            Entity e = handler.entity.get(i);

            if (e.getId()==Id.mini) {
                if (getBoundsBottom().intersects(e.getBoundsTop())) {
                    e.die();
                }
                else if (getBounds().intersects(e.getBounds())) {
                    die();
                }
            }
        }

        if (jumping) {
            gravity -= 0.15;
            setVelY((int)-gravity);
            if (gravity<= 0.6) {
                jumping = false;
                falling = true;
            }
        }
        if (falling) {
            gravity+=0.15;
            setVelY((int)gravity);
        }

        if (velX!=0) {
            frameDelay++;
            if (frameDelay>=3) {
                frame++;
                if (frame>=5) {
                    frame = 0;
                }
                frameDelay = 0;
            }
        }
    }
}
