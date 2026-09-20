package JumpAndRun.Entity;

import JumpAndRun.Main.Handler;
import JumpAndRun.Main.Id;
import JumpAndRun.Main.Main;
import JumpAndRun.States.BossState;

import java.awt.*;

public abstract class Entity {
    public int x, y;
    
    public final int width, height;
    public final Id id;
    public final Handler handler;
    public final boolean solid;
    
    public int facing = 0;
    public int hp;
    public int phaseTime;
    public boolean jumping = false;
    public boolean falling = true;
    public int velX, velY;
    public BossState bossstate;
    public boolean attackable = false;
    public double gravity = 0.0;

    public Entity(Builder<?> builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.id = builder.id;
        this.handler = builder.handler;
        this.solid = builder.solid;
        this.hp = builder.hp;
        this.attackable = builder.attackable;
    }

    public abstract void render(Graphics g);
    public abstract void tick();

    public void die() {
        handler.removeEntity(this);
        if (getId()==Id.player) {
            Main.leben--;
            Main.deathscreen = true;

            if (Main.leben <= 0) Main.gameOver = true;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Id getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public boolean isSolid() {
        return solid;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), width, height);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(getX()+10, getY(), width-20, 5);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(getX(), getY()+height-5, width-20, 5);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(getX(), getY()+10,5 , height-20);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(getX()+width-5, getY()+10, 5, height-20);
    }

    public abstract static class Builder<T extends Builder<T>> {
        private final int x, y;
        private final int width, height;
        private final Id id;
        private final Handler handler;

        private boolean solid = false;
        private int hp = 0;
        private boolean attackable = false;

        public Builder(int x, int y, int width, int height, Id id, Handler handler) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.id = id;
            this.handler = handler;
        }

        public T solid(boolean val) {
            solid = val;
            return self();
        }

        public T hp(int val) {
            hp = val;
            return self();
        }

        public T attackable(boolean val) {
            attackable = val;
            return self();
        }

        public abstract Entity build();

        protected abstract T self();
    }
}
