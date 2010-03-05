package de.objectcode.soatools.logstore.ws.soap.types;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "query")
public class Query {
	Date from;
	Date until;
	List<TagCondition> conditions;

	@XmlElement(name = "from", required = false)
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	@XmlElement(name = "until", required = false)
	public Date getUntil() {
		return until;
	}

	public void setUntil(Date until) {
		this.until = until;
	}

	@XmlElement(name = "tag-condition", required = false)
	public List<TagCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<TagCondition> conditions) {
		this.conditions = conditions;
	}

}
