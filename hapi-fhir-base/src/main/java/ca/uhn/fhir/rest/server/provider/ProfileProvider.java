package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class ProfileProvider implements IResourceProvider {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Profile.class;
	}
	
	public ProfileProvider(FhirContext theCtx) {
		Profile p = new Profile();
//		p.
	}

}
