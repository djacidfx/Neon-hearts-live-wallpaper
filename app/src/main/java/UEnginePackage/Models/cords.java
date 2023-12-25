package UEnginePackage.Models;


public class cords {
    public double x;
    public double y;
    public double z;

    public cords(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public static cords parse(String str) {
        String[] split = str.split(",");
        return new cords(Double.parseDouble(split[0]), Double.parseDouble(split[1]), split.length == 3 ? Double.parseDouble(split[2]) : 0.0d);
    }
}
