package io.github.mateuszlubian00.itemcompare.util;

import io.github.mateuszlubian00.itemcompare.model.Actor;
import io.github.mateuszlubian00.itemcompare.model.Item;
import io.github.mateuszlubian00.itemcompare.model.ItemAccess;

import java.sql.SQLException;
import java.util.function.Function;

public class CalculatorUtil {

    public static Function<Actor, Actor> calculateWithItem(Integer itemID){
        try {
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
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while accessing item " + itemID);
            System.exit(0);
        }

        // Useless return
        return null;
    }
}
