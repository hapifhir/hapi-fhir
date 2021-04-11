package ca.uhn.fhir.jpa.bulk.imp.model;

import java.io.Serializable;

public class RawBulkImportRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String myTenantName;
	private final String myRowContent;

	public RawBulkImportRecord(String theTenantName, String theRowContent) {
		myTenantName = theTenantName;
		myRowContent = theRowContent;
	}

	public String getTenantName() {
		return myTenantName;
	}

	public String getRowContent() {
		return myRowContent;
	}
}
