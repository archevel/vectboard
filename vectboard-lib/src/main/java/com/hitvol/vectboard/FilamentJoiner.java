package com.hitvol.vectboard;

import java.util.List;

import com.google.common.collect.Lists;
import com.hitvol.vectboard.Filament.Point;

/**
 * Created by archevel on 2015-12-23.
 */
public class FilamentJoiner {
    public List<Filament> joinAdjacent(final List<Filament> originalFilaments, double distanceThreshold) {
        List<Filament> filaments = Lists.newLinkedList(originalFilaments);
        for (int i = 0; i < filaments.size(); i++) {
            Filament f1 = filaments.get(i);
            Point start1 = f1.getPoints().get(0);
            Point end1 = f1.getPoints().get(f1.getPoints().size() - 1);
            for (int j = i + 1; j < filaments.size(); j++) {
                Filament f2 = filaments.get(j);
                Point start2 = f2.getPoints().get(0);
                Point end2 = f2.getPoints().get(f2.getPoints().size() - 1);
                Filament joined = getJointFilament(distanceThreshold, f1, start1, end1, f2, start2, end2);
                if (joined != null) {
                    filaments.add(i, joined);
                    filaments.remove(f1);
                    filaments.remove(f2);
                    i = i - 1;
                    break;
                }
            }
        }
        return filaments;
    }

    private Filament getJointFilament(double distanceThreshold, Filament f1, Point start1, Point end1, Filament f2, Point start2, Point end2) {
        Filament joined = null;
        if(distanceBetween(end1, start2) <= distanceThreshold) {
            joined = new Filament();
            joined.addAll(f1.getPoints());
            joined.addAll(f2.getPoints());
        } else if (distanceBetween(start1, start2) <= distanceThreshold) {
            joined = new Filament();

            joined.addAll(Lists.reverse(f2.getPoints()));
            joined.addAll(f1.getPoints());
        } else if (distanceBetween(end1, end2) <= distanceThreshold) {
            joined = new Filament();

            joined.addAll(f1.getPoints());
            joined.addAll(Lists.reverse(f2.getPoints()));
        } else if(distanceBetween(start1, end2) <= distanceThreshold) {
            joined = new Filament();
            joined.addAll(f2.getPoints());
            joined.addAll(f1.getPoints());
        }
        return joined;
    }

    private double distanceBetween(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
