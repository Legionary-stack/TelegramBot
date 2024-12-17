package org.smyta.characters;

import org.jetbrains.annotations.NotNull;
import org.smyta.DataBase;
import org.smyta.enums.BotState;
import org.smyta.enums.KeyboardState;

import java.util.Map;
import java.util.Random;

public class Person extends Сharacter {
    private long chatId;
    private int skillPoints;
    private int currentExperiencePoints;
    private int maxExperiencePoints;
    private String picture;
    private String playClass;
    private final byte growFactor = 10;

    public Person(String name, long chatId, String picture, String playClass) {
        super(name);
        this.chatId = chatId;
        this.picture = picture;
        this.playClass = playClass;
        strength = 15;
        intelligence = 15;
        agility = 15;
        vitality = 10;
        skillPoints = 3;
        currentExperiencePoints = 0;
        riseUpdate();
    }

    private void riseUpdate() {
        maxHealthPoints = vitality * growFactor;
        maxManaPoints = intelligence * growFactor;
        currentHealthPoints = maxHealthPoints;
        currentManaPoints = maxManaPoints;
        maxExperiencePoints = calculateMaxExperiencePoints();
    }

    private int calculateMaxExperiencePoints() {
        return (int) (1000 * Math.pow(1.5, level - 1));
    }

    public void revive() {
        //TODO нужна другая реализация а вообще она вовсе не нужна
        riseUpdate();

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
        return Math.min(0.5, agility / 150.0);
    }

    public double calculateCriticalDamage(double baseDamage) {
        // Урон критического удара
        return baseDamage * (1 + (agility * (1.5 + (double) level / 10)) / 100.0);
    }

    public int getDamage(@NotNull Enemy opponent) {
        Random random = new Random();
        double criticalChance = calculateCriticalHitChance();
        boolean isCriticalHit = random.nextDouble() < criticalChance;

        double missChance = Math.min(0.4, (double) (agility) / (10 * level)
                - opponent.getAgility() / (20.0 * opponent.level));

        double randomValue = random.nextDouble();
        System.out.println(missChance);
        System.out.println(randomValue);
        boolean isHit = randomValue > missChance;

        int baseDamage = (int) (strength * (Math.exp((double) -opponent.getDefense() /
                (level * 4 + 225)) + 0.01 * level));

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
        return (int) ((this.agility + this.vitality) / 1.8);
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

    public boolean riseStat(String type) {
        if (skillPoints > 0)
            skillPoints -= 1;
        else
            return false;

        switch (type) {
            case "strength":
                setStrength(getStrength() + 1);
                break;
            case "intelligence":
                setIntelligence(getIntelligence() + 1);
                riseUpdate();
                break;
            case "agility":
                setAgility(getAgility() + 1);
                break;
            case "vitality":
                setVitality(getVitality() + 1);
                riseUpdate();
                break;
        }
        return true;
    }

    public void loadFromDatabase(@NotNull DataBase db, long chatId) {
        //TODO
        //По возможности изменить и добавить новые слоты

        Map<String, Object> userInfo = db.userStatsGetInfo(chatId);
        if (userInfo != null) {
            this.setChatId(chatId);
            this.setName((String) userInfo.get("userName"));
            this.setLevel((int) userInfo.get("level"));
            this.setCurrentHealthPoints((int) userInfo.get("currentHealthPoints"));
            this.setCurrentManaPoints((int) userInfo.get("currentManaPoints"));
            this.setCurrentExperiencePoints((int) userInfo.get("currentExpPoints"));
            this.setStrength((int) userInfo.get("strength"));
            this.setIntelligence((int) userInfo.get("intelligence"));
            this.setAgility((int) userInfo.get("agility"));
            this.setVitality((int) userInfo.get("vitality"));
            this.setPlayClass((String) userInfo.get("class"));
            this.setPicture((String) userInfo.get("pic"));
            this.setSkillPoints((int) userInfo.get("skillPoints"));
            maxHealthPoints = vitality * growFactor;
            maxManaPoints = intelligence * growFactor;
            maxExperiencePoints = calculateMaxExperiencePoints();
        }
    }

    public void saveToDatabase(@NotNull DataBase db, long chatId, KeyboardState keyboardState, BotState botState) {
        db.userStatsPostFull(chatId, true, getName(), getLevel(), getCurrentHealthPoints(),
                getCurrentManaPoints(), getCurrentExperiencePoints(), getStrength(), getIntelligence(),
                getAgility(), getVitality(), getPlayClass(), getPicture(), keyboardState, botState,
                skillPoints);
    }


    public String getPlayClass() {
        return playClass;
    }

    public void setPlayClass(String playClass) {
        this.playClass = playClass;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

