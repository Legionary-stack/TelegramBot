package org.smyta;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ImageGenerator {
    public void genFunction(String apiKey, String secretKey, String prompt, int style) {
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3", "GeneratePicTerminal.py",
                    "--api_key", apiKey,
                    "--secret_key", secretKey,
                    "--prompt", prompt,
                    "--style", String.valueOf(style)
            );

            processBuilder.directory(new File(
                            "TelegramBot/TelegramBot/src/main/java/org/telegram/py_file"
                    )
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}