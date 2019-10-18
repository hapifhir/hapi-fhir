package ca.uhn.fhir.jpa.binstore;

import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

/**
 * Wraps an Attachment datatype or Binary resource, since they both
 * hold binary content but don't look entirely similar
 */
interface IBinaryTarget {

	void setSize(Integer theSize);

	String getContentType();

	void setContentType(String theContentType);

	byte[] getData();

	void setData(byte[] theBytes);

	IBaseHasExtensions getTarget();

}
