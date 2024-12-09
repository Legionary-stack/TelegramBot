package org.smyta.getters;

//import java.io.File;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.nio.file.Paths;

public class GetSecretKey {
    public static @NotNull String getSecret() throws IOException {
        Path tokenPath = Path.of("TelegramBot/TelegramBot/token/secret_key.txt");
        return Files.readString(tokenPath).trim();
    }
}