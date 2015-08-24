package example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.AddTags;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.DeleteTags;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

@SuppressWarnings("unused")
public abstract class RestfulPatientResourceProviderMore implements IResourceProvider {

   public interface ITestClient extends IBasicClient
   {

      @Search
      List<Patient> getPatientByDob(@RequiredParam(name=Patient.SP_BIRTHDATE) DateParam theParam);
      
   }
   
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


//START SNIPPET: summaryAndElements
@Search
public List<Patient> search(
      SummaryEnum theSummary, // will receive the summary (no annotation required)
      @Elements Set<String> theElements // (requires the @Elements annotation)
      ) {
   return null; // todo: populate
}
//END SNIPPET: summaryAndElements

//START SNIPPET: searchCompartment
public class PatientRp implements IResourceProvider {
   
   @Override
   public Class<? extends IResource> getResourceType() {
      return Patient.class;
   }
   
   @Search(compartmentName="Condition")
   public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
      List<IResource> retVal=new ArrayList<IResource>(); 
      
      // populate this with resources of any type that are a part of the
      // "Condition" compartment for the Patient with ID "thePatientId"
      
      return retVal;
   }

   // .. also include other Patient operations ..
}
//END SNIPPET: searchCompartment


//START SNIPPET: sort
@Search
public List<Patient> findPatients(
		@RequiredParam(name=Patient.SP_IDENTIFIER) StringParam theParameter,
		@Sort SortSpec theSort) {
   List<Patient> retVal=new ArrayList<Patient>(); // populate this

   // theSort is null unless a _sort parameter is actually provided 
   if (theSort != null) {
      
      // The name of the param to sort by
      String param = theSort.getParamName();

      // The sort order, or null
      SortOrderEnum order = theSort.getOrder();

      // This will be populated if a second _sort was specified
      SortSpec subSort = theSort.getChain();
      
      // ...apply the sort...
   }

   return retVal;
}
//END SNIPPET: sort

//START SNIPPET: underlyingReq
@Search
public List<Patient> findPatients(
		@RequiredParam(name="foo") StringParam theParameter,
		HttpServletRequest theRequest, 
		HttpServletResponse theResponse) {
 List<Patient> retVal=new ArrayList<Patient>(); // populate this
 return retVal;
}
//END SNIPPET: underlyingReq

//START SNIPPET: referenceSimple
@Search
public List<Patient> findPatientsWithSimpleReference(
		@OptionalParam(name=Patient.SP_CAREPROVIDER) ReferenceParam theProvider
		) {
   List<Patient> retVal=new ArrayList<Patient>();

   // If the parameter passed in includes a resource type (e.g. ?provider:Patient=123)
   // that resoruce type is available. Here we just check that it is either not provided
   // or set to "Patient"
   if (theProvider.hasResourceType()) {
      String resourceType = theProvider.getResourceType();
      if ("Patient".equals(resourceType) == false) {
         throw new InvalidRequestException("Invalid resource type for parameter 'provider': " + resourceType);
      }
   }
   
   if (theProvider != null) {
      // ReferenceParam extends IdDt so all of the resource ID methods are available
      String providerId = theProvider.getIdPart();
      
      // .. populate retVal will Patient resources having provider with id "providerId" ..
      
   }
   
   return retVal;

}
//END SNIPPET: referenceSimple


//START SNIPPET: referenceWithChain
@Search
public List<DiagnosticReport> findReportsWithChain(
    @RequiredParam(name=DiagnosticReport.SP_SUBJECT, chainWhitelist= {Patient.SP_FAMILY, Patient.SP_GENDER}) ReferenceParam theSubject
    ) {
   List<DiagnosticReport> retVal=new ArrayList<DiagnosticReport>();

   String chain = theSubject.getChain();
   if (Patient.SP_FAMILY.equals(chain)) {
      String familyName = theSubject.getValue();
      // .. populate with reports matching subject family name ..
   }
   if (Patient.SP_GENDER.equals(chain)) {
      String gender = theSubject.getValue();
      // .. populate with reports matching subject gender ..
   }

   return retVal;
}
//END SNIPPET: referenceWithChain


