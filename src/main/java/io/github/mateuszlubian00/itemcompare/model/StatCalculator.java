package io.github.mateuszlubian00.itemcompare.model;

public class StatCalculator {

    private Actor player;
    private Actor enemy;
    private Item item1;
    private Item item2;
    private Item item3;
    private Actor originalPlayer;
    private Actor originalEnemy;
    private Item originalItem1;
    private Item originalItem2;
    private Item originalItem3;


    public StatCalculator(Actor player, Actor enemy, Item item1, Item item2, Item item3) {
        this.player = player;
        this.originalPlayer = player;
        this.enemy = enemy;
        this.originalEnemy = enemy;
        this.item1 = item1;
        this.originalItem1 = item1;
        this.item2 = item2;
        this.originalItem2 = item2;
        this.item3 = item3;
        this.originalItem3 = item3;
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
        item1 = originalItem1;
        item2 = originalItem2;
        item3 = originalItem3;
    }

    // ========== Returning Total Stats ==========

    public long getPlayerTotalAttack(int itemID) {
        Item item = selectItem(itemID);
        return player.getAttack() + item.getBonusAttack();
    }

    public double getPlayerTotalAttackSpeed(int itemID) {
        Item item = selectItem(itemID);
        return player.getAttackSpeed() + item.getBonusAttackSpeed();
    }

    public double getPlayerTotalCritChance(int itemID) {
        Item item = selectItem(itemID);
        return player.getCriticalHitChance() + item.getBonusCriticalHitChance();
    }

    public long getEnemyTotalHP() {
        return enemy.getHP() + item3.getBonusHP();
    }

    public long getEnemyTotalDefense() {
        return enemy.getDefense() + item3.getBonusDefense();
    }

    // ========== Handling Items ==========
    private Item selectItem(int ID) {
        switch (ID) {
            case 0 -> {return item1;}
            case 1 -> {return item2;}
            case 2 -> {return item3;}
        }
        System.out.println("Couldn't not find the specified item, exiting.");
        System.exit(0);
        return null;
    }

    public void setItemAttack(int ID, long attack) {
        Item item = selectItem(ID);
        item.setBonusAttack(attack);
    }

    public long getItemAttack(int ID) {
        Item item = selectItem(ID);
        return item.getBonusAttack();
    }

    public void setItemHP(int ID, long HP) {
        Item item = selectItem(ID);
        item.setBonusHP(HP);
    }

    public long getItemHP(int ID) {
        Item item = selectItem(ID);
        return item.getBonusHP();
    }

    public void setItemDefense(int ID, long defense) {
        Item item = selectItem(ID);
        item.setBonusDefense(defense);
    }

    public long getItemDefense(int ID) {
        Item item = selectItem(ID);
        return item.getBonusDefense();
    }

    public void setItemAttackSpeed(int ID, double attackSpeed) {
        Item item = selectItem(ID);
        item.setBonusAttackSpeed(attackSpeed);
    }

    public double getItemAttackSpeed(int ID) {
        Item item = selectItem(ID);
        return item.getBonusAttackSpeed();
    }

    public void setItemCritChance(int ID, double critChance) {
        Item item = selectItem(ID);
        item.setBonusCriticalHitChance(critChance);
    }

    public double getItemCritChance(int ID) {
        Item item = selectItem(ID);
        return item.getBonusCriticalHitChance();
    }

    // ========== Handling Actors ==========

    // Enemy :

    public void setEnemyHP(long HP) { enemy.setHP(HP); }
    public long getEnemyHP() { return enemy.getHP(); }
    public void setEnemyDefense(long defense) { enemy.setDefense(defense); }
    public long getEnemyDefense() { return enemy.getDefense(); }
    public void setEnemyAttack(long attack) { enemy.setAttack(attack); }
    public long getEnemyAttack() { return enemy.getAttack(); }
    public void setEnemyAttackSpeed(double attackSpeed) { enemy.setAttackSpeed(attackSpeed); }
    public double getEnemyAttackSpeed() { return enemy.getAttackSpeed(); }
    public void setEnemyCritChance(double critChance) { enemy.setCriticalHitChance(critChance); }
    public double getEnemyCritChance() { return enemy.getCriticalHitChance(); }

    // Player :

    public void setPlayerHP(long HP) { player.setHP(HP); }
    public long getPlayerHP() { return player.getHP(); }
    public void setPlayerDefense(long defense) { player.setDefense(defense); }
    public long getPlayerDefense() { return player.getDefense(); }
    public void setPlayerAttack(long attack) { player.setAttack(attack); }
    public long getPlayerAttack() { return player.getAttack(); }
    public void setPlayerAttackSpeed(double attackSpeed) { player.setAttackSpeed(attackSpeed); }
    public double getPlayerAttackSpeed() { return player.getAttackSpeed(); }
    public void setPlayerCritChance(double critChance) { player.setCriticalHitChance(critChance); }
    public double getPlayerCritChance() { return player.getCriticalHitChance(); }
}
