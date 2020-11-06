package de.bghw.dokumentensteuerung.dale.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.zipkin.ZipkinTracer;
import org.springframework.stereotype.Component;

@Component
public class MiniDAVRouter extends RouteBuilder {

	private ZipkinTracer zipkin = new ZipkinTracer();

	private void configureZipkin() {
		// create zipkin
		zipkin.setEndpoint("http://localhost:9411/api/v2/spans");
		// set the service name
		zipkin.setServiceName("MiniDAVRouter");
		// capture 100% of all the events
		zipkin.setRate(1.0f);
		
		// include message bodies in the traces (not recommended for production)
		zipkin.setIncludeMessageBodyStreams(true);
		// add zipkin to CamelContext
		zipkin.init(getContext());
	}

	@Override
	public void configure() throws Exception {
		// Zipkin zum Camelcontext verbinden
		configureZipkin();

		// Tracing aktivieren
		getContext().setTracing(true);

		// Exceptionhandling (bei Exception) anhalten
		onException(Exception.class).handled(true);//.stop();

		restConfiguration().component("undertow").host("localhost").port(5050).bindingMode(RestBindingMode.json);
		
		//REST-Endpoints setzen
//		rest("/say").consumes("text/plain").produces("text/html")
//        .get("/hello").route().to("direct:hello");
//        
		rest("/say")
        .get("/bye").consumes("text/plain").to("direct:hello");
//        .post("/bye").to("mock:update");
//
    from("direct:hello").transform().constant("{}");

//
//    from("direct:bye")
//        .transform().constant("Bye World");
    
        //from("timer://myTimer?period=1000").log("hallo Eugen");
		
		
//		from("rest:get:hello:/{me}")
//		  .transform().simple("Hi ${header.me}");
		
		// Route 1 :Dokumente einlesen und in Queue ablegen
//		from("file:input").id("minidav").
//		process(new Processor() {
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				//throw new RuntimeCamelException("Eigenen Fehler geworfen");
//				System.out.println("Dokument angenommen");
//			}
//		}).
//		to("file:output").to("jms:queue:daledokument");
//		
//
//		// Route 2: Dokumente aus der Queue verarbeiten
//		from("jms:queue:daledokument").
//		process(new Processor() {
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				//throw new RuntimeCamelException("Eigenen Fehler geworfen");
//				System.out.println("Dokumentverarbeitung gestartet");
//			}
//		});
		
	}

}
