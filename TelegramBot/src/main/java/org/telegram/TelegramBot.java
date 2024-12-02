package org.telegram;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.enums.BotState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;
import org.telegram.characters.Person;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.telegram.GetToken.getToken;
import static org.telegram.GetDefaultPicture.getDefPic;
import static org.telegram.GetApiKey.getApiKey;
import static org.telegram.GetSecretKey.getSecret;

public class TelegramBot extends TelegramLongPollingBot {
    private final Keyboards Keyboards = new Keyboards();
    private BotState currentState = BotState.WAITING_FOR_COMMAND;
    private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, Person> userPersons = new HashMap<>();
    final String default_pic;
    final String api_key;
    final String secret_key;

    {
        try {
            default_pic = getDefPic();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    {
        try {
            api_key = getApiKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    {
        try {
            secret_key = getSecret();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DataBase db;
    private final ImageGenerator image_gen;

    public TelegramBot() {
        db = new DataBase();
        image_gen = new ImageGenerator();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            //String text = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            currentState = userStates.getOrDefault(chatId, BotState.WAITING_FOR_COMMAND);
            boolean isSuccess = false;

            if (update.hasCallbackQuery()) {
                handleCallbackQuery(update.getCallbackQuery());
            } else {
                switch (currentState) {
                    case WAITING_FOR_COMMAND:
                        isSuccess = handleCommand(update, chatId);
                        break;
                    case COLLECTING_NAME:
                        collectName(update, chatId);
                        break;
                    case FINISHED:
                        notifyRegistrationResult(chatId, isSuccess);
                        openMainMenu(chatId);
                        break;
                }
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private boolean handleCommand(@NotNull Update update, long chatId) {
        String command = update.getMessage().getText();
        boolean isSuccess = false;
        switch (command) {
            case "/start":
                // Регистрация пользователя в базе данных
                isSuccess = registerUser(chatId);
                if (isSuccess) {
                    userStates.put(chatId, BotState.FINISHED);
                }
                // Проверка, есть ли у пользователя класс и картинка
                if (!hasClassAndImage(chatId)) {
                    // Если нет, предложить выбор класса
                    sendClassSelection(chatId);
                } else {
                    // Если есть, открыть основное меню
                    openMainMenu(chatId);
                }
                break;
            // Другие команды
        }
        return isSuccess;
    }

    private boolean registerUser(long chatId) {
        // Код для проверки существования и
        // регистрации пользователя в базе данных
        if (db.getIdByChatId(chatId) == null) {
            db.userBaseModify(chatId, true);
            return true;
        }
        return false;
    }

    private boolean hasClassAndImage(long chatId) {
        // Проверка, есть ли у пользователя класс и картинка
        if (db.userStatsGetInfo(chatId) == null) {
            return false;
        }
        return true;
    }

    private void sendClassSelection(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите класс:");
        message.setReplyMarkup(Keyboards.getClassSelectionKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void openMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        //message.setText("Основное меню:");
        message.setReplyMarkup(Keyboards.getMainGameMenuKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(@NotNull CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        String userName = callbackQuery.getFrom().getFirstName();
        String picture = "";
        String promt = "";
        String play_class = "";

        switch (callbackData) {
            case "select_wizard":
                // Логика выбора класса "Колдун"
                promt = "Отважный колдун, " + userName + ", с магическим посохом и в тканном доспехе.";
                play_class = "wizard";
                break;
            case "select_butcher":
                // Логика выбора класса "Мясник"
                promt = "Седовласый ведьмак, " + userName + ", из Ривии, с двумя мечами и доспехами школы волка.";
                play_class = "butcher";
                break;
            case "select_archer":
                // Логика выбора класса "Лучник"
                promt = "Отважный стрелок, " + userName + ", с дубовым луком и шелковой мантией.";
                play_class = "archer";
                break;
            // Другие случаи
        }
        try {
            image_gen.genFunction(api_key, secret_key, promt, 2);
            sendMessage(chatId, promt);
            picture = sendPhoto(
                    chatId,
                    "TelegramBot\\TelegramBot\\src\\main\\java\\org\\telegram\\py_file\\image.jpg"
            );
            openMainMenu(chatId);
        } catch (Exception e) {
            // Обработка исключения
            picture = default_pic;
            e.printStackTrace();
        }

        db.userStatsPostFull(
                chatId,
                true,
                userName,
                1,
                100,
                100,
                0,
                100,
                100,
                100,
                100,
                play_class,
                picture);
    }

    private void aproved_registr(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Подтвердите регистрацию:");
        message.setReplyMarkup(Keyboards.getConfirmationKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private @Nullable String sendPhoto(long chatId, String filePath) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));
        photo.setPhoto(new InputFile(new java.io.File(filePath)));

        try {
            Message message = this.execute(photo);
            // Получаем список фотографий из сообщения
            List<PhotoSize> photos = message.getPhoto();
            if (photos != null && !photos.isEmpty()) {
                // Возвращаем file_id самой большой фотографии
                return photos.get(photos.size() - 1).getFileId();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null; // Возвращаем null, если не удалось получить ID
    }

//    private void handleCallbackQuery(@NotNull CallbackQuery callbackQuery) {
//        String callbackData = callbackQuery.getData();
//        Long chatId = callbackQuery.getMessage().getChatId();
//
//        switch (callbackData) {
//            case "example_action":
//                sendMessage(chatId, "Вы нажали на кнопку 'Пример'!");
//                break;
//            case "weapon":
//                sendMessage(chatId, "Вы выбрали 'Оружие'.");
//                break;
//            case "eat":
//                sendMessage(chatId, "Вы выбрали 'Поесть'.");
//                break;
//            case "improve":
//                sendMessage(chatId, "Вы выбрали 'Прокачка'.");
//                break;
//            case "go_back":
//                sendMessage(chatId, "Вы вернулись назад.");
//                break;
//            // Добавьте другие случаи для других кнопок
//            default:
//                sendMessage(chatId, "Неизвестное действие.");
//                break;
//        }
//    }




//    private void handleCommand(@NotNull Update update, long chatId) {
//        // Логика обработки команды
//        String command = update.getMessage().getText();
//        switch (command) {
//            case "/start":
//                userStates.put(chatId, BotState.COLLECTING_NAME);
//                sendMessage(chatId, "Привет! Как тебя зовут?");
//                break;
//            case "кто я":
//                getInformationAboutPerson(chatId);
//                break;
//            case "увеличить живучесть на 10":
//                userPersons.get(chatId).riseVitality(10);
//                sendMessage(chatId, "Нормик");
//                break;
//        }
//    }

    private void collectName(@NotNull Update update, long chatId) {
        String userName = update.getMessage().getText();
        Person person = new Person(userName, chatId);
        userPersons.put(chatId, person);

        sendMessage(chatId, "Отлично, " + person.getName() + "!");
        userStates.put(chatId, BotState.WAITING_FOR_COMMAND);
    }

    private void getInformationAboutPerson(long chatId) {
        Person person = userPersons.get(chatId);
        String text = person == null ? "ты никто" : String.format(
                """
                        Ты: %s.
                        Твой уровень: %s\s
                        Твой интеллект: %s, твоя сила: %s
                        твоя ловкость: %s, твоя живучесть: %s
                        Очки здоровья: %s/%s
                        Очки маны: %s/%s""",
                person.getName(), person.getLevel(), person.getIntelligence(), person.getStrength(),
                person.getAgility(), person.getVitality(), person.getCurrentHealthPoints(),
                person.getMaxHealthPoints(), person.getCurrentManaPoints(), person.getMaxManaPoints());
        userStates.put(chatId, BotState.WAITING_FOR_COMMAND);
        sendMessage(chatId, text);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        //Keyboards.setButtons(message);
        //message.setReplyMarkup(Keyboards.getInlineInventory());
        //message.setReplyMarkup(Keyboards.getInlineAboutMyself());
        //message.setReplyMarkup(Keyboards.getInlineStore());
        // //message.setReplyMarkup(Keyboards.getInlineMeditation());

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            System.out.println("oshibks");
            e.printStackTrace();
        }
    }

    private void notifyRegistrationResult(long chatId, boolean isSuccess) {
        String messageText = isSuccess ? "Регистрация успешно пройдена!" : "Регистрация не пройдена!";
        sendMessage(chatId, messageText);
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

