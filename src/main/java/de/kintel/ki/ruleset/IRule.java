package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IRule {
    boolean isValidMove(@Nonnull final Move move);
}
