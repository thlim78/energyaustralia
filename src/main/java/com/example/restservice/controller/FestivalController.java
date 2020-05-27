package com.example.restservice.controller;

import java.util.List;
import com.example.restservice.model.Record;
import com.example.restservice.service.FestivalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class FestivalController {

	private final FestivalService festivalService;

	private FestivalController(FestivalService festivalService) {
		this.festivalService = festivalService;
	}

	@GetMapping("/records")
	public ResponseEntity<List<Record>> getRecords() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(festivalService.getRecords());
	}
}
