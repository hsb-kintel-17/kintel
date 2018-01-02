package de.kintel.ki.gui;

import de.kintel.ki.KiApplication;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Field;
import eu.lestard.grid.GridModel;
import eu.lestard.grid.GridView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;

public class KiFxApplication extends Application implements GUIActions {

    private Scene scene;
    private AnchorPane root;
    private StackPane stackPane;
    private GridModel<String> gridModel;
    private GridView<String> gridView;
    private KI ki;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ki = KiApplication.getInstance().getKi();
        KiApplication.getInstance().setGUIActor(this);

        root = new AnchorPane();

        gridModel = new GridModel<>();

        gridModel.setDefaultState("");
        gridModel.setNumberOfColumns(ki.getBoard().getWidth());
        gridModel.setNumberOfRows(ki.getBoard().getHeight());

        gridView = new GridView<>();
        gridView.setGridModel(gridModel);

        gridView.setNodeFactory(cell -> "".equals(cell.getState()) ? null : new Label(cell.getState()));



        stackPane = new StackPane();


        stackPane.getChildren().add(gridView);

        root.getChildren().add(stackPane);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane,0.0);
        AnchorPane.setLeftAnchor(stackPane,0.0);
        AnchorPane.setRightAnchor(stackPane,0.0);

        scene = new Scene(root, 500, 500);


        final URL resource = this.getClass().getResource("/style.css");
        if(resource != null) {
            final String stylesheet = resource.toExternalForm();
            System.out.println(stylesheet);
            scene.getStylesheets().add(stylesheet);
        }


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void refresh() {
        while (gridModel == null) {}
        while (gridView == null) {}
        while (ki == null) {}
        int width_offset = 32;
        Platform.runLater(() -> root.getChildren().removeIf( c -> c instanceof Arrow));
        Platform.runLater(() -> {
            gridModel.getCells().forEach(c -> c.changeState(ki.getBoard().getField(c.getRow(), c.getColumn()).toString()));
            ki.getPossibleMoves().forEach( move -> {
                final Arrow arrow = new Arrow();

                final Point2D from2d = locate(move.getSourceField());//locate(move.getSourceField());
                final Point2D to2d = locate(move.getTargetField());

                arrow.setStart(from2d.getX() + width_offset, from2d.getY());
                arrow.setEnd(to2d.getX() + width_offset, to2d.getY());
                arrow.draw();
                root.getChildren().add(arrow);
            });
        });

    }

    private Point2D locate(Field field) {
        final Coordinate2D coordinateFrom = ki.getBoard().getCoordinate(field);

        return gridView.getCellPane(
                gridModel.getCell(coordinateFrom.getY(), coordinateFrom.getX()))
                                            .localToScene(scene.getX(), scene.getY());

    }
}
