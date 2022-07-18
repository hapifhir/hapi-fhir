package ca.uhn.fhir.jpa.term.models;

public class DeleteLinksPropertiesAndDesignationsResult {

	private int myDeletedLinks;

	private int myDeletedProperties;

	private int myDeletedDesignations;

	public int getDeletedLinks() {
		return myDeletedLinks;
	}

	public void setDeletedLinks(int theDeletedLinks) {
		myDeletedLinks = theDeletedLinks;
	}

	public int getDeletedProperties() {
		return myDeletedProperties;
	}

	public void setDeletedProperties(int theDeletedProperties) {
		myDeletedProperties = theDeletedProperties;
	}

	public int getDeletedDesignations() {
		return myDeletedDesignations;
	}

	public void setDeletedDesignations(int theDeletedDesignations) {
		myDeletedDesignations = theDeletedDesignations;
	}
}
