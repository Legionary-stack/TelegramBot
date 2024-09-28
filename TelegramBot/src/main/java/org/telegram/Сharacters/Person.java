package org.telegram.Сharacters;

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


}

