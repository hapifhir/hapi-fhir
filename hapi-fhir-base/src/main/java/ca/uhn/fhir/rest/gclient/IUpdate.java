package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.IResource;

public interface IUpdate {

	IUpdateTyped resource(IResource theResource);

	IUpdateTyped resource(String theResourceBody);

}
