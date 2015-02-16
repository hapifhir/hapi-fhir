package ca.uhn.fhir.model.base.resource;

import ca.uhn.fhir.model.api.IResource;

public interface BaseBinary extends IResource {

	byte[] getContent();

	String getContentAsBase64();

	String getContentType();

	void setContent(byte[] theContent);

	void setContentAsBase64(String theContent);

	void setContentType(String theContentType);
}
