package org.telegram.characters;

public class Enemy extends Сharacter {
    private String type;

    public Enemy(String name, String type) {
        super(name);
        this.type = type;
        strength = 8;
        intelligence = 5;
        agility = 7;
        vitality = 12;

        riseUpdate();
    }

    private void riseUpdate() {
        maxHealthPoints = vitality * 20;
        maxManaPoints = intelligence * 10;
        currentHealthPoints = maxHealthPoints;
        currentManaPoints = maxManaPoints;
    }

    public String getType() {
        return type;
    }

    public void attack(Person player) {
        int damage = this.strength - player.getDefense();
        if (damage > 0) {
            player.takeDamage(damage);
        }
    }

    public void takeDamage(int damage) {
        this.currentHealthPoints -= damage;
        if (this.currentHealthPoints < 0) {
            this.currentHealthPoints = 0;
        }
    }

    public int getDefense() {
        return this.agility;
    }
}
