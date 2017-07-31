package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.util.CoverageIgnore;

final class AllowStatusChangeMetadata extends ResourceMetadataKeyEnum<Object> {
	private static final long serialVersionUID = 1;

	AllowStatusChangeMetadata(String theValue) {
		super(theValue);
	}

	@CoverageIgnore
	@Override
	public Object get(IResource theResource) {
		throw new UnsupportedOperationException();
	}

	@CoverageIgnore
	@Override
	public void put(IResource theResource, Object theObject) {
		throw new UnsupportedOperationException();
	}
}