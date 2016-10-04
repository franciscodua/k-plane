package ramun.kplane;

import java.util.*;

/**
 * Created by francisco on 16/09/16.
 */
public class Kplane {

    public static double computeError(List<Cluster> clusters) {
        double error = 0;
        for (Cluster cluster : clusters) {
            error += cluster.getError();
        }
        return error / clusters.size();
    }

    public static List<Cluster> kplane(List<Point> points, int k, double cutoff) {
        int dimensionality = points.get(0).getSize();
        List<Cluster> clusters = new ArrayList<>();
        List<Point> initCluster;

        for(int i = 0; i < k; i++) {
            initCluster = new ArrayList<>();
            Collections.shuffle(points);
            for (int j = 0; j < dimensionality+1; j++) {
                initCluster.add(points.get(j));
            }

            clusters.add(new Cluster(initCluster));
        }

        int loopCounter = 0;
        double smallestDistance;
        double distance;
        int clusterIndex;
        while (loopCounter < 100) {
            loopCounter ++;

            for(Point point : points) {
                smallestDistance = clusters.get(0).getError(point);
                clusterIndex = 0;

                for(int m = 1; m < clusters.size(); m++) {
                    distance = clusters.get(m).getError(point);
                    if(distance < smallestDistance) {
                        smallestDistance = distance;
                        clusterIndex = m;
                    }
                }

                clusters.get(clusterIndex).addPoint(point);
            }

            for(Cluster cluster : clusters) {
                if (!cluster.enoughPoints()) {
                    Collections.shuffle(points);
                    for (int j = 0; j < dimensionality+1; j++) {
                        cluster.addPoint(points.get(j));
                    }
                }
            }
            double shift;
            double maxShift = 0;
            for (Cluster cluster : clusters) {
                shift = cluster.update();
                maxShift = Math.max(shift, maxShift);
            }

            if (maxShift < cutoff) {
                break;
            }
        }
        return clusters;
    }

    public static List<Cluster> fit(List<Point> points, int k, double cutoff) {
        List<Cluster> clusters = kplane(points, k, cutoff);
        double error = computeError(clusters);
        List<Cluster> newClusters;
        double newError;

        for(int i = 0; i < 100; i++) {
            newClusters = kplane(points, k, cutoff);
            newError = computeError(newClusters);

            if(newError < error) {
                error = newError;
                clusters = newClusters;
            }
        }
        return clusters;
    }

    public static int chooseK(List<Point> points, double cutoff) {
        Collections.shuffle(points);
        int testSize = Math.toIntExact(Math.round(points.size() * 0.1));
        int begin, end;
        List<Point> train, test;
        int k = 1;
        List<Cluster> clusters;

        for(int iteration = 0; iteration < 10; iteration++) {

            train = new ArrayList<>();
            test = new ArrayList<>();
            for(int i = 0; i < points.size(); i++) {
                begin = iteration * testSize;
                end = begin + testSize - 1;
                if(begin <= i && i <= end) {
                    test.add(points.get(i));
                } else {
                    train.add(points.get(i));
                }
            }
            clusters = fit(train, k, cutoff);
        }
        return k;
    }

}
