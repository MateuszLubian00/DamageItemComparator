package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.ActorAccess;
import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

public class GraphsController {

    @FXML
    protected BarChart chart1Attack;
    @FXML
    protected AreaChart<String,Number> chart10Attacks;
    @FXML
    protected AreaChart<String,Number> chart30Attacks;
    @FXML
    protected AreaChart<String,Number> chart100Attacks;
    @FXML
    protected AreaChart<String,Number> chart5Seconds;
    @FXML
    protected AreaChart<String,Number> chart10Seconds;
    @FXML
    protected AreaChart<String,Number> chart25Seconds;
    protected Actor playerSet1;
    protected Actor playerSet2;
    protected Actor enemySet;

    @FXML
    protected void initialize() throws SQLException {
        ObservableList<Actor> actorList = ActorAccess.selectManyActors();

        playerSet1 = CalculatorUtil.calculateWithItem(0).apply(actorList.get(0));
        playerSet2 = CalculatorUtil.calculateWithItem(1).apply(actorList.get(0));
        enemySet = CalculatorUtil.calculateWithItem(2).apply(actorList.get(1));

        setChart1Attack(chart1Attack);
        setChartAttacks(10, chart10Attacks);
        setChartAttacks(30, chart30Attacks);
        setChartAttacks(100, chart100Attacks);
        setChartSeconds(5, chart5Seconds);
        setChartSeconds(10, chart10Seconds);
        setChartSeconds(25, chart25Seconds);
    }

    /** Special case to show only 1 attack */
    protected  void setChart1Attack(BarChart chart) {
        XYChart.Series<String,Number> set1 = new XYChart.Series<String,Number>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<String,Number>();

        set1.setName("With Item 1");
        set2.setName("With Item 2");

        set1.getData().add(new XYChart.Data<>("Normal Attack", (playerSet1.getAttack() - enemySet.getDefense())));
        set1.getData().add(new XYChart.Data<>("Critical Attack", ((playerSet1.getAttack() * 2) - enemySet.getDefense())));

        set2.getData().add(new XYChart.Data<>("Normal Attack", (playerSet2.getAttack() - enemySet.getDefense())));
        set2.getData().add(new XYChart.Data<>("Critical Attack", ((playerSet2.getAttack() * 2) - enemySet.getDefense())));

        if (playerSet1.getCriticalHitChance() >= 100D) {
            set1.getData().remove(0);
        } else if (playerSet1.getCriticalHitChance() <= 0D) {
            set1.getData().remove(1);
        }

        if (playerSet2.getCriticalHitChance() >= 100D) {
            set2.getData().remove(0);
        } else if (playerSet2.getCriticalHitChance() <= 0D) {
            set2.getData().remove(1);
        }

        chart.getData().addAll(set1, set2);
    }

    protected void setChartAttacks(int attacks, AreaChart chart) {
        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();
        set1.setName("With Item 1");
        set2.setName("With Item 2");
        Double crit1 = playerSet1.getCriticalHitChance();
        Double crit2 = playerSet2.getCriticalHitChance();

        Long attack1 = playerSet1.getAttack();
        Long attack2 = playerSet2.getAttack();

        Long defense = enemySet.getDefense();

        Long total1 = 0L;
        Long total2 = 0L;

        for (int i = 1; i - 1 < attacks; i++) {
            if (crit1 >= 100D) {
                total1 += ((2* attack1) - defense);
                crit1 -= 100D;
            } else {
                total1 += (attack1 - defense);
            }
            set1.getData().add(new XYChart.Data<>(String.valueOf(i), total1));

            if (crit2 >= 100D) {
                total2 += ((2 * attack2) - defense);
                crit2 -= 100D;
            } else {
                total2 += (attack2 - defense);
            }
            set2.getData().add(new XYChart.Data<>(String.valueOf(i), total2));

            crit1 += playerSet1.getCriticalHitChance();
            crit2 += playerSet2.getCriticalHitChance();
        }

        chart.getData().addAll(set1, set2);
    }

    protected void setChartSeconds(int seconds, AreaChart chart) {
        XYChart.Series<String,Number> set1 = new XYChart.Series<>();
        XYChart.Series<String,Number> set2 = new XYChart.Series<>();
        set1.setName("With Item 1");
        set2.setName("With Item 2");
        Double crit1 = playerSet1.getCriticalHitChance();
        Double crit2 = playerSet2.getCriticalHitChance();

        Long attack1 = playerSet1.getAttack();
        Long attack2 = playerSet2.getAttack();

        Long defense = enemySet.getDefense();

        Long total1 = 0L;
        Long total2 = 0L;

        Double attackTime1 = 1D / playerSet1.getAttackSpeed();
        Double attackTime2 = 1D / playerSet2.getAttackSpeed();

        Double totalTime1 = 0D;
        Double totalTime2 = 0D;

        for (Double i = 0.5; i <= seconds; i++) {
            while (totalTime1 + attackTime1 < i) {
                if (crit1 >= 100D) {
                    total1 += ((2* attack1) - defense);
                    crit1 -= 100D;
                } else {
                    total1 += (attack1 - defense);
                }
                totalTime1 += attackTime1;
            }
            set1.getData().add(new XYChart.Data<>(String.valueOf(i), total1));

            while (totalTime2 + attackTime2 < i) {
                if (crit2 >= 100D) {
                    total2 += ((2* attack2) - defense);
                    crit2 -= 100D;
                } else {
                    total2 += (attack2 - defense);
                }
                totalTime2 += attackTime2;
            }
            set2.getData().add(new XYChart.Data<>(String.valueOf(i), total2));
        }

        chart.getData().addAll(set1, set2);
    }
}
