package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.ComparatorApplication;
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
    protected TextField formulaDefenseMultiplier;


    @FXML
    private void initialize () {
        Actor actor = ActorAccess.selectActor(1);

        // Base statistics of enemy
        baseHealth.setText(String.valueOf(actor.getHP()));
        baseDefense.setText(String.valueOf(actor.getDefense()));

        // Formulas of how statistics are applied
        formulaTotalHP.setText(CalculatorUtil.formulas.totalHP.toString());
        formulaTotalDefense.setText(CalculatorUtil.formulas.totalDefense.toString());
        formulaDefenseMultiplier.setText(CalculatorUtil.formulas.defenseMultiplier.toString());

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
        // Currently enemy unit has item 3, with ID 2
        calcHealth.setText(String.valueOf(CalculatorUtil.calculator.getTotalHP(false, 2)));
        calcDefense.setText(String.valueOf(CalculatorUtil.calculator.getTotalDefense(false, 2)));
    }

    // ========== Formulas ==========

    /** Opens up a help window for creating formulas. */
    @FXML
    private void getHelp() {
        ComparatorApplication.openHelp();
    }

    @FXML
    protected void updateFormulaHP() {
        if (CalculatorUtil.setNewFormula(formulaTotalHP, CalculatorUtil.formulas.totalHP)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateFormulaDefense() {
        if (CalculatorUtil.setNewFormula(formulaTotalDefense, CalculatorUtil.formulas.totalDefense)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateFormulaDefenseMultiplier() {
        if (CalculatorUtil.setNewFormula(formulaDefenseMultiplier, CalculatorUtil.formulas.defenseMultiplier)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateAllFormulas() {
        updateFormulaHP();
        updateFormulaDefense();
        updateFormulaDefenseMultiplier();
    }
}
