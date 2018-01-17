package de.kintel.ki.gui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.gui.util.Arrow;
import de.kintel.ki.model.*;
import de.saxsys.mvvmfx.FxmlView;
import eu.lestard.grid.GridModel;
import eu.lestard.grid.GridView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Scope("singleton")
public class MainView implements FxmlView<MainViewModel> {

    @FXML
    private AnchorPane root;
    @FXML
    private StackPane stackPane;
    @FXML
    private GridView<String> gridView;
    @FXML
    private Group groupPossibleMoves;
    @FXML
    private Group groupBestMove;
    @FXML
    private Label winLabel;

    private GridModel<String> gridModel;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private KI ki;
    @Value("${board.width}")
    private int width;
    @Value("${board.height}")
    private int height;
    private Player lastPlayer;
    private Board board;

    @PostConstruct
    public void init(){
        eventBus.register(this);
    }

    public void initialize() {
        gridModel = new GridModel<>();

        gridModel.setDefaultState("");
        gridModel.setNumberOfColumns(width);
        gridModel.setNumberOfRows(height);

        gridView.setGridModel(gridModel);
        gridView.setNodeFactory(cell -> "".equals(cell.getState()) ? null : new Label(cell.getState()));

        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane,0.0);
        AnchorPane.setLeftAnchor(stackPane,0.0);
        AnchorPane.setRightAnchor(stackPane,0.0);
    }

    private Point2D locate(Coordinate2D coordinate) {
        final Pane cellPane = gridView.getCellPane(gridModel.getCell(coordinate.getY(), coordinate.getX()));
        final Bounds boundsInParent = cellPane.localToScene(cellPane.getLayoutBounds());
        final Point2D point2D = new Point2D(boundsInParent.getMaxX(), boundsInParent.getMaxY()).midpoint(boundsInParent.getMinX(), boundsInParent.getMinY());
        return point2D;
    }

    @Subscribe
    public void newPossibleMoves(PossibleMovesEvent e) {
        board = e.getBoard();
        final List<Move> possibleMoves = e.getPossibleMoves();
        Platform.runLater(() -> {
            groupPossibleMoves.getChildren().clear();
            gridModel.getCells().forEach(c -> c.changeState(board.getField(c.getRow(), c.getColumn()).toString()));
            possibleMoves.forEach( move -> {
                drawArrow(groupPossibleMoves, Color.BLACK, move.getSourceCoordinate(), move.getTargetCoordinate());
            });
        });
    }

    @Subscribe
    public void newBestMove(BestMoveEvent e) {
        final Move bestMove = e.getBestMove();
        Platform.runLater(() -> {
            if( bestMove == null ) {
                winLabel.setId("winLabel");
                winLabel.setVisible(true);
                winLabel.setText("Winner is " + lastPlayer);
            } else {
                lastPlayer = bestMove.getCurrentPlayer();
                groupBestMove.getChildren().clear();
                drawArrow(groupBestMove, Color.RED, bestMove.getSourceCoordinate(), bestMove.getTargetCoordinate());
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