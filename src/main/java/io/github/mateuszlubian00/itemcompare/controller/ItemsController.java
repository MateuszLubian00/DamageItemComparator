package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Item;
import io.github.mateuszlubian00.itemcompare.model.ItemAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class ItemsController {

    @FXML
    protected TextField itemHealth;
    @FXML
    protected TextField itemDefense;
    @FXML
    protected TextField itemAttack;
    @FXML
    protected TextField itemAttackSpeed;
    @FXML
    protected TextField itemCritChance;
    @FXML
    protected ChoiceBox<String> itemPicker;
    @FXML
    protected Text selectedItem;
    protected Integer ID = 0;

    @FXML
    protected void initialize() {
        ObservableList<Item> itemList;
        try {
            itemList = ItemAccess.selectManyItems();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        itemPicker.setValue("Item " + (ID + 1));
        selectedItem.setText("Item " + (ID + 1));

        ObservableList<String> choices = FXCollections.observableArrayList();

        for (int i = 1; i <= itemList.size(); i++) {
            choices.add("Item " + i);
        }
        itemPicker.setItems(choices);

        itemPicker.setOnAction(e -> switchID());

        refreshData();
    }

    @FXML
    protected void updateHealth( ) throws SQLException {
        itemHealth.getStyleClass().remove("invalid");
        Long health;
        try {
            health = Long.parseLong(itemHealth.getText());
        } catch (NumberFormatException e) {
            itemHealth.getStyleClass().add("invalid");
            return;
        }

        ItemAccess.updateItemField(ID, "BONUS_HP", health);
    }

    @FXML
    protected void updateDefense() throws SQLException {
        itemDefense.getStyleClass().remove("invalid");
        Long defense;
        try {
            defense = Long.parseLong(itemDefense.getText());
        } catch (NumberFormatException e) {
            itemDefense.getStyleClass().add("invalid");
            return;
        }

        ItemAccess.updateItemField(ID, "BONUS_DEFENSE", defense);
    }

    @FXML
    protected void updateAttack() throws SQLException {
        itemAttack.getStyleClass().remove("invalid");
        Long attack;
        try {
            attack = Long.parseLong(itemAttack.getText());
        } catch (NumberFormatException e) {
            itemAttack.getStyleClass().add("invalid");
            return;
        }

        ItemAccess.updateItemField(ID, "BONUS_ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() throws SQLException {
        itemAttackSpeed.getStyleClass().remove("invalid");
        Double attackSpeed;
        try {
            attackSpeed = Double.parseDouble(itemAttackSpeed.getText());
        } catch (NumberFormatException e) {
            itemAttackSpeed.getStyleClass().add("invalid");
            return;
        }

        ItemAccess.updateItemField(ID, "BONUS_ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() throws SQLException {
        itemCritChance.getStyleClass().remove("invalid");
        Double critChance;
        try {
            critChance = Double.parseDouble(itemCritChance.getText());
        } catch (NumberFormatException e) {
            itemCritChance.getStyleClass().add("invalid");
            return;
        }

        ItemAccess.updateItemField(ID, "BONUS_CRITICAL_HIT_CHANCE", critChance);
    }

    @FXML
    protected void switchID() {
        String text = itemPicker.getValue();
        ID = Integer.parseInt(text.substring(5)) - 1;

        refreshData();
    }

    protected void refreshData() {
        Item item;
        try {
            item = ItemAccess.selectItem(ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        selectedItem.setText("Item " + (ID + 1));

        itemHealth.setText(String.valueOf(item.getBonusHP()));
        itemDefense.setText(String.valueOf(item.getBonusDefense()));
        itemAttack.setText(String.valueOf(item.getBonusAttack()));
        itemAttackSpeed.setText(String.valueOf(item.getBonusAttackSpeed()));
        itemCritChance.setText(String.valueOf(item.getBonusCriticalHitChance()));
    }

    @FXML
    protected void updateAll() throws SQLException {
        updateHealth();
        updateDefense();
        updateAttack();
        updateAttackSpeed();
        updateCritChance();
    }
}