//START SNIPPET: referenceWithChainCombo
@Search
public List<DiagnosticReport> findReportsWithChainCombo (
  @RequiredParam(name=DiagnosticReport.SP_SUBJECT, chainWhitelist= {"", Patient.SP_FAMILY}) ReferenceParam theSubject
  ) {
   List<DiagnosticReport> retVal=new ArrayList<DiagnosticReport>();

   String chain = theSubject.getChain();
   if (Patient.SP_FAMILY.equals(chain)) {
      String familyName = theSubject.getValue();
      // .. populate with reports matching subject family name ..
   }
   if ("".equals(chain)) {
      String resourceId = theSubject.getValue();
      // .. populate with reports matching subject with resource ID ..
   }

   return retVal;
}
//END SNIPPET: referenceWithChainCombo


//START SNIPPET: referenceWithStaticChain
@Search
public List<Patient> findObservations(
		@RequiredParam(name=Observation.SP_SUBJECT+'.'+Patient.SP_IDENTIFIER) TokenParam theProvider
		) {

  String system = theProvider.getSystem();
  String identifier = theProvider.getValue();

  // ...Do a search for all observations for the given subject...
  
  List<Patient> retVal=new ArrayList<Patient>(); // populate this
  return retVal;

}
//END SNIPPET: referenceWithStaticChain


//START SNIPPET: referenceWithDynamicChain
@Search()
public List<Observation> findBySubject(
      @RequiredParam(name=Observation.SP_SUBJECT, chainWhitelist = {"", Patient.SP_IDENTIFIER, Patient.SP_BIRTHDATE}) ReferenceParam subject
      ) {
    List<Observation> observations = new ArrayList<Observation>();

    String chain = subject.getChain();
    if (Patient.SP_IDENTIFIER.equals(chain)) {

       // Because the chained parameter "subject.identifier" is actually of type
       // "token", we convert the value to a token before processing it. 
       TokenParam tokenSubject = subject.toTokenParam();
       String system = tokenSubject.getSystem();
       String identifier = tokenSubject.getValue();
       
       // TODO: populate all the observations for the identifier
       
    } else if (Patient.SP_BIRTHDATE.equals(chain)) {

          // Because the chained parameter "subject.birthdate" is actually of type
          // "date", we convert the value to a date before processing it. 
          DateParam dateSubject = subject.toDateParam();
          DateTimeDt birthDate = dateSubject.getValueAsDateTimeDt();
          
          // TODO: populate all the observations for the birthdate
          
    } else if ("".equals(chain)) {
        
       String resourceId = subject.getValue();
        // TODO: populate all the observations for the resource id
        
    }

    return observations;
}
//END SNIPPET: referenceWithDynamicChain


//START SNIPPET: read
@Read()
public Patient getResourceById(@IdParam IdDt theId) {
   Patient retVal = new Patient();
   
   // ...populate...
   retVal.addIdentifier().setSystem("urn:mrns").setValue("12345");
   retVal.addName().addFamily("Smith").addGiven("Tester").addGiven("Q");
   // ...etc...
   
   // if you know the version ID of the resource, you should set it and HAPI will 
   // include it in a Content-Location header
   retVal.setId(new IdDt("Patient", "123", "2"));
   
   return retVal;
}
//END SNIPPET: read

//START SNIPPET: delete
@Delete()
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


//START SNIPPET: deleteConditional
@Delete()
public void deletePatientConditional(@IdParam IdDt theId, @ConditionalUrlParam String theConditionalUrl) {
   // Only one of theId or theConditionalUrl will have a value depending
   // on whether the URL receieved was a logical ID, or a conditional
   // search string
   if (theId != null) {
      // do a normal delete
   } else {
      // do a conditional delete
   }
   
   // otherwise, delete was successful
   return; // can also return MethodOutcome
}
//END SNIPPET: deleteConditional

