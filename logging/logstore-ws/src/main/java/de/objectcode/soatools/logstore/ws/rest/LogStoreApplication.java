package de.objectcode.soatools.logstore.ws.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class LogStoreApplication extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> result = new HashSet<Class<?>>();
		
		return result;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> result = new HashSet<Object>();
		
		result.add(new ServerInfoResource());
		result.add(new JcaResource());
		result.add(new JbmResource());
		result.add(new EsbResource());
		result.add(new VersionsResource());

		return result;
	}

}
