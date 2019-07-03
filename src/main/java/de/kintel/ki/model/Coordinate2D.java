/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import com.google.common.base.Objects;
import org.eclipse.jdt.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

public class Coordinate2D implements Serializable {

    private static final long serialVersionUID = -5775552204030261616L;

    /**
     * Comparator to compare by distance.
     */
    class DistanceToOrder implements Comparator<Coordinate2D> {
        @Override
        public int compare(@NonNull final Coordinate2D e1, @NonNull final Coordinate2D e2) {
            int dist1 = distanceSquaredTo(e1);
            int dist2 = distanceSquaredTo(e2);
            return Integer.compare(dist1, dist2);
        }
    }

    /**
     * Compares two points by distance to this point.
     *
     * @return the comparator
     */
    public Comparator<Coordinate2D> distanceToOrder() {
        return new DistanceToOrder();
    }

    private int x;
    private int y;

    public Coordinate2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    public int distanceSquaredTo(@NonNull final Coordinate2D that) {
        int dx = this.x - that.x;
        int dy = this.y - that.y;
        return dx*dx + dy*dy;
    }

    /**
     * Returns the coordinate between a and b.
     * @param a coordinate a
     * @param b coordinate b
     * @return the coordinate between a and b.
     */
    public static Coordinate2D between(Coordinate2D a, Coordinate2D b) {
        int x = (a.getX() + b.getX()) / 2;
        int y = (a.getY() + b.getY()) / 2;
        return new Coordinate2D(x,y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coordinate2D{");
        sb.append("x=")
          .append(x);
        sb.append(", y=")
          .append(y);
        sb.append('}');
        return sb.toString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate2D that = (Coordinate2D) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }
}
