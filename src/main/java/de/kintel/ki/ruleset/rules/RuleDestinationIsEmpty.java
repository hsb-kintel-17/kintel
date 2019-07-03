/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RuleDestinationIsEmpty implements IRule {

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidMove(@NonNull final Move move, Board board) {
        return !board.getField(move.getTargetCoordinate()).peekHead().isPresent();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getOrder() {
        return 2;
    }
}
