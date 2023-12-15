package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.ComparatorApplication;
import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.model.StatCalculator;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    @FXML
    protected TextField formulaTotalAttack;
    @FXML
    protected TextField formulaTotalAttackSpeed;
    @FXML
    protected TextField formulaTotalCritChance;


    @FXML
    private void initialize () {
        Actor actor = ActorAccess.selectActor(0);

        // Base statistics of player
        baseAttack.setText(String.valueOf(actor.getAttack()));
        baseAttackSpeed.setText(String.valueOf(actor.getAttackSpeed()));
        baseCritChance.setText(String.valueOf(actor.getCriticalHitChance()));

        // Formulas of how statistics are applied
        formulaTotalAttack.setText(CalculatorUtil.formulas.totalAttack.toString());
        formulaTotalAttackSpeed.setText(CalculatorUtil.formulas.totalAttackSpeed.toString());
        formulaTotalCritChance.setText(CalculatorUtil.formulas.totalCritChance.toString());

        updateCalculations();
    }

    // ========== Update Stat Methods ==========

    @FXML
    protected void updateAttack() {
        Long attack = null;
        attack = CalculatorUtil.updateFromText(baseAttack, attack);
        if (attack == null) {return;}

        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK", attack);
    }

    @FXML
    protected void updateAttackSpeed() {
        Double attackSpeed = null;
        attackSpeed = CalculatorUtil.updateFromText(baseAttackSpeed, attackSpeed);
        if (attackSpeed == null) {return;}

        updateCalculations();
        ActorAccess.updateActorField(0, "ATTACK_SPEED", attackSpeed);
    }

    @FXML
    protected void updateCritChance() {
        Double critChance = null;
        critChance = CalculatorUtil.updateFromText(baseCritChance, critChance);
        if (critChance == null) {return;}

        updateCalculations();
        ActorAccess.updateActorField(0, "CRITICAL_HIT_CHANCE", critChance);
    }

    /** Updates all values at once, used on a button. */
    @FXML
    protected void updateAll() {
        updateAttack();
        updateAttackSpeed();
        updateCritChance();
    }

    /** Updates the statistics of player actor calculated with items. */
    protected void updateCalculations() {
        calcAttack1.setText(String.valueOf(StatCalculator.getTotalAttack(true, 0)));
        calcAttackSpeed1.setText(String.valueOf(StatCalculator.getTotalAttackSpeed(true, 0)));
        calcCritChance1.setText(String.valueOf(StatCalculator.getTotalCritChance(true, 0)));

        calcAttack2.setText(String.valueOf(StatCalculator.getTotalAttack(true, 1)));
        calcAttackSpeed2.setText(String.valueOf(StatCalculator.getTotalAttackSpeed(true, 1)));
        calcCritChance2.setText(String.valueOf(StatCalculator.getTotalCritChance(true, 1)));
    }

    // ========== Formulas ==========

    /** Opens up a help window for creating formulas. */
    @FXML
    private void getHelp() {
        ComparatorApplication.openHelp();
    }

    @FXML
    protected void updateFormulaAttack() {
        if (CalculatorUtil.setNewFormula(formulaTotalAttack, CalculatorUtil.formulas.totalAttack)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateFormulaAttackSpeed() {
        if (CalculatorUtil.setNewFormula(formulaTotalAttackSpeed, CalculatorUtil.formulas.totalAttackSpeed)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateFormulaCritChance() {
        if (CalculatorUtil.setNewFormula(formulaTotalCritChance, CalculatorUtil.formulas.totalCritChance)) {
            updateCalculations();
        }
    }

    @FXML
    protected void updateAllFormulas() {
        updateFormulaAttack();
        updateFormulaAttackSpeed();
        updateFormulaCritChance();
    }
}