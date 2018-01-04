package de.kintel.ki.gui.util;


/**
 * Utility class containing helper methods relating to geometry, positions, etc.
 */
public class GeometryUtils {

    private static final double HALF_A_PIXEL = 0.5;

    /**
     * Moves an x or y position value on-pixel.
     *
     * <p>
     * Lines drawn off-pixel look blurry. They should therefore have integer x and y values.
     * </p>
     *
     * @param position the position to move on-pixel
     *
     * @return the position rounded to the nearest integer
     */
    public static double moveOnPixel(final double position) {
        return Math.ceil(position);
    }

    /**
     * Moves an x or y position value off-pixel.
     *
     * <p>
     * This is for example useful for a 1-pixel-wide stroke with a stroke-type of centered. The x and y positions need
     * to be off-pixel so that the stroke is on-pixel.
     * </p>
     *
     * @param position the position to move off-pixel
     *
     * @return the position moved to the nearest value halfway between two integers
     */
    public static double moveOffPixel(final double position) {
        return Math.ceil(position) - HALF_A_PIXEL;
    }


}