//START SNIPPET: history
@History()
public List<Patient> getPatientHistory(@IdParam IdDt theId) {
   List<Patient> retVal = new ArrayList<Patient>();
   
   Patient patient = new Patient();
   patient.addName().addFamily("Smith");
   
   // Set the ID and version
   patient.setId(theId.withVersion("1"));
   
   // ...populate the rest...
   return retVal;
}
//END SNIPPET: history


//START SNIPPET: vread
@Read(version=true)
public Patient readOrVread(@IdParam IdDt theId) {
   Patient retVal = new Patient();

   if (theId.hasVersionIdPart()) {
      // this is a vread   
   } else {
      // this is a read
   }

   // ...populate...
   
   return retVal;
}
//END SNIPPET: vread

//START SNIPPET: searchStringParam
@Search()
public List<Patient> searchByLastName(@RequiredParam(name=Patient.SP_FAMILY) StringParam theFamily) {
   String valueToMatch = theFamily.getValue();
   
   if (theFamily.isExact()) {
	   // Do an exact match search
   } else {
	   // Do a fuzzy search if possible
   }
   
   // ...populate...
   Patient patient = new Patient();
   patient.addIdentifier().setSystem("urn:mrns").setValue("12345");
   patient.addName().addFamily("Smith").addGiven("Tester").addGiven("Q");
   // ...etc...

   //  Every returned resource must have its logical ID set. If the server
   //  supports versioning, that should be set too
   String logicalId = "4325";
   String versionId = "2"; // optional
   patient.setId(new IdDt("Patient", logicalId, versionId));
   
   /*
    * This is obviously a fairly contrived example since we are always
    * just returning the same hardcoded patient, but in a real scenario
    * you could return as many resources as you wanted, and they
    * should actually match the given search criteria.
    */
   List<Patient> retVal = new ArrayList<Patient>();
   retVal.add(patient);
   
   return retVal;
}
//END SNIPPET: searchStringParam

//START SNIPPET: searchNamedQuery
@Search(queryName="namedQuery1")
public List<Patient> searchByNamedQuery(@RequiredParam(name="someparam") StringParam theSomeParam) {
 List<Patient> retVal = new ArrayList<Patient>();
 // ...populate...
 return retVal;
}
//END SNIPPET: searchNamedQuery

//START SNIPPET: searchComposite
@Search()
public List<Observation> searchByComposite(
		@RequiredParam(name=Observation.SP_CODE_VALUE_DATE, compositeTypes= {TokenParam.class, DateParam.class}) 
		CompositeParam<TokenParam, DateParam> theParam) {
  // Each of the two values in the composite param are accessible separately.
  // In the case of Observation's name-value-date, the left is a string and
  // the right is a date.
  TokenParam observationName = theParam.getLeftValue();
  DateParam observationValue = theParam.getRightValue();
	
  List<Observation> retVal = new ArrayList<Observation>();
  // ...populate...
  return retVal;
}
//END SNIPPET: searchComposite


