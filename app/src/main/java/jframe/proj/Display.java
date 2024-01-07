package jframe.proj;

import jframe.proj.graphics.Screen;
import jframe.proj.input.Controller;
import jframe.proj.input.InputHandler;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
    private InputHandler input;
    private int FPS;

    //variables used by the mouse look camera to judge position
    private int mouseNewX;
    private int mouseOldX;
    private int mouseNewY;
    private int mouseOldY;

    public Display() {
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    @Override
    public void run() {

        int frames = 0;
        double unProcessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        this.requestFocus();

        while (running) {

            //logic to handle frames and fps counting
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;

            previousTime = currentTime;
            unProcessedSeconds += passedTime / 1000000000.0;

            while (unProcessedSeconds > secondsPerTick) {

                tick();

                unProcessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;

                if (tickCount % 60 == 0) {
                    FPS = frames;
                    previousTime += 1000;
                    frames = 0;
                }

                if (ticked) {
                    render();
                    frames++;
                }

                mouseNewX = input.getMouseX();
                    if (mouseNewX > mouseOldX) {
                        Controller.turnRight = true;
                    }
                    if (mouseNewX < mouseOldX) {
                        Controller.turnLeft = true;
                    }
                    if (mouseNewX == mouseOldX) {
                        Controller.turnLeft = false;
                        Controller.turnRight = false;
                    }
                    mouseOldX = mouseNewX;

//                mouseNewY = input.getMouseY();
//                    if (mouseNewY < mouseOldY) {
//                        Controller.lookUp = true;
//                        System.out.println("UP");
//                    }
//                    if (mouseNewY > mouseOldY) {
//                        Controller.lookDown = true;
//                        System.out.println("DOWN");
//                    }
//                    if (mouseNewY == mouseOldY) {
//                        Controller.lookUp = false;
//                        Controller.lookDown = false;
//                    }
//                    mouseOldY = mouseNewY;

                render();
                frames++;

            }
        }
    }

    private void tick() {
        game.tick(input.key);
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

        //draws the FPS counter on the screen
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        graphics.setFont(new Font("Verdana", Font.BOLD, 25));
        graphics.setColor(Color.yellow);
        graphics.drawString(FPS + " FPS", 10, 50);

        //draws the crosshair
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;
        graphics.setColor(Color.WHITE);
        graphics.fillRect(centerX, centerY, 5, 5);

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



}
