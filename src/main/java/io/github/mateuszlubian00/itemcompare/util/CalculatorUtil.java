package io.github.mateuszlubian00.itemcompare.util;

import io.github.mateuszlubian00.itemcompare.model.*;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CalculatorUtil {

    /** Stored calculator class for any sort of calculations. */
    public static StatCalculator calculator = null;
    /** Stored Formulas class to change and use any custom formulas */
    public static Formulas formulas = null;
    /** Helper variable to quickly assess if a String is an allowed operation.
     *  Used in setNewFormula method.
     */
    private static final HashSet<String> allowedOps = new HashSet<>(List.of("+", "-", "*", "/", "(", ")"));
    /** Same as above, but for number references
     */
    private static final HashSet<String> allowedNums = new HashSet<>(List.of("atk", "atkspd", "crit", "hp", "def", "i_atk", "i_atkspd", "i_crit", "i_hp", "i_def", "e_atk", "e_atkspd", "e_crit", "e_hp", "e_def", "t_atk", "t_atkspd", "t_crit", "t_hp", "t_def"));

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

    /** Method to parse user inputted formula and store it.
     *  Returns true if the formula was proper.
     */
    public static boolean setNewFormula(TextInputControl input, Formulas.formula target) {
        ArrayList<String> arguments = new ArrayList<>();
        StringBuilder formula = new StringBuilder();
        List<String> split = List.of(input.getText().toLowerCase().split("\\s+"));
        // number of expected numbers or references
        int expected = 1;
        // tracking brackets
        int brackets = 0;

        input.getStyleClass().remove("invalid");

        for (String s : split) {
            if (allowedOps.contains(s)) {
                // It's an operation
                formula.append(s).append(" ");
                if (s.equals(")")) {
                    expected--;
                    brackets--;
                } else {
                    expected++;
                    // petty way to refuse opening a closed bracket, ex. ") ("
                    if (s.equals("(") && brackets >= 0) {
                        brackets++;
                    }
                }
            } else {
                boolean isNumber = true;

                try {
                    // It's either a number or error
                    Double __ = Double.parseDouble(s);
                }
                catch (NumberFormatException e) {
                    isNumber = false;
                }

                if (allowedNums.contains(s) || isNumber) {
                    // It's a reference
                    formula.append("%s ");
                    arguments.add(s);
                    expected--;
                } else {
                    input.getStyleClass().add("invalid");
                    return false;
                }
            }
        }

        if (expected != 0 || brackets != 0) {
            input.getStyleClass().add("invalid");
            return false;
        }

        target.setText(formula.toString());
        target.setRequirements(arguments.toArray(new String[0]));

        return true;
    }
}
