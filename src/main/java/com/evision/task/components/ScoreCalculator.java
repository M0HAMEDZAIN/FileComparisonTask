package com.evision.task.components;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {
	// calculate the score of similarity
	public double calculate(Set<String> referenceWords, Set<String> candidateWords) {
		long commonWords = countIntersection(referenceWords, candidateWords);
		long unionSize = (long) referenceWords.size() + candidateWords.size() - commonWords;

		return unionSize == 0 ? 100.0 : Math.round((commonWords * 10000.0) / unionSize) / 100.0;
	}

	private long countIntersection(Set<String> referenceWords, Set<String> candidateWords) {
		long common = 0;

		if (referenceWords.size() <= candidateWords.size()) {
			for (String word : referenceWords) {
				if (candidateWords.contains(word))
					common++;
			}

		} else {
			for (String word : candidateWords) {
				if (referenceWords.contains(word))
					common++;
			}
		}
		return common;
	}
}
