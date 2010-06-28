package de.objectcode.soatools.logstore.gwt.utils.client.ui;

public class TableHeader {
	String header;
	String width;	

	public TableHeader(String header) {
		this.header = header;
	}

	public TableHeader(String header, String width) {
		this.header = header;
		this.width = width;
	}

	public String getHeader() {
		return header;
	}

	public String getWidth() {
		return width;
	}

}
