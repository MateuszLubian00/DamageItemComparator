package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    @FXML
    protected TextField formulaTotalHP;
    @FXML
    protected TextField formulaTotalDefense;
    @FXML
    protected TextField formulaDefenseEffect;


    @FXML
    private void initialize () {
        Actor actor = ActorAccess.selectActor(1);

        baseHealth.setText(String.valueOf(actor.getHP()));
        baseDefense.setText(String.valueOf(actor.getDefense()));

        formulaTotalHP.setText("wip");
        formulaTotalDefense.setText("wip");
        formulaDefenseEffect.setText("wip");

        updateCalculations();
    }

    // ========== Update Stat Methods ==========

    @FXML
    protected void updateHealth() {
        Long health = null;
        health = CalculatorUtil.updateFromText(baseHealth, health);
        if (health == null) {return;}

        CalculatorUtil.calculator.setEnemyHP(health);
        updateCalculations();
        ActorAccess.updateActorField(1, "HP", health);
    }

    @FXML
    protected void updateDefense() {
        Long defense = null;
        defense = CalculatorUtil.updateFromText(baseDefense, defense);
        if (defense == null) {return;}

        CalculatorUtil.calculator.setEnemyDefense(defense);
        updateCalculations();
        ActorAccess.updateActorField(1, "DEFENSE", defense);
    }

    /** Updates all values at once, used on a button. */
    @FXML
    protected void updateAll() {
        updateHealth();
        updateDefense();
    }

    /** Updates the statistics of enemy actor calculated with item. */
    protected void updateCalculations() {
        calcHealth.setText(String.valueOf(CalculatorUtil.calculator.getEnemyTotalHP()));
        calcDefense.setText(String.valueOf(CalculatorUtil.calculator.getEnemyTotalDefense()));
    }

    // ========== Formulas ==========

    @FXML
    protected void updateFormulaHP() {

    }

    @FXML
    protected void updateFormulaDefense() {

    }

    @FXML
    protected void updateFormulaDefenseEffect() {

    }

    @FXML
    protected void updateAllFormulas() {
        updateFormulaHP();
        updateFormulaDefense();
        updateFormulaDefenseEffect();
    }
}
