package org.telegram;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import java.sql.*;

public class DataBase {
    private final String URL = "jdbc:sqlite:TelegramBot/TelegramBot/db/main_data.db";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established." + URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void userBaseModify(long chatId, boolean add) {
        /*Функция для создания (add == true) или удаления (add == false) записей в базе данных
        * по выбранному telegram id = chatId который берется с типом long*/
        String sql;
        if (add) {
            sql = "INSERT INTO userBase(chatId) VALUES(?)";
        } else {
            sql = "DELETE FROM userBase WHERE chatId = ?";
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, chatId);

            int affectedRows = pstmt.executeUpdate();
            if (add) {
                System.out.println("Новая запись с chatId " + chatId + " добавлена в таблицу userBase.");
            } else {
                if (affectedRows > 0) {
                    System.out.println("Запись с chatId " + chatId + " удалена из таблицы userBase.");
                } else {
                    System.out.println("Запись с chatId " + chatId + " не найдена для удаления.");
                }
            }

        } catch (SQLException e) {
            if (add) {
                System.out.println("Ошибка при добавлении chatId " + chatId + " : " + e.getMessage());
            } else {
                System.out.println("Ошибка при удалении chatId " + chatId + " : " + e.getMessage());
            }
        }
    }

    public Integer getIdByChatId(long chatId) {
        String sql = "SELECT id FROM userBase WHERE chatId = ?";
        Integer id = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, chatId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении id по chatId " + chatId + " : " + e.getMessage());
        }
        return id;
    }

    public void userStatsPostFull(long chatId, boolean add,
                                    String userName, Integer level, Integer currentHealthPoints,
                                    Integer currentManaPoints, Integer currentExpPoints, Integer strength,
                                    Integer intelligence, Integer agility, Integer vitality,
                                  String play_class, String pic) {

        Integer thisObjUserId = getIdByChatId(chatId);
        String sql = "INSERT INTO userStats(userId, userName, level, currentHealthPoints, currentManaPoints, " +
                "currentExpPoints, strength, intelligence, agility, vitality, class, pic) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Установка значений для вставки
            pstmt.setInt(1, thisObjUserId);
            pstmt.setString(2, userName);
            pstmt.setInt(3, level);
            pstmt.setInt(4, currentHealthPoints);
            pstmt.setInt(5, currentManaPoints);
            pstmt.setInt(6, currentExpPoints);
            pstmt.setInt(7, strength);
            pstmt.setInt(8, intelligence);
            pstmt.setInt(9, agility);
            pstmt.setInt(10, vitality);
            pstmt.setString(11, play_class);
            pstmt.setString(12, pic);

            pstmt.executeUpdate();
            System.out.println("Запись добавлена в таблицу userStats.");

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении записи userId " + thisObjUserId + " : " + e.getMessage());
        }
    }

