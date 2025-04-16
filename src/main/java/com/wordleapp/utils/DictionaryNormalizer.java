package com.wordleapp.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DictionaryNormalizer {

    public static void main(String[] args) throws IOException {
        String inputPath = "src/main/resources/dictionary.txt";
        String outputPath = "src/main/resources/dictionary-cleaned.txt";

        String content = Files.readString(Paths.get(inputPath));
        String[] words = content.split("\\s+");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            for (String word : words) {
                if (word.length() == 5) {
                    writer.write(word.toUpperCase());
                    writer.newLine();
                }
            }
        }

        log.info("✅ Normalized file successfully created in: {}", outputPath);
    }
}
