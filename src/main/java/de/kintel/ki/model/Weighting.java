package de.kintel.ki.model;

public enum Weighting {
    LOOSE(-1),
    DRAW(0),
    WIN(1);

    private final int id;
    Weighting(int id) {this.id = id;}
    public int getValue() {return id;}

    public static Weighting fromValue(int id) {
        for (Weighting aip: values()) {
            if (aip.getValue() == id) {
                return aip;
            }
        }
        return null;
    }
}