package de.bghw.daleagent.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CusaController {

	@PostMapping("/cusa/rul")
	public Greeting aufgabe(@RequestParam(value = "archivID") String archivID) {
		return new Greeting(1000l, "");
	}

}