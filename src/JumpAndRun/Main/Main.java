package JumpAndRun.Main;

import JumpAndRun.Entity.Entity;
import JumpAndRun.Input.KeyInput;
import JumpAndRun.Main.Graphics.Sprite;
import JumpAndRun.Main.Graphics.SpriteSheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Canvas implements Runnable {
    public static final int WIDTH = 320;
    public static final int HEIGHT = 180;
    public static final int SCALE = 4;
    public static final String TITLE = "Jump And Run";

    private Thread thread;
    private boolean running = false;
    private BufferedImage image;

    public static int leben = 5;
    public static int deathscreenTime = 0;

    public static boolean deathscreen = true;
    public static boolean gameOver = false;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Camera cam;

    public static Sprite grass;
    public static Sprite player[] = new Sprite[10];
    public static Sprite mini[] = new Sprite[10];

    public Main() {
        Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private void init() {
        handler = new Handler();
        sheet = new SpriteSheet("/SpriteSheet.png");
        cam = new Camera();

        addKeyListener(new KeyInput());

        grass = new Sprite(sheet, 2, 1);

        for (int i = 0; i<mini.length;i++) {
            mini[i] = new Sprite(sheet, i+1,15);
        }

        for (int i = 0; i<player.length;i++) {
            player[i] = new Sprite(sheet, i+1,16);
        }

        try {
            image = ImageIO.read(getClass().getResource("/level.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void start() {
        if(running) return;
        running  = true;
        thread = new Thread(this, "Thread");
        thread.start();
    }

    private synchronized void stop() {
        if(!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        JFrame frame = new JFrame(TITLE);
        frame.add(main);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        main.start();
    }

    public void run() {
        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0/60.0;
        int frames = 0;
        int ticks = 0;
        while (running) {
            long now = System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime = now;
            while (delta>=1) {
                update();
                ticks++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis()-timer>1000) {
                timer+=1000;
                System.out.println(frames + " Frames Per Seconds " + ticks + " Updates per Second");
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, getWidth(), getHeight());
        if (deathscreen) {
            if (!gameOver) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier",Font.BOLD,50));
                g.drawImage(Main.player[0].getBufferedImage(),500,300,100,100,null);
                g.drawString("x" + leben,590,390);
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier",Font.BOLD,50));
                //g.drawImage(Main.player[0].getBufferedImage(),500,300,100,100,null);
                g.drawString("Game Over",530,390);
            }
        }
        g.translate(cam.getX(), cam.getY());
        if (!deathscreen) handler.render(g);
        g.dispose();
        bs.show();
    }

    public static int getFrameWidth() {
        return WIDTH*SCALE;
    }

    public static int getFrameHeight() {
        return HEIGHT*SCALE;
    }

    public void update() {
        handler.tick();

        for (Entity e:handler.entity) {
            if (e.getId()==Id.player) {
                cam.tick(e);
            }
        }

        if (deathscreen) deathscreenTime++;
        if (deathscreenTime>=180) {
            deathscreen = false;
            deathscreenTime = 0;
            handler.clearlevel();
            handler.createLevel(image);
        }
    }
}
