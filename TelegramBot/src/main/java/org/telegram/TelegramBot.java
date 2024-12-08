package org.telegram;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.characters.Enemy;
import org.telegram.enums.BotState;
import org.telegram.enums.KeyboardState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

import org.telegram.characters.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.telegram.getters.GetToken.getToken;
import static org.telegram.getters.GetDefaultPicture.getDefPic;
import static org.telegram.getters.GetApiKey.getApiKey;
import static org.telegram.getters.GetSecretKey.getSecret;

public class TelegramBot extends TelegramLongPollingBot {
    private final Keyboards Keyboards = new Keyboards();
    private BotState currentBotState = BotState.REGISTRATION;
    private KeyboardState currentKeyboardState = KeyboardState.REGISTRATION;
    //private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, Person> userPersons = new HashMap<>();
    private final Map<Long, Enemy> enemyPerson = new HashMap<>();
    // private final Map<Long, KeyboardState> keyboardStates = new HashMap<>();
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
        db.initializeDatabase(db);
        image_gen = new ImageGenerator();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            //String text = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            System.out.print(db.getIdByChatId(chatId));
            System.out.println(message.getText());
            if (db.getIdByChatId(chatId) != null) {
                Person person = new Person(null, chatId, null, null);
                person.loadFromDatabase(db, chatId);
                userPersons.put(chatId, person);
                currentBotState = BotState.valueOf(
                        (String) db.userStatsGetInfo(chatId).get("botState"));
                currentKeyboardState = KeyboardState.valueOf(
                        (String) db.userStatsGetInfo(chatId).get("keyboardState"));
            } else {
                System.out.println("HEY");
                currentBotState = BotState.REGISTRATION;
                currentKeyboardState = KeyboardState.REGISTRATION;

            }
            // userStates.putIfAbsent(chatId, BotState.REGISTRATION);
            // keyboardStates.putIfAbsent(chatId, KeyboardState.REGISTRATION);
            System.out.println(currentBotState);
            System.out.println(currentKeyboardState);
            boolean isSuccess = false;


