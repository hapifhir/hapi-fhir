package ca.uhn.fhir.rest.server.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.operations.Search;

public class ProfileProvider implements IResourceProvider {

	private FhirContext myContext;

	public ProfileProvider(FhirContext theContext) {
		myContext = theContext;
	}
	
	@Override
	public Class<? extends IResource> getResourceType() {
		return Profile.class;
	}
	
	@Read()
	public Profile getProfileById(@Read.IdParam IdDt theId) {
		RuntimeResourceDefinition retVal = myContext.getResourceDefinitionById(theId.getValue());
		if (retVal==null) {
			return null;
		}
		return retVal.toProfile();
	}

	@Search()
	public List<Profile> getAllProfiles() {
		List<RuntimeResourceDefinition> defs = new ArrayList<RuntimeResourceDefinition>(myContext.getResourceDefinitions());
		Collections.sort(defs, new Comparator<RuntimeResourceDefinition>() {
			@Override
			public int compare(RuntimeResourceDefinition theO1, RuntimeResourceDefinition theO2) {
				int cmp = theO1.getName().compareTo(theO2.getName());
				if (cmp==0) {
					cmp=theO1.getResourceProfile().compareTo(theO2.getResourceProfile());
				}
				return cmp;
			}});
		ArrayList<Profile> retVal = new ArrayList<Profile>();
		for (RuntimeResourceDefinition next : defs) {
			retVal.add(next.toProfile());
		}
		return retVal;
	}

}
