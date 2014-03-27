package example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Id;
import ca.uhn.fhir.rest.annotation.Include;
import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.VersionId;
import ca.uhn.fhir.rest.client.ITestClient;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
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
public Patient getResourceById(@Id IdDt theId) {
   Patient retVal = new Patient();
   // ...populate...
   return retVal;
}
//END SNIPPET: read

//START SNIPPET: vread
@Read()
public Patient getResourceById(@Id IdDt theId, 
                               @VersionId IdDt theVersionId) {
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

//START SNIPPET: searchNamedQuery
@Search(queryName="namedQuery1")
public List<Patient> searchByNamedQuery(@Required(name="someparam") StringDt theSomeParam) {
 List<Patient> retVal = new ArrayList<Patient>();
 // ...populate...
 return retVal;
}
//END SNIPPET: searchNamedQuery

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



//START SNIPPET: pathSpec
@Search()
public List<DiagnosticReport> getDiagnosticReport( 
               @Required(name=DiagnosticReport.SP_IDENTIFIER) 
               IdentifierDt theIdentifier,
               @Include(allow= {"DiagnosticReport.subject"}) 
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
public List<Observation> getObservationsByDateRange(@Required(name="subject.identifier") IdentifierDt theSubjectId,
                                                    @Required(name=Observation.SP_DATE) DateRangeParam theRange) {
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

}


