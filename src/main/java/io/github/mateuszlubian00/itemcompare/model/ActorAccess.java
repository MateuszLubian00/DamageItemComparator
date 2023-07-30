package io.github.mateuszlubian00.itemcompare.model;

import io.github.mateuszlubian00.itemcompare.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActorAccess {

    public static Actor selectActor (int ID) throws SQLException {
        String selectStatement = "SELECT * FROM actors WHERE ID="+ID;
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getActorFromResultSet(resultSet);
    }

    private static Actor getActorFromResultSet(ResultSet resultSet) throws SQLException
    {
        Actor actor = null;
        if (resultSet.next()) {
            actor = new Actor();
            actor.setAttack(resultSet.getLong("ATTACK"));
            actor.setAttackSpeed(resultSet.getDouble("ATTACK_SPEED"));
            actor.setHP(resultSet.getLong("HP"));
            actor.setDefense(resultSet.getLong("DEFENSE"));
            actor.setCriticalHitChance(resultSet.getDouble("CRITICAL_HIT_CHANCE"));
        }
        return actor;
    }

    public static ObservableList<Actor> selectManyActors () throws SQLException {
        String selectStatement = "SELECT * FROM actors";
        ResultSet resultSet = DBUtil.dbExecuteQuery(selectStatement);
        return getActorList(resultSet);
    }

    private static ObservableList<Actor> getActorList(ResultSet resultSet) throws SQLException {
        ObservableList<Actor> actorList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Actor actor = new Actor();
            actor.setAttack(resultSet.getLong("ATTACK"));
            actor.setAttackSpeed(resultSet.getDouble("ATTACK_SPEED"));
            actor.setHP(resultSet.getLong("HP"));
            actor.setDefense(resultSet.getLong("DEFENSE"));
            actor.setCriticalHitChance(resultSet.getDouble("CRITICAL_HIT_CHANCE"));
            actorList.add(actor);
        }
        return actorList;
    }

    public static void updateActorField (int ID, String field, Object value) throws SQLException {
        String updateStatement =
                        "UPDATE actors\n" +
                        "   SET " + field + " = " + value + "\n" +
                        "   WHERE ID = " + ID;
        DBUtil.dbExecuteUpdate(updateStatement);
    }

    public static void insertActor (int ID, Long HP, Long Defense, Long Attack, Double AttackSpeed, double CritChance) throws SQLException {
        String updateStmt =
                        "INSERT INTO actors\n" +
                        "(ID, HP, DEFENSE, ATTACK, ATTACK_SPEED, CRITICAL_HIT_CHANCE)\n" +
                        "VALUES\n" +
                        "("+ID+", "+HP+", "+Defense+", "+Attack+", "+AttackSpeed+", "+CritChance+")\n";
        DBUtil.dbExecuteUpdate(updateStmt);
    }
}
