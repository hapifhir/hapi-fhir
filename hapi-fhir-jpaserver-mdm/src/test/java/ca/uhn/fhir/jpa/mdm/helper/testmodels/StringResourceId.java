package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;

/**
 * A test ResourceId with a non-long implementation of the id
 */
public class StringResourceId extends BaseResourcePersistentId<String> {

	private final String myId;

	public StringResourceId(String theId) {
		super(null);
		myId = theId;
	}

	public StringResourceId(Long theVersion, String theId) {
		super(theVersion, null);
		myId = theId;
	}

	@Override
	public String getId() {
		return myId;
	}
}
