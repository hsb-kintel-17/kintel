package de.kintel.ki.model;

import java.util.Comparator;

public class Coordinate2D {

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    public int distanceSquaredTo(Coordinate2D that) {
        int dx = this.x - that.x;
        int dy = this.y - that.y;
        return dx*dx + dy*dy;
    }


    public class DistanceToOrder implements Comparator<Coordinate2D> {
        @Override
        public int compare(Coordinate2D e1, Coordinate2D e2) {
            int dist1 = distanceSquaredTo(e1);
            int dist2 = distanceSquaredTo(e2);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
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

    private final int x;
    private final int y;

    public Coordinate2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
}
