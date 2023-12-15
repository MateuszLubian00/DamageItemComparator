package io.github.mateuszlubian00.itemcompare.model;

import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;

public class StatCalculator {

    /** Calculates the damage of N amount of attacks */
    public static synchronized double[] nextAttacks(int itemID, int attackAmount) {
        if (attackAmount < 1) {
            return new double[]{0};
        }

        long attack = getTotalAttack(true, itemID);
        double defenseModifier = getDefenseMultiplier(false, 2);
        double baseCriticalChance = getTotalCritChance(true, itemID);
        double calculatedCriticalChance = baseCriticalChance;

        double[] damage = new double[attackAmount];
        int currAttack = 0;

        while (currAttack < attackAmount) {
            if (calculatedCriticalChance >= 100D) {
                damage[currAttack] = (attack * 2) * defenseModifier;
                calculatedCriticalChance -= 100D;
            } else {
                damage[currAttack] = attack * defenseModifier;
            }
            calculatedCriticalChance += baseCriticalChance;
            currAttack++;
        }

        return damage;
    }

    public synchronized static double[] nextAttacks(int itemID, double attackAmount) {
        return nextAttacks(itemID, (int) attackAmount);
    }

    /** Calculates the critical damage dealt by a single attack.
     *  Used only in GraphsController.oneAttackHelper()
     */
    public static double nextCritical(int itemID) {
        long attack = getTotalAttack(true, itemID);
        double defenseModifier = getDefenseMultiplier(false, 2);

        return (attack * 2) * defenseModifier;
    }

    // ========== Returning Total Stats ==========

    /** Helper method to return any formula with regard to item and unit. */
    private synchronized static double getTotalStat(boolean isPlayer, int itemID, Formulas.formula f) {
        Item item = ItemAccess.selectItem(itemID);
        Actor player = ActorAccess.selectActor(0);
        Actor enemy = ActorAccess.selectActor(1);
        // only thing that changes is the unit argument positions
        if (isPlayer) {
            return parseFormula(f, player, enemy, item);
        } else {
            return parseFormula(f, enemy, player, item);
        }
    }

    public static long  getTotalAttack(boolean isPlayer, int itemID) {
        // losing precision when casting to long
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalAttack);
    }

    public static double getTotalAttackSpeed(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalAttackSpeed);
    }

    public static double getTotalCritChance(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalCritChance);
    }

    public static long getTotalHP(boolean isPlayer, int itemID) {
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalHP);
    }

    public static long getTotalDefense(boolean isPlayer, int itemID) {
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalDefense);
    }

    public static double getDefenseMultiplier(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.defenseMultiplier);
    }

    // ========== Formulas ==========

    /** Allows to map formula arguments to the correct variables. */
    private static Queue<Double> mapFormulaArgs(String[] args, Actor unit, Actor enemy, Item item) {
        Queue<Double> result = new ArrayDeque<>();
        for (String elem : args) {
            // Manually translating all possible references
            if (elem.startsWith("i_")) {
                switch (elem) {
                    // Item
                    case "i_atk" -> result.offer((double) item.getBonusAttack());
                    case "i_atkspd" -> result.offer(item.getBonusAttackSpeed());
                    case "i_crit" -> result.offer(item.getBonusCriticalHitChance());
                    case "i_hp" -> result.offer((double) item.getBonusHP());
                    case "i_def" -> result.offer((double) item.getBonusDefense());
                    default -> result.offer(0d);
                }
            } else if (elem.startsWith("e_")) {
                switch (elem) {
                    // Enemy
                    case "e_atk" -> result.offer((double) enemy.getAttack());
                    case "e_atkspd" -> result.offer(enemy.getAttackSpeed());
                    case "e_crit" -> result.offer(enemy.getCriticalHitChance());
                    case "e_hp" -> result.offer((double) enemy.getHP());
                    case "e_def" -> result.offer((double) enemy.getDefense());
                    default -> result.offer(0d);
                }
            } else if (elem.startsWith("t_")) {
                switch (elem) {
                    // Total Player
                    case "t_atk" -> result.offer(parseFormula(CalculatorUtil.formulas.totalAttack, unit, enemy, item));
                    case "t_atkspd" -> result.offer(parseFormula(CalculatorUtil.formulas.totalAttackSpeed, unit, enemy, item));
                    case "t_crit" -> result.offer(parseFormula(CalculatorUtil.formulas.totalCritChance, unit, enemy, item));
                    case "t_hp" -> result.offer(parseFormula(CalculatorUtil.formulas.totalHP, unit, enemy, item));
                    case "t_def" -> result.offer(parseFormula(CalculatorUtil.formulas.totalDefense, unit, enemy, item));
                    default -> result.offer(0d);
                }
            } else {
                switch (elem) {
                    // Player
                    case "atk" -> result.offer((double) unit.getAttack());
                    case "atkspd" -> result.offer(unit.getAttackSpeed());
                    case "crit" -> result.offer(unit.getCriticalHitChance());
                    case "hp" -> result.offer((double) unit.getHP());
                    case "def" -> result.offer((double) unit.getDefense());
                    default -> {
                        // The inputted value might be a fixed value
                        try {
                            result.offer(Double.parseDouble(elem));
                        } catch (NumberFormatException e) {
                            result.offer(0d);
                        }
                    }
                }
            }
        }

        return result;
    }

    /** Takes in a formula and selected actors and item to calculate the result. */
    private static double parseFormula(Formulas.formula f, Actor unit, Actor enemy, Item item) {
        Queue<Double> args = mapFormulaArgs(f.getRequirements(), unit, enemy, item);
        Queue<String> text = new ArrayDeque<>(List.of(f.getText().split("\\s+")));
        return parseRecFormula(text, args);
    }

    /** Helper method to recursively apply a formula. */
    private static double parseRecFormula(Queue<String> formula, Queue<Double> args) {
        double value = 0d;
        BiFunction<Double, Double, Double> operation = Double::sum;

        while (!formula.isEmpty()) {
            String expr = formula.remove();

            switch (expr) {
                // Basic operations
                case "+" -> operation = Double::sum;
                case "-" -> operation = (a, b) -> a-b;
                case "*" -> operation = (a, b) -> a*b;
                case "/" -> operation = (a, b) -> a/b;
                // Parenthesis
                case "(" -> value = operation.apply(value, parseRecFormula(formula, args));
                case ")" -> { return value; }
                // Value
                case "%s" -> value = operation.apply(value, args.remove());
            }
        }

        return value;
    }
}
