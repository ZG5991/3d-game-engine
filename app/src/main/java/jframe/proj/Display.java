package jframe.proj;

import jframe.proj.graphics.Screen;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Display extends Canvas implements Runnable{

    private static final long serialVersionUID = -8506770875897726055L;

    public static int WIDTH = 800;
    public static int HEIGHT = 800;
    private Thread thread;
    private boolean running = false;
    private BufferedImage image;
    private Screen screen;
    private int[] pixels;

    private Game game;

    public Display() {
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    @Override
    public void run() {
        int frames = 0;
        double unProcessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unProcessedSeconds += passedTime / 1000000000.0;

            while (unProcessedSeconds > secondsPerTick) {
                tick();
                unProcessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount ++;
                if (tickCount % 60 == 0) {
                    System.out.println(frames + "FPS");
                    previousTime += 1000;
                    frames = 0;
                }
                if (ticked == true) {
                    render();
                    frames ++;
                }
                render();
                frames ++;
            }
        }
    }

    private void tick() {
        game.tick();
    }
    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        graphics.dispose();
        bufferStrategy.show();
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        thread = new Thread(this);
        thread.start();

        System.out.println("Working...");
    }

    private void stop() {
        if (!running) {
            return;
        }

        running = false;
        System.out.println("Attempting to shut down...");
        try {
            thread.join();

            System.out.println("Shut down successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void setWIDTH(int WIDTH) {
        Display.WIDTH = WIDTH;
    }

    public static void setHEIGHT(int HEIGHT) {
        Display.HEIGHT = HEIGHT;
    }
}