//START SNIPPET: searchIdentifierParam
@Search()
public List<Patient> searchByIdentifier(@RequiredParam(name=Patient.SP_IDENTIFIER) TokenParam theId) {
   String identifierSystem = theId.getSystem();
   String identifier = theId.getValue();
   
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchIdentifierParam

//START SNIPPET: searchOptionalParam
@Search()
public List<Patient> searchByNames( @RequiredParam(name=Patient.SP_FAMILY) StringParam theFamilyName,
                                    @OptionalParam(name=Patient.SP_GIVEN)  StringParam theGivenName ) {
   String familyName = theFamilyName.getValue();
   String givenName = theGivenName != null ? theGivenName.getValue() : null;
   
   List<Patient> retVal = new ArrayList<Patient>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchOptionalParam

//START SNIPPET: searchWithDocs
@Description(shortDefinition="This search finds all patient resources matching a given name combination")
@Search()
public List<Patient> searchWithDocs( 
          @Description(shortDefinition="This is the patient's last name - Supports partial matches")
          @RequiredParam(name=Patient.SP_FAMILY) StringParam theFamilyName,

          @Description(shortDefinition="This is the patient's given names")
          @OptionalParam(name=Patient.SP_GIVEN)  StringParam theGivenName ) {
	
  List<Patient> retVal = new ArrayList<Patient>();
  // ...populate...
  return retVal;
}
//END SNIPPET: searchWithDocs


//START SNIPPET: searchMultiple
@Search()
public List<Observation> searchByObservationNames( 
		@RequiredParam(name=Observation.SP_CODE) TokenOrListParam theCodings ) {

   // The list here will contain 0..* codings, and any observations which match any of the 
   // given codings should be returned
   List<BaseCodingDt> wantedCodings = theCodings.getListAsCodings();
   
   List<Observation> retVal = new ArrayList<Observation>();
   // ...populate...
   return retVal;
}
//END SNIPPET: searchMultiple


//START SNIPPET: searchMultipleAnd
@Search()
public List<Patient> searchByPatientAddress( 
		@RequiredParam(name=Patient.SP_ADDRESS) StringAndListParam theAddressParts ) {

  // StringAndListParam is a container for 0..* StringOrListParam, which is in turn a
  // container for 0..* strings. It is a little bit weird to understand at first, but think of the
  // StringAndListParam to be an AND list with multiple OR lists inside it. So you will need 
  // to return results which match at least one string within every OR list. 
  List<StringOrListParam> wantedCodings = theAddressParts.getValuesAsQueryTokens();
  for (StringOrListParam nextOrList : wantedCodings) {
    List<StringParam> queryTokens = nextOrList.getValuesAsQueryTokens();
	// Only return results that match at least one of the tokens in the list below
    for (StringParam nextString : queryTokens) {
    	   // ....check for match...
    }
  }
  
  List<Patient> retVal = new ArrayList<Patient>();
  // ...populate...
  return retVal;
}
//END SNIPPET: searchMultipleAnd


//START SNIPPET: dates
@Search()
public List<Patient> searchByObservationNames( @RequiredParam(name=Patient.SP_BIRTHDATE) DateParam theDate ) {
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
DateParam param = new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02");
List<Patient> response = client.getPatientByDob(param);
//END SNIPPET: dateClient
}

//START SNIPPET: dateRange
@Search()
public List<Observation> searchByDateRange(
    @RequiredParam(name=Observation.SP_DATE) DateRangeParam theRange ) {
  
  Date from = theRange.getLowerBoundAsInstant();
  Date to = theRange.getUpperBoundAsInstant();
	
  List<Observation> retVal = new ArrayList<Observation>();
  // ...populate...
  return retVal;
}
//END SNIPPET: dateRange


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
               TokenParam theIdentifier,
               
               @IncludeParam(allow= {"DiagnosticReport.subject"}) 
               Set<Include> theIncludes ) {
	
  List<DiagnosticReport> retVal = new ArrayList<DiagnosticReport>();
 
  // Assume this method exists and loads the report from the DB
  DiagnosticReport report = loadSomeDiagnosticReportFromDatabase(theIdentifier);

  // If the client has asked for the subject to be included:
  if (theIncludes.contains(new Include("DiagnosticReport.subject"))) {
	 
    // The resource reference should contain the ID of the patient
    IdDt subjectId = report.getSubject().getReference();
	
    // So load the patient ID and return it
    Patient subject = loadSomePatientFromDatabase(subjectId);
    report.getSubject().setResource(subject);
	
  }
 
  retVal.add(report);
  return retVal;
}
//END SNIPPET: pathSpec

//START SNIPPET: revInclude
@Search()
public List<DiagnosticReport> getDiagnosticReport( 
             @RequiredParam(name=DiagnosticReport.SP_IDENTIFIER) 
             TokenParam theIdentifier,
             
             @IncludeParam() 
             Set<Include> theIncludes, 
             
             @IncludeParam(reverse=true)
             Set<Include> theReverseIncludes
         ) {
 
return new ArrayList<DiagnosticReport>(); // populate this
}
//END SNIPPET: revInclude

//START SNIPPET: pathSpecSimple
@Search()
public List<DiagnosticReport> getDiagnosticReport( 
             @RequiredParam(name=DiagnosticReport.SP_IDENTIFIER) 
             TokenParam theIdentifier,
             
             @IncludeParam(allow= {"DiagnosticReport.subject"}) 
             String theInclude ) {
	
  List<DiagnosticReport> retVal = new ArrayList<DiagnosticReport>();

  // Assume this method exists and loads the report from the DB
  DiagnosticReport report = loadSomeDiagnosticReportFromDatabase(theIdentifier);

  // If the client has asked for the subject to be included:
  if ("DiagnosticReport.subject".equals(theInclude)) {
	 
    // The resource reference should contain the ID of the patient
    IdDt subjectId = report.getSubject().getReference();
	
    // So load the patient ID and return it
    Patient subject = loadSomePatientFromDatabase(subjectId);
    report.getSubject().setResource(subject);
	
  }

  retVal.add(report);
  return retVal;
}
//END SNIPPET: pathSpecSimple

//START SNIPPET: quantity
@Search()
public List<Observation> getObservationsByQuantity(
        @RequiredParam(name=Observation.SP_VALUE_QUANTITY) QuantityParam theQuantity) {
  
  List<Observation> retVal = new ArrayList<Observation>();
  
  QuantityCompararatorEnum comparator = theQuantity.getComparator();
  DecimalDt value = theQuantity.getValue();
  String units = theQuantity.getUnits();
  // .. Apply these parameters ..
  
  // ... populate ...
  return retVal;
}
//END SNIPPET: quantity

private DiagnosticReport loadSomeDiagnosticReportFromDatabase(TokenParam theIdentifier) {
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
    /* It is also possible to pass an OperationOutcome resource
     * to the UnprocessableEntityException if you want to return
     * a custom populated OperationOutcome. Otherwise, a simple one
     * is created using the string supplied below. 
     */
    throw new UnprocessableEntityException("No identifier supplied");
  }
	
  // Save this patient to the database...
  savePatientToDatabase(thePatient);

  // This method returns a MethodOutcome object which contains
  // the ID (composed of the type Patient, the logical ID 3746, and the
  // version ID 1)
  MethodOutcome retVal = new MethodOutcome();
  retVal.setId(new IdDt("Patient", "3746", "1"));
  
  // You can also add an OperationOutcome resource to return
  // This part is optional though:
  OperationOutcome outcome = new OperationOutcome();
  outcome.addIssue().setDiagnostics("One minor issue detected");
  retVal.setOperationOutcome(outcome);  
  
  return retVal;
}
//END SNIPPET: create


