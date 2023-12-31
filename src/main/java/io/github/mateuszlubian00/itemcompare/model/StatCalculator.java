package io.github.mateuszlubian00.itemcompare.model;

import io.github.mateuszlubian00.itemcompare.util.CalculatorUtil;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;

public class StatCalculator {

    private Actor player;
    private Actor enemy;
    private Item item1;
    private Item item2;
    private Item item3;


    public StatCalculator(Actor player, Actor enemy, Item item1, Item item2, Item item3) {
        this.player = player;
        this.enemy = enemy;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    /** Calculates the damage of N amount of attacks */
    public double[] nextAttacks(int itemID, int attackAmount) {
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

    // ========== Returning Total Stats ==========

    /** Helper method to return any formula with regard to item and unit. */
    private double getTotalStat(boolean isPlayer, int itemID, Formulas.formula f) {
        Item item = selectItem(itemID);
        // only thing that changes is the unit argument positions
        if (isPlayer) {
            return parseFormula(f, player, enemy, item);
        } else {
            return parseFormula(f, enemy, player, item);
        }
    }

    public long getTotalAttack(boolean isPlayer, int itemID) {
        // losing precision when casting to long
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalAttack);
    }

    public double getTotalAttackSpeed(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalAttackSpeed);
    }

    public double getTotalCritChance(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalCritChance);
    }

    public long getTotalHP(boolean isPlayer, int itemID) {
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalHP);
    }

    public long getTotalDefense(boolean isPlayer, int itemID) {
        return (long) getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.totalDefense);
    }

    public double getDefenseMultiplier(boolean isPlayer, int itemID) {
        return getTotalStat(isPlayer, itemID, CalculatorUtil.formulas.defenseMultiplier);
    }

    // ========== Handling Items ==========
    private Item selectItem(int ID) {
        switch (ID) {
            case 0 -> {return item1;}
            case 1 -> {return item2;}
            case 2 -> {return item3;}
        }
        System.out.printf("Couldn't find the specified item ID: %s, exiting.\n", ID);
        System.exit(0);
        return null;
    }

    public void setItemAttack(int ID, long attack) {
        Item item = selectItem(ID);
        item.setBonusAttack(attack);
    }

    public long getItemAttack(int ID) {
        Item item = selectItem(ID);
        return item.getBonusAttack();
    }

    public void setItemHP(int ID, long HP) {
        Item item = selectItem(ID);
        item.setBonusHP(HP);
    }

    public long getItemHP(int ID) {
        Item item = selectItem(ID);
        return item.getBonusHP();
    }

    public void setItemDefense(int ID, long defense) {
        Item item = selectItem(ID);
        item.setBonusDefense(defense);
    }

    public long getItemDefense(int ID) {
        Item item = selectItem(ID);
        return item.getBonusDefense();
    }

    public void setItemAttackSpeed(int ID, double attackSpeed) {
        Item item = selectItem(ID);
        item.setBonusAttackSpeed(attackSpeed);
    }

    public double getItemAttackSpeed(int ID) {
        Item item = selectItem(ID);
        return item.getBonusAttackSpeed();
    }

    public void setItemCritChance(int ID, double critChance) {
        Item item = selectItem(ID);
        item.setBonusCriticalHitChance(critChance);
    }

    public double getItemCritChance(int ID) {
        Item item = selectItem(ID);
        return item.getBonusCriticalHitChance();
    }

    // ========== Handling Actors ==========

    // Enemy :

    public void setEnemyHP(long HP) { enemy.setHP(HP); }
    public long getEnemyHP() { return enemy.getHP(); }
    public void setEnemyDefense(long defense) { enemy.setDefense(defense); }
    public long getEnemyDefense() { return enemy.getDefense(); }
    public void setEnemyAttack(long attack) { enemy.setAttack(attack); }
    public long getEnemyAttack() { return enemy.getAttack(); }
    public void setEnemyAttackSpeed(double attackSpeed) { enemy.setAttackSpeed(attackSpeed); }
    public double getEnemyAttackSpeed() { return enemy.getAttackSpeed(); }
    public void setEnemyCritChance(double critChance) { enemy.setCriticalHitChance(critChance); }
    public double getEnemyCritChance() { return enemy.getCriticalHitChance(); }

    // Player :

    public void setPlayerHP(long HP) { player.setHP(HP); }
    public long getPlayerHP() { return player.getHP(); }
    public void setPlayerDefense(long defense) { player.setDefense(defense); }
    public long getPlayerDefense() { return player.getDefense(); }
    public void setPlayerAttack(long attack) { player.setAttack(attack); }
    public long getPlayerAttack() { return player.getAttack(); }
    public void setPlayerAttackSpeed(double attackSpeed) { player.setAttackSpeed(attackSpeed); }
    public double getPlayerAttackSpeed() { return player.getAttackSpeed(); }
    public void setPlayerCritChance(double critChance) { player.setCriticalHitChance(critChance); }
    public double getPlayerCritChance() { return player.getCriticalHitChance(); }

    // ========== Formulas ==========

    /** Allows to map formula arguments to the correct variables. */
    private Queue<Double> mapFormulaArgs(String[] args, Actor unit, Actor enemy, Item item) {
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
    private double parseFormula(Formulas.formula f, Actor unit, Actor enemy, Item item) {
        Queue<Double> args = mapFormulaArgs(f.getRequirements(), unit, enemy, item);
        Queue<String> text = new ArrayDeque<>(List.of(f.getText().split("\\s+")));
        return parseRecFormula(text, args);
    }

    /** Helper method to recursively apply a formula. */
    private double parseRecFormula(Queue<String> formula, Queue<Double> args) {
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
