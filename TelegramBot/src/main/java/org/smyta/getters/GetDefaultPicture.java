package org.smyta.getters;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GetDefaultPicture {
    public static @NotNull String getDefPic() throws IOException {
        Path defaultPicturePath = Path.of("TelegramBot/TelegramBot/token/default_pic.txt");
        return Files.readString(defaultPicturePath).trim();
    }
}