//START SNIPPET: createConditional
@Create
public MethodOutcome createPatientConditional(
      @ResourceParam Patient thePatient,
      @ConditionalUrlParam String theConditionalUrl) {

   if (theConditionalUrl != null) {
      // We are doing a conditional create

      // populate this with the ID of the existing resource which 
      // matches the conditional URL
      return new MethodOutcome();  
   } else {
      // We are doing a normal create
      
      // populate this with the ID of the newly created resource
      return new MethodOutcome();  
   }
   
}
//END SNIPPET: createConditional


//START SNIPPET: createClient
@Create
public abstract MethodOutcome createNewPatient(@ResourceParam Patient thePatient);
//END SNIPPET: createClient

//START SNIPPET: updateConditional
@Update
public MethodOutcome updatePatientConditional(
      @ResourceParam Patient thePatient, 
      @IdParam IdDt theId, 
      @ConditionalUrlParam String theConditional) {

   // Only one of theId or theConditional will have a value and the other will be null,
   // depending on the URL passed into the server. 
   if (theConditional != null) {
      // Do a conditional update. theConditional will have a value like "Patient?identifier=system%7C00001"
   } else {
      // Do a normal update. theId will have the identity of the resource to update
   }
   
   return new MethodOutcome(); // populate this
}
//END SNIPPET: updateConditional

