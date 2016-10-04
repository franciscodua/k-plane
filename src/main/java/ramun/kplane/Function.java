package ramun.kplane;

import java.util.List;

/**
 * Created by francisco on 16/09/16.
 */
public class Function extends Point {

    public Function(double[] coords) {
        super(coords);
    }

    public double predict(Point point) {
        assert getSize() == point.getSize() : "Function and point should have same dimensionality";

        double accumulator = getCoordinate(0); // intersection

        for (int i = 1; i < getSize(); i++) {
            accumulator += getCoordinate(i) * point.getCoordinate(i-1);
        }
        return accumulator;
    }

    public double getError(Point point) {
        assert getSize() == point.getSize() : "Function and point should have same dimensionality";
        int output_index = getSize() - 1;
        double prediction = predict(point);

        return Math.abs(prediction - point.getCoordinate(output_index));
    }

    public double computeFitError(List<Point> points) {
        double error = 0;
        double prediction;
        for (Point point : points) {
            prediction = predict(point);
            error += getError(point) / Math.abs(prediction);
        }
        return error / points.size();
    }
}
