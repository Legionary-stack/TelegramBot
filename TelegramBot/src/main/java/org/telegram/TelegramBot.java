package org.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    int i = 0;

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        System.out.println(chatId + " " + text);

        setButtons(message);
        try {
            switch (text) {
                case "/start":
                    message.setText("ты текст текст");
                    break;
                case "gleb":
                    message.setText("sorry bro");
                    break;
                case "ТЫ СКАЗАЛ ЧТО ТЫ ШАРИШ В ЭТОЙ ТЕБЕ ТЫ":
                    message.setText("блин");
                    break;
                default:
                    message.setText("Ты чото попутал");
            }
            this.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setButtons(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("ТЫ СКАЗАЛ ЧТО ТЫ ШАРИШ В ЭТОЙ ТЕБЕ ТЫ"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("gleb"));

        // Добавляем все строчки клавиатуры в список
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotToken() {
        return "6761562683:AAFUfFQEFgWGVJuX9aah028Svsz2rccZ54I";
    }

    @Override
    public String getBotUsername() {
        return "miklejordan228_bot";
    }
}



