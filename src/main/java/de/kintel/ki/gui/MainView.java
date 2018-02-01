package de.kintel.ki.gui;

import de.kintel.ki.event.GuiEventType;
import de.kintel.ki.gui.util.Arrow;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import eu.lestard.grid.Cell;
import eu.lestard.grid.GridView;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.fusesource.jansi.Ansi;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Scope("singleton")
public class MainView implements FxmlView<MainViewModel>, Initializable {

    @InjectViewModel
    MainViewModel viewModel;

    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    private BorderPane root;
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

    private Tooltip tooltip;
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltip = new Tooltip();

        progressIndicator.progressProperty().bind(viewModel.progressProperty());
        gridView.setGridModel(viewModel.getGridModel());
        gridView.setNodeFactory(cellField -> "".equals(cellField.getState().toString()) ? null : getRankAwareLabel(cellField));

        winLabel.setId("winLabel");
        winLabel.visibleProperty().bind(viewModel.winnerProperty().isNotNull());
        winLabel.textProperty().bind(
                Bindings.when(
                        viewModel.winnerProperty().isEqualTo("").or(viewModel.winnerProperty().isNull()))
                        .then("Tie")
                        .otherwise(Bindings.concat("Winner is ", viewModel.winnerProperty())));

        gridView.getGridModel().cells().stream().map(c -> gridView.getCellPane(c)).forEach( cellPane -> {
            cellPane.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // popup tooltip on the right, you can adjust these values for different positions
                    tooltip.show( cellPane.getScene().getWindow(), cellPane.getLayoutX(), cellPane.getLayoutY());
                } else {
                    tooltip.hide();
                }
            });
        });

        gridView.cellBorderColorProperty().set(Color.DARKGRAY);
        gridView.cellBorderWidthProperty().set(0.5);
        // Color for forbidden fields
        gridView.addColorMapping(new Field(true), Color.web("f4f3f3"));

        gridView.getGridModel().cells().forEach(c -> {

            Pane cellPane = gridView.getCellPane(c);
            cellPane.setOnMouseEntered(event -> {
                tooltip.setText("" + (char) ( viewModel.getGridModel().getNumberOfRows() - c.getRow() +'a' - 1) + (c.getColumn() + 1));
                Point2D p = cellPane.localToScreen(cellPane.getLayoutBounds().getMaxX(), cellPane.getLayoutBounds().getMaxY()); //I'm position the tooltip at bottom right of the node (see below for explanation)
                tooltip.show(cellPane, p.getX(), p.getY());
            });
            cellPane.setOnMouseExited(e -> tooltip.hide());
        });

        viewModel.possibleMovesProperty().addListener((InvalidationListener) observable -> updateArrows(GuiEventType.POSSIBLE_MOVES));
        viewModel.bestMoveProperty().addListener(observable -> updateArrows(GuiEventType.BEST_MOVE));
    }

    private void updateArrows(GuiEventType id) {
        Platform.runLater(() -> {
            switch (id) {
                case BEST_MOVE:
                    groupBestMove.getChildren().clear();
                    final Move bestMove = viewModel.bestMoveProperty().get();
                    drawArrow(groupBestMove, Color.RED, bestMove.getSourceCoordinate(), bestMove.getTargetCoordinate());
                    break;
                case POSSIBLE_MOVES:
                    groupPossibleMoves.getChildren().clear();
                    final List<Move> moves = new ArrayList<>(viewModel.possibleMovesProperty());
                    moves.forEach( move -> {
                        drawArrow(groupPossibleMoves, Color.BLACK, move.getSourceCoordinate(), move.getTargetCoordinate());
                    });
                    break;
            }
        });

    }

    private Node getRankAwareLabel(Cell<Field> cellField) {
        final String text = cellField.getState().toString();

        Color fxColor;
        Ansi.Color rankColor = cellField.getState().getRankColor();
        if(rankColor == Ansi.Color.DEFAULT) {
            fxColor = Color.BLACK;
        } else {
            fxColor = Color.valueOf(rankColor.name());
            // Yellow is really hard to read on screen so replace it with better color
            if( fxColor == Color.YELLOW ) {
                fxColor = Color.GOLD;
            }
        }

        final Label headLabel = new Label("" + text.charAt(0));
        headLabel.setTextFill(fxColor);
        final Label tailLabel = new Label("" + text.substring(1));
        tailLabel.setTextFill(Color.BLACK);
        final HBox hBox = new HBox(headLabel, tailLabel);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void drawArrow(Group group, Color color, Coordinate2D from, Coordinate2D to) {
        int width_offset = 0;
        final Arrow arrow = new Arrow();
        System.out.println(String.format("Draw arrow from %s to %s", from, to));
        System.out.println(String.format("group coord %s  %s", group.getLayoutX(), group.getLayoutY()));
        final Pane cellPane = gridView.getCellPane(gridView.getGridModel().getCell(from.getY(), from.getX() ));
        final Pane cellPane2 = gridView.getCellPane(gridView.getGridModel().getCell(to.getY() , to.getX()));
        cellPane.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        cellPane2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        Bounds n1InCommonAncestor = getRelativeBounds(cellPane, stackPane);
        Bounds n2InCommonAncestor = getRelativeBounds(cellPane2, stackPane);
        Point2D n1Center = getCenter(n1InCommonAncestor);
        Point2D n2Center = getCenter(n2InCommonAncestor);

        System.out.println(n1Center);
        System.out.println(n2Center);

        arrow.setStart(n1Center.getX(), n1Center.getY());
        arrow.setEnd(n2Center.getX(), n2Center.getY());
        arrow.draw(color);
        group.getChildren().add(arrow);
    }

    private Point2D locate(Coordinate2D coordinate) {
        final Cell<Field> cell = gridView.getGridModel()
                                         .getCell(coordinate.getY(), coordinate.getX());
        final Pane cellPane = gridView.getCellPane(gridView.getGridModel().getCell(coordinate.getY(), coordinate.getX()));

        //Bounds bounds = cellPane.localToScene(new Point2D(0.0, 0.0));
        //return new Point2D(bounds.getMaxX(), bounds.getMaxY()).midpoint(bounds.getMinX(), bounds.getMinY());
        return cellPane.localToScreen(0.0, 0.0);
    }

    private Bounds getRelativeBounds(Node node, Node relativeTo) {
        Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
        return relativeTo.sceneToLocal(nodeBoundsInScene);
    }

    private Point2D getCenter(Bounds b) {
        return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
    }

}
