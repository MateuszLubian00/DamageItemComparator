package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.ComparatorApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelpController {

    @FXML
    protected Label text;
    @FXML
    protected Label examples;

    @FXML
    private void initialize() {
        text.setText("""
                Tips for making a proper custom formula are as follows:
                    1. Each symbol, number or reference must have a space in between.
                    2. All operations are performed left to right.
                    3. To change order of operations, use brackets.

                List of possible references and their modifiers can be found here:""");
        examples.setText("""
                Examples of different correct formulas:
                    1. "atk + i_atk" - formula that adds base attack value and item attack value.
                    2. "100 / ( atk - e_def )" - formula that divides number 100 by base attack value minus enemy base defense value.
                    3. "100" - formula that completely disregards any variables.
                    4. "( ( atk * crit ) * ( e_hp / e_def ) ) - i_atk" - formula with nested brackets.
                    5. "10.128 * crit" - use of decimals is permitted.
                    
                Examples of incorrect formulas:
                    1. "atk+i_atk" - this formula contains no spaces, and as such will be treated as a single (invalid) reference.
                    2. "( atk - ( e_def )" - one of the brackets is not closed.
                    3. ") ( 100 / 2" - the bracket is malformed.
                    4. "hp - defense" - invalid reference name 'defense'.
                    5. "atk - ( e_hp * f_def )" - invalid modifier 'f_'.
                    6. "crit - e_i_crit)" - using multiple modifiers at once is not allowed.
                """);
    }

    @FXML
    public void closeHelp(ActionEvent event) {
        ComparatorApplication.closeHelp();
    }
}
