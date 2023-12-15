package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Item;
import io.github.mateuszlubian00.itemcompare.model.ItemAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    protected Label selectedItem;
    protected Integer ID = 0;

    @FXML
    protected void initialize() {
        ObservableList<Item> itemList = ItemAccess.selectManyItems();

        // Set currently selected item name in selection box
        itemPicker.setValue("Item " + (ID + 1));

        // Fill selection box with names of all the items
        ObservableList<String> choices = FXCollections.observableArrayList();
        for (int i = 1; i <= itemList.size(); i++) {
            choices.add("Item " + i);
        }
        itemPicker.setItems(choices);

        // replace current item with the one selected by user
        itemPicker.setOnAction(e -> switchID());

        refreshData();
    }

    // ========== Update Stat Methods ==========

    @FXML
    protected void updateHealth() {
        Long health = null;
        health = CalculatorUtil.updateFromText(itemHealth, health);
        if (health == null) {return;}

        ItemAccess.updateItemField(ID, "BONUS_HP", health);
    }

    @FXML
    protected void updateDefense() {
        Long defense = null;
        defense = CalculatorUtil.updateFromText(itemDefense, defense);
        if (defense == null) {return;}

        ItemAccess.updateItemField(ID, "BONUS_DEFENSE", defense);
    }

    @FXML
    protected void updateAttack() {
        Long attack = null;
        attack = CalculatorUtil.updateFromText(itemAttack, attack);
        if (attack == null) {return;}

        ItemAccess.updateItemField(ID, "BONUS_ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() {
        Double attackSpeed = null;
        attackSpeed = CalculatorUtil.updateFromText(itemAttackSpeed, attackSpeed);
        if (attackSpeed == null) {return;}

        ItemAccess.updateItemField(ID, "BONUS_ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() {
        Double critChance = null;
        critChance = CalculatorUtil.updateFromText(itemCritChance, critChance);
        if (critChance == null) {return;}

        ItemAccess.updateItemField(ID, "BONUS_CRITICAL_HIT_CHANCE", critChance);
    }

    /** Updates all values at once, used on a button. */
    @FXML
    protected void updateAll() {
        updateHealth();
        updateDefense();
        updateAttack();
        updateAttackSpeed();
        updateCritChance();
    }

    // ========== Helper Methods ==========

    /** Takes user selected item from ChoiceBox, updates ID and switches visible values. */
    @FXML
    protected void switchID() {
        String text = itemPicker.getValue();
        // Strip the word 'Item ' from current selection
        ID = Integer.parseInt(text.substring(5)) - 1;

        refreshData();
    }

    /** Grabs the selected item from database and shows its values to user. */
    protected void refreshData() {
        // We don't keep or access all items, grab new one each time user asks
        Item item = ItemAccess.selectItem(ID);

        // Set name of the selected item
        selectedItem.setText("Item " + (ID + 1));

        // Output statistics
        itemHealth.setText(String.valueOf(item.getBonusHP()));
        itemDefense.setText(String.valueOf(item.getBonusDefense()));
        itemAttack.setText(String.valueOf(item.getBonusAttack()));
        itemAttackSpeed.setText(String.valueOf(item.getBonusAttackSpeed()));
        itemCritChance.setText(String.valueOf(item.getBonusCriticalHitChance()));
    }
}
