package de.bghw.daleagent.routes;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.zipkin.ZipkinTracer;
import org.springframework.stereotype.Component;

import de.bghw.daleagent.routes.minidav.XMLtoJSONProcessor;


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

		// Exceptionhandling
		//onException(Exception.class).handled(true);

		// Route 1 :Dokumente einlesen und in Queue ablegeN
		from("file:input?delay=1000&readLock=changed&noop=true").id("route1_FileToRabbitMQ").choice().when()
				.simple("${file:name.ext} == 'xml'").log("Verarbeite Datei: ${file:name}").to("file:output")
		.process(new  XMLtoJSONProcessor()).marshal().json(JsonLibrary.Jackson).to("rabbitmq:localhost:5672/dokumentensteuerung?username=guest&password=guest&routingkey=dale&queue=dokumentensteuerung&autoDelete=false&autoAck=false");

//		// Route 2: Dokumente aus der Queue holen, verarbeiten
		from("rabbitmq:localhost:5672/dokumentensteuerung?username=guest&password=guest&routingkey=dale&queue=dokumentensteuerung&autoDelete=false&autoAck=false").routeId("route2_RABBITMQ_ToXX")
		.unmarshal().json().log(body().toString());
		}

}