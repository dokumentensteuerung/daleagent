package de.bghw.daleagent.routes.minidav;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
		//unmarshal().json().
		log(body().toString()).
		log("Pärchen nach DMS").
		to("direct:moveData2DMS");
		
		//Call DMS
		from("direct:moveData2DMS")
		//.marshal().json()
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.to("http://localhost:8080/dms/")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String in = exchange.getIn().getBody(String.class);
				System.out.println("Result von DMS:"+in);
				exchange.getIn().setBody(in);
			}
		})
		.log("DMS-Verarbeitung erfolgreich").to("direct:moveData2CUSA");
		
		//Call Cusa
		from("direct:moveData2CUSA")
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.to("http://localhost:8080/rul/")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Result von Cusa: "+exchange.getIn().getBody(String.class));	
			}
		}).
		log("Verarbeitung in cusa erfolgreich");
		
		}

}