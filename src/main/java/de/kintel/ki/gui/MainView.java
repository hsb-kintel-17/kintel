package de.kintel.ki.gui;

import de.kintel.ki.algorithm.KI;
import de.saxsys.mvvmfx.JavaView;
import eu.lestard.grid.GridModel;
import eu.lestard.grid.GridView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class MainView implements JavaView<MainViewModel> {

    private Scene scene;
    private AnchorPane root;
    private StackPane stackPane;
    private GridModel<String> gridModel;
    private GridView<String> gridView;

    @Autowired
    KI ki;

    public MainView() {
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

    }
}
