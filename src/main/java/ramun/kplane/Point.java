package ramun.kplane;


/**
 * Created by francisco on 16/09/16.
 */
public class Point {

    private double[] coords;
    private int size;


    public Point(double[] coords) {
        this.coords = coords;
        this.size = this.coords.length;
    }

    public double[] getCoords() {
        return coords;
    }

    public double getCoordinate(int index) {
        return coords[index];
    }

    public int getSize() {
        return size;
    }

    public double[] getInputArray() {
        double[] array = new double[size - 1];
        for(int i = 0; i < size - 1; i++) {
            array[i] = coords[i];
        }
        return array;
    }

    public double getOutput() {
        return coords[size - 1];
    }

    public Double getDistance(Point point) {
        assert getSize() == point.getSize() : "Different sizes";
        double[] pointCoords = point.getCoords();
        float accumulator = 0;
        for(int i = 0; i < getSize(); i++) {
            accumulator += Math.pow(coords[i] - pointCoords[i], 2);
        }
        return Math.sqrt(accumulator);
    }

}
