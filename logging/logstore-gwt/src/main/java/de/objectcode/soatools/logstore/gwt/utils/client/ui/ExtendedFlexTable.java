package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedFlexTable extends FlexTable {

	private Element tHeadElement;
	private List<Widget> headerWidgets;

	public ExtendedFlexTable() {
	}

	public void setHeaderStyleName(String styleName) {
		UIObject.setStyleName(tHeadElement.getFirstChildElement(), styleName);
	}

	public void setHeaderStyleName(int column, String styleName) {
		Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()), column);
		UIObject.setStyleName(th, styleName);
	}

	public void setHeaders(TableHeader... headers) {
		if (headerWidgets != null) {
			for (Widget widget : headerWidgets) {
				if (widget != null) {
					orphan(widget);
				}
			}
			headerWidgets.clear();
		}
		DOM.setInnerHTML(DOM.getFirstChild(getTHeadElement()), "");

		prepareHeaderCell(headers.length - 1);

		for (int i = 0; i < headers.length; i++) {
			TableHeader column = headers[i];

			setHeaderText(i, column.getWidth(), column.getHeader());
		}
	}

	public void setHeaderText(int column, String width, String text) {
		prepareHeaderCell(column);

		Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()), column);
		internalClearCell(th, true);

		Element span = DOM.createSpan();
		DOM.appendChild(th, span);
		DOM.setInnerText(span, text);

		if (width != null)
			DOM.setStyleAttribute(th, "width", width);
	}

	public void setHeaderWidget(int column, String width, Widget widget) {
		prepareHeaderCell(column);

		if (widget != null) {
			widget.removeFromParent();

			Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()),
					column);
			internalClearCell(th, true);

			// Physical attach.
			DOM.appendChild(th, widget.getElement());

			if (width != null)
				DOM.setStyleAttribute(th, "width", width);

			List<Widget> headerWidgets = getHeaderWidgets();
			if (headerWidgets.size() > column
					&& headerWidgets.get(column) != null)
				headerWidgets.set(column, widget);
			else
				headerWidgets.add(column, widget);

			adopt(widget);
		}
	}

	@Override
	public Cell getCellForEvent(ClickEvent event) {
		Element tdorth = getEventTargetCell(Event.as(event.getNativeEvent()));
		if (tdorth == null) {
			return null;
		}

		Element tr = DOM.getParent(tdorth);
		int column = DOM.getChildIndex(tr, tdorth);
		if (DOM.getElementProperty(tdorth, "tagName").equalsIgnoreCase("th")) {
			return new ExtendedCell(-1, column);
		} else {
			Element body = DOM.getParent(tr);
			int row = DOM.getChildIndex(body, tr);

			return new ExtendedCell(row, column);
		}
	}

	@Override
	protected Element getEventTargetCell(Event event) {
		Element tdorth = DOM.eventGetTarget(event);
		for (; tdorth != null; tdorth = DOM.getParent(tdorth)) {
			// If it's a TD, it might be the one we're looking for.
			if (DOM.getElementProperty(tdorth, "tagName")
					.equalsIgnoreCase("td")) {
				// Make sure it's directly a part of this table before returning
				// it.
				Element tr = DOM.getParent(tdorth);
				Element body = DOM.getParent(tr);
				if (body == getBodyElement()) {
					return tdorth;
				}
			} else if (DOM.getElementProperty(tdorth, "tagName")
					.equalsIgnoreCase("th")) {
				Element tr = DOM.getParent(tdorth);
				Element head = DOM.getParent(tr);

				if (head == tHeadElement) {
					return tdorth;
				}
			}

			// If we run into this table's body, we're out of options.
			if (tdorth == getBodyElement() || tdorth == tHeadElement) {
				return null;
			}
		}
		return null;

	}

	protected void prepareHeaderCell(int column) {
		if (column < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot create a column with a negative index: " + column);
		}

		List<Widget> headerWidgets = getHeaderWidgets();
		if (headerWidgets.size() <= column || headerWidgets.get(column) == null) {
			int required = column + 1
					- DOM.getChildCount(DOM.getChild(getTHeadElement(), 0));
			if (required > 0)
				addHeaderCells(getTHeadElement(), required);
		}
	}

	protected List<Widget> getHeaderWidgets() {
		if (headerWidgets == null)
			headerWidgets = new ArrayList<Widget>();
		return headerWidgets;
	}

	protected Element getTHeadElement() {
		if (tHeadElement == null) {
			tHeadElement = DOM.createTHead();
			tHeadElement = DOM.createElement("thead");
			DOM.insertChild(getElement(), getTHeadElement(), 0);
			Element tr = DOM.createTR();
			DOM.insertChild(getTHeadElement(), tr, 0);
		}
		return tHeadElement;
	}

	protected native void addHeaderCells(Element tHead, int num)/*-{
		var rowElem = tHead.rows[0];
		for(var i = 0; i < num; i++){
		  var cell = $doc.createElement("th");
		  rowElem.appendChild(cell);
		}
	}-*/;

	public class ExtendedCell extends Cell {

		protected ExtendedCell(int rowIndex, int cellIndex) {
			super(rowIndex, cellIndex);
		}

	}
}
