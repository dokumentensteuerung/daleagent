package de.bghw.daleagent.routes.minidav;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;


@Component
public class MiniDAVRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Tracing aktivieren
		getContext().setTracing(true);
		
		// Exceptionhandling
		//onException(Exception.class).maximumRedeliveries(10).redeliveryDelay(1000).handled(true);
		
		//errorHandler(deadLetterChannel("rabbitmq:localhost:5672/dead?deadLetterExchange=dead&deadLetterExchangeType=direct&deadLetterQueue=dead&deadLetterRoutingKey=dead&username=guest&password=guest").useOriginalMessage());

		// Route 1 :XML-Dokumente einlesen --> Map to JSON --> JSON in Queue ablegen
		from("file:input?delay=1000&readLock=changed&noop=true&include=(?i).*.xml").
		id("route1_FileToRabbitMQ").
		filter().method(Utils.class, "pdfExists").
		log("Pärchen gefunden: Verarbeite Datei: ${file:name}").
		process(new  XMLtoJSONProcessor()).
		marshal().json(JsonLibrary.Jackson).
		log("Verschiebe Auftrag(${file:name}\") in die Queue").
		to("rabbitmq:localhost:5672/dokumentensteuerung?username=guest&password=guest&routingkey=dale&queue=dokumentensteuerung&autoDelete=false&autoAck=false");

//		// Route 2: Dokumente aus der Queue holen, verarbeiten
		// 
		from("rabbitmq:localhost:5672/dokumentensteuerung?username=guest&password=guest&routingkey=dale&queue=dokumentensteuerung&autoDelete=false&autoAck=false").
		routeId("route2_RABBITMQ_ToXX").
		unmarshal().json().
		log(body().toString()).
		log("Pärchen nach DMS").
		to("direct:moveData2DMS");
		
		from("direct:moveData2DMS").
		log("verarbeitung in dms läuft..");
		
		from("direct:moveData2CUSA").
		log("verarbeitung in cusa läuft..");
		
		}

}