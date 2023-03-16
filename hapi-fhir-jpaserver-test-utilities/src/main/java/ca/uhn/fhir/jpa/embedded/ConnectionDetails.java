package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

public class ConnectionDetails {

	private DriverTypeEnum myDriverType;
	private String myDriverClassName;
	private String myUsername;
	private String myPassword;
	private String myUrl;

	public ConnectionDetails(DriverTypeEnum theDriverType, String theUrl, String theUsername, String thePassword) {
		myDriverType = theDriverType;
		myDriverClassName = theDriverType.getDriverClassName();
		myUsername = theUsername;
		myPassword = thePassword;
		myUrl = theUrl;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public String getDriverClassName() {
		return myDriverClassName;
	}

	public String getUsername() {
		return myUsername;
	}

	public String getPassword() {
		return myPassword;
	}

	public String getUrl() {
		return myUrl;
	}
}
