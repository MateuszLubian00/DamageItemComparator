package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

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

    @FXML
    protected void initialize() {
        Thread thread1 = new Thread(() -> setChart1Attack(chart1Attack));
        Thread thread2 = new Thread(() -> setChartAttacks(10, chart10Attacks));
        Thread thread3 = new Thread(() -> setChartAttacks(30, chart30Attacks));

        thread1.start();
        thread2.start();
        thread3.start();

        Thread thread4 = new Thread(() -> setChartSeconds(5D, 0.5, chart5Seconds));
        Thread thread5 = new Thread(() -> setChartSeconds(10D, 0.5, chart10Seconds));

        thread4.start();
        thread5.start();
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

    /** Opens up javafx thread to update chart.
     *  Doing so manually may result it 'thread not in javafx application' error.
     */
    protected void updateChart(XYChart<String, Number> chart, XYChart.Series<String,Number> set1, XYChart.Series<String,Number> set2) {
        Platform.runLater(() -> chart.setData(FXCollections.observableArrayList(set1, set2)));
    }

    /** Special case to show only 1 attack */
    protected void setChart1Attack(XYChart<String, Number> chart) {
        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();

        set1.setName("With Item 1");
        set2.setName("With Item 2");

        oneAttackHelper(0, set1);
        oneAttackHelper(1, set2);

        updateChart(chart, set1, set2);
    }

    /** Helper method to get both normal and critical attack of an attack */
    private void oneAttackHelper(int itemID, XYChart.Series<String,Number> set) {
        double attacks = CalculatorUtil.calculator.nextAttacks(itemID, 1)[0];
        double critChance = CalculatorUtil.calculator.getTotalCritChance(true, itemID);
        if (critChance < 100D) {
            set.getData().add(new XYChart.Data<>("Normal Attack", (attacks)));
        }
        if (critChance > 0D) {
            attacks = CalculatorUtil.calculator.nextCritical(itemID);
            set.getData().add(new XYChart.Data<>("Critical Attack", (attacks)));
        }
    }

    /** Set chart for N amount of attacks */
    protected void setChartAttacks(int attacks, XYChart<String, Number> chart) {
        ThreadLocal<XYChart.Series<String,Number>> set1 = new ThreadLocal<>();
        set1.set(new XYChart.Series<>());
        ThreadLocal<XYChart.Series<String,Number>> set2 = new ThreadLocal<>();
        set2.set(new XYChart.Series<>());
        set1.get().setName("With Item 1");
        set2.get().setName("With Item 2");

        ThreadLocal<double[]> damage1 = new ThreadLocal<>();
        damage1.set(CalculatorUtil.calculator.nextAttacks(0, attacks));
        ThreadLocal<double[]> damage2 = new ThreadLocal<>();
        damage2.set(CalculatorUtil.calculator.nextAttacks(1, attacks));

        ThreadLocal<Double> total1 = new ThreadLocal<>();
        total1.set(0D);
        ThreadLocal<Double> total2 = new ThreadLocal<>();
        total2.set(0D);

        ThreadLocal<Integer> i = new ThreadLocal<>();
        i.set(1);
        for (; i.get() - 1 < attacks; i.set(i.get() + 1)) {
            total1.set(total1.get() + damage1.get()[i.get() - 1]);
            set1.get().getData().add(new XYChart.Data<>(String.valueOf(i.get()), total1.get()));
            total2.set(total2.get() + damage2.get()[i.get() - 1]);
            set2.get().getData().add(new XYChart.Data<>(String.valueOf(i.get()), total2.get()));
        }

        updateChart(chart, set1.get(), set2.get());
    }

    /** Set chart for N amount of seconds of attacks */
    protected void setChartSeconds(double seconds, double timeIncrement, XYChart<String, Number> chart) {
        ThreadLocal<XYChart.Series<String,Number>> set1 = new ThreadLocal<>();
        set1.set(new XYChart.Series<>());
        ThreadLocal<XYChart.Series<String,Number>> set2 = new ThreadLocal<>();
        set2.set(new XYChart.Series<>());
        set1.get().setName("With Item 1");
        set2.get().setName("With Item 2");

        ThreadLocal<Double> attackTime1 = new ThreadLocal<>();
        attackTime1.set(CalculatorUtil.calculator.getTotalAttackSpeed(true, 0));
        ThreadLocal<Double> attackTime2 = new ThreadLocal<>();
        attackTime2.set(CalculatorUtil.calculator.getTotalAttackSpeed(true, 1));

        /* Adding 1 to total attacks is a hack to combat imprecision.
        *  Example: adding 0.7 and 0.1 results in 0.799999 attack speed.
        *  This would result in one less attack, even though the code would try to add it in (Error!).
        */
        ThreadLocal<Double> totalAttacks = new ThreadLocal<>();
        totalAttacks.set(seconds * attackTime1.get() + 1);
        ThreadLocal<double[]> attacks1 = new ThreadLocal<>();
        attacks1.set(CalculatorUtil.calculator.nextAttacks(0, totalAttacks.get()));
        totalAttacks.set(seconds * attackTime2.get() + 1);
        ThreadLocal<double[]> attacks2 = new ThreadLocal<>();
        attacks2.set(CalculatorUtil.calculator.nextAttacks(1, totalAttacks.get()));

        // total damage dealt
        ThreadLocal<Double> total1 = new ThreadLocal<>(), total2 = new ThreadLocal<>();
        total1.set(0D);
        total2.set(0D);

        // seconds per attack
        attackTime1.set(1D / attackTime1.get());
        attackTime2.set(1D / attackTime2.get());

        // pointers for damage arrays
        ThreadLocal<Integer> pointer1 = new ThreadLocal<>(), pointer2 = new ThreadLocal<>();
        pointer1.set(0);
        pointer2.set(0);
        // total time spent on auto attacking
        ThreadLocal<Double> totalTime1 = new ThreadLocal<>(), totalTime2 = new ThreadLocal<>();
        totalTime1.set(0D);
        totalTime2.set(0D);

        // loop variables
        ThreadLocal<Double> i = new ThreadLocal<>();
        i.set(timeIncrement);
        ThreadLocal<Integer> attack = new ThreadLocal<>();

        // TODO: optimize maybe
        for (; i.get() <= seconds; i.set(i.get() + timeIncrement)) {
            attack.set(0);
            while (totalTime1.get() + attackTime1.get() <= i.get()) {
                attack.set(attack.get() + 1);
                totalTime1.set(totalTime1.get() + attackTime1.get());
            }
            for (; attack.get() > 0; attack.set(attack.get() - 1)) {
                total1.set(total1.get() + attacks1.get()[pointer1.get()]);
                pointer1.set(pointer1.get() + 1);
            }

            attack.set(0);
            while (totalTime2.get() + attackTime2.get() <= i.get()) {
                attack.set(attack.get() + 1);
                totalTime2.set(totalTime2.get() + attackTime2.get());
            }
            for (; attack.get() > 0; attack.set(attack.get() - 1)) {
                total2.set(total2.get() + attacks2.get()[pointer2.get()]);
                pointer2.set(pointer2.get() + 1);
            }

            // Switching to float feels weird, but looks better due to cutting off imprecision.
            String time = String.valueOf(Float.valueOf(i.get().toString()));
            set1.get().getData().add(new XYChart.Data<>(time, total1.get()));
            set2.get().getData().add(new XYChart.Data<>(time, total2.get()));
        }

        updateChart(chart, set1.get(), set2.get());
    }
}
