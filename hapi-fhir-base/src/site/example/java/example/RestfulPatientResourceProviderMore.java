package example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.ITestClient;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

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

//START SNIPPET: searchMultiple
@Search()
public List<Observation> searchByObservationNames( @Required(name=Observation.SP_NAME) CodingListParam theCodings ) {
   // This search should return any observations matching one or more
   // of the codings here.
   List<CodingDt> wantedCodings = theCodings.getCodings();
   
   List<Observation> retVal = new ArrayList<Observation>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchMultiple

//START SNIPPET: dates
@Search()
public List<Patient> searchByObservationNames( @Required(name=Patient.SP_BIRTHDATE) QualifiedDateParam theDate ) {
   QuantityCompararatorEnum comparator = theDate.getComparator(); // e.g. <=
   Date date = theDate.getValue(); // e.g. 2011-01-02
   TemporalPrecisionEnum precision = theDate.getPrecision(); // e.g. DAY
	
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: dates

public void dateClientExample() {
ITestClient client = provideTc();
//START SNIPPET: dateClient
QualifiedDateParam param = new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02");
List<Patient> response = client.getPatientByDob(param);
//END SNIPPET: dateClient
}

private ITestClient provideTc() {
	return null;
}
@Override
public Class<? extends IResource> getResourceType() {
   return null;
}

}


