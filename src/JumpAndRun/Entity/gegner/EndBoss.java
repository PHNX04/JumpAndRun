package JumpAndRun.Entity.gegner;

import JumpAndRun.Entity.Entity;
import JumpAndRun.Main.Handler;
import JumpAndRun.Main.Id;
import JumpAndRun.States.BossState;
import JumpAndRun.tile.Tile;

import java.awt.*;
import java.util.Random;

public class EndBoss extends Entity {

    public int jumpTime = 0;
    public boolean addJumpTime = false;

    private Random random;

    public EndBoss(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
        this.hp = hp;

        bossstate = BossState.IDLE;

        random = new Random();
    }

    @Override
    public void render(Graphics g) {
        if (bossstate==BossState.IDLE||bossstate==BossState.SPINNING) g.setColor(Color.GRAY);
        else if (bossstate==BossState.RECOVERING) g.setColor(Color.RED);
        else g.setColor(Color.ORANGE);

        g.fillRect(x,y,width,height);
    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        if (hp<=0) die();

        phaseTime++;

        if ((phaseTime>=180&&bossstate==BossState.IDLE)||(phaseTime>=600&&bossstate!=BossState.SPINNING)) chooseState();

        if (bossstate==BossState.IDLE||bossstate==BossState.RECOVERING) {
            setVelX(0);
            setVelY(0);
        }

        if (bossstate==BossState.JUMPING||bossstate==BossState.RUNNING) attackable = true;
        else attackable = false;

        if (bossstate!=BossState.JUMPING) {
            addJumpTime = false;
            jumpTime = 0;
        }

        if (addJumpTime=true) {
            jumpTime++;
            if (jumpTime>=30) {
                addJumpTime = false;
                jumpTime = 0;
            }

            if (!jumping&&!falling) {
                jumping = true;
                gravity = 8.0;
            }
        }

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
                    if (falling) {
                        falling = false;
                        addJumpTime = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(0);
                    if (bossstate==BossState.RUNNING)setVelX(4);
                    x = t.getX()+t.width;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(0);
                    if (bossstate==BossState.RUNNING)setVelX(-4);
                    x = t.getX()-t.width;
                }
            }
        }

        for (int i=0;i<handler.entity.size();i++) {
            Entity e = handler.entity.get(i);
            if (e.getId()==Id.player) {
                if (bossstate==BossState.JUMPING) {
                    if (jumping||falling) {
                        if (e.getX()<getX()) setVelX(-3);
                        else if (e.getX()>getX()) setVelX(3);
                    } else setVelX(0);
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
    }
    public void chooseState() {
        int nextPhase = random.nextInt(2);
        if (nextPhase==0) {
            bossstate = BossState.RUNNING;
            int dir = random.nextInt(2);
            if (dir == 0) setVelX(-4);
            else setVelX(4);
        } else if (nextPhase==1) {
            bossstate=BossState.JUMPING;

            jumping = true;
            gravity = 8.0;
        }

        phaseTime = 0;
    }
}
