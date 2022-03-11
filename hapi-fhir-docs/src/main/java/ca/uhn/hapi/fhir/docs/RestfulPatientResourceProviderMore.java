package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class RestfulPatientResourceProviderMore implements IResourceProvider {

   public interface ITestClient extends IBasicClient
   {

      @Search
      List<Patient> getPatientByDob(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theParam);
      
   }
   
private boolean detectedVersionConflict;
private boolean conflictHappened;
private boolean couldntFindThisId;
private FhirContext myContext;

//START SNIPPET: searchAll
@Search
public List<Organization> getAllOrganizations() {
   List<Organization> retVal=new ArrayList<Organization>(); // populate this
   return retVal;
}
//END SNIPPET: searchAll

//START SNIPPET: updateEtag
@Update
public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
   String resourceId = theId.getIdPart();
   String versionId = theId.getVersionIdPart(); // this will contain the ETag
   
   String currentVersion = "1"; // populate this with the current version
   
   if (!versionId.equals(currentVersion)) {
      throw new ResourceVersionConflictException(Msg.code(632) + "Expected version " + currentVersion);
   }
   
   // ... perform the update ...
   return new MethodOutcome();
   
}
//END SNIPPET: updateEtag

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
   public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
   }
   
   @Search(compartmentName="Condition")
   public List<IBaseResource> searchCompartment(@IdParam IdType thePatientId) {
      List<IBaseResource> retVal=new ArrayList<IBaseResource>(); 
      
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

//START SNIPPET: count
@Search
public List<Patient> findPatients(
	@RequiredParam(name=Patient.SP_IDENTIFIER) StringParam theParameter,
	@Count Integer theCount) {
	List<Patient> retVal=new ArrayList<Patient>(); // populate this

	// count is null unless a _count parameter is actually provided
	if (theCount != null) {
		// ... do search with count ...
	} else {
		// ... do search without count ...
	}

	return retVal;
}
//END SNIPPET: count

//START SNIPPET: offset
@Search
public List<Patient> findPatients(
	@RequiredParam(name=Patient.SP_IDENTIFIER) StringParam theParameter,
	@Offset Integer theOffset,
	@Count Integer theCount) {
	List<Patient> retVal=new ArrayList<Patient>(); // populate this

	// offset is null unless a _offset parameter is actually provided
	if (theOffset != null) {
		// ... do search with offset ...
	} else {
		// ... do search without offset ...
	}

	return retVal;
}
//END SNIPPET: offset

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
public List<DiagnosticReport> findDiagnosticReportsWithSubjet(
		@OptionalParam(name=DiagnosticReport.SP_SUBJECT) ReferenceParam theSubject
		) {
   List<DiagnosticReport> retVal=new ArrayList<DiagnosticReport>();

   // If the parameter passed in includes a resource type (e.g. ?subject:Patient=123)
   // that resource type is available. Here we just check that it is either not provided
   // or set to "Patient"
   if (theSubject.hasResourceType()) {
      String resourceType = theSubject.getResourceType();
      if ("Patient".equals(resourceType) == false) {
         throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'subject': " + resourceType);
      }
   }
   
   if (theSubject != null) {
      // ReferenceParam extends IdType so all of the resource ID methods are available
      String subjectId = theSubject.getIdPart();
      
      // .. populate retVal with DiagnosticReport resources having 
      // subject with id "subjectId" ..
      
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
		@RequiredParam(name= Observation.SP_SUBJECT+'.'+Patient.SP_IDENTIFIER) TokenParam theProvider
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
       TokenParam tokenSubject = subject.toTokenParam(myContext);
       String system = tokenSubject.getSystem();
       String identifier = tokenSubject.getValue();
       
       // TODO: populate all the observations for the identifier
       
    } else if (Patient.SP_BIRTHDATE.equals(chain)) {

          // Because the chained parameter "subject.birthdate" is actually of type
          // "date", we convert the value to a date before processing it. 
          DateParam dateSubject = subject.toDateParam(myContext);
          DateTimeType birthDate = new DateTimeType(dateSubject.getValueAsString());
          
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
public Patient getResourceById(@IdParam IdType theId) {
   Patient retVal = new Patient();
   
   // ...populate...
   retVal.addIdentifier().setSystem("urn:mrns").setValue("12345");
   retVal.addName().setFamily("Smith").addGiven("Tester").addGiven("Q");
   // ...etc...
   
   // if you know the version ID of the resource, you should set it and HAPI will 
   // include it in a Content-Location header
   retVal.setId(new IdType("Patient", "123", "2"));
   
   return retVal;
}
//END SNIPPET: read

//START SNIPPET: delete
@Delete()
public void deletePatient(@IdParam IdType theId) {
	// .. Delete the patient ..
	if (couldntFindThisId) {
		throw new ResourceNotFoundException(Msg.code(634) + "Unknown version");
	}
	if (conflictHappened) {
		throw new ResourceVersionConflictException(Msg.code(635) + "Couldn't delete because [foo]");
	}
	// otherwise, delete was successful
	return; // can also return MethodOutcome
}
//END SNIPPET: delete


//START SNIPPET: deleteConditional
@Delete()
public void deletePatientConditional(@IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl) {
   // Only one of theId or theConditionalUrl will have a value depending
   // on whether the URL received was a logical ID, or a conditional
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
public List<Patient> getPatientHistory(
      @IdParam IdType theId,
      @Since InstantType theSince,
      @At DateRangeParam theAt
      ) {
   List<Patient> retVal = new ArrayList<Patient>();
   
   Patient patient = new Patient();
   patient.addName().setFamily("Smith");
   
   // Set the ID and version
   patient.setId(theId.withVersion("1"));
   
   // ...populate the rest...
   return retVal;
}
//END SNIPPET: history


//START SNIPPET: vread
@Read(version=true)
public Patient readOrVread(@IdParam IdType theId) {
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
   patient.addName().setFamily("Smith").addGiven("Tester").addGiven("Q");
   // ...etc...

   //  Every returned resource must have its logical ID set. If the server
   //  supports versioning, that should be set too
   String logicalId = "4325";
   String versionId = "2"; // optional
   patient.setId(new IdType("Patient", logicalId, versionId));
   
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
   List<TokenParam> wantedCodings = theCodings.getValuesAsQueryTokens();
   
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
   ParamPrefixEnum prefix = theDate.getPrefix(); // e.g. gt, le, etc..
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
DateParam param = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, "2011-01-02");
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
public Class<? extends IBaseResource> getResourceType() {
   return null;
}



//START SNIPPET: pathSpec
@Search()
public List<DiagnosticReport> getDiagnosticReport( 
               @RequiredParam(name=DiagnosticReport.SP_IDENTIFIER) 
               TokenParam theIdentifier,
               
               @IncludeParam(allow= {"DiagnosticReport:subject"}) 
               Set<Include> theIncludes ) {
	
  List<DiagnosticReport> retVal = new ArrayList<DiagnosticReport>();
 
  // Assume this method exists and loads the report from the DB
  DiagnosticReport report = loadSomeDiagnosticReportFromDatabase(theIdentifier);

  // If the client has asked for the subject to be included:
  if (theIncludes.contains(new Include("DiagnosticReport:subject"))) {
	 
    // The resource reference should contain the ID of the patient
    IIdType subjectId = report.getSubject().getReferenceElement();
	
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
             
             @IncludeParam(allow= {"DiagnosticReport:subject"}) 
             String theInclude ) {
	
  List<DiagnosticReport> retVal = new ArrayList<DiagnosticReport>();

  // Assume this method exists and loads the report from the DB
  DiagnosticReport report = loadSomeDiagnosticReportFromDatabase(theIdentifier);

  // If the client has asked for the subject to be included:
  if ("DiagnosticReport:subject".equals(theInclude)) {
	 
    // The resource reference should contain the ID of the patient
    IIdType subjectId = report.getSubject().getReferenceElement();
	
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
  
  ParamPrefixEnum prefix = theQuantity.getPrefix();
  BigDecimal value = theQuantity.getValue();
  String units = theQuantity.getUnits();
  // .. Apply these parameters ..
  
  // ... populate ...
  return retVal;
}
//END SNIPPET: quantity

private DiagnosticReport loadSomeDiagnosticReportFromDatabase(TokenParam theIdentifier) {
	return null;
}

private Patient loadSomePatientFromDatabase(IIdType theId) {
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
    throw new UnprocessableEntityException(Msg.code(636) + "No identifier supplied");
  }
	
  // Save this patient to the database...
  savePatientToDatabase(thePatient);

  // This method returns a MethodOutcome object which contains
  // the ID (composed of the type Patient, the logical ID 3746, and the
  // version ID 1)
  MethodOutcome retVal = new MethodOutcome();
  retVal.setId(new IdType("Patient", "3746", "1"));
  
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
      @IdParam IdType theId, 
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
    @IdParam IdType theId) {

   // Save the patient to the database
   
   // Update the version and last updated time on the resource
   IdType updatedId = theId.withVersion("123");
   thePatient.setId(updatedId);
   InstantType lastUpdated = InstantType.withCurrentTime();
   thePatient.getMeta().setLastUpdatedElement(lastUpdated);
   
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
    @IdParam IdType theId, 
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
public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient) {

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
    throw new UnprocessableEntityException(Msg.code(637) + "No identifier supplied");
  }

  String versionId = theId.getVersionIdPart();
  if (versionId != null) {
    // If the client passed in a version number in an If-Match header, they are
    // doing a version-aware update. You may wish to throw an exception if the supplied
    // version is not the latest version. Note that as of DSTU2 the FHIR specification uses
    // ETags and If-Match to handle version aware updates, so PreconditionFailedException (HTTP 412)
    // is used instead of ResourceVersionConflictException (HTTP 409)
    if (detectedVersionConflict) {
      throw new PreconditionFailedException(Msg.code(638) + "Unexpected version");
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
public abstract MethodOutcome updateSomePatient(@IdParam IdType theId, @ResourceParam Patient thePatient);
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
    throw new UnprocessableEntityException(Msg.code(639) + "No identifier supplied");
  }
	
  // This method returns a MethodOutcome object
  MethodOutcome retVal = new MethodOutcome();

  // You may also add an OperationOutcome resource to return
  // This part is optional though:
  OperationOutcome outcome = new OperationOutcome();
  outcome.addIssue().setSeverity(IssueSeverity.WARNING).setDiagnostics("One minor issue detected");
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
private void savePatientToDatabase(IdType theId, Patient thePatient) {
	// nothing
}

//START SNIPPET: metadataProvider
public class CapabilityStatementProvider {

  @Metadata
  public CapabilityStatement getServerMetadata() {
	  CapabilityStatement retVal = new CapabilityStatement();
    // ..populate..
    return retVal;
  }

}
//END SNIPPET: metadataProvider



//START SNIPPET: metadataClient
public interface MetadataClient extends IRestfulClient {
  
  @Metadata
  CapabilityStatement getServerMetadata();
  
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
  Bundle getHistoryPatientInstance(@IdParam IdType theId);

  /**
   * Either (or both) of the "since" and "count" parameters can
   * also be included in any of the methods above.
   */
  @History
  Bundle getHistoryServerWithCriteria(@Since Date theDate, @Count Integer theCount);

}
//END SNIPPET: historyClient


public void bbbbb() throws DataFormatException, IOException {
//START SNIPPET: metadataClientUsage
FhirContext ctx = FhirContext.forR4();
MetadataClient client = ctx.newRestfulClient(MetadataClient.class, "http://spark.furore.com/fhir");
CapabilityStatement metadata = client.getServerMetadata();
System.out.println(ctx.newXmlParser().encodeResourceToString(metadata));
//END SNIPPET: metadataClientUsage
}

//START SNIPPET: readTags
@Read()
public Patient readPatient(@IdParam IdType theId) {
  Patient retVal = new Patient();
 
  // ..populate demographics, contact, or anything else you usually would..

  // Populate some tags
  retVal.getMeta().addTag("http://animals", "Dog", "Canine Patient"); // TODO: more realistic example
  retVal.getMeta().addTag("http://personality", "Friendly", "Friendly"); // TODO: more realistic example
  
  return retVal;
}
//END SNIPPET: readTags

//START SNIPPET: clientReadInterface
private interface IPatientClient extends IBasicClient
{
  /** Read a patient from a server by ID */
  @Read
  Patient readPatient(@IdParam IdType theId);

  // Only one method is shown here, but many methods may be 
  // added to the same client interface!
}
//END SNIPPET: clientReadInterface

public void clientRead() {
//START SNIPPET: clientReadTags
IPatientClient client = FhirContext.forR4().newRestfulClient(IPatientClient.class, "http://foo/fhir");
Patient patient = client.readPatient(new IdType("1234"));
  
// Access the tag list
List<Coding> tagList = patient.getMeta().getTag();
for (Coding next : tagList) {
  // ..process the tags somehow..
}
//END SNIPPET: clientReadTags

//START SNIPPET: clientCreateTags
Patient newPatient = new Patient();

// Populate the resource object
newPatient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setValue("123");
newPatient.addName().setFamily("Jones").addGiven("Frank");

// Populate some tags
newPatient.getMeta().addTag("http://animals", "Dog", "Canine Patient"); // TODO: more realistic example
newPatient.getMeta().addTag("http://personality", "Friendly", "Friendly"); // TODO: more realistic example

// ...invoke the create method on the client...
//END SNIPPET: clientCreateTags
}

//START SNIPPET: createTags
@Create
public MethodOutcome createPatientResource(@ResourceParam Patient thePatient) {

  // ..save the resource..
  IdType id = new IdType("123"); // the new database primary key for this resource

  // Get the tag list
  List<Coding> tags = thePatient.getMeta().getTag();
  for (Coding tag : tags) {
    // process/save each tag somehow	
  }
  
  return new MethodOutcome(id);
}
//END SNIPPET: createTags

//START SNIPPET: transaction
@Transaction
public Bundle transaction(@TransactionParam Bundle theInput) {
   for (BundleEntryComponent nextEntry : theInput.getEntry()) {
      // Process entry
   }

   Bundle retVal = new Bundle();
   // Populate return bundle
   return retVal;
}
//END SNIPPET: transaction

}


