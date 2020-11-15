package de.bghw.daleagent.routes.minidav;

import java.io.File;

import org.apache.camel.Header;

public class Utils {

	public static boolean pdfExists(@Header("CamelFileAbsolutePath") String xmlFileName)
	{
		boolean result = false;
	
		if (xmlFileName.toLowerCase().endsWith(".xml"))
		{
			File pdfFile = new File(xmlFileName.split(".xml")[0]+".pdf");
			if(pdfFile.exists())
			{
				System.out.println("PDF gefunden: " +xmlFileName.split(".xml")[0]+".pdf");
				result = true;
			}
	}
		return result;
}
}