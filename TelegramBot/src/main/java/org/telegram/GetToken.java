package org.telegram;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetToken {
    public static String getToken() throws IOException  {
        Path tokenPath = Path.of("TelegramBot/TelegramBot/token/token.txt");
        //System.out.println("Текущая рабочая директория: " + System.getProperty("user.dir"));
        return Files.readString(tokenPath).trim();
    }
}
