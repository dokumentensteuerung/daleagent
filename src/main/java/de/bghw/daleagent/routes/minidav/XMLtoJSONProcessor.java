package de.bghw.daleagent.routes.minidav;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import de.bghw.daleagent.pojo.Document;

public class XMLtoJSONProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		Document document =new Document();
	
		String xmlDoc = exchange.getIn().getBody(String.class);
		
		document.setFileURL(exchange.getIn().getHeader("CamelFileName").toString());
	
		exchange.getIn().setBody(document);
	}
	

}
