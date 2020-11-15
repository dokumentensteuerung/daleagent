package de.bghw.daleagent.routes.minidav;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.xml.sax.InputSource;

import de.bghw.daleagent.pojo.Document;

public class XMLtoJSONProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String xmlDoc = exchange.getIn().getBody(String.class);		

		Document pojo =new Document();
		
		//passende PDF zum XML suchen
		String fileName = exchange.getIn().getHeader("CamelFileAbsolutePath").toString();
		
		if (fileName.toLowerCase().endsWith(".xml"))
		{
			File pdfFile = new File(fileName.split(".xml")[0]+".pdf");
			
			if(pdfFile.exists())
			{ 
				//parse xml, find element unh_2
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder = factory.newDocumentBuilder();
			    InputSource is = new InputSource(new StringReader(xmlDoc));
			    String docTyp = builder.parse(is).getElementsByTagName("unh_2").item(0).getTextContent();
			    
				pojo.setDokType(docTyp);
			}
		
		//MAP Values to pojo
		pojo.setFileURL(exchange.getIn().getHeader("CamelFileName").toString());
		
		//set Pojo as Body-Element
		exchange.getIn().setBody(pojo);
	}
	}

}
