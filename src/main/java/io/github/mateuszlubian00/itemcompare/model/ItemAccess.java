package io.github.mateuszlubian00.itemcompare.model;

import io.github.mateuszlubian00.itemcompare.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemAccess {
    public static Item selectItem (int ID) throws SQLException {
        String selectStatement = "SELECT * FROM items WHERE ID="+ID;
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getItemFromResultSet(resultSet);
    }

    private static Item getItemFromResultSet(ResultSet resultSet) throws SQLException
    {
        Item item = null;
        if (resultSet.next()) {
            item = new Item();
            item.setBonusAttack(resultSet.getLong("BONUS_ATTACK"));
            item.setBonusAttackSpeed(resultSet.getDouble("BONUS_ATTACK_SPEED"));
            item.setBonusHP(resultSet.getLong("BONUS_HP"));
            item.setBonusDefense(resultSet.getLong("BONUS_DEFENSE"));
            item.setBonusCriticalHitChance(resultSet.getDouble("BONUS_CRITICAL_HIT_CHANCE"));
        }
        return item;
    }

    public static ObservableList<Item> selectManyItems () throws SQLException {
        String selectStatement = "SELECT * FROM items";
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getItemList(resultSet);
    }

    private static ObservableList<Item> getItemList(ResultSet resultSet) throws SQLException {
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Item item = new Item();

            item.setBonusAttack(resultSet.getLong("BONUS_ATTACK"));
            item.setBonusAttackSpeed(resultSet.getDouble("BONUS_ATTACK_SPEED"));
            item.setBonusHP(resultSet.getLong("BONUS_HP"));
            item.setBonusDefense(resultSet.getLong("BONUS_DEFENSE"));
            item.setBonusCriticalHitChance(resultSet.getDouble("BONUS_CRITICAL_HIT_CHANCE"));
            itemList.add(item);
        }
        return itemList;
    }

    public static void updateItemField (int ID, String field, Object value) throws SQLException {
        String updateStatement =
                "UPDATE items\n" +
                        "   SET " + field + " = " + value + "\n" +
                        "   WHERE ID = " + ID;

        DBUtil.dbExecuteUpdate(updateStatement);
    }

    public static void insertItem (int ID, Long bonusHP, Long bonusDefense, Long bonusAttack, Double bonusAttackSpeed, double bonusCritChance) throws SQLException {
        String updateStmt =
                "INSERT INTO items\n" +
                        "(ID, BONUS_HP, BONUS_DEFENSE, BONUS_ATTACK, BONUS_ATTACK_SPEED, BONUS_CRITICAL_HIT_CHANCE)\n" +
                        "VALUES\n" +
                        "("+ID+", "+bonusHP+", "+bonusDefense+", "+bonusAttack+", "+bonusAttackSpeed+", "+bonusCritChance+")\n";
        DBUtil.dbExecuteUpdate(updateStmt);
    }
}
