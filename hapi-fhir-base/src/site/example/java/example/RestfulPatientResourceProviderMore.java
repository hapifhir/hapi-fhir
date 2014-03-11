package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.operations.Search;
import ca.uhn.fhir.rest.server.parameters.Optional;
import ca.uhn.fhir.rest.server.parameters.Required;

@SuppressWarnings("unused")
public class RestfulPatientResourceProviderMore implements IResourceProvider {

//START SNIPPET: searchAll
@Search
public List<Organization> getAllOrganizations() {
	List<Organization> retVal=new ArrayList<Organization>(); // populate this
	return retVal;
}
//END SNIPPET: searchAll

//START SNIPPET: read
@Read()
public Patient getResourceById(@Read.IdParam IdDt theId) {
	Patient retVal = new Patient();
	// ...populate...
	return retVal;
}
//END SNIPPET: read

//START SNIPPET: vread
@Read()
public Patient getResourceById(@Read.IdParam IdDt theId, 
                               @Read.VersionIdParam IdDt theVersionId) {
	Patient retVal = new Patient();
	// ...populate...
	return retVal;
}
//END SNIPPET: vread

//START SNIPPET: searchStringParam
@Search()
public List<Patient> searchByLastName(@Required(name=Patient.SP_FAMILY) StringDt theId) {
	List<Patient> retVal = new ArrayList<Patient>();
	// ...populate...
	return retVal;
}
//END SNIPPET: searchStringParam

//START SNIPPET: searchIdentifierParam
@Search()
public List<Patient> searchByIdentifier(@Required(name=Patient.SP_IDENTIFIER) IdentifierDt theId) {
	String identifierSystem = theId.getSystem().getValueAsString();
	String identifier = theId.getValue().getValue();
	
	List<Patient> retVal = new ArrayList<Patient>();
	// ...populate...
	return retVal;
}
//END SNIPPET: searchIdentifierParam

//START SNIPPET: searchOptionalParam
@Search()
public List<Patient> searchByNames( @Required(name=Patient.SP_FAMILY) StringDt theFamilyName,
                                    @Optional(name=Patient.SP_GIVEN)  StringDt theGivenName ) {
	String familyName = theFamilyName.getValue();
	String givenName = theGivenName != null ? theGivenName.getValue() : null;
	
	List<Patient> retVal = new ArrayList<Patient>();
	// ...populate...
	return retVal;
}
//END SNIPPET: searchOptionalParam

@Override
public Class<? extends IResource> getResourceType() {
	return null;
}

}


