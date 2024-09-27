package com.minewaku.trilog.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class IndexController {

	@GetMapping
	public ResponseEntity<String> index() {
		return ResponseEntity.ok("Hallo Im Emu Otori!");
	}
	
}