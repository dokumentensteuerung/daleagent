package de.bghw.daleagent.pojo;

/**
 * Document repräsentiert eine Message(später im JSON-Format)
 * @param dmsID
 */

public class Document {
	
	private String fileURL = ""; //File-URL
	private String dokType = ""; //DABE/RE13..
	
	private String refCusaID = "";
	private String refDmsID = "";
	
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getDokType() {
		return dokType;
	}
	public void setDokType(String dokType) {
		this.dokType = dokType;
	}
	public String getRefCusaID() {
		return refCusaID;
	}
	public void setRefCusaID(String refCusaID) {
		this.refCusaID = refCusaID;
	}
	public String getRefDmsID() {
		return refDmsID;
	}
	public void setRefDmsID(String refDmsID) {
		this.refDmsID = refDmsID;
	}
	

	
}
