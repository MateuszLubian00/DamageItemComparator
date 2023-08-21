package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Item;
import io.github.mateuszlubian00.itemcompare.model.ItemAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
        ObservableList<Item> itemList = ItemAccess.selectManyItems();

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
    protected void updateHealth() {
        Long health = null;
        health = CalculatorUtil.updateFromText(itemHealth, health);
        if (health == null) {return;}

        CalculatorUtil.calculator.setItemHP(ID, health);
        ItemAccess.updateItemField(ID, "BONUS_HP", health);
    }

    @FXML
    protected void updateDefense() {
        Long defense = null;
        defense = CalculatorUtil.updateFromText(itemDefense, defense);
        if (defense == null) {return;}

        CalculatorUtil.calculator.setItemDefense(ID, defense);
        ItemAccess.updateItemField(ID, "BONUS_DEFENSE", defense);
    }

    @FXML
    protected void updateAttack() {
        Long attack = null;
        attack = CalculatorUtil.updateFromText(itemAttack, attack);
        if (attack == null) {return;}

        CalculatorUtil.calculator.setItemAttack(ID, attack);
        ItemAccess.updateItemField(ID, "BONUS_ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() {
        Double attackSpeed = null;
        attackSpeed = CalculatorUtil.updateFromText(itemAttackSpeed, attackSpeed);
        if (attackSpeed == null) {return;}

        CalculatorUtil.calculator.setItemAttackSpeed(ID, attackSpeed);
        ItemAccess.updateItemField(ID, "BONUS_ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() {
        Double critChance = null;
        critChance = CalculatorUtil.updateFromText(itemCritChance, critChance);
        if (critChance == null) {return;}

        CalculatorUtil.calculator.setItemCritChance(ID, critChance);
        ItemAccess.updateItemField(ID, "BONUS_CRITICAL_HIT_CHANCE", critChance);
    }

    @FXML
    protected void switchID() {
        String text = itemPicker.getValue();
        ID = Integer.parseInt(text.substring(5)) - 1;

        refreshData();
    }

    protected void refreshData() {
        Item item = ItemAccess.selectItem(ID);

        selectedItem.setText("Item " + (ID + 1));

        itemHealth.setText(String.valueOf(item.getBonusHP()));
        itemDefense.setText(String.valueOf(item.getBonusDefense()));
        itemAttack.setText(String.valueOf(item.getBonusAttack()));
        itemAttackSpeed.setText(String.valueOf(item.getBonusAttackSpeed()));
        itemCritChance.setText(String.valueOf(item.getBonusCriticalHitChance()));
    }

    @FXML
    protected void updateAll() {
        updateHealth();
        updateDefense();
        updateAttack();
        updateAttackSpeed();
        updateCritChance();
    }
}
