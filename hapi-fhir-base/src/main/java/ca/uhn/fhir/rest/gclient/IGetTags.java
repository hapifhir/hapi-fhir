package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;

public interface IGetTags extends IClientExecutable<IGetTags, TagList> {

	IGetTags forResource(Class<? extends IResource> theClass);

	IGetTags forResource(Class<? extends IResource> theClass, String theId);

	IGetTags forResource(Class<? extends IResource> theClass, String theId, String theVersionId);

}
