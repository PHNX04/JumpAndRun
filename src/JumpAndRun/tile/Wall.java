package JumpAndRun.tile;

import JumpAndRun.Main.Handler;
import JumpAndRun.Main.Id;
import JumpAndRun.Main.Main;

import java.awt.*;

public class Wall extends Tile{

    public Wall(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    public void render(Graphics g) {
        g.drawImage(Main.grass.getBufferedImage(), x ,y, width, height, null);
    }

    public void tick() {

    }
}
