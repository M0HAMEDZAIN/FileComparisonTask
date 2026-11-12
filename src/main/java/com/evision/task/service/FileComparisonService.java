package com.evision.task.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import com.evision.task.components.FileReader;
import com.evision.task.components.ScoreCalculator;
import com.evision.task.config.FileProperties;
import com.evision.task.model.ComparisonResult;
import com.evision.task.model.ScoreReport;

@Service
public class FileComparisonService {

	private final FileProperties properties;
	private final FileReader fileReader;
	private final ScoreCalculator calculator;

	public FileComparisonService(FileProperties properties, FileReader fileReader,
			ScoreCalculator calculator) {
		this.properties = properties;
		this.fileReader = fileReader;
		this.calculator = calculator;
	}

	public ScoreReport compareAll() throws IOException {
		Path referencePath = Path.of(properties.getReferencePath()).toAbsolutePath().normalize();
		Path poolDirectory = Path.of(properties.getPoolDirectory()).toAbsolutePath().normalize();

		validateReferenceFile(referencePath);
		validatePoolDirectory(poolDirectory);

		// Read the reference once and reuse it for every candidate.
		Set<String> referenceWords = fileReader.readUniqueWords(referencePath);

		List<ComparisonResult> results;
		try (Stream<Path> paths = Files.list(poolDirectory)) {
			results = paths.filter(Files::isRegularFile)
					.map(Path::toAbsolutePath)
					.map(Path::normalize)
					.filter(path -> !path.equals(referencePath))
					.map(path -> compareOne(referenceWords, path))
					.toList();
		}

		List<ComparisonResult> sortedResults = results.stream()
				.sorted(Comparator.comparingDouble(ComparisonResult::score).reversed())
				.toList();

		return new ScoreReport(referencePath.getFileName().toString(), sortedResults.size(), sortedResults);
	}

	private ComparisonResult compareOne(Set<String> referenceWords, Path candidatePath) {
		try {
			Set<String> candidateWords = fileReader.readUniqueWords(candidatePath);

			double score = calculator.calculate(referenceWords, candidateWords);

			return new ComparisonResult(candidatePath.getFileName().toString(), score);
		} catch (IOException exception) {
			throw new RuntimeException("Could not read candidate file: " + candidatePath, exception);
		}
	}

	private void validateReferenceFile(Path referencePath) {
		if (!Files.exists(referencePath)) {
			throw new RuntimeException("Reference file does not exist: " + referencePath);
		}
		if (!Files.isRegularFile(referencePath) || !Files.isReadable(referencePath)) {
			throw new RuntimeException("Reference path is not a readable regular file: " + referencePath);
		}
	}

	private void validatePoolDirectory(Path poolDirectory) {
		if (!Files.isDirectory(poolDirectory) || !Files.isReadable(poolDirectory)) {
			throw new RuntimeException("Pool path is not a readable directory: " + poolDirectory);
		}
	}
}
