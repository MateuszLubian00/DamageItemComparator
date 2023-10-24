package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class GraphsController {

    @FXML
    protected BarChart<String, Number> chart1Attack;
    @FXML
    protected AreaChart<String,Number> chart10Attacks;
    @FXML
    protected AreaChart<String,Number> chart30Attacks;
    @FXML
    protected AreaChart<String,Number> chart5Seconds;
    @FXML
    protected AreaChart<String,Number> chart10Seconds;
    @FXML
    protected AreaChart<String,Number> chartCustomSeconds;
    @FXML
    protected AreaChart<String,Number> chartCustomAttacks;
    @FXML
    protected TextField customSeconds;
    @FXML
    protected TextField customSecondsIncrement;
    @FXML
    protected TextField customAttack;
    protected Actor playerSet1;
    protected Actor playerSet2;
    protected Actor enemySet;

    @FXML
    protected void initialize() {
        ObservableList<Actor> actorList = ActorAccess.selectManyActors();

        playerSet1 = CalculatorUtil.calculateWithItem(0).apply(actorList.get(0));
        playerSet2 = CalculatorUtil.calculateWithItem(1).apply(actorList.get(0));
        enemySet = CalculatorUtil.calculateWithItem(2).apply(actorList.get(1));

        setChart1Attack(chart1Attack);
        setChartAttacks(10, chart10Attacks);
        setChartAttacks(30, chart30Attacks);

        setChartSeconds(5D, 0.5, chart5Seconds);
        setChartSeconds(10D, 0.5, chart10Seconds);

        // Set up custom charts with junk data, otherwise it doesn't scale properly
        setChartAttacks(5, chartCustomAttacks);
        setChartSeconds(5d, 1d, chartCustomSeconds);
    }

    @FXML
    protected void setCustomAttacks() {
        Integer attacks = null;
        attacks = CalculatorUtil.updateFromText(customAttack, attacks);
        if (attacks == null) {return;}

        // Special case to not allow less than 2 attacks.
        if (attacks < 2) {
            customAttack.getStyleClass().add("invalid");
            return;
        }

        setChartAttacks(attacks, chartCustomAttacks);
    }

    @FXML
    protected void setCustomSeconds() {
        boolean exit = false;
        Double seconds = null;
        Double increment = null;
        seconds = CalculatorUtil.updateFromText(customSeconds, seconds);
        increment = CalculatorUtil.updateFromText(customSecondsIncrement, increment);

        // Setting up time

        if (seconds == null) {
            exit = true;
        } else if (seconds < 1) {
            customSeconds.getStyleClass().add("invalid");
            exit = true;
        }

        // Setting up increments

        if (increment == null) {
            exit = true;
        } else if (increment < 0.1) {
            customSecondsIncrement.getStyleClass().add("invalid");
            exit = true;
        }

        // If any field contains wrong values
        if (exit) {return;}

        setChartSeconds(seconds, increment, chartCustomSeconds);
    }

    // ========== Chart modifying ==========

    /** Special case to show only 1 attack */
    protected void setChart1Attack(BarChart<String, Number> chart) {
        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();

        set1.setName("With Item 1");
        set2.setName("With Item 2");

        oneAttackHelper(0, set1);
        oneAttackHelper(1, set2);

        chart.getData().addAll(set1, set2);
    }

    /** Helper method to get both normal and critical attack of an attack */
    private void oneAttackHelper(int itemID, XYChart.Series<String,Number> set) {
        double[] attacks = CalculatorUtil.calculator.nextAttacks(itemID, 1);
        double critChance = CalculatorUtil.calculator.getTotalCritChance(true, itemID);
        if (critChance < 100D) {
            set.getData().add(new XYChart.Data<>("Normal Attack", (attacks[0])));
        }
        if (critChance > 0D) {
            // hacky way to force a critical hit
            double baseCrit = CalculatorUtil.calculator.getPlayerCritChance();
            CalculatorUtil.calculator.setPlayerCritChance(100D);
            attacks = CalculatorUtil.calculator.nextAttacks(itemID, 1);
            CalculatorUtil.calculator.setPlayerCritChance(baseCrit);

            set.getData().add(new XYChart.Data<>("Critical Attack", (attacks[0])));
        }
    }

    /** Set chart for N amount of attacks */
    protected void setChartAttacks(int attacks, AreaChart<String, Number> chart) {
        chart.setData(FXCollections.observableArrayList());

        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();
        set1.setName("With Item 1");
        set2.setName("With Item 2");

        double[] damage1 = CalculatorUtil.calculator.nextAttacks(0, attacks);
        double[] damage2 = CalculatorUtil.calculator.nextAttacks(1, attacks);

        long total1 = 0;
        long total2 = 0;

        for (int i = 1; i - 1 < attacks; i++) {
            total1 += damage1[i - 1];
            set1.getData().add(new XYChart.Data<>(String.valueOf(i), total1));
            total2 += damage2[i - 1];
            set2.getData().add(new XYChart.Data<>(String.valueOf(i), total2));
        }

        chart.getData().addAll(set1, set2);
    }

    /** Set chart for N amount of seconds of attacks */
    protected void setChartSeconds(double seconds, double timeIncrement, AreaChart<String, Number> chart) {
        chart.setData(FXCollections.observableArrayList());

        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();

        set1.setName("With Item 1");
        set2.setName("With Item 2");

        double attackTime1 = CalculatorUtil.calculator.getTotalAttackSpeed(true, 0);
        double attackTime2 = CalculatorUtil.calculator.getTotalAttackSpeed(true, 1);

        /* Adding 1 to total attacks is a hack to combat imprecision.
        *  Example: adding 0.7 and 0.1 results in 0.799999 attack speed.
        *  This would result in one less attack, even though the code would try to add it in (Error!).
        */
        double totalAttacks = seconds * attackTime1 + 1;
        double[] attacks1 = CalculatorUtil.calculator.nextAttacks(0, (int) totalAttacks);
        totalAttacks = seconds * attackTime2 + 1;
        double[] attacks2 = CalculatorUtil.calculator.nextAttacks(1, (int) totalAttacks);

        // total damage dealt
        long total1 = 0, total2 = 0;

        // seconds per attack
        attackTime1 = 1D / attackTime1;
        attackTime2 = 1D / attackTime2;

        // pointers for damage arrays
        int pointer1 = 0,
            pointer2 = 0;
        // total time spent on auto attacking
        double totalTime1 = 0D,
               totalTime2 = 0D;

        // TODO: optimize maybe
        for (double i = timeIncrement; i <= seconds; i += timeIncrement) {
            int attack = 0;
            while (totalTime1 + attackTime1 <= i) {
                attack++;
                totalTime1 += attackTime1;
            }
            for (; attack > 0; attack--) {
                total1 += attacks1[pointer1];
                pointer1++;
            }

            attack = 0;
            while (totalTime2 + attackTime2 <= i) {
                attack++;
                totalTime2 += attackTime2;
            }
            for (; attack > 0; attack--) {
                total2 += attacks2[pointer2];
                pointer2++;
            }

            // Casting to float feels weird, but looks better due to cutting off imprecision.
            String time = String.valueOf((float) i);
            set1.getData().add(new XYChart.Data<>(time, total1));
            set2.getData().add(new XYChart.Data<>(time, total2));
        }

        // Due to settings and attack speeds, there is nothing to show
        if (set1.getData().isEmpty() && set2.getData().isEmpty()) {
            return;
        } else {
            chart.getData().addAll(set1, set2);
        }
    }
}
