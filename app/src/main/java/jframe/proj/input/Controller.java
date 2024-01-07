package jframe.proj.input;

public class Controller {

    public double x, z, rotation, x_a, z_a, rotation_a, rotation_b;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean lookUp = false;
    public static boolean lookDown = false;

    public void tick(boolean forward, boolean back, boolean left, boolean right) {
        double rotationSpeed = 0.02;
        double walkSpeed = 1;
        double xMove = 0;
        double zMove = 0;

        if (forward) {
            zMove++;
        }

        if (back) {
            zMove--;
        }

        if (left) {
            xMove--;
        }

        if (right) {
            xMove++;
        }

        if (turnLeft) {
            rotation_a -= rotationSpeed;
        }

        if (turnRight) {
            rotation_a += rotationSpeed;
        }

        if (lookUp) {
            rotation_b -= rotationSpeed;
        }

        if (lookDown) {
            rotation_b += rotationSpeed;
        }

        x_a += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation) * walkSpeed);
        z_a += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation) * walkSpeed);

        x += x_a;
        z += z_a;
        x_a *= 0.1;
        z_a *= 0.1;
        rotation += rotation_a;
        rotation_a *= 0.8;

        rotation += rotation_b;
        rotation_b *= 0.8;

    }
}
