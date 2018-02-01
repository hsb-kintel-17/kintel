/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.gui;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.GuiEventType;
import de.kintel.ki.event.MeasuredTimeEvent;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.grid.GridModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class MainViewModel implements ViewModel {

    @Value("${board.width}")
    private int width;
    @Value("${board.height}")
    private int height;
    @Autowired
    private EventBus eventBus;

    private Player lastPlayer;
    private Board board;
    private GridModel<Field> gridModel;

    private DoubleProperty progress = new SimpleDoubleProperty(this, "progress", -1);
    private ObjectProperty<Player> winner = new SimpleObjectProperty<>(this, "winner");
    private ObservableList<Move> possibleMoves = FXCollections.observableArrayList();
    private ObjectProperty<Move> bestMove = new SimpleObjectProperty<>(this, "bestMove");

    public void initialize() {
        eventBus.register(this);
        gridModel = new GridModel<>();

        gridModel.setDefaultState( new Field(true) );
        gridModel.setNumberOfColumns(width);
        gridModel.setNumberOfRows(height);
    }

    private void updateFields(GuiEventType id, List<Move> moves) {
        //set new fields first to trigger change event of gridView
        Platform.runLater(() -> {
            gridModel.getCells().forEach(c -> c.changeState( new Field(true) ));
            gridModel.getCells().forEach(c -> c.changeState( board.getField(c.getRow(), c.getColumn())) );
        });
    }

    @Subscribe
    public void newPossibleMoves(PossibleMovesEvent e) {
        board = e.getBoard();
        possibleMoves.setAll(e.getPossibleMoves());
        updateFields(e.id, e.getPossibleMoves());
    }

    @Subscribe
    public void newBestMove(BestMoveEvent e) {
        // The board should be set by newPossibleMoves event but eventbus is asynchronous so
        // maybe newBestMove is processed before newPossibleMoves
        if( null == board ) {
            return;
        }
        final Move bestMove = e.getBestMove();
        Platform.runLater(() -> {
            if(bestMove != null){
                lastPlayer = bestMove.getCurrentPlayer();
                updateFields(e.id, Lists.newArrayList(bestMove));
            }
        });
    }

    @Subscribe
    public void newMeasuredTime(MeasuredTimeEvent e) {
        Platform.runLater(() -> progress.set(((100 / 240d) * e.getTimeConsumedInSeconds()) / 100d ));

    }

    public GridModel<Field> getGridModel() {
        return gridModel;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public ObjectProperty<Player> winnerProperty() {
        return winner;
    }

    public ObservableList<Move> possibleMovesProperty() {
        return possibleMoves;
    }

    public ObjectProperty<Move> bestMoveProperty() {
        return bestMove;
    }
}
