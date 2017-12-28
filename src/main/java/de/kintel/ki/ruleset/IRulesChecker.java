package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;

import java.util.List;

public interface IRulesChecker {
    boolean isValidMove(Move move);
    List<Move> stripValidMoves(List<Move> moves);
}
