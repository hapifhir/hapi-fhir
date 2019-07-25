package ca.uhn.fhir.example.ex1;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

/**
 * Note, this is an incomplete example of a resource provider. It does not
 * do anything but is used as examples of methods that can be used.
 */
public class Example01_ResourceProviders implements IResourceProvider {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}

	@Read()
	public Patient read(@IdParam IdDt theId) {
		return null; // populate this 
	}
	
	@Create 
	void create(@ResourceParam Patient thePatient) {
		// save the resource
	}
	
	@Search
	List<Patient> search(
			@OptionalParam(name="family") StringParam theFamily,
			@OptionalParam(name="given") StringParam theGiven
			) {
		return null; // populate this
	}
	
}
