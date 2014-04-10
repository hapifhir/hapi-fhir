package example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.ITestClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

@SuppressWarnings("unused")
public abstract class RestfulPatientResourceProviderMore implements IResourceProvider {

private boolean detectedVersionConflict;
private boolean conflictHappened;
private boolean couldntFindThisId;
//START SNIPPET: searchAll
@Search
public List<Organization> getAllOrganizations() {
   List<Organization> retVal=new ArrayList<Organization>(); // populate this
   return retVal;
}
//END SNIPPET: searchAll

//START SNIPPET: read
@Read()
public Patient getResourceById(@IdParam IdDt theId) {
   Patient retVal = new Patient();
   // ...populate...
   return retVal;
}
//END SNIPPET: read

//START SNIPPET: delete
@Read()
public void deletePatient(@IdParam IdDt theId) {
	// .. Delete the patient ..
	if (couldntFindThisId) {
		throw new ResourceNotFoundException("Unknown version");
	}
	if (conflictHappened) {
		throw new ResourceVersionConflictException("Couldn't delete because [foo]");
	}
	// otherwise, delete was successful
	return; // can also return MethodOutcome
}
//END SNIPPET: delete


//START SNIPPET: vread
@Read()
public Patient getResourceById(@IdParam IdDt theId, 
                               @VersionIdParam IdDt theVersionId) {
   Patient retVal = new Patient();
   // ...populate...
   return retVal;
}
//END SNIPPET: vread

//START SNIPPET: searchStringParam
@Search()
public List<Patient> searchByLastName(@RequiredParam(name=Patient.SP_FAMILY) StringDt theId) {
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchStringParam

//START SNIPPET: searchNamedQuery
@Search(queryName="namedQuery1")
public List<Patient> searchByNamedQuery(@RequiredParam(name="someparam") StringDt theSomeParam) {
 List<Patient> retVal = new ArrayList<Patient>();
 // ...populate...
 return retVal;
}
//END SNIPPET: searchNamedQuery

//START SNIPPET: searchIdentifierParam
@Search()
public List<Patient> searchByIdentifier(@RequiredParam(name=Patient.SP_IDENTIFIER) IdentifierDt theId) {
   String identifierSystem = theId.getSystem().getValueAsString();
   String identifier = theId.getValue().getValue();
   
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchIdentifierParam

//START SNIPPET: searchOptionalParam
@Search()
public List<Patient> searchByNames( @RequiredParam(name=Patient.SP_FAMILY) StringDt theFamilyName,
                                    @OptionalParam(name=Patient.SP_GIVEN)  StringDt theGivenName ) {
   String familyName = theFamilyName.getValue();
   String givenName = theGivenName != null ? theGivenName.getValue() : null;
   
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchOptionalParam

//START SNIPPET: searchMultiple
@Search()
public List<Observation> searchByObservationNames( @RequiredParam(name=Observation.SP_NAME) CodingListParam theCodings ) {
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
public List<Patient> searchByObservationNames( @RequiredParam(name=Patient.SP_BIRTHDATE) QualifiedDateParam theDate ) {
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



//START SNIPPET: pathSpec
@Search()
public List<DiagnosticReport> getDiagnosticReport( 
               @RequiredParam(name=DiagnosticReport.SP_IDENTIFIER) 
               IdentifierDt theIdentifier,
               @IncludeParam(allow= {"DiagnosticReport.subject"}) 
               Set<PathSpecification> theIncludes ) {
  List<DiagnosticReport> retVal = new ArrayList<DiagnosticReport>();
 
  // Assume this method exists and loads the report from the DB
  DiagnosticReport report = loadSomeDiagnosticReportFromDatabase(theIdentifier);

  // If the client has asked for the subject to be included:
  if (theIncludes.contains(new PathSpecification("DiagnosticReport.subject"))) {
	 
    // The resource reference should contain the ID of the patient
    IdDt subjectId = report.getSubject().getId();
	
    // So load the patient ID and return it
    Patient subject = loadSomePatientFromDatabase(subjectId);
    report.getSubject().setResource(subject);
	
  }
 
  retVal.add(report);
  return retVal;
}
//END SNIPPET: pathSpec

//START SNIPPET: dateRange
@Search()
public List<Observation> getObservationsByDateRange(@RequiredParam(name="subject.identifier") IdentifierDt theSubjectId,
                                                    @RequiredParam(name=Observation.SP_DATE) DateRangeParam theRange) {
  List<Observation> retVal = new ArrayList<Observation>();
  
  // The following two will be set as the start and end
  // of the range specified by the query parameter
  Date from = theRange.getLowerBoundAsInstant();
  Date to   = theRange.getUpperBoundAsInstant();
  
  // ... populate ...
  return retVal;
}
//END SNIPPET: dateRange

private DiagnosticReport loadSomeDiagnosticReportFromDatabase(IdentifierDt theIdentifier) {
	return null;
}

private Patient loadSomePatientFromDatabase(IdDt theId) {
	return null;
}


//START SNIPPET: create
@Create
public MethodOutcome createPatient(@ResourceParam Patient thePatient) {

  /* 
   * First we might want to do business validation. The UnprocessableEntityException
   * results in an HTTP 422, which is appropriate for business rule failure
   */
  if (thePatient.getIdentifierFirstRep().isEmpty()) {
    throw new UnprocessableEntityException("No identifier supplied");
  }
	
  // Save this patient to the database...
  savePatientToDatabase(thePatient);

  // This method returns a MethodOutcome object which contains
  // the ID and Version ID for the newly saved resource
  MethodOutcome retVal = new MethodOutcome();
  retVal.setCreated(true);
  retVal.setId(new IdDt("3746"));
  retVal.setVersionId(new IdDt("1"));
  return retVal;
}
//END SNIPPET: create

//START SNIPPET: createClient
@Create
public abstract MethodOutcome createNewPatient(@ResourceParam Patient thePatient);
//END SNIPPET: createClient


//START SNIPPET: update
@Update
public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient) {

  /* 
   * First we might want to do business validation. The UnprocessableEntityException
   * results in an HTTP 422, which is appropriate for business rule failure
   */
  if (thePatient.getIdentifierFirstRep().isEmpty()) {
    throw new UnprocessableEntityException("No identifier supplied");
  }
	
  // Save this patient to the database...
  savePatientToDatabase(theId, thePatient);

  // This method returns a MethodOutcome object which contains
  // the ID and Version ID for the newly saved resource
  MethodOutcome retVal = new MethodOutcome();
  retVal.setCreated(true);
  retVal.setId(theId);
  retVal.setVersionId(new IdDt("2")); // Leave this blank if the server doesn't version
  return retVal;
}
//END SNIPPET: update

//START SNIPPET: updateClient
@Update
public abstract MethodOutcome updateSomePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient);
//END SNIPPET: updateClient

//START SNIPPET: updateVersion
@Update
public MethodOutcome updatePatient(@IdParam IdDt theId, @VersionIdParam IdDt theVersionId, @ResourceParam Patient thePatient) {
  // ..Process..
  if (detectedVersionConflict) {
	  throw new ResourceVersionConflictException("Invalid version");
  }
  MethodOutcome retVal = new MethodOutcome();
return retVal;
}
//END SNIPPET: updateVersion




public static void main(String[] args) throws DataFormatException, IOException {


}


private void savePatientToDatabase(Patient thePatient) {
	// nothing
}
private void savePatientToDatabase(IdDt theId, Patient thePatient) {
	// nothing
}

//START SNIPPET: metadataProvider
public class ConformanceProvider {

  @Metadata
  public Conformance getServerMetadata() {
    Conformance retVal = new Conformance();
    // ..populate..
    return retVal;
  }

}
//END SNIPPET: metadataProvider



//START SNIPPET: metadataClient
public interface MetadataClient extends IRestfulClient {
  
  @Metadata
  Conformance getServerMetadata();
  
  // ....Other methods can also be added as usual....
  
}
//END SNIPPET: metadataClient

public void bbbbb() throws DataFormatException, IOException {
//START SNIPPET: metadataClientUsage
FhirContext ctx = new FhirContext();
MetadataClient client = ctx.newRestfulClient(MetadataClient.class, "http://spark.furore.com/fhir");
Conformance metadata = client.getServerMetadata();
System.out.println(ctx.newXmlParser().encodeResourceToString(metadata));
//END SNIPPET: metadataClientUsage
}


}