//START SNIPPET: updatePrefer
@Update
public MethodOutcome updatePatientPrefer(
    @ResourceParam Patient thePatient, 
    @IdParam IdDt theId) {

   // Save the patient to the database
   
   // Update the version and last updated time on the resource
   IdDt updatedId = theId.withVersion("123");
   thePatient.setId(updatedId);
   InstantDt lastUpdated = InstantDt.withCurrentTime();
   ResourceMetadataKeyEnum.UPDATED.put(thePatient, lastUpdated);
   
   // Add the resource to the outcome, so that it can be returned by the server
   // if the client requests it
   MethodOutcome outcome = new MethodOutcome();
   outcome.setId(updatedId);
   outcome.setResource(thePatient);
   return outcome;
}
//END SNIPPET: updatePrefer

//START SNIPPET: updateRaw
@Update
public MethodOutcome updatePatientWithRawValue (
    @ResourceParam Patient thePatient, 
    @IdParam IdDt theId, 
    @ResourceParam String theRawBody,
    @ResourceParam EncodingEnum theEncodingEnum) {

   // Here, thePatient will have the parsed patient body, but
   // theRawBody will also have the raw text of the resource 
   // being created, and theEncodingEnum will tell you which
   // encoding was used
 
 return new MethodOutcome(); // populate this
}
//END SNIPPET: updateRaw

//START SNIPPET: update
@Update
public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient) {

  /* 
   * First we might want to do business validation. The UnprocessableEntityException
   * results in an HTTP 422, which is appropriate for business rule failure
   */
  if (thePatient.getIdentifierFirstRep().isEmpty()) {
    /* It is also possible to pass an OperationOutcome resource
     * to the UnprocessableEntityException if you want to return
     * a custom populated OperationOutcome. Otherwise, a simple one
     * is created using the string supplied below. 
     */
    throw new UnprocessableEntityException("No identifier supplied");
  }

  String versionId = theId.getVersionIdPart();
  if (versionId != null) {
    // If the client passed in a version number in an If-Match header, they are
    // doing a version-aware update. You may wish to throw an exception if the supplied
    // version is not the latest version. Note that as of DSTU2 the FHIR specification uses
    // ETags and If-Match to handle version aware updates, so PreconditionFailedException (HTTP 412)
    // is used instead of ResourceVersionConflictException (HTTP 409)
    if (detectedVersionConflict) {
      throw new PreconditionFailedException("Unexpected version");
    }
  }
  
  // Save this patient to the database...
  savePatientToDatabase(theId, thePatient);

  // This method returns a MethodOutcome object which contains
  // the ID and Version ID for the newly saved resource
  MethodOutcome retVal = new MethodOutcome();
  String newVersion = "2"; // may be null if the server is not version aware
  retVal.setId(theId.withVersion(newVersion));
  
  // You can also add an OperationOutcome resource to return
  // This part is optional though:
  OperationOutcome outcome = new OperationOutcome();
  outcome.addIssue().setDiagnostics("One minor issue detected");
  retVal.setOperationOutcome(outcome);
  
  // If your server supports creating resources during an update if they don't already exist
  // (this is not mandatory and may not be desirable anyhow) you can flag in the response
  // that this was a creation as follows:
 // retVal.setCreated(true);
  
  return retVal;
}
//END SNIPPET: update

//START SNIPPET: updateClient
@Update
public abstract MethodOutcome updateSomePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient);
//END SNIPPET: updateClient

