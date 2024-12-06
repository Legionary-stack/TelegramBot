package org.telegram.characters;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Enemy extends Сharacter {
    private final String type;

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

    public int getDamage(@NotNull Person player) {
        double missChance = (double) 100 / (this.agility * 2);
        Random random = new Random();
        double randomValue = random.nextDouble();
        boolean isHit = randomValue > missChance;

        return isHit ? (int) (this.strength * (Math.exp((double) -player.getDefense() /
                (this.level * 4 + 225)) + 0.01 * this.level)) : 0;
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
