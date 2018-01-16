package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

public interface RankMaker {
    void processRankChange(Move move, Board board);
}
