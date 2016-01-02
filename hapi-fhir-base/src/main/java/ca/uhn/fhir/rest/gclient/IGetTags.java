package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.TagList;

public interface IGetTags extends IClientExecutable<IGetTags, TagList> {

	IGetTags forResource(Class<? extends IBaseResource> theClass);

	IGetTags forResource(Class<? extends IBaseResource> theClass, String theId);

	IGetTags forResource(Class<? extends IBaseResource> theClass, String theId, String theVersionId);

}
