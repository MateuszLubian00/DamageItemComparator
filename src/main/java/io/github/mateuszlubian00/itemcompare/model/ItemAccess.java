package io.github.mateuszlubian00.itemcompare.model;

import io.github.mateuszlubian00.itemcompare.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemAccess {
    public static Item selectItem (int ID) {
        String selectStatement = "SELECT * FROM items WHERE ID="+ID;
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getItemFromResultSet(resultSet);
    }

    private static Item getItemFromResultSet(ResultSet resultSet) {
        Item item = null;
        try {
            if (resultSet.next()) {
                item = new Item();
                item.setBonusAttack(resultSet.getLong("BONUS_ATTACK"));
                item.setBonusAttackSpeed(resultSet.getDouble("BONUS_ATTACK_SPEED"));
                item.setBonusHP(resultSet.getLong("BONUS_HP"));
                item.setBonusDefense(resultSet.getLong("BONUS_DEFENSE"));
                item.setBonusCriticalHitChance(resultSet.getDouble("BONUS_CRITICAL_HIT_CHANCE"));
            }
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while accessing result set " + resultSet);
            System.exit(0);
        }
        return item;
    }

    public static ObservableList<Item> selectManyItems () {
        String selectStatement = "SELECT * FROM items";
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getItemList(resultSet);
    }

    private static ObservableList<Item> getItemList(ResultSet resultSet) {
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Item item = new Item();
                item.setBonusAttack(resultSet.getLong("BONUS_ATTACK"));
                item.setBonusAttackSpeed(resultSet.getDouble("BONUS_ATTACK_SPEED"));
                item.setBonusHP(resultSet.getLong("BONUS_HP"));
                item.setBonusDefense(resultSet.getLong("BONUS_DEFENSE"));
                item.setBonusCriticalHitChance(resultSet.getDouble("BONUS_CRITICAL_HIT_CHANCE"));
                itemList.add(item);
            }
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while accessing result set " + resultSet);
            System.exit(0);
        }
        return itemList;
    }

    public static void updateItemField (int ID, String field, Object value) {
        String updateStatement =
                "UPDATE items\n" +
                        "   SET " + field + " = " + value + "\n" +
                        "   WHERE ID = " + ID;

        DBUtil.dbExecuteUpdate(updateStatement);
    }

    public static void insertItem (int ID, Long bonusHP, Long bonusDefense, Long bonusAttack, Double bonusAttackSpeed, double bonusCritChance) {
        String updateStmt =
                "INSERT INTO items\n" +
                        "(ID, BONUS_HP, BONUS_DEFENSE, BONUS_ATTACK, BONUS_ATTACK_SPEED, BONUS_CRITICAL_HIT_CHANCE)\n" +
                        "VALUES\n" +
                        "("+ID+", "+bonusHP+", "+bonusDefense+", "+bonusAttack+", "+bonusAttackSpeed+", "+bonusCritChance+")\n";
        DBUtil.dbExecuteUpdate(updateStmt);
    }
}
