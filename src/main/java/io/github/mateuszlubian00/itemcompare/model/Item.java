package io.github.mateuszlubian00.itemcompare.model;

import javafx.beans.property.*;

public class Item {

    private IntegerProperty ID;
    private LongProperty bonusHP;
    private LongProperty bonusDefense;
    private LongProperty bonusAttack;
    private DoubleProperty bonusAttackSpeed;
    private DoubleProperty bonusCriticalHitChance;

    public Item() {
        this.ID = new SimpleIntegerProperty();
        this.bonusHP = new SimpleLongProperty();
        this.bonusDefense = new SimpleLongProperty();
        this.bonusAttack = new SimpleLongProperty();
        this.bonusAttackSpeed = new SimpleDoubleProperty();
        this.bonusCriticalHitChance = new SimpleDoubleProperty();
    }

    public int getID() {
        return ID.get();
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public long getBonusHP() {
        return bonusHP.get();
    }

    public void setBonusHP(long bonusHP) {
        this.bonusHP.set(bonusHP);
    }

    public long getBonusDefense() {
        return bonusDefense.get();
    }

    public void setBonusDefense(long bonusDefense) {
        this.bonusDefense.set(bonusDefense);
    }

    public long getBonusAttack() {
        return bonusAttack.get();
    }

    public void setBonusAttack(long bonusAttack) {
        this.bonusAttack.set(bonusAttack);
    }

    public double getBonusAttackSpeed() {
        return bonusAttackSpeed.get();
    }

    public void setBonusAttackSpeed(double bonusAttackSpeed) {
        this.bonusAttackSpeed.set(bonusAttackSpeed);
    }

    public double getBonusCriticalHitChance() {
        return bonusCriticalHitChance.get();
    }

    public void setBonusCriticalHitChance(double bonusCriticalHitChance) {
        this.bonusCriticalHitChance.set(bonusCriticalHitChance);
    }
}
