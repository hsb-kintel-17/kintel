package de.kintel.ki.model;

public enum Weight {
    LOOSE(-1),
    DRAW(0),
    WIN(1);

    private final int value;
    Weight(int value) {this.value = value;}
    public int getValue() {return value;}

    public static Weight fromValue(int id) {
        for (Weight aip: values()) {
            if (aip.getValue() == id) {
                return aip;
            }
        }
        return null;
    }
}