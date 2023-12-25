package UEnginePackage.Models;

import java.util.Random;


public class range {
    public double max;
    public double min;

    public range(double d, double d2) {
        this.max = d;
        this.min = d2;
    }

    public static range parse(String str) {
        return new range(Double.parseDouble(str.split(",")[1]), Double.parseDouble(str.split(",")[0]));
    }

    public double getRandomValue() {
        double nextDouble = new Random().nextDouble();
        double d = this.min;
        return ((this.max - d) * nextDouble) + d;
    }
}
