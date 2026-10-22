package JumpAndRun.Main;

import JumpAndRun.Entity.Entity;
import JumpAndRun.Entity.gegner.EndBoss;
import JumpAndRun.Entity.gegner.Player;
import JumpAndRun.Entity.gegner.mini;
import JumpAndRun.tile.Tile;
import JumpAndRun.tile.Wall;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.HashSet;

public class Handler {
  public LinkedList<Entity> entity = new LinkedList<Entity>();
  public LinkedList<Tile> tile = new LinkedList<Tile>();
  private LinkedList<Entity> deadEntities = new LinkedList<Entity>();
  private HashSet<Long> solidTiles = new HashSet<Long>();

  public int levelHeight;

  public void render(Graphics g) {
    for (Tile ti : tile) {
      ti.render(g);
    }

    for (Entity en : entity) {
      en.render(g);
    }
  }

  public void tick() {
    for (int i = 0; i < entity.size(); i++) {
      Entity en = entity.get(i);
      en.tick();
    }

    for (int i = 0; i < deadEntities.size(); i++) {
      Entity den = deadEntities.get(i);
      entity.remove(den);
    }

    deadEntities.clear();

    for (int i = 0; i < tile.size(); i++) {
      Tile ti = tile.get(i);
      ti.tick();
    }
  }

  public void addEntity(Entity en) {
    entity.add(en);
  }

  public void removeEntity(Entity en) {
    deadEntities.add(en);
  }

  public void addTile(Tile ti) {
    tile.add(ti);
    if (ti.isSolid()) {
      solidTiles.add(key(Math.floorDiv(ti.getX(), 64), Math.floorDiv(ti.getY(), 64)));
    }
  }

  public void removeTile(Tile ti) {
    tile.remove(ti);
    solidTiles.remove(key(Math.floorDiv(ti.getX(), 64), Math.floorDiv(ti.getY(), 64)));
  }

  private static long key(int col, int row) {
    return ((long) col << 32 | row & 0xFFFFFFFFL);
  }

  public boolean isSolidAt(int col, int row) {
    return solidTiles.contains(key(col, row));
  }

  public void createLevel(BufferedImage level) {
    int width = level.getWidth();
    int height = level.getHeight();
    int lowest = 0;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixel = level.getRGB(x, y);

        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        if (red == 0 && green == 0 && blue == 0) {
          addTile(new Wall(x * 64, y * 64, 64, 64, true, Id.wall, this));
          if (y > lowest) {
            lowest = y;
          }
        }

        if (red == 0 && green == 0 && blue == 255)
          addEntity(
              new Player.Builder(x * 64, y * 64, 64, 64, Id.player, this).solid(false).build());

        if (red == 255 && green == 119 && blue == 0)
          addEntity(new mini.Builder(x * 64, y * 64, 64, 64, Id.mini, this).solid(true).hp(2).build());

        if (red == 255 && green == 0 && blue == 255)
          addEntity(new EndBoss.Builder(x * 64, y * 64, 64, 64, Id.endboss, this).hp(10).build());
      }
    }
    levelHeight = lowest * 64;
  }

  public void clearlevel() {
    entity.clear();
    tile.clear();
    deadEntities.clear();
    solidTiles.clear();
  }
}
