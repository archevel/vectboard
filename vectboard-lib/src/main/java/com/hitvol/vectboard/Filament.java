package com.hitvol.vectboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by archevel on 2015-11-14.
 */
public class Filament {

    private final ArrayList<Point> points;

    public Filament() {
        this.points = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }

    public void add(int x, int y) {
        points.add(new Point(x, y));
    }

    public void addAll(List<Point> points) {
        this.points.addAll(points);
    }

    public static final class Point {
        public final int x;
        public final int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
