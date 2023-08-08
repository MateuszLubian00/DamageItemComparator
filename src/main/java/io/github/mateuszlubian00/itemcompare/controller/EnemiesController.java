package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class EnemiesController {

    // Note: currently there is only one enemy, so the ID field will always be 1
    // TODO: more enemies

    @FXML
    protected TextField baseHealth;
    @FXML
    protected TextField baseDefense;
    @FXML
    protected TextField calcHealth;
    @FXML
    protected TextField calcDefense;
    protected Actor cachedActor;
    protected Function<Actor, Actor> itemSet;

    @FXML
    private void initialize () {
        Actor actor = ActorAccess.selectActor(1);

        cachedActor = actor;

        baseHealth.setText(String.valueOf(actor.getHP()));
        baseDefense.setText(String.valueOf(actor.getDefense()));

        itemSet = CalculatorUtil.calculateWithItem(2);

        updateCalculations();
    }

    @FXML
    protected void updateHealth() {
        Long health = null;
        health = CalculatorUtil.updateFromText(baseHealth, health);
        if (health == null) {return;}

        cachedActor.setHP(health);
        updateCalculations();
        ActorAccess.updateActorField(1, "HP", health);
    }

    @FXML
    protected void updateDefense() {
        Long defense = null;
        defense = CalculatorUtil.updateFromText(baseDefense, defense);
        if (defense == null) {return;}

        cachedActor.setDefense(defense);
        updateCalculations();
        ActorAccess.updateActorField(1, "DEFENSE", defense);
    }

    @FXML
    protected void updateAll() {
        updateHealth();
        updateDefense();
    }

    protected void updateCalculations() {
        Actor calculated = itemSet.apply(cachedActor);

        calcHealth.setText(String.valueOf(calculated.getHP()));
        calcDefense.setText(String.valueOf(calculated.getDefense()));
    }
}
