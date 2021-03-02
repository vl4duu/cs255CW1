import javafx.scene.paint.Color;

public class Vector {
    double red;
    double green;
    double blue;
    double opacity;

    Vector(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    Vector(double r, double g, double b, double opacity) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.opacity = opacity;
    }

    Vector(double r, double g, double b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }


    void addColors(Vector v) {
        this.red += v.getRed();
        this.green += v.getGreen();
        this.blue += v.getBlue();
    }

    Vector multiply(double d) {

        return new Vector(this.red * d, this.green * d, this.blue * d, this.opacity * d);
    }

    public Vector multiplyColor(double d) {
        return new Vector(this.red * d, this.green * d, this.blue * d);


    }


    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getOpacity() {
        return opacity;
    }

    public Color getColor() {
        if (this.red > 1.0) {
            this.red = 1;
        }
        if (this.green > 1.0) {
            this.green = 1;
        }
        if (this.blue > 1.0) {
            this.blue = 1;
        }
        return new Color(this.red, this.green, this.blue, 1);
    }
}
