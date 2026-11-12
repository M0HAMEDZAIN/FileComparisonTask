package com.evision.task.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evision.task.model.ScoreReport;
import com.evision.task.service.FileComparisonService;

@RestController
@RequestMapping("/api/similarity")
public class FileComparisonController {
	private final FileComparisonService service;

	public FileComparisonController(FileComparisonService service) {
		this.service = service;
	}

	@GetMapping("/results")
	public ScoreReport getResults() throws IOException {
		return service.compareAll();
	}
}
