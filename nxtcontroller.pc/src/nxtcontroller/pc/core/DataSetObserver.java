package nxtcontroller.pc.core;

import nxtcontroller.pc.ui.SensorPanel;

/**
 * Simple Observer interface which defines the operations
 * a observer should implement to show the values it
 * retrieves from the NXT. 
 * 
 * @see SensorPanel
 * 
 * @author Martin Morgenstern <s4810525@mail.zih.tu-dresden.de>
 */
public interface DataSetObserver {
	public void update(DataSet dataSet);
}
