package com.hitvol.vectboard;

/**
 * Created by archevel on 2015-12-20.
 */
public class PerpendicularDistanceCalculator {
    private final Filament.Point point1;
    private final Filament.Point point2;

    public PerpendicularDistanceCalculator(Filament.Point point1, Filament.Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }


    public double distanceTo(Filament.Point p) {
        double denominator = ((point2.y - point1.y) * p.x) - ((point2.x - point1.x) * p.y) + (point2.x*point1.y) - (point2.y*point1.x);
        double numerator = ((point2.y - point1.y) * (point2.y - point1.y)) +((point2.x - point1.x) * (point2.x - point1.x));
        double result = Math.abs(denominator) / Math.sqrt(numerator);
        return result;
    }
}
