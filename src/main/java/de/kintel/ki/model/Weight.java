package de.kintel.ki.model;

public enum Weight {
    LOOSE(-1),
    DRAW(0),
    WIN(1);

    private final int id;
    Weight(int id) {this.id = id;}
    private int getValue() {return id;}

    public static Weight fromValue(int id) {
        for (Weight aip: values()) {
            if (aip.getValue() == id) {
                return aip;
            }
        }
        return null;
    }
}