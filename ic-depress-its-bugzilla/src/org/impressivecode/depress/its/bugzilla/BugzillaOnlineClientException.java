package org.impressivecode.depress.its.bugzilla;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public class BugzillaOnlineClientException extends Exception {

	private static final long serialVersionUID = 5768161942969989883L;

	public BugzillaOnlineClientException() {
	}

	public BugzillaOnlineClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BugzillaOnlineClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugzillaOnlineClientException(String message) {
		super(message);
	}

	public BugzillaOnlineClientException(Throwable cause) {
		super(cause);
	}

}
