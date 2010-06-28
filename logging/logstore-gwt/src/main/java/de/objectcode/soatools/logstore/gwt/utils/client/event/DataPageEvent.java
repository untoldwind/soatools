package de.objectcode.soatools.logstore.gwt.utils.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DataPageEvent extends GwtEvent<DataPageHandler> {
	private static Type<DataPageHandler> TYPE;

	private final int pageNumber;

	protected DataPageEvent(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public static void fire(HasDataPageHandlers source, int pageNumber) {
		if (TYPE != null) {
			DataPageEvent event = new DataPageEvent(pageNumber);
			source.fireEvent(event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Type<DataPageHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(DataPageHandler handler) {
		handler.onDataPage(this);
	}

	public static Type<DataPageHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<DataPageHandler>();
		}
		return TYPE;
	}
}
