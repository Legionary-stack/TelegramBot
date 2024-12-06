package org.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.telegram.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.management.remote.JMXConnectorFactory.connect;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBot());

        System.out.println("BOT is running NOW");
        //DataBase db = new DataBase();
        //db.connect();
    }
}