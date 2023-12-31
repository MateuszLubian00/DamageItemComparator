package io.github.mateuszlubian00.itemcompare;

import io.github.mateuszlubian00.itemcompare.model.*;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;
import io.github.mateuszlubian00.itemcompare.util.DBUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ComparatorApplication extends Application {

    /** Stage used in formula help window. */
    public static Stage helpStage = null;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("main-frame.fxml"));
        Scene mainScene = new Scene(mainFxmlLoader.load());
        changeSubScene("player-stats.fxml", mainScene);

        stage.setTitle("Damage Item Comparator");
        // Scene is 625 by 425, but window size is larger?
        stage.setMinWidth(641);
        stage.setMinHeight(464);
        stage.setScene(mainScene);
        stage.show();
    }

    /** Changes the SubScene in the main scene.
     *   Assumes the scene looks like this:
     *Scene
     *  ⊢ AnchorPane
     *      ⊢ Something
     *      ⊢AnchorPane
     *          ⊢Group View (this is the element to change)
    */
    public static void changeSubScene(String name, Scene main) throws IOException {

        // Load main scene, select its AnchorPane and the SubScene withing it.
        AnchorPane mainPane = (AnchorPane) main.getRoot();
        AnchorPane sub = (AnchorPane) mainPane.getChildren().get(1);

        // Create a new view that consists of some SubScene,
        // Replace the existing SubScene with the new one.
        Group g = new Group();
        SubScene someSubscene = new SubScene(new FXMLLoader(ComparatorApplication.class.getResource(name)).load(), sub.getWidth(), sub.getHeight());

        // Make SubScene resizable
        someSubscene.widthProperty().bind(sub.widthProperty());
        someSubscene.heightProperty().bind(sub.heightProperty());

        g.getChildren().add(someSubscene);
        sub.getChildren().set(0, g);
    }

    /** Helper method to create a database with some initial data values,
     *  as well as initialize some CalculatorUtil variables.
     *  Normally not needed, but this app uses in-memory database.
     */
    private static void initializeData() {
        DBUtil.dbExecuteUpdate(
                "CREATE TABLE actors (" +
                "ID int PRIMARY KEY , " +
                "HP bigint, " +
                "DEFENSE bigint, " +
                "ATTACK bigint, " +
                "ATTACK_SPEED double, " +
                "CRITICAL_HIT_CHANCE double)");
        // Player
        ActorAccess.insertActor(0, 0L, 0L, 100L, 0.5D, 10D);
        // Enemy
        // TODO: more enemies
        ActorAccess.insertActor(1, 100L, 50L, 0L, 0D, 0D);

        DBUtil.dbExecuteUpdate(
                "CREATE TABLE items (" +
                        "ID int PRIMARY KEY , " +
                        "BONUS_HP bigint, " +
                        "BONUS_DEFENSE bigint, " +
                        "BONUS_ATTACK bigint, " +
                        "BONUS_ATTACK_SPEED double, " +
                        "BONUS_CRITICAL_HIT_CHANCE double)");
        // Item #1
        ItemAccess.insertItem(0, 0L, 0L, 50L, 0.1D, 0D);
        // Item #2
        ItemAccess.insertItem(1, 0L, 0L, 10L, 0.75D, 5D);
        // Item #3
        ItemAccess.insertItem(2, 10L, 20L, 0L, 0.25D, 0D);
        // TODO: more items

        // Creation of calculator
        CalculatorUtil.calculator = new StatCalculator(
                ActorAccess.selectActor(0),
                ActorAccess.selectActor(1),
                ItemAccess.selectItem(0),
                ItemAccess.selectItem(1),
                ItemAccess.selectItem(2)
        );
        // Creation of formulas
        CalculatorUtil.formulas = new Formulas();
    }

    /** Opens up a new window that explains how formulas should be formed. Only one window is allowed. */
    public static void openHelp() {
        if (helpStage != null) {
            // Stage was already made, show it
            helpStage.show();
            // setIconified == setMinimized
            // great naming convention
            helpStage.setIconified(false);
            helpStage.toFront();
        } else {
            try {
                FXMLLoader xFxmlLoader = new FXMLLoader(ComparatorApplication.class.getResource("help.fxml"));
                Parent root = xFxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Formulas Help");
                stage.setScene(new Scene(root));
                // same minimum size as the main window
                stage.setMinWidth(641);
                stage.setMinHeight(464);

                helpStage = stage;
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /** Closes the formula help window. */
    public static void closeHelp() {
        // Hides the stage, doesn't actually close it
        helpStage.close();
    }

    public static void main(String[] args)  {
        initializeData();
        launch();
    }
}