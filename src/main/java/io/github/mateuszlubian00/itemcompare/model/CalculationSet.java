package io.github.mateuszlubian00.itemcompare.model;

public class CalculationSet {

    private Actor player;
    private Actor enemy;
    private Item item;
    private Actor originalPlayer;
    private Actor originalEnemy;
    private Item originalItem;


    public CalculationSet(Actor player, Actor enemy, Item item) {
        this.player = player;
        this.originalPlayer = player;
        this.enemy = enemy;
        this.originalEnemy = enemy;
        this.item = item;
        this.originalItem = item;
    }

    public Double nextAttack() {
        return 0D;
    }

    public Double nextAttackTime() {
        return 0D;
    }

    public Double nextCritChance() {
        return 0D;
    }

    public Double nextCritDamage() {
        return 0D;
    }

    public Double nextDefense() {
        return 0D;
    }

    public void reset() {
        player = originalPlayer;
        enemy = originalEnemy;
        item = originalItem;
    }
}