//    public void userStatsDeleteFull(long chatId, boolean add) {
//
//        Integer thisObjUserId = getIdByChatId(chatId);
//        String sql = "DELETE FROM userStats WHERE userId = ?";
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, thisObjUserId);
//            pstmt.executeUpdate();
//            System.out.println("Запись удалена из таблицы userStats.");
//
//        } catch (SQLException e) {
//            System.out.println("Ошибка при удалении записи userId " + thisObjUserId + " : " + e.getMessage());
//        }
//    }
//
//    public void userStatsGetInfo(long chatId) {
//        Integer thisObjUserId = getIdByChatId(chatId);
//
//        if (thisObjUserId == null) {
//            System.out.println("UserId не найден для chatId: " + chatId);
//            return;
//        }
//
//        String sql = "SELECT * FROM userStats WHERE userId = ?";
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, thisObjUserId);
//            ResultSet rs = pstmt.executeQuery();
//
//            // Извлечение данных из результата запроса
//            if (rs.next()) {
//                int userId = rs.getInt("userId");
//                String userName = rs.getString("userName");
//                int level = rs.getInt("level");
//                int currentHealthPoints = rs.getInt("currentHealthPoints");
//                int currentManaPoints = rs.getInt("currentManaPoints");
//                int currentExpPoints = rs.getInt("currentExpPoints");
//                int strength = rs.getInt("strength");
//                int intelligence = rs.getInt("intelligence");
//                int agility = rs.getInt("agility");
//                int vitality = rs.getInt("vitality");
//                String userClass = rs.getString("class");
//
//                // Вывод информации о пользователе
//                /*
//                System.out.println("UserId: " + userId);
//                System.out.println("UserName: " + userName);
//                System.out.println("Level: " + level);
//                System.out.println("Current Health Points: " + currentHealthPoints);
//                System.out.println("Current Mana Points: " + currentManaPoints);
//                System.out.println("Current Exp Points: " + currentExpPoints);
//                System.out.println("Strength: " + strength);
//                System.out.println("Intelligence: " + intelligence);
//                System.out.println("Agility: " + agility);
//                System.out.println("Vitality: " + vitality);
//                System.out.println("Class: " + userClass);*/
//            } else {
//                System.out.println("Запись не найдена для userId: " + thisObjUserId);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Ошибка при извлечении данных: " + e.getMessage());
//        }
//    }

    public Map<String, Object> userStatsGetInfo(long chatId) {
        Integer thisObjUserId = getIdByChatId(chatId);

        if (thisObjUserId == null) {
            System.out.println("UserId не найден для chatId: " + chatId);
            return null; // Возвращаем null, если пользователь не найден
        }

        String sql = "SELECT * FROM userStats WHERE userId = ?";
        Map<String, Object> userInfo = new HashMap<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, thisObjUserId);
            ResultSet rs = pstmt.executeQuery();

            // Извлечение данных из результата запроса
            if (rs.next()) {
                userInfo.put("userId", rs.getInt("userId"));
                userInfo.put("userName", rs.getString("userName"));
                userInfo.put("level", rs.getInt("level"));
                userInfo.put("currentHealthPoints", rs.getInt("currentHealthPoints"));
                userInfo.put("currentManaPoints", rs.getInt("currentManaPoints"));
                userInfo.put("currentExpPoints", rs.getInt("currentExpPoints"));
                userInfo.put("strength", rs.getInt("strength"));
                userInfo.put("intelligence", rs.getInt("intelligence"));
                userInfo.put("agility", rs.getInt("agility"));
                userInfo.put("vitality", rs.getInt("vitality"));
                userInfo.put("class", rs.getString("class"));
                userInfo.put("pic", rs.getString("pic"));
            } else {
                System.out.println("Запись не найдена для userId: " + thisObjUserId);
                return null; // Возвращаем null, если запись не найдена
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при извлечении данных: " + e.getMessage());
            return null; // Возвращаем null в случае ошибки
        }

        return userInfo; // Возвращаем собранную информацию
    }

    // Метод для проверки допустимости имени столбца
    private boolean isValidColumn(String column) {
        // Список допустимых имен столбцов
        String[] validColumns = {"userName", "level", "currentHealthPoints", "currentManaPoints", "currentExpPoints",
                "strength", "intelligence", "agility", "vitality", "class", "pic"};
        for (String validColumn : validColumns) {
            if (validColumn.equals(column)) {
                return true;
            }
        }
        return false;
    }

    // Метод для обновления значения в таблице userStats
    public void userStatsPathOnePunch( long chatId, String changeColumn, Object changeValue) {
        Integer thisObjUserId = getIdByChatId(chatId);

        if (changeValue == null) {
            System.out.println("Объект changeValue не должен быть null");
            return;
        }

        if (thisObjUserId == null) {
            System.out.println("UserId не найден для chatId: " + chatId);
            return;
        }
        // Проверка на допустимость имени столбца
        if (!isValidColumn(changeColumn)) {
            System.out.println("Недопустимое имя столбца: " + changeColumn);
            return;
        }
        // SQL-запрос для обновления значения в указанном столбце
        String sql = "UPDATE userStats SET " + changeColumn + " = ? WHERE userId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Установка значения для обновления
            pstmt.setObject(1, changeValue);
            pstmt.setInt(2, thisObjUserId);

            pstmt.executeUpdate();
            System.out.println("Запись обновлена в таблице userStats.");

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении записи: " + e.getMessage());
        }
    }
}