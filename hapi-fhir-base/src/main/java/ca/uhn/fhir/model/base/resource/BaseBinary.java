package ca.uhn.fhir.model.base.resource;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;

public abstract class BaseBinary extends BaseResource implements IResource {

	public abstract byte[] getContent();
	
	public abstract String getContentType();
	
}
