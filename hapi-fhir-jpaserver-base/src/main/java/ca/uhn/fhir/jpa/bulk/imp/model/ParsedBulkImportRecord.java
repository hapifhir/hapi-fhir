package ca.uhn.fhir.jpa.bulk.imp.model;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.Serializable;

public class ParsedBulkImportRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String myTenantName;
	private final IBaseResource myRowContent;

	public ParsedBulkImportRecord(String theTenantName, IBaseResource theRowContent) {
		myTenantName = theTenantName;
		myRowContent = theRowContent;
	}

	public String getTenantName() {
		return myTenantName;
	}

	public IBaseResource getRowContent() {
		return myRowContent;
	}
}
