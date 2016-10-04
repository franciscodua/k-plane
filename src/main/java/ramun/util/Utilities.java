package ramun.util;

import ramun.kplane.Cluster;
import ramun.kplane.Kplane;
import ramun.kplane.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by francisco on 21/09/16.
 */
public class Utilities {
    public static Point getPointFromLine(String line) {
        String[] values = line.split(",");
        double[] coords = new double[values.length];
        for(int i = 0; i < coords.length; i++) {
            coords[i] = Double.parseDouble(values[i]);
        }

        return new Point(coords);
    }

    public static List<Point> readPointsFromFile(String input_file) {
        final List<Point> points= new ArrayList<>();

        try(Stream<String> lines = Files.lines(Paths.get(input_file))) {
            lines.forEach(line -> {
                points.add(getPointFromLine(line));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void main(String[] args) {
        //String input_file = args[0];
        String input_file = "/Users/francisco/Documents/IST/Inforum/data/graph_data.txt";

        final List<Point> points= new ArrayList<>();

        try(Stream<String> lines = Files.lines(Paths.get(input_file))) {
            lines.forEach(line -> {
                points.add(getPointFromLine(line));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Cluster> clusters = Kplane.fit(points, 0.05);
        System.out.println(clusters);
        // clusters.get(1).getPoints().forEach(point->System.out.println(point.getCoords()));
        System.out.println(clusters.get(0).getPoints().size());
        System.out.println(clusters.get(1).getPoints().size());
    }
}
