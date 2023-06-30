package jframe.proj.graphics;

import jframe.proj.Game;

import java.util.Random;

public class Screen extends Render{

    private Render test;
    private Render3d render3d;


    public Screen(int width, int height) {
        super(width, height);
        Random random = new Random();
        render3d = new Render3d(width, height);

        test = new Render(256, 256);
        for (int i = 0; i < 256 * 256; i++) {
           test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
        }
    }

    public void render(Game game) {
        for (int i = 0; i < width * height; i++) {
            pixels[i] = 0;
        }

        render3d.floor(game);
        draw(render3d, 0, 0);
    }

}
