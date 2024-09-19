package org.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.telegram.GetToken.getToken;


public class TelegramBot extends TelegramLongPollingBot {
    public enum BotState {
        WAITING_FOR_COMMAND, COLLECTING_NAME, FINISHED
    }

    private BotState currentState = BotState.WAITING_FOR_COMMAND;
    private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, Person> userPersons = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        currentState = userStates.getOrDefault(chatId, BotState.WAITING_FOR_COMMAND);

        if (userPersons.get(chatId) != null) {
            System.out.print(userPersons.get(chatId).getName());
        }
        System.out.println(userPersons.get(chatId) + " " + chatId + " " + text);

        setButtons(message);
        switch (currentState) {
            case WAITING_FOR_COMMAND:
                handleCommand(update, chatId);
                break;
            case COLLECTING_NAME:
                collectName(update, chatId);
                break;
            case FINISHED:
                // логика для завершенного состояния?
                break;
        }

    }

    private void handleCommand(Update update, long chatId) {
        // Логика обработки команды
        String command = update.getMessage().getText();
        switch (command) {
            case "/start":
                userStates.put(chatId, BotState.COLLECTING_NAME);
                sendMessage(chatId, "Привет! Как тебя зовут?");
                break;
            case "кто я":
                getInformationAboutPerson(chatId);
                break;
            case "увеличить живучесть на 10":
                userPersons.get(chatId).riseVitality(10);
                sendMessage(chatId, "Нормик");
                break;
        }
    }

    private void collectName(Update update, long chatId) {
        String userName = update.getMessage().getText();
        Person person = new Person(userName, chatId);
        userPersons.put(chatId, person);

        sendMessage(chatId, "Отлично, " + person.getName() + "!");
        userStates.put(chatId, BotState.WAITING_FOR_COMMAND);
    }

    private void getInformationAboutPerson(long chatId) {
        Person person = userPersons.get(chatId);
        String text = person == null ? "ты никто" : String.format(
                "Ты: %s.\nТвой уровень: %s \nТвой интеллект: %s, твоя сила: %s\n" +
                        "твоя ловкость: %s, твоя живучесть: %s\n"+
                        "Очки здоровья: %s/%s\nОчки маны: %s/%s",
                person.getName(), person.getLevel(),person.getIntelligence(), person.getStrength(),
                person.getAgility(), person.getVitality(),person.getCurrentHealthPoints(),
                person.getMaxHealthPoints(),person.getCurrentManaPoints(), person.getMaxManaPoints());
        sendMessage(chatId, text);
        userStates.put(chatId, BotState.WAITING_FOR_COMMAND);

    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        setButtons(message);
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            System.out.println("oshibks");
            e.printStackTrace();
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
        keyboardFirstRow.add(new KeyboardButton("кто я"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/start"));
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("увеличить живучесть на 10"));
        // Добавляем все строчки клавиатуры в список
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotToken() {
        try {
            return getToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "miklejordan228_bot";
    }
}

