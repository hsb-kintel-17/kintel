package de.kintel.ki.model;

import javax.annotation.Nonnull;
import java.util.Comparator;

public class Coordinate2D {

    class DistanceToOrder implements Comparator<Coordinate2D> {
        @Override
        public int compare(@Nonnull final Coordinate2D e1, @Nonnull final Coordinate2D e2) {
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

    private final int x;
    private final int y;

    public Coordinate2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    public int distanceSquaredTo(@Nonnull final Coordinate2D that) {
        int dx = this.x - that.x;
        int dy = this.y - that.y;
        return dx*dx + dy*dy;
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
