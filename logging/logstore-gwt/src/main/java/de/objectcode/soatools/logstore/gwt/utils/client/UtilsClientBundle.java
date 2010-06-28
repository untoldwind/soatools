package de.objectcode.soatools.logstore.gwt.utils.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UtilsClientBundle extends ClientBundle {
	public static final UtilsClientBundle INSTANCE =  GWT.<UtilsClientBundle>create(UtilsClientBundle.class);

	ImageResource active();
	
	ImageResource inactive();
	
	ImageResource bulletLeft();

	ImageResource bulletRight();

	ImageResource left();
	
	ImageResource leftDisabled();
	
	ImageResource right();
	
	ImageResource rightDisabled();
	
}
