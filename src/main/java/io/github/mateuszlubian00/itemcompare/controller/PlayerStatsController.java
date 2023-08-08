package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    private void initialize () {
        Actor actor = ActorAccess.selectActor(0);

        cachedActor = actor;

        baseAttack.setText(String.valueOf(actor.getAttack()));
        baseAttackSpeed.setText(String.valueOf(actor.getAttackSpeed()));
        baseCritChance.setText(String.valueOf(actor.getCriticalHitChance()));

        itemSet1 = CalculatorUtil.calculateWithItem(0);
        itemSet2 = CalculatorUtil.calculateWithItem(1);

        updateCalculations();
    }

    @FXML
    protected void updateAttack() {
        Long attack = null;
        attack = CalculatorUtil.updateFromText(baseAttack, attack);
        if (attack == null) {return;}

        cachedActor.setAttack(attack);
        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() {
        Double attackSpeed = null;
        attackSpeed = CalculatorUtil.updateFromText(baseAttackSpeed, attackSpeed);
        if (attackSpeed == null) {return;}

        cachedActor.setAttackSpeed(attackSpeed);
        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() {
        Double critChance = null;
        critChance = CalculatorUtil.updateFromText(baseCritChance, critChance);
        if (critChance == null) {return;}

        cachedActor.setCriticalHitChance(critChance);
        updateCalculations();
        ActorAccess.updateActorField(0, "CRITICAL_HIT_CHANCE", critChance);
    }

    @FXML
    protected void updateAll() {
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