package org.telegram.getters;

//import java.io.File;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.nio.file.Paths;

public class GetApiKey {
    public static @NotNull String getApiKey() throws IOException  {
        Path tokenPath = Path.of("TelegramBot/TelegramBot/token/api_key.txt");
        return Files.readString(tokenPath).trim();
    }
}