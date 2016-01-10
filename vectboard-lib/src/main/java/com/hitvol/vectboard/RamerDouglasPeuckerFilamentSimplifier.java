package com.hitvol.vectboard;

import com.hitvol.vectboard.Filament.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by archevel on 2015-12-20.
 *
 *
 */
public class RamerDouglasPeuckerFilamentSimplifier {
    public static Filament simplifyFilament(Filament f, double tolerance) {
        List<Point> points = f.getPoints();
        List<Point> resultPoints = new ArrayList<Point>();
        resultPoints.add(points.get(0));

        Stack<Integer> pointsIndexStack= new Stack<Integer>();
        pointsIndexStack.push(0);
        pointsIndexStack.push(points.size() - 1);

        while(!pointsIndexStack.empty()) {
            double dMax = 0;
            int index = 0;
            int lastIndex = pointsIndexStack.pop();
            int firstIndex = pointsIndexStack.pop();
            Point pFirst = points.get(firstIndex);
            Point pLast = points.get(lastIndex);
            PerpendicularDistanceCalculator calc = new PerpendicularDistanceCalculator(pFirst, pLast);
            for (int i = firstIndex + 1; i < lastIndex; i++) {
                double dist = calc.distanceTo(points.get(i));
                if (dist >= dMax) {
                    dMax = dist;
                    index = i;
                }
            }

            if (dMax <= tolerance || firstIndex + 1 == lastIndex) {
                resultPoints.add(pLast);
            } else {
                pointsIndexStack.push(index);
                pointsIndexStack.push(lastIndex);
                pointsIndexStack.push(firstIndex);
                pointsIndexStack.push(index);
            }
        }

        Filament fout = new Filament();
        fout.addAll(resultPoints);

        return fout;
    }
}
