package bio.knowledge.scrollgrid;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;

import bio.knowledge.scrollgrid.GridScrollDetectorRpc;

public abstract class GridScrollDetector extends AbstractExtension {

	public GridScrollDetector() {
		registerRpc(new GridScrollDetectorRpc() {

			@Override
			public void thresholdReached() {
				endHasBeenReached();
			}
		});
	}

	public void extend(Grid target) {
		super.extend(target);
	}

	public abstract void endHasBeenReached();
}