package ramun.kplane;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 16/09/16.
 */
public class Cluster {

    private Function centroid;
    private List<Point> points;
    private List<Point> newPoints;
    private double error = 1;

    public Cluster(List<Point> points) {
        assert points.size() == 0 : "empty cluster";

        this.points = points;
        this.newPoints = new ArrayList<>();

        // could assert here that every point has same dimensionality

        this.centroid = computeCentroid();
    }

    public double getError() {
        return error;
    }

    public void addPoint(Point point) {
        this.newPoints.add(point);
    }

    public Function computeCentroid() {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[][] x = new double[points.size()][];
        double[] y = new double[points.size()];

        for(int i = 0; i<points.size(); i++) {
            x[i] = points.get(i).getInputArray();
            y[i] = points.get(i).getOutput();
        }

        regression.newSampleData(y, x);
        double[] beta = regression.estimateRegressionParameters();
        return new Function(beta);
    }

    public double update() {
        Function oldCentroid = this.centroid;
        this.points = this.newPoints;
        this.centroid = computeCentroid();
        this.error = this.centroid.computeFitError(this.points);
        this.newPoints = new ArrayList<>();
        return this.centroid.getDistance(oldCentroid); // shift distance
    }

    public double getError(Point point) {
        return this.centroid.getError(point);
    }

    public boolean enoughPoints() {
        return this.centroid.getSize() < this.newPoints.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cluster { centroid = [");
        for (int i = 0; i < centroid.getSize(); i++) {
            sb.append(centroid.getCoordinate(i));
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    public List<Point> getPoints(){
        return points;
    }
}
