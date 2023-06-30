package jframe.proj.graphics;

import jframe.proj.Game;

public class Render3d extends Render {

    public Render3d(int width, int height) {
        super(width, height);
    }


    public void floor(Game game) {

        double floorPosition = 8;
        double ceilingPosition = 16;

        double forward = game.controls.z;
        double backward = game.time / -10.0;
        double right = game.controls.x;
        double left = game.time / -10.0;

        double rotation = game.controls.rotation;
        double coSine = Math.cos(rotation);
        double sine = Math.sin(rotation);

        for (int y = 0; y < height; y++) {

            double ceiling = (y - height / 2.0) / height;

            double z = floorPosition / ceiling;

            if (ceiling < 0) {
                z = ceilingPosition / -ceiling;
            }


            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;

                double xx = depth * coSine + z * sine; //+ left/right
                double yy = z * coSine - depth * sine; //+forward /backward

                int xPixels = (int) (xx + right);
                int yPixels = (int) (yy + forward);

                pixels[x + y * width] = ((xPixels & 15) * 16) | ((yPixels & 15) * 16) << 8;
            }
        }
    }

}
