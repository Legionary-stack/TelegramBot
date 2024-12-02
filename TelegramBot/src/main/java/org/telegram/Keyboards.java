package org.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Keyboards {

    public void setMainGameMenu(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Арена"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Инвентарь"));
        keyboardSecondRow.add(new KeyboardButton("Магазин"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("О себе"));
        keyboardThirdRow.add(new KeyboardButton("Медетация"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public InlineKeyboardMarkup getInlineInventory() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton weapon = new InlineKeyboardButton();
        weapon.setText("Оружие");
        weapon.setCallbackData("weapon");

        InlineKeyboardButton eat = new InlineKeyboardButton();
        eat.setText("Поесть");
        eat.setCallbackData("eat");

        InlineKeyboardButton improve = new InlineKeyboardButton();
        improve.setText("Прокачка");
        improve.setCallbackData("improve");

        buttons1.add(weapon);
        buttons1.add(eat);
        buttons1.add(improve);

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton goBack = new InlineKeyboardButton();
        goBack.setText("Назад");
        goBack.setCallbackData("go_back");

        buttons2.add(goBack);
        buttons.add(buttons1);
        buttons.add(buttons2);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public InlineKeyboardMarkup getInlineAboutMyself() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton stats = new InlineKeyboardButton();
        stats.setText("Статы");
        stats.setCallbackData("stats");

        InlineKeyboardButton achievements = new InlineKeyboardButton();
        achievements.setText("Достижения");
        achievements.setCallbackData("achievements");

        InlineKeyboardButton registrationDate = new InlineKeyboardButton();
        registrationDate.setText("Дата регистрации");
        registrationDate.setCallbackData("registration_date");

        buttons1.add(stats);
        buttons1.add(achievements);
        buttons1.add(registrationDate);

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton goBack = new InlineKeyboardButton();
        goBack.setText("Назад");
        goBack.setCallbackData("go_back");

        buttons2.add(goBack);
        buttons.add(buttons1);
        buttons.add(buttons2);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public InlineKeyboardMarkup getInlineStore() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton buyFood = new InlineKeyboardButton();
        buyFood.setText("Купить еду");
        buyFood.setCallbackData("buy_food");

        InlineKeyboardButton buyEquipment = new InlineKeyboardButton();
        buyEquipment.setText("Купить снаряжение");
        buyEquipment.setCallbackData("buy_equipment");

        buttons1.add(buyFood);
        buttons1.add(buyEquipment);

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton goBack = new InlineKeyboardButton();
        goBack.setText("Назад");
        goBack.setCallbackData("go_back");

        buttons2.add(goBack);
        buttons.add(buttons1);
        buttons.add(buttons2);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public InlineKeyboardMarkup getInlineMeditation() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton meditation = new InlineKeyboardButton();
        meditation.setText("Медитировать");
        meditation.setCallbackData("meditation");

        InlineKeyboardButton cooldown = new InlineKeyboardButton();
        cooldown.setText("Посмотреть кулдауны?");
        cooldown.setCallbackData("cooldown");

        buttons1.add(meditation);
        buttons1.add(cooldown);

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton goBack = new InlineKeyboardButton();
        goBack.setText("Назад");
        goBack.setCallbackData("go_back");

        buttons2.add(goBack);
        buttons.add(buttons1);
        buttons.add(buttons2);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public InlineKeyboardMarkup getInlineDifficultySelection() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();

        InlineKeyboardButton simpleOpponent = new InlineKeyboardButton();
        simpleOpponent.setText("Простой противник");
        simpleOpponent.setCallbackData("simple_opponent");

        InlineKeyboardButton averageOpponent = new InlineKeyboardButton();
        averageOpponent.setText("Средний противник");
        averageOpponent.setCallbackData("average_opponent");

        InlineKeyboardButton difficultOpponent = new InlineKeyboardButton();
        difficultOpponent.setText("Сложный противник");
        difficultOpponent.setCallbackData("difficult_opponent");

        buttons1.add(simpleOpponent);
        buttons1.add(averageOpponent);
        buttons1.add(difficultOpponent);

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton goBack = new InlineKeyboardButton();
        goBack.setText("Назад");
        goBack.setCallbackData("go_back");

        buttons2.add(goBack);
        buttons.add(buttons1);
        buttons.add(buttons2);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public void setButtons(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("кто я"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/start"));
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("увеличить живучесть на 10"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public InlineKeyboardMarkup getConfirmationKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton confirmationButton = new InlineKeyboardButton();
        confirmationButton.setText("\u2705"); // \u2705 is the Unicode for the green check mark
        confirmationButton.setCallbackData("confirm_registration");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(confirmationButton);
        buttons.add(row);

        markup.setKeyboard(buttons);
        return markup;
    }

    public InlineKeyboardMarkup getClassSelectionKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton wizardButton = new InlineKeyboardButton();
        wizardButton.setText("Колдун");
        wizardButton.setCallbackData("select_wizard");

        InlineKeyboardButton butcherButton = new InlineKeyboardButton();
        butcherButton.setText("Мясник");
        butcherButton.setCallbackData("select_butcher");

        InlineKeyboardButton archerButton = new InlineKeyboardButton();
        archerButton.setText("Лучник");
        archerButton.setCallbackData("select_archer");

        buttons.add(Arrays.asList(wizardButton, butcherButton, archerButton));
        markup.setKeyboard(buttons);
        return markup;
    }

    public ReplyKeyboardMarkup getMainGameMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Арена"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Инвентарь"));
        keyboardSecondRow.add(new KeyboardButton("Магазин"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("О себе"));
        keyboardThirdRow.add(new KeyboardButton("Медитация"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
