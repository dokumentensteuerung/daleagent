package de.bghw.daleagent.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.bghw.daleagent.pojo.Document;

@RestController
public class CusaController {

	@PostMapping( value = "/rul", consumes = "application/json", produces = "application/json")
	public Document createAufgabe(@RequestBody Document document) {
		return new Document(document.getFileURL(), document.getDokType(), "123456789", document.getRefDmsID());
	}

}