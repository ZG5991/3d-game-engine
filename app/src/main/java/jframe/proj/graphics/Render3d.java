package jframe.proj.graphics;

import jframe.proj.Game;

public class Render3d extends Render {

    public double[] zBuffer;
    public double renderDist = 6500;

    public Render3d(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }


    public void floor(Game game) {

        double floorPosition = 8;
        double ceilingPosition = 16;


        double zMovement = game.controls.z;
        double xMovement = game.controls.x;

        double rotation = game.controls.rotation;
        double coSine = Math.cos(rotation);
        double sine = Math.sin(rotation);

        double rotation_v = game.controls.rotation_b;
        double coSine_v = Math.cos(rotation_v);
        double sine_v = Math.sin(rotation_v);

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
                double zz = z * coSine_v - depth * sine_v;

                int xPixels = (int) (xx + xMovement);
                int yPixels = (int) (yy + zMovement);

                zBuffer[x + y * width] = z;

                pixels[x + y * width] = ((xPixels & 15) * 16) | ((yPixels & 15) * 16) << 8;

            }
        }
    }

    public void renderDistLimiter() {

        for (int i = 0; i < width * height; i++) {
            int color = pixels[i];
            int brightness = (int) (renderDist / (zBuffer[i]));

            if (brightness < 0) {
                brightness = 0;
            }

            if (brightness > 255) {
                brightness = 255;
            }

            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = (color) & 0xff;

            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            pixels[i] = r << 16 | g << 8 | b;

        }
    }

}
