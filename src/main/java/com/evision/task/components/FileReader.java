package com.evision.task.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class FileReader {

	// 64 KB buffer size for reading files
	private static final int BUFFER_SIZE = 64 * 1024;

	public Set<String> readUniqueWords(Path filePath) throws IOException {
		Set<String> words = new HashSet<>();
		StringBuilder currentWord = new StringBuilder();
		char[] buffer = new char[BUFFER_SIZE];

		// just open the file without loading the entire file into memory
		// try with resources to ensure the BufferedReader is closed after use
		try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
			int charsRead;

			// read the file in chunks
			while ((charsRead = reader.read(buffer)) != -1) {
				for (int i = 0; i < charsRead; i++) {
					char current = buffer[i];

					if (Character.isLetter(current)) {
						currentWord.append(current);
					} else {
						addWordIfNotExist(words, currentWord);
					}
				}
			}
		}

		addWordIfNotExist(words, currentWord);
		return words;
	}

	private void addWordIfNotExist(Set<String> words, StringBuilder currentWord) {
		if (!currentWord.isEmpty()) {
			words.add(currentWord.toString().toLowerCase(Locale.ROOT));
			currentWord.setLength(0);
		}
	}
}
