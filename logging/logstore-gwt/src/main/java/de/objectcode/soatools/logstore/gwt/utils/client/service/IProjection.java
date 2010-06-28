package de.objectcode.soatools.logstore.gwt.utils.client.service;

public interface IProjection<DTOClass> {
	Object project(DTOClass dto);
	
	boolean isSortable();
}
