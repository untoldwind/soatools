package de.objectcode.soatools.util.healthcheck.check;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mvel2.MVEL;

import de.objectcode.soatools.util.healthcheck.HealthState;

@XmlType(name = "check")
public class Check {
	String name;
	String okExpr;
	Serializable okCompiled;
	String warnExpr;
	Serializable warnCompiled;
	String errorExpr;
	Serializable errorCompiled;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "ok")
	public String getOkExpr() {
		return okExpr;
	}

	public void setOkExpr(String okExpr) {
		this.okExpr = okExpr;
		
		okCompiled = MVEL.compileExpression(okExpr);
	}

	@XmlElement(name = "warn")
	public String getWarnExpr() {
		return warnExpr;
	}

	public void setWarnExpr(String warnExpr) {
		this.warnExpr = warnExpr;
		
		warnCompiled = MVEL.compileExpression(warnExpr);
	}

	@XmlElement(name = "error")
	public String getErrorExpr() {
		return errorExpr;
	}

	public void setErrorExpr(String errorExpr) {
		this.errorExpr = errorExpr;
		
		errorCompiled = MVEL.compileExpression(errorExpr);
	}

	public HealthState perform(Service service, Map<String, Object> values)
			throws Exception {
		if (errorCompiled != null && MVEL.executeExpression(errorCompiled, service, values, Boolean.class))
			return HealthState.ERROR;
		if (warnCompiled != null && MVEL.executeExpression(warnCompiled, service, values, Boolean.class))
			return HealthState.WARN;
		if (okCompiled != null) {
			if (MVEL.executeExpression(okCompiled, service, values, Boolean.class))
				return HealthState.OK;
			else
				return HealthState.ERROR;
		}
		
		return HealthState.OK;
	}
}
