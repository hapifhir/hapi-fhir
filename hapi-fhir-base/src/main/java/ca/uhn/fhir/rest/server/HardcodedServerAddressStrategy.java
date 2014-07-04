package ca.uhn.fhir.rest.server;

import javax.servlet.http.HttpServletRequest;

/**
 * Server address strategy which simply returns a hardcoded URL
 */
public class HardcodedServerAddressStrategy implements IServerAddressStrategy {

	private String myValue;

	public HardcodedServerAddressStrategy() {
		//nothing
	}
	
	public HardcodedServerAddressStrategy(String theValue) {
		myValue=theValue;
	}

	@Override
	public String determineServerBase(HttpServletRequest theRequest) {
		return myValue;
	}

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

}
