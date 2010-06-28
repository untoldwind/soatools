package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

public class LoadingLayoutPanel extends LayoutPanel {

	private Label loadingLabel;
	private int blockCount = 0;
	private Timer timer = new Timer() {
		@Override
		public void run() {
			loadingLabel.setStyleName("utils-loadingLabel");
		}

	};

	public LoadingLayoutPanel() {
		setStyleName("utils-loadingPanel");

		loadingLabel = new Label();
		loadingLabel.setStyleName("utils-loadingLabel");
	}

	public void block() {
		if ((blockCount++) == 0) {
			loadingLabel.setStyleName("utils-loadingLabel-block");
			add(loadingLabel);
			setWidgetLeftRight(loadingLabel, 0, Unit.PX, 0, Unit.PX);
			setWidgetTopBottom(loadingLabel, 0, Unit.PX, 0, Unit.PX);

			timer.schedule(300);
		}
	}
	
	public void hardBlock() {
		if ((blockCount++) == 0) {
			loadingLabel.setStyleName("utils-loadingLabel");
			add(loadingLabel);
			setWidgetLeftRight(loadingLabel, 0, Unit.PX, 0, Unit.PX);
			setWidgetTopBottom(loadingLabel, 0, Unit.PX, 0, Unit.PX);			
		}
	}

	public void unblock() {
		if (blockCount == 0)
			return;

		if ((--blockCount) == 0) {
			timer.cancel();
			remove(loadingLabel);
		}
	}
}
