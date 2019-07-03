/*
 * hsb-kintel-17
 * Copyright (C) 2019 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;

/**
 * The guard is intended to be used while development to check board is the same before doing and undoing a move
 */
@Component
@Scope("singleton")
public class MoveMakerGuardDecorator implements MoveMaker {

    private final Logger logger = LoggerFactory.getLogger(MoveMakerImpl.class);
    /**
     * Debugging mechanism that stores board contents before the move for comparision after undo move. If the states differ then the undo was incorrect.
     */
    private final HashMap<Move, String> guard = new HashMap<>();

    @Qualifier("moveMakerBasic")
    private final MoveMaker moveMaker;

    @Autowired
    public MoveMakerGuardDecorator(@Qualifier("moveMakerBasic") MoveMaker moveMakerBasic) {
        this.moveMaker = moveMakerBasic;
        logger.info("Guard is enabled");
    }

    @Override
    public void makeMove(@Nonnull Move move, Board board) {
        guard.put(move, board.toString());
        moveMaker.makeMove(move, board);
    }

    @Override
    public void undoMove(@Nonnull Move move, Board board) {
        moveMaker.undoMove(move, board);
        final String expected = guard.get(move);
        final String actual = board.toString();
        if (!expected.equals(actual)) {
            throw new IllegalStateException(String.format("Incorrect undo of move %s! The field after the undo is not the same as before the do - but it should of course. move: %s", move.getUuid(), board.toString()));
        }
    }
}
