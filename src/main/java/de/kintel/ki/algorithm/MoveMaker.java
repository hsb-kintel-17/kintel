package de.kintel.ki.algorithm;

import de.kintel.ki.model.Move;

public interface MoveMaker {
    void makeMove(Move move);
    void undoMove(Move move);
}
