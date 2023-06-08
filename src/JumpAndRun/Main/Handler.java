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

public class Handler {
    public LinkedList<Entity> entity = new LinkedList<Entity>();
    public LinkedList<Tile> tile = new LinkedList<Tile>();

    public void render(Graphics g) {
        for (Entity en:entity) {
            en.render(g);
        }

        for (Tile ti:tile) {
            ti.render(g);
        }
    }

    public void tick() {
        for (int i=0;i<entity.size();i++) {
        	Entity en = entity.get(i);
            en.tick();
        }

        for (int i=0;i<tile.size();i++) {
        	Tile ti = tile.get(i);
            ti.tick();
        }
    }

    public void addEntity(Entity en) {
        entity.add(en);
    }

    public void removeEntity(Entity en) {
        entity.remove(en);
    }

    public void addTile(Tile ti) {
        tile.add(ti);
    }

    public void removeTile(Tile ti) {
        tile.remove(ti);
    }

    public void createLevel(BufferedImage level) {
        int width = level.getWidth();
        int height = level.getHeight();

        for (int y=0;y<height;y++) {
            for (int x=0;x < width;x++) {
                int pixel = level.getRGB(x,y);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red==0&&green==0&&blue==0) addTile(new Wall(x*64, y*64,64,64,true, Id.wall,this));
                if (red==0&&green==0&&blue==255) addEntity(new Player(x*64,y*64,64,64,false,Id.player,this));
                if (red==255&&green==119&&blue==0) addEntity(new mini(x*64,y*64,64,64,true,Id.mini,this));
                if (red==255&&green==0&&blue==255) addEntity(new EndBoss(x*64,y*64,64,64,Id.endboss,this));
            }
        }
    }

    public void clearlevel() {
        entity.clear();
        tile.clear();
    }
}