//START SNIPPET: validate
@Validate
public MethodOutcome validatePatient(@ResourceParam Patient thePatient, 
                                     @Validate.Mode ValidationModeEnum theMode,
                                     @Validate.Profile String theProfile) {

  // Actually do our validation: The UnprocessableEntityException
  // results in an HTTP 422, which is appropriate for business rule failure
  if (thePatient.getIdentifierFirstRep().isEmpty()) {
    /* It is also possible to pass an OperationOutcome resource
     * to the UnprocessableEntityException if you want to return
     * a custom populated OperationOutcome. Otherwise, a simple one
     * is created using the string supplied below. 
     */
    throw new UnprocessableEntityException("No identifier supplied");
  }
	
  // This method returns a MethodOutcome object
  MethodOutcome retVal = new MethodOutcome();

  // You may also add an OperationOutcome resource to return
  // This part is optional though:
  OperationOutcome outcome = new OperationOutcome();
  outcome.addIssue().setSeverity(IssueSeverityEnum.WARNING).setDiagnostics("One minor issue detected");
  retVal.setOperationOutcome(outcome);  

  return retVal;
}
//END SNIPPET: validate




public static void main(String[] args) throws DataFormatException, IOException {
//nothing
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

//START SNIPPET: historyClient
public interface HistoryClient extends IBasicClient {
  /** Server level (history of ALL resources) */ 
  @History
  Bundle getHistoryServer();

  /** Type level (history of all resources of a given type) */
  @History(type=Patient.class)
  Bundle getHistoryPatientType();

  /** Instance level (history of a specific resource instance by type and ID) */
  @History(type=Patient.class)
  Bundle getHistoryPatientInstance(@IdParam IdDt theId);

  /**
   * Either (or both) of the "since" and "count" paramaters can
   * also be included in any of the methods above.
   */
  @History
  Bundle getHistoryServerWithCriteria(@Since Date theDate, @Count Integer theCount);

}
//END SNIPPET: historyClient


public void bbbbb() throws DataFormatException, IOException {
//START SNIPPET: metadataClientUsage
FhirContext ctx = FhirContext.forDstu2();
MetadataClient client = ctx.newRestfulClient(MetadataClient.class, "http://spark.furore.com/fhir");
Conformance metadata = client.getServerMetadata();
System.out.println(ctx.newXmlParser().encodeResourceToString(metadata));
//END SNIPPET: metadataClientUsage
}

//START SNIPPET: readTags
@Read()
public Patient readPatient(@IdParam IdDt theId) {
  Patient retVal = new Patient();
 
  // ..populate demographics, contact, or anything else you usually would..
 
  // Create a TagList and place a complete list of the patient's tags inside
  TagList tags = new TagList();
  tags.addTag("http://animals", "Dog", "Canine Patient"); // TODO: more realistic example
  tags.addTag("http://personality", "Friendly", "Friendly"); // TODO: more realistic example
  
  // The tags are then stored in the Patient resource instance
  retVal.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tags);
 
  return retVal;
}
//END SNIPPET: readTags

//START SNIPPET: clientReadInterface
private interface IPatientClient extends IBasicClient
{
  /** Read a patient from a server by ID */
  @Read
  Patient readPatient(@IdParam IdDt theId);

  // Only one method is shown here, but many methods may be 
  // added to the same client interface!
}
//END SNIPPET: clientReadInterface

public void clientRead() {
//START SNIPPET: clientReadTags
IPatientClient client = FhirContext.forDstu2().newRestfulClient(IPatientClient.class, "http://foo/fhir");
Patient patient = client.readPatient(new IdDt("1234"));
  
// Access the tag list
TagList tagList = (TagList) patient.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
for (Tag next : tagList) {
  // ..process the tags somehow..
}
//END SNIPPET: clientReadTags

//START SNIPPET: clientCreateTags
Patient newPatient = new Patient();

// Populate the resource object
newPatient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setValue("123");
newPatient.addName().addFamily("Jones").addGiven("Frank");

// Populate tags
TagList tags = new TagList();
tags.addTag("http://animals", "Dog", "Canine Patient"); // TODO: more realistic example
tags.addTag("http://personality", "Friendly", "Friendly"); // TODO: more realistic example
newPatient.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tags);

