package com.evision.task.model;

import java.util.List;

public record ScoreReport(String referenceFile, int comparedFiles, List<ComparisonResult> results) {

}
