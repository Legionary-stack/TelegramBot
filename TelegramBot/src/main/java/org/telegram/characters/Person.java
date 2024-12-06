package org.telegram.characters;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Person extends Сharacter {
    private long chatId;
    private int skillPoints;
    private int currentExperiencePoints;
    private int maxExperiencePoints;


    public Person(String name, long chatId) {
        super(name);
        this.chatId = chatId;
        strength = 10;
        intelligence = 10;
        agility = 10;
        vitality = 10;
        skillPoints = 0;
        currentExperiencePoints = 0;
        riseUpdate();
    }

    private void riseUpdate() {
        maxHealthPoints = vitality * 20;
        maxManaPoints = intelligence * 10;
        currentHealthPoints = maxHealthPoints;
        currentManaPoints = maxManaPoints;
        maxExperiencePoints = level * 1000;
    }


    private void levelUpdate() {
        level += 1;
        strength += 1;
        intelligence += 1;
        agility += 1;
        vitality += 1;
        skillPoints += 3;
        riseUpdate();
    }

    public boolean expUpdate() {
        boolean isLevelUpdate = false;
        while (currentExperiencePoints >= maxExperiencePoints) {
            currentExperiencePoints -= maxExperiencePoints;
            isLevelUpdate = true;
            levelUpdate();
        }
        return isLevelUpdate;
    }

    public void riseVitality(int count) {
        vitality += count;
        riseUpdate();
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public double calculateCriticalHitChance() {
        return agility / 100.0;
    }

    public double calculateCriticalDamage(double baseDamage) {
        // Урон критического удара
        return baseDamage * (1 + (agility * 1.5) / 100.0);
    }

    public int getDamage(@NotNull Enemy opponent) {
        Random random = new Random();
        double criticalChance = calculateCriticalHitChance();
        boolean isCriticalHit = random.nextDouble() < criticalChance;

        double missChance = (double) 100 / (this.agility * 3);
        double randomValue = random.nextDouble();
        boolean isHit = randomValue > missChance;

        int baseDamage = (int) (this.strength * (Math.exp((double) -opponent.getDefense() /
                (this.level * 4 + 225)) + 0.01 * this.level));

        return isHit ? (isCriticalHit ? (int)
                calculateCriticalDamage(baseDamage) : baseDamage) : 0;
    }

    public void takeDamage(int damage) {
        this.currentHealthPoints -= damage;
        if (this.currentHealthPoints < 0) {
            this.currentHealthPoints = 0;
        }
    }

    public int getDefense() {
        return this.agility; // Example: using agility as defense
    }


    public int getCurrentExperiencePoints() {
        return currentExperiencePoints;
    }

    public void setCurrentExperiencePoints(int currentExperiencePoints) {
        this.currentExperiencePoints = currentExperiencePoints;
    }

    public int getMaxExperiencePoints() {
        return maxExperiencePoints;
    }

    public void setMaxExperiencePoints(int maxExperiencePoints) {
        this.maxExperiencePoints = maxExperiencePoints;
    }
}

