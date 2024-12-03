package org.telegram.characters;

public class Person extends Сharacter {
    private long chatId;
    private int skillPoints;

    private void riseUpdate() {
        maxHealthPoints = vitality * 20;
        maxManaPoints = intelligence * 10;
        currentHealthPoints = maxHealthPoints;
        currentManaPoints = maxManaPoints;
    }

    public Person(String name, long chatId) {
        super(name);
        this.chatId = chatId;
        strength = 10;
        intelligence = 10;
        agility = 10;
        vitality = 10;
        skillPoints = 0;

        riseUpdate();
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

    public void attack(Person opponent) {
        int damage = this.strength - opponent.getDefense();
        if (damage > 0) {
            opponent.takeDamage(damage);
        }
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


}

