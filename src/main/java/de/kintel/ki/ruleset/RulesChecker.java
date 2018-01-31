/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

@Component
@Scope("singleton")
public class RulesChecker implements IRulesChecker {

    private List<IRule> rules;

    @Autowired
    public RulesChecker(List<IRule> rules) {
        this.rules = rules;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidMove(@Nonnull final Move move, Board board) {
        for (IRule r : rules) {
            if (!r.isValidMove(move, board)) {
                return false;
            }
        }
        return true;
    }

}
