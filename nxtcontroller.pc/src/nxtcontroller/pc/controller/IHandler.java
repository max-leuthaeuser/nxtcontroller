package nxtcontroller.pc.controller;

import nxtcontroller.pc.core.RemoteController;

/**
 * Interface which defines all methods a 
 * device handler should implement.
 * 
 * @author Max Leuthäuser
 */
public interface IHandler {
	boolean attach();
	void destroy();
	void startPolling();
	void setRemoteController(RemoteController remoteController);
}
