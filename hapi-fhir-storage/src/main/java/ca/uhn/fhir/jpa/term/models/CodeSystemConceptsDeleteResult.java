package ca.uhn.fhir.jpa.term.models;

public class CodeSystemConceptsDeleteResult {

	private int myDeletedLinks;

	private int myDeletedProperties;

	private int myDeletedDesignations;

	private int myCodeSystemConceptDelete;

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

	public int getCodeSystemConceptDelete() {
		return myCodeSystemConceptDelete;
	}

	public void setCodeSystemConceptDelete(int theCodeSystemConceptDelete) {
		myCodeSystemConceptDelete = theCodeSystemConceptDelete;
	}
}
