package de.kintel.ki.gui;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.GuiEventType;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.gui.util.Arrow;
import de.kintel.ki.model.*;
import de.saxsys.mvvmfx.FxmlView;
import eu.lestard.grid.Cell;
import eu.lestard.grid.GridModel;
import eu.lestard.grid.GridView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.fusesource.jansi.Ansi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
public class MainView implements FxmlView<MainViewModel> {

    @FXML
    private AnchorPane root;
    @FXML
    private StackPane stackPane;
    @FXML
    private GridView<Field> gridView;
    @FXML
    private Group groupPossibleMoves;
    @FXML
    private Group groupBestMove;
    @FXML
    private Label winLabel;

    private GridModel<Field> gridModel;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private KI ki;
    @Value("${board.width}")
    private int width;
    @Value("${board.height}")
    private int height;
    private Player lastPlayer;
    @Autowired
    private Board board;

    public void initialize() {
        eventBus.register(this);
        gridModel = new GridModel<>();

        gridModel.setDefaultState( new Field(true) );
        gridModel.setNumberOfColumns(width);
        gridModel.setNumberOfRows(height);

        gridView.setGridModel(gridModel);
        gridView.setNodeFactory(cellField -> "".equals(cellField.getState().toString()) ? null : getRankAwareLabel(cellField));

        gridModel.getCells().forEach(c -> {
            final Tooltip t = new Tooltip("" + (char)(height-c.getRow()+'a' - 1) + (c.getColumn() + 1 ));
            Pane cellPane = gridView.getCellPane(c);
            cellPane.setOnMouseEntered(event -> {
                Point2D p = cellPane.localToScreen(cellPane.getLayoutBounds().getMaxX(), cellPane.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
                t.show(cellPane, p.getX(), p.getY());
            });
            cellPane.setOnMouseExited(e -> t.hide());
        });

        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane,0.0);
        AnchorPane.setLeftAnchor(stackPane,0.0);
        AnchorPane.setRightAnchor(stackPane,0.0);
    }

    private Node getRankAwareLabel(Cell<Field> cellField) {
        final String text = cellField.getState().toString();

        Color fxColor;
        Ansi.Color rankColor = cellField.getState().getRankColor();
        if(rankColor == Ansi.Color.DEFAULT) {
            fxColor = Color.BLACK;
        } else {
            fxColor = Color.valueOf(rankColor.name());
        }

        final Label headLabel = new Label("" + text.charAt(0));
        headLabel.setTextFill(fxColor);
        final Label tailLabel = new Label("" + text.substring(1));
        tailLabel.setTextFill(Color.BLACK);
        final HBox hBox = new HBox(headLabel, tailLabel);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    private Point2D locate(Coordinate2D coordinate) {
        final Pane cellPane = gridView.getCellPane(gridModel.getCell(coordinate.getY(), coordinate.getX()));
        final Bounds boundsInParent = cellPane.localToScene(cellPane.getLayoutBounds());
        final Point2D point2D = new Point2D(boundsInParent.getMaxX(), boundsInParent.getMaxY()).midpoint(boundsInParent.getMinX(), boundsInParent.getMinY());
        return point2D;
    }

    private void updateFields(GuiEventType id, List<Move> moves) {
        //set new fields first to trigger change event of gridView
        gridModel.getCells().forEach(c -> c.changeState( new Field(true) ));
        gridModel.getCells().forEach(c -> c.changeState(board.getField(c.getRow(), c.getColumn())));

        switch (id){
            case BEST_MOVE:
                groupBestMove.getChildren().clear();
                drawArrow(groupBestMove, Color.RED, moves.get(0).getSourceCoordinate(), moves.get(0).getTargetCoordinate());
                break;
            case POSSIBLE_MOVES:
                groupPossibleMoves.getChildren().clear();
                moves.forEach( move -> {
                    drawArrow(groupPossibleMoves, Color.BLACK, move.getSourceCoordinate(), move.getTargetCoordinate());

                });
                break;
        }

    }

    @Subscribe
    public void newPossibleMoves(PossibleMovesEvent e) {
        Platform.runLater(() -> {
            updateFields(e.id, e.getPossibleMoves());
            if( e.getPossibleMoves().isEmpty()) {
                winLabel.setId("winLabel");
                winLabel.setVisible(true);
                winLabel.setText("Winner is " + lastPlayer);
            }
        });

    }

    @Subscribe
    public void newBestMove(BestMoveEvent e) {
        final Move bestMove = e.getBestMove();
        Platform.runLater(() -> {
            if(bestMove != null){
                lastPlayer = bestMove.getCurrentPlayer();
                updateFields(e.id, Lists.newArrayList(bestMove));
            }
        });
    }

    private void drawArrow(Group group, Color color, Coordinate2D from, Coordinate2D to) {
        int width_offset = 25;
        final Arrow arrow = new Arrow();

        final Point2D from2d = locate(from);
        final Point2D to2d = locate(to);

        arrow.setStart(from2d.getX(), from2d.getY() + width_offset);
        arrow.setEnd(to2d.getX(), to2d.getY() + width_offset);
        arrow.draw(color);

        group.getChildren().add(arrow);
    }
}