// ...invoke the create method on the client...
//END SNIPPET: clientCreateTags
}

//START SNIPPET: createTags
@Create
public MethodOutcome createPatientResource(@ResourceParam Patient thePatient) {

  // ..save the resouce..
  IdDt id = new IdDt("123"); // the new databse primary key for this resource

  // Get the tag list
  TagList tags = (TagList) thePatient.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
  for (Tag tag : tags) {
    // process/save each tag somehow	
  }
  
  return new MethodOutcome(id);
}
//END SNIPPET: createTags

//START SNIPPET: tagMethodProvider
public class TagMethodProvider 
{
  /** Return a list of all tags that exist on the server */
  @GetTags
  public TagList getAllTagsOnServer() {
    return new TagList(); // populate this
  }

  /** Return a list of all tags that exist on at least one instance
   *  of the given resource type */
  @GetTags(type=Patient.class)
  public TagList getTagsForAllResourcesOfResourceType() {
    return new TagList(); // populate this
  }

  /** Return a list of all tags that exist on a specific instance
   *  of the given resource type */
  @GetTags(type=Patient.class)
  public TagList getTagsForResources(@IdParam IdDt theId) {
    return new TagList(); // populate this
  }

  /** Return a list of all tags that exist on a specific version
   *  of the given resource type */
  @GetTags(type=Patient.class)
  public TagList getTagsForResourceVersion(@IdParam IdDt theId) {
    return new TagList(); // populate this
  }

  /** Add tags to a resource */
  @AddTags(type=Patient.class)
  public void getTagsForResourceVersion(@IdParam IdDt theId, 
                                        @TagListParam TagList theTagList) {
    // add tags
  }

  /** Add tags to a resource version */
  @AddTags(type=Patient.class)
  public void addTagsToResourceVersion(@IdParam IdDt theId,
                                       @TagListParam TagList theTagList) {
    // add tags
  }

  /** Remove tags from a resource */
  @DeleteTags(type=Patient.class)
  public void deleteTagsFromResourceVersion(@IdParam IdDt theId,
                                            @TagListParam TagList theTagList) {
    // add tags
  }

}
//END SNIPPET: tagMethodProvider

//START SNIPPET: transaction
@Transaction
public List<IResource> transaction(@TransactionParam List<IResource> theResources) {
   // theResources will contain a complete bundle of all resources to persist
   // in a single transaction
   for (IResource next : theResources) {
      InstantDt deleted = (InstantDt) next.getResourceMetadata().get(ResourceMetadataKeyEnum.DELETED_AT);
      if (deleted != null && deleted.isEmpty() == false) {
         // delete this resource
      } else {
         // create or update this resource
      }
   }

   // According to the specification, a bundle must be returned. This bundle will contain
   // all of the created/updated/deleted resources, including their new/updated identities.
   //
   // The returned list must be the exact same size as the list of resources
   // passed in, and it is acceptable to return the same list instance that was
   // passed in. 
   List<IResource> retVal = new ArrayList<IResource>(theResources);
   for (IResource next : theResources) {
      /*
       * Populate each returned resource with the new ID for that resource,
       * including the new version if the server supports versioning.
       */
      IdDt newId = new IdDt("Patient", "1", "2"); 
      next.setId(newId);
   }

   // If wanted, you may optionally also return an OperationOutcome resource
   // If present, the OperationOutcome must come first in the returned list.
   OperationOutcome oo = new OperationOutcome();
   oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDiagnostics("Completed successfully");
   retVal.add(0, oo);
   
   return retVal;
}
//END SNIPPET: transaction


}


