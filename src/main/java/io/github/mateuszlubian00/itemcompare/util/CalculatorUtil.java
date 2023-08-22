package io.github.mateuszlubian00.itemcompare.util;

import io.github.mateuszlubian00.itemcompare.model.*;
import javafx.scene.control.TextInputControl;

import java.util.function.Function;

public class CalculatorUtil {

    /** Stored calculator class for any sort of calculations. */
    public static StatCalculator calculator = null;
    /** Stored Formulas class to change and use any custom formulas */
    public static Formulas formulas = null;

    /** Creates a function that applies item statistics to an Actor, returning a new Actor. */
    public static Function<Actor, Actor> calculateWithItem(Integer itemID){
        final Item item = ItemAccess.selectItem(itemID);
        return (actor -> {
            Actor result = new Actor();

            result.setHP(actor.getHP() + item.getBonusHP());
            result.setDefense(actor.getDefense() + item.getBonusDefense());
            result.setAttack(actor.getAttack() + item.getBonusAttack());
            result.setAttackSpeed(actor.getAttackSpeed() + item.getBonusAttackSpeed());
            result.setCriticalHitChance(actor.getCriticalHitChance() + item.getBonusCriticalHitChance());

            return result;
        });
    }

    /** Helper methods to try to gather numbers from input fields.
     *  Also manages css "invalid" class.
     */
    public static Long updateFromText(TextInputControl source, Long target) {
        source.getStyleClass().remove("invalid");
        try {
            target = Long.parseLong(source.getText());
        } catch (NumberFormatException e) {
            source.getStyleClass().add("invalid");
            return null;
        }
        return target;
    }
    public static Double updateFromText(TextInputControl source, Double target) {
        source.getStyleClass().remove("invalid");
        try {
            target = Double.parseDouble(source.getText());
        } catch (NumberFormatException e) {
            source.getStyleClass().add("invalid");
            return null;
        }
        return target;
    }
    public static Integer updateFromText(TextInputControl source, Integer target) {
        source.getStyleClass().remove("invalid");
        try {
            target = Integer.parseInt(source.getText());
        } catch (NumberFormatException e) {
            source.getStyleClass().add("invalid");
            return null;
        }
        return target;
    }
}
