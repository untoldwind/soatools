package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedSplitLayoutPanel extends DockLayoutPanel {
	private static final int SPLITTER_SIZE = 8;

	protected int centerMinWidth = 0;
	protected int centerMinHeight = 0;

	public ExtendedSplitLayoutPanel() {
		super(Unit.PX);
		setStyleName("gwt-SplitLayoutPanel");
	}

	@Override
	public void insert(Widget child, Direction direction, double size,
			Widget before) {
		super.insert(child, direction, size, before);
		if (direction != Direction.CENTER) {
			insertSplitter(before);
		}
	}

	@Override
	public boolean remove(Widget child) {
		assert !(child instanceof Splitter) : "Splitters may not be directly removed";

		if (super.remove(child)) {
			// Remove the associated splitter, if any.
			int idx = getWidgetIndex(child);
			if (idx < getWidgetCount() - 1) {
				remove(idx + 1);
			}
			return true;
		}
		return false;
	}

	public void setCenterMinSize(int minWidth, int minHeight) {
		centerMinWidth = minWidth;
		centerMinHeight = minHeight;
	}

	public void setWidgetMinSize(Widget child, int minSize) {
		Splitter splitter = getAssociatedSplitter(child);
		splitter.setMinSize(minSize);
	}

	public void maximizeChild(Widget child) {
		assert (child == null) || (child.getParent() == this) : "The specified widget is not a child of this panel";

		LayoutData layoutData = (LayoutData) child.getLayoutData();

		if (layoutData.direction == Direction.CENTER) {
			for (int i = 1; i < getWidgetCount(); i += 2) {
				Splitter splitter = (Splitter) getWidget(i);

				splitter.setSize(splitter.getMinSize());
			}
		} else {
			Splitter splitter = getAssociatedSplitter(child);

			if (splitter instanceof VSplitter) {
				int height = getCenter().getOffsetHeight() - centerMinHeight;

				for (int i = 1; i < getWidgetCount(); i += 2) {
					Splitter otherSplitter = (Splitter) getWidget(i);

					if (splitter != otherSplitter
							&& otherSplitter instanceof VSplitter) {
						height += otherSplitter.getSize()
								- otherSplitter.getMinSize();
						otherSplitter.setSize(otherSplitter.getMinSize());
					}
				}

				splitter.setSize(splitter.getSize() + height);
			} else {
				int width = getCenter().getOffsetWidth() - centerMinWidth;

				for (int i = 1; i < getWidgetCount(); i += 2) {
					Splitter otherSplitter = (Splitter) getWidget(i);

					if (splitter != otherSplitter
							&& otherSplitter instanceof HSplitter) {
						width += otherSplitter.getSize()
								- otherSplitter.getMinSize();
						otherSplitter.setSize(otherSplitter.getMinSize());
					}
				}

				splitter.setSize(splitter.getSize() + width);
			}
		}

		animate(200);
	}

	public void minimizeChild(Widget child) {
		assert (child == null) || (child.getParent() == this) : "The specified widget is not a child of this panel";

		LayoutData layoutData = (LayoutData) child.getLayoutData();

		if (layoutData.direction == Direction.CENTER) {
			int wCount = 0;
			int hCount = 0;
			for (int i = 1; i < getWidgetCount(); i += 2) {
				Splitter splitter = (Splitter) getWidget(i);

				if (splitter instanceof VSplitter) {
					hCount++;
				} else {
					wCount++;
				}
			}
			int width = getCenter().getOffsetWidth() - centerMinWidth;
			int height = getCenter().getOffsetHeight() - centerMinHeight;
			for (int i = 1; i < getWidgetCount(); i += 2) {
				Splitter splitter = (Splitter) getWidget(i);

				if (splitter instanceof VSplitter) {
					splitter.setSize(splitter.getSize() + height / hCount);
				} else {
					splitter.setSize(splitter.getSize() + width / wCount);
				}
			}

		} else {
			Splitter splitter = getAssociatedSplitter(child);

			splitter.setSize(splitter.getMinSize());

		}
		animate(200);

	}

	private Splitter getAssociatedSplitter(Widget child) {
		// If a widget has a next sibling, it must be a splitter, because the
		// only
		// widget that *isn't* followed by a splitter must be the CENTER, which
		// has
		// no associated splitter.
		int idx = getWidgetIndex(child);
		if (idx < getWidgetCount() - 2) {
			Widget splitter = getWidget(idx + 1);
			assert splitter instanceof Splitter : "Expected child widget to be splitter";
			return (Splitter) splitter;
		}
		return null;
	}

	private void insertSplitter(Widget before) {
		assert getChildren().size() > 0 : "Can't add a splitter before any children";
		assert getCenter() == null : "Can't add a splitter after the CENTER widget";

		Widget lastChild = getChildren().get(getChildren().size() - 1);
		LayoutData lastChildLayout = (LayoutData) lastChild.getLayoutData();
		Splitter splitter = null;
		switch (lastChildLayout.direction) {
		case WEST:
			splitter = new HSplitter(lastChild, false);
			break;
		case EAST:
			splitter = new HSplitter(lastChild, true);
			break;
		case NORTH:
			splitter = new VSplitter(lastChild, false);
			break;
		case SOUTH:
			splitter = new VSplitter(lastChild, true);
			break;
		default:
			assert false : "Unexpected direction";
		}

		super
				.insert(splitter, lastChildLayout.direction, SPLITTER_SIZE,
						before);
	}

	public abstract class Splitter extends Widget {
		protected final Widget target;

		private int offset;
		private boolean mouseDown;
		private Command layoutCommand;

		private final boolean reverse;
		private int minSize;
		private int maxSize;

		public Splitter(Widget target, boolean reverse) {
			this.target = target;
			this.reverse = reverse;

			setElement(Document.get().createDivElement());
			sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE
					| Event.ONDBLCLICK);
		}

		@Override
		public void onBrowserEvent(Event event) {
			switch (event.getTypeInt()) {
			case Event.ONMOUSEDOWN:
				mouseDown = true;
				offset = getEventPosition(event) - getAbsolutePosition();
				// maxSize = how much more the child is allowed to grow before
				// center is too small
				maxSize = getMaxSize();
				Event.setCapture(getElement());
				event.preventDefault();
				break;

			case Event.ONMOUSEUP:
				mouseDown = false;
				Event.releaseCapture(getElement());
				event.preventDefault();
				break;

			case Event.ONMOUSEMOVE:
				if (mouseDown) {
					int size;
					if (reverse) {
						size = getTargetPosition() + getTargetSize()
								- getEventPosition(event) - offset;
					} else {
						size = getEventPosition(event) - getTargetPosition()
								- offset;
					}
					setAssociatedWidgetSize(size);
					event.preventDefault();
				}
				break;
			}
		}

		public int getMinSize() {
			return minSize;
		}

		public void setMinSize(int minSize) {
			this.minSize = minSize;
			// Ignore max for this
			this.maxSize = Integer.MAX_VALUE;

			LayoutData layout = (LayoutData) target.getLayoutData();

			// Try resetting the associated widget's size, which will enforce
			// the new
			// minSize value.
			setAssociatedWidgetSize((int) layout.size);
		}

		protected abstract int getAbsolutePosition();

		protected abstract int getEventPosition(Event event);

		protected abstract int getTargetPosition();

		protected abstract int getTargetSize();

		protected abstract int getMaxSize();

		private void setAssociatedWidgetSize(int size) {
			if (size > maxSize)
				size = maxSize;

			if (size < minSize) {
				size = minSize;
			}

			LayoutData layout = (LayoutData) target.getLayoutData();
			if (size == layout.size) {
				return;
			}

			layout.size = size;

			// Defer actually updating the layout, so that if we receive many
			// mouse events before layout/paint occurs, we'll only update once.
			if (layoutCommand == null) {
				layoutCommand = new Command() {
					public void execute() {
						layoutCommand = null;
						forceLayout();
					}
				};
				DeferredCommand.addCommand(layoutCommand);
			}
		}

		public int getSize() {
			LayoutData layout = (LayoutData) target.getLayoutData();

			return (int) layout.size;
		}

		public void setSize(int size) {
			LayoutData layout = (LayoutData) target.getLayoutData();

			layout.size = size;
		}
	}

	public class HSplitter extends Splitter {
		public HSplitter(Widget target, boolean reverse) {
			super(target, reverse);
			getElement().getStyle().setPropertyPx("width", SPLITTER_SIZE);
			setStyleName("gwt-SplitLayoutPanel-HDragger");
		}

		@Override
		protected int getAbsolutePosition() {
			return getAbsoluteLeft();
		}

		@Override
		protected int getEventPosition(Event event) {
			return event.getClientX();
		}

		@Override
		protected int getTargetPosition() {
			return target.getAbsoluteLeft();
		}

		@Override
		protected int getTargetSize() {
			return target.getOffsetWidth();
		}

		@Override
		protected int getMaxSize() {
			return getCenter().getOffsetWidth() - centerMinWidth
					+ target.getOffsetWidth();
		}
	}

	public class VSplitter extends Splitter {
		public VSplitter(Widget target, boolean reverse) {
			super(target, reverse);
			getElement().getStyle().setPropertyPx("height", SPLITTER_SIZE);
			setStyleName("gwt-SplitLayoutPanel-VDragger");
		}

		@Override
		protected int getAbsolutePosition() {
			return getAbsoluteTop();
		}

		@Override
		protected int getEventPosition(Event event) {
			return event.getClientY();
		}

		@Override
		protected int getTargetPosition() {
			return target.getAbsoluteTop();
		}

		@Override
		protected int getTargetSize() {
			return target.getOffsetHeight();
		}

		@Override
		protected int getMaxSize() {
			return getCenter().getOffsetHeight() - centerMinHeight
					+ target.getOffsetHeight();
		}
	}

}
