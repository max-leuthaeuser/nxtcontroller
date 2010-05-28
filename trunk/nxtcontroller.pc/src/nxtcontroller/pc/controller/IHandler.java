package nxtcontroller.pc.controller;

import nxtcontroller.pc.core.RemoteController;

/**
 * Interface which defines all methods a 
 * device handler should implement.
 * 
 * @author Max Leuth�user
 */
public interface IHandler {
	boolean attach();
	void destroy();
	void setRemoteController(RemoteController remoteController);
}