            if (update.hasCallbackQuery()) {
                handleCallbackQuery(update.getCallbackQuery());
            } else {
                switch (currentBotState) {
                    case REGISTRATION:
                        System.out.println("Zashlo");
                        isSuccess = userRegistration(update, chatId);

                        break;
                    case WAITING_FOR_COMMAND:
                        handleCommand(update, chatId);
                        break;
                    case IN_BATTLE:
                        if (update.hasCallbackQuery()) {
                            Battle(update.getCallbackQuery());
                        }

                }
            }
            if (userPersons.get(chatId) != null && db.getIdByChatId(chatId) != null) {
                userPersons.get(chatId).saveToDatabase(db, chatId, currentKeyboardState, currentBotState);
            }

        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());

        }

    }

    private boolean userRegistration(@NotNull Update update, long chatId) {
        String command = update.getMessage().getText();
        boolean isSuccess = false;

        if (command.equals("/start")) {
            System.out.println("ADAD");
            isSuccess = registerUser(chatId);
            if (isSuccess) {
                System.out.println("REG done");
                //db.userStatsPathOnePunch(chatId,
                //       "keyboardState", KeyboardState.MAIN_GAME_MENU);
                //db.userStatsPathOnePunch(chatId,
                //      "botState", BotState.WAITING_FOR_COMMAND);
                //userStates.put(chatId, BotState.WAITING_FOR_COMMAND);
                // keyboardStates.put(chatId, KeyboardState.MAIN_GAME_MENU);
            } else {
                System.out.println("WTF");
            }
            // Проверка, есть ли у пользователя класс и картинка
            if (!hasClassAndImage(chatId)) {
                // Если нет, предложить выбор класса
                sendClassSelection(chatId);
            }
        } else {
            String text = "Вы на этапе регистрации. Нажмите кнопку /start";
            sendMessage(chatId, text);
        }
        return isSuccess;
    }

    private void handleCommand(@NotNull Update update, long chatId) {
        String command = update.getMessage().getText();
        db.userStatsPathOnePunch(chatId,
                "keyboardState", KeyboardState.MAIN_GAME_MENU);
        currentKeyboardState = KeyboardState.MAIN_GAME_MENU;
        switch (command) {
            case "Арена":
                if (db.getIdByChatId(chatId) != null) {
                    System.out.println("TOze");
                    startPvEBattle(chatId);
                }
                break;
            case "О себе":
                db.userStatsPathOnePunch(chatId,
                        "keyboardState", KeyboardState.MAIN_GAME_MENU);
                currentKeyboardState = KeyboardState.MAIN_GAME_MENU;

                getInformationAboutPerson(chatId);
                break;
            case "Медитация":
                db.userStatsPathOnePunch(chatId,
                        "keyboardState", KeyboardState.MAIN_GAME_MENU);
                currentKeyboardState = KeyboardState.MAIN_GAME_MENU;
                if (userPersons.get(chatId) != null) {
                    userPersons.get(chatId).revive();
                    sendMessage(chatId, "HP восстановлено ");
                }
                break;
            case "Инвентарь":
                db.userStatsPathOnePunch(chatId,
                        "keyboardState", KeyboardState.STAT_UPGRADE);
                currentKeyboardState = KeyboardState.STAT_UPGRADE;
                sendMessage(chatId, "Инвентарь пока не реализован," +
                        "но вы можете улучшить статы");
                break;
        }
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


    private void handleCallbackQuery(@NotNull CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        String userName = callbackQuery.getFrom().getFirstName();
        String picture = "";
        String promt = "";
        String play_class = null;

        switch (callbackData) {
            case "select_wizard":
                promt = "Отважный колдун, " + userName + ", с магическим посохом и в тканном доспехе.";
                play_class = "wizard";
                break;
            case "select_butcher":
                promt = "Седовласый ведьмак, " + userName + ", из Ривии, с двумя мечами и доспехами школы волка.";
                play_class = "butcher";
                break;
            case "select_archer":
                promt = "Отважный стрелок, " + userName + ", с дубовым луком и шелковой мантией.";
                play_class = "archer";
                break;
            case "attack":
            case "defend":
                Battle(callbackQuery);
                return;
            case "plus_strength":
                if (userPersons.get(chatId) != null)
                    if (!userPersons.get(chatId).riseStat("strength")) {
                        sendMessage(chatId, "Недостаточно очков");
                        break;
                    }
                sendMessage(chatId, "Сила повышена");
                break;
            case "plus_vitality":
                if (userPersons.get(chatId) != null)
                    if (!userPersons.get(chatId).riseStat("vitality")) {
                        sendMessage(chatId, "Недостаточно очков");
                        break;
                    }
                sendMessage(chatId, "Живучесть повышена");
                break;
            case "plus_intelligence":
                if (userPersons.get(chatId) != null)
                    if (!userPersons.get(chatId).riseStat("intelligence")) {
                        sendMessage(chatId, "Недостаточно очков");
                        break;
                    }
                sendMessage(chatId, "Интеллект повышен");
                break;
            case "plus_agility":
                if (userPersons.get(chatId) != null)
                    if (!userPersons.get(chatId).riseStat("agility")) {
                        sendMessage(chatId, "Недостаточно очков");
                        break;
                    }
                sendMessage(chatId, "Ловкость повышена");
                break;
            case "go_back":
                db.userStatsPathOnePunch(chatId,
                        "keyboardState", KeyboardState.MAIN_GAME_MENU);
                db.userStatsPathOnePunch(chatId,
                        "botState", BotState.WAITING_FOR_COMMAND);
                currentKeyboardState = KeyboardState.MAIN_GAME_MENU;
                currentBotState = BotState.WAITING_FOR_COMMAND;

                sendMessage(chatId, "Главное меню");
                break;
        }
        if (play_class != null) {
            registrationUser(chatId, promt, userName, play_class);
        }

    }

    private void registrationUser(long chatId, String promt, String userName,
                                  String playClass) {
        String picture = "";
        try {
            System.out.println("Попытка создания фото");
            sendMessage(chatId, "Пытаемся создать фото... " +
                    "Приблизительное время ожидания ~ 20 секунд");
            image_gen.genFunction(api_key, secret_key, promt, 2);
            currentBotState = BotState.WAITING_FOR_COMMAND;
            currentKeyboardState = KeyboardState.MAIN_GAME_MENU;
            sendMessage(chatId, promt);
            picture = sendPhoto(
                    chatId,
                    "TelegramBot/TelegramBot/src/main/java/org/telegram/py_file/image.jpg"
            );
        } catch (Exception e) {
            // Обработка исключения
            picture = default_pic;
            e.printStackTrace();
        }
        Person person = new Person(userName, chatId, picture, playClass);
        userPersons.put(chatId, person);
        db.userStatsPostFull(
                chatId,
                true,
                userName,
                person.getLevel(),
                person.getCurrentHealthPoints(),
                person.getCurrentManaPoints(),
                person.getCurrentExperiencePoints(),
                person.getStrength(),
                person.getIntelligence(),
                person.getAgility(),
                person.getVitality(),
                playClass,
                picture,
                KeyboardState.MAIN_GAME_MENU,
                BotState.WAITING_FOR_COMMAND
        );

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
        photo.setPhoto(new InputFile(new File(filePath)));

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

    private void startPvEBattle(long chatId) {
        Person player = userPersons.get(chatId);
        Enemy enemy = new Enemy("Goblin", "Goblin");
        enemyPerson.put(chatId, enemy);
        db.userStatsPathOnePunch(chatId,
                "keyboardState", KeyboardState.NONE);
        db.userStatsPathOnePunch(chatId,
                "botState", BotState.IN_BATTLE);
        currentBotState = BotState.IN_BATTLE;
        currentKeyboardState = KeyboardState.NONE;
        //userStates.put(chatId, BotState.IN_BATTLE);
        // keyboardStates.put(chatId, KeyboardState.NONE);
        sendBattleMessage(chatId, player, enemy);
    }


    private void Battle(@NotNull CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        Person player = userPersons.get(chatId);
        Enemy enemy = enemyPerson.get(chatId);

        switch (callbackData) {
            case "attack":
                int playerDamage = player.getDamage(enemy);
                enemy.setCurrentHealthPoints(enemy.getCurrentHealthPoints() - playerDamage);
                sendMessage(chatId, "Вы нанесли " + playerDamage + " урона " + enemy.getName() + ".");
                if (enemy.getCurrentHealthPoints() <= 0) {
                    int exp = 1000;
                    sendMessage(chatId, enemy.getName() + " побежден!");
                    sendMessage(chatId, "Начислено " + exp + " опыта");
                    player.setCurrentExperiencePoints(player.getCurrentExperiencePoints() + exp);
                    if (player.expUpdate()) {
                        sendMessage(chatId, "Новый уровень! Текущий уровень равен " +
                                player.getLevel());
                    }
                    userPersons.put(chatId, player);
                    db.userStatsPathOnePunch(chatId,
                            "keyboardState", KeyboardState.MAIN_GAME_MENU);
                    db.userStatsPathOnePunch(chatId,
                            "botState", BotState.WAITING_FOR_COMMAND);

                } else {
                    enemyTurn(chatId, player, enemy);
                }
                break;
            case "defend":
                // Реализуйте логику защиты
                sendMessage(chatId, "Вы защищаетесь.");
                enemyTurn(chatId, player, enemy);
                break;

        }
    }


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


    private void getInformationAboutPerson(long chatId) {
        Person person = userPersons.get(chatId);
        String text = person == null ? "ты никто" : String.format(
                """
                        Ты: %s.
                        Твой уровень: %s\s
                        Твой интеллект: %s, твоя сила: %s
                        твоя ловкость: %s, твоя живучесть: %s
                        Очки здоровья: %s/%s
                        Очки маны: %s/%s
                        Шкала опыта: %s/%s
                        Очки улучшения: %s""",
                person.getName(), person.getLevel(), person.getIntelligence(), person.getStrength(),
                person.getAgility(), person.getVitality(), person.getCurrentHealthPoints(),
                person.getMaxHealthPoints(), person.getCurrentManaPoints(), person.getMaxManaPoints(),
                person.getCurrentExperiencePoints(), person.getMaxExperiencePoints(),
                person.getSkillPoints());
        db.userStatsPathOnePunch(chatId,
                "botState", BotState.WAITING_FOR_COMMAND);
        sendMessage(chatId, text);
    }

    private void enemyTurn(long chatId, Person player, Enemy enemy) {
        int enemyDamage = enemy.getDamage(player);
        player.setCurrentHealthPoints(player.getCurrentHealthPoints() - enemyDamage);
        sendMessage(chatId, enemy.getName() + " наносит " + enemyDamage + " урона вам.");
        if (player.getCurrentHealthPoints() <= 0) {
            sendMessage(chatId, "Вы побеждены!");
            db.userStatsPathOnePunch(chatId,
                    "botState", BotState.WAITING_FOR_COMMAND);
            db.userStatsPathOnePunch(chatId,
                    "keyboardState", KeyboardState.MAIN_GAME_MENU);
        } else {
            sendBattleMessage(chatId, player, enemy);
        }
    }

    private void sendBattleMessage(long chatId, Person player, Enemy enemy) {
        String battleLog = "Вы сражаетесь с " + enemy.getName() + "!\n";
        battleLog += "Ваши HP: " + player.getCurrentHealthPoints() + "\n";
        battleLog += enemy.getName() + " HP: " + enemy.getCurrentHealthPoints() + "\n";
        battleLog += "Выберите действие:";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(battleLog);
        message.setReplyMarkup(Keyboards.getBattleActionKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
        ReplyKeyboardMarkup replyKeyboardMarkup = null;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;

        switch (currentKeyboardState) {
            case REGISTRATION:
                replyKeyboardMarkup = Keyboards.setRegistration();
                break;
            case MAIN_GAME_MENU:
                replyKeyboardMarkup = Keyboards.getMainGameMenu();
                break;
            case NONE:
                replyKeyboardMarkup = Keyboards.clearKeyboard();
            case STAT_UPGRADE:
                inlineKeyboardMarkup = Keyboards.getStatUpgrade();
        }
        if (replyKeyboardMarkup != null)
            message.setReplyMarkup(replyKeyboardMarkup);
        else if (inlineKeyboardMarkup != null)
            message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            System.out.println("oshibks");
            e.printStackTrace();
        }
    }

    private void notifyRegistrationResult(long chatId, boolean isSuccess) {
        String messageText = userPersons.get(chatId) != null ?
                "Регистрация успешно пройдена!" : "Регистрация не пройдена!";

        sendMessage(chatId, messageText);
        // userStates.put(chatId, BotState.WAITING_FOR_COMMAND);
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
        return "eshkere2281337_bot";
    }
}

