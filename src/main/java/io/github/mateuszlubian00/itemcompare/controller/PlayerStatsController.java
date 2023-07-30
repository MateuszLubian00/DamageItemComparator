package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;

import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.function.Function;

public class PlayerStatsController {

    // Note: any operation in here revolves around player
    // As such all IDs are '0'

    @FXML
    protected TextField calcAttack1;
    @FXML
    protected TextField calcAttack2;
    @FXML
    protected TextField calcAttackSpeed1;
    @FXML
    protected TextField calcAttackSpeed2;
    @FXML
    protected TextField calcCritChance1;
    @FXML
    protected TextField calcCritChance2;
    @FXML
    protected TextField baseAttack;
    @FXML
    protected TextField baseAttackSpeed;
    @FXML
    protected TextField baseCritChance;
    protected Function<Actor, Actor> itemSet1;
    protected Function<Actor, Actor> itemSet2;
    protected Actor cachedActor;


    @FXML
    private void initialize () throws SQLException {
        Actor actor;
        try {
            actor = ActorAccess.selectActor(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        cachedActor = actor;

        baseAttack.setText(String.valueOf(actor.getAttack()));
        baseAttackSpeed.setText(String.valueOf(actor.getAttackSpeed()));
        baseCritChance.setText(String.valueOf(actor.getCriticalHitChance()));

        itemSet1 = CalculatorUtil.calculateWithItem(0);
        itemSet2 = CalculatorUtil.calculateWithItem(1);

        updateCalculations();
    }

    @FXML
    protected void updateAttack() throws SQLException {
        baseAttack.getStyleClass().remove("invalid");
        Long attack;
        try {
            attack = Long.parseLong(baseAttack.getText());
        } catch (NumberFormatException e) {
            baseAttack.getStyleClass().add("invalid");
            return;
        }

        cachedActor.setAttack(attack);
        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() throws SQLException {
        baseAttackSpeed.getStyleClass().remove("invalid");
        Double attackSpeed;
        try {
            attackSpeed = Double.parseDouble(baseAttackSpeed.getText());
        } catch (NumberFormatException e) {
            baseAttackSpeed.getStyleClass().add("invalid");
            return;
        }

        cachedActor.setAttackSpeed(attackSpeed);
        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() throws SQLException {
        baseCritChance.getStyleClass().remove("invalid");
        Double critChance;
        try {
            critChance = Double.parseDouble(baseCritChance.getText());
        } catch (NumberFormatException e) {
            baseCritChance.getStyleClass().add("invalid");
            return;
        }

        cachedActor.setCriticalHitChance(critChance);
        updateCalculations();
        ActorAccess.updateActorField(0, "CRITICAL_HIT_CHANCE", critChance);
    }

    @FXML
    protected void updateAll() throws SQLException {
        updateAttack();
        updateAttackSpeed();
        updateCritChance();
    }

    protected void updateCalculations() {
        Actor calculated = itemSet1.apply(cachedActor);

        calcAttack1.setText(String.valueOf(calculated.getAttack()));
        calcAttackSpeed1.setText(String.valueOf(calculated.getAttackSpeed()));
        calcCritChance1.setText(String.valueOf(calculated.getCriticalHitChance()));

        calculated = itemSet2.apply(cachedActor);

        calcAttack2.setText(String.valueOf(calculated.getAttack()));
        calcAttackSpeed2.setText(String.valueOf(calculated.getAttackSpeed()));
        calcCritChance2.setText(String.valueOf(calculated.getCriticalHitChance()));
    }
}