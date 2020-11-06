package de.bghw.daleagent.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RESTRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Tracing aktivieren
		getContext().setTracing(true);

		// Exceptionhandling
		onException(Exception.class).handled(true);
		
		restConfiguration().component("servlet")
	       .bindingMode(RestBindingMode.auto);
		
		rest().get("/hello")
	      .to("direct:hello");
	 
	    from("direct:hello")
	      .log(LoggingLevel.INFO, "Hello World")
	      .transform().simple("Hello World");

		
	}

}
