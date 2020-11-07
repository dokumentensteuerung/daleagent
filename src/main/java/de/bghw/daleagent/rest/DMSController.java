package de.bghw.daleagent.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMSController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	//private static Logger log = LoggerFactory.getLogger(DemoController.class);

	@GetMapping("/dms")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}