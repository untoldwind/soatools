package de.objectcode.soatools.logstore.ws.rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import de.objectcode.soatools.logstore.VersionStore;

@Path("/versions")
public class VersionsResource {
	@GET
	@Produces( { "application/xml", "text/xml" })
	public Document getVersions() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element versions = document.addElement("versions");

		for (Map.Entry<String, String> version : VersionStore.getInstance()
				.getVersions().entrySet()) {
			Element versionElement = versions.addElement("versions");
			versionElement.addAttribute("name", version.getKey());
			versionElement.addAttribute("version", version.getValue());
		}
		
		return document;
	}

}
