package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.model.primitive.IdDt;

public interface IReadTyped<T extends IBaseResource> {

	IReadExecutable<T> withId(String theId);

	IReadExecutable<T> withIdAndVersion(String theId, String theVersion);

	/**
	 * Search using an ID. Note that even if theId contains a base URL it will be
	 * ignored in favour of the base url for the given client. If you want to specify 
	 * an absolute URL including a base and have that base used instead, use
	 * {@link #withUrl(IdDt)}
	 */
	IReadExecutable<T> withId(IdDt theId);

	IReadExecutable<T> withUrl(String theUrl);

	IReadExecutable<T> withUrl(IdDt theUrl);
}
