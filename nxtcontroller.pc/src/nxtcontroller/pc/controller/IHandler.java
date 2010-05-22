package nxtcontroller.pc.controller;

/**
 * Interface which defines all methods a 
 * device handler should implement.
 * 
 * @author Max Leuthäuser
 */
public interface IHandler {
	void attach();
	void destroy();
}
