package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.operations.Search;

public class RestfulPatientResourceProviderMore implements IResourceProvider {

//START SNIPPET: searchAll
@Search
public List<Organization> getAllOrganizations() {
	List<Organization> retVal=new ArrayList<Organization>(); // populate this
	return retVal;
}
//END SNIPPET: searchAll

@Override
public Class<? extends IResource> getResourceType() {
	return null;
}

}


