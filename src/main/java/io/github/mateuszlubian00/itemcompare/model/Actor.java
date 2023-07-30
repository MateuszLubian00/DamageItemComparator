package io.github.mateuszlubian00.itemcompare.model;

import javafx.beans.property.*;

public class Actor {

    private IntegerProperty ID;
    private LongProperty HP;
    private LongProperty Defense;
    private LongProperty Attack;
    private DoubleProperty AttackSpeed;
    private DoubleProperty CriticalHitChance;

    public Actor() {
        this.ID = new SimpleIntegerProperty();
        this.HP = new SimpleLongProperty();
        this.Defense = new SimpleLongProperty();
        this.Attack = new SimpleLongProperty();
        this.AttackSpeed = new SimpleDoubleProperty();
        this.CriticalHitChance = new SimpleDoubleProperty();
    }

    public int getID() {
        return ID.get();
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public long getHP() {
        return HP.get();
    }

    public void setHP(long HP) {
        this.HP.set(HP);
    }

    public long getDefense() {
        return Defense.get();
    }

    public void setDefense(long defense) {
        this.Defense.set(defense);
    }

    public long getAttack() {
        return Attack.get();
    }

    public void setAttack(long attack) {
        this.Attack.set(attack);
    }

    public Double getAttackSpeed() {
        return AttackSpeed.get();
    }

    public void setAttackSpeed(double attackSpeed) {
        this.AttackSpeed.set(attackSpeed);
    }

    public Double getCriticalHitChance() {
        return CriticalHitChance.get();
    }

    public void setCriticalHitChance(Double criticalHitChance) {
        this.CriticalHitChance.set(criticalHitChance);
    }
}
