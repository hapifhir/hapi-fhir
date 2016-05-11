package example;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Provenance;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;

public class GenericClientExample {

   public static void simpleExample() {
      // START SNIPPET: simple
      // We're connecting to a DSTU1 compliant server in this example
      FhirContext ctx = FhirContext.forDstu2();
      String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
      
      IGenericClient client = ctx.newRestfulGenericClient(serverBase);

      // Perform a search
      Bundle results = client
            .search()
            .forResource(Patient.class)
            .where(Patient.FAMILY.matches().value("duck"))
            .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
            .execute();

      System.out.println("Found " + results.getEntry().size() + " patients named 'duck'");
      // END SNIPPET: simple
   }

   @SuppressWarnings("unused")
   public static void fluentSearch() {
      FhirContext ctx = FhirContext.forDstu2();
      IGenericClient client = ctx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
      {
         // START SNIPPET: create
         Patient patient = new Patient();
         // ..populate the patient object..
         patient.addIdentifier().setSystem("urn:system").setValue("12345");
         patient.addName().addFamily("Smith").addGiven("John");

         // Invoke the server create method (and send pretty-printed JSON
         // encoding to the server
         // instead of the default which is non-pretty printed XML)
         MethodOutcome outcome = client.create()
            .resource(patient)
            .prettyPrint()
            .encodedJson()
            .execute();
         
         // The MethodOutcome object will contain information about the
         // response from the server, including the ID of the created 
         // resource, the OperationOutcome response, etc. (assuming that
         // any of these things were provided by the server! They may not
         // always be)
         IdDt id = (IdDt) outcome.getId();
         System.out.println("Got ID: " + id.getValue());
         // END SNIPPET: create
      }
      {
          Patient patient = new Patient();
          // START SNIPPET: createConditional
          // One form
          MethodOutcome outcome = client.create()
                  .resource(patient)
                  .conditionalByUrl("Patient?identifier=system%7C00001")
                  .execute();

          // Another form
          MethodOutcome outcome2 = client.create()
                  .resource(patient)
                  .conditional()
                  .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("system", "00001"))
                  .execute();

          // This will return Boolean.TRUE if the server responded with an HTTP 201 created,
          // otherwise it will return null.
          Boolean created = outcome.getCreated();

          // The ID of the created, or the pre-existing resource
          IdDt id = (IdDt) outcome.getId();
          // END SNIPPET: createConditional
      }
      {
         // START SNIPPET: validate
         Patient patient = new Patient();
         patient.addIdentifier().setSystem("http://hospital.com").setValue("123445");
         patient.addName().addFamily("Smith").addGiven("John");
         
         // Validate the resource
         MethodOutcome outcome = client.validate()
            .resource(patient)
            .execute();
         
         // The returned object will contain an operation outcome resource
         OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
         
         // If the OperationOutcome has any issues with a severity of ERROR or SEVERE,
         // the validation failed.
         for (Issue nextIssue : oo.getIssue()) {
            if (nextIssue.getSeverityElement().getValueAsEnum().ordinal() >= IssueSeverityEnum.ERROR.ordinal()) {
               System.out.println("We failed validation!");
            }
         }
         // END SNIPPET: validate
     }
       {
         // START SNIPPET: update
         Patient patient = new Patient();
         // ..populate the patient object..
         patient.addIdentifier().setSystem("urn:system").setValue("12345");
         patient.addName().addFamily("Smith").addGiven("John");

         // To update a resource, it should have an ID set (if the resource
         // object
         // comes from the results of a previous read or search, it will already
         // have one though)
         patient.setId("Patient/123");

         // Invoke the server update method
         MethodOutcome outcome = client.update()
            .resource(patient)
            .execute();

         // The MethodOutcome object will contain information about the
         // response from the server, including the ID of the created 
         // resource, the OperationOutcome response, etc. (assuming that
         // any of these things were provided by the server! They may not
         // always be)
         IdDt id = (IdDt) outcome.getId();
         System.out.println("Got ID: " + id.getValue());
         // END SNIPPET: update
      }
      {
          Patient patient = new Patient();
          // START SNIPPET: updateConditional
          client.update()
                  .resource(patient)
                  .conditionalByUrl("Patient?identifier=system%7C00001")
                  .execute();

          client.update()
                  .resource(patient)
                  .conditional()
                  .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("system", "00001"))
                  .execute();
          // END SNIPPET: updateConditional
      }
       {
         // START SNIPPET: etagupdate
         // First, let's retrive the latest version of a resource
         // from the server
         Patient patient = client.read().resource(Patient.class).withId("123").execute();

         // If the server is a version aware server, we should now know the latest version
         // of the resource
         System.out.println("Version ID: " + patient.getId().getVersionIdPart());
         
         // Now let's make a change to the resource
         patient.setGender(AdministrativeGenderEnum.FEMALE);

         // Invoke the server update method - Because the resource has
         // a version, it will be included in the request sent to 
         // the server
         try {
            MethodOutcome outcome = client
               .update()
               .resource(patient)
               .execute();
         } catch (PreconditionFailedException e) {
            // If we get here, the latest version has changed
            // on the server so our update failed.
         }
         // END SNIPPET: etagupdate
      }
      {
         // START SNIPPET: conformance
         // Retrieve the server's conformance statement and print its
         // description
         Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();
         System.out.println(conf.getDescriptionElement().getValue());
         // END SNIPPET: conformance
      }
      {
         // START SNIPPET: delete
          BaseOperationOutcome resp = client.delete().resourceById(new IdDt("Patient", "1234")).execute();

         // outcome may be null if the server didn't return one
          if (resp != null) {
              OperationOutcome outcome = (OperationOutcome) resp;
            System.out.println(outcome.getIssueFirstRep().getDetailsElement().getValue());
         }
         // END SNIPPET: delete
      }
       {
           // START SNIPPET: deleteConditional
           client.delete()
                   .resourceConditionalByUrl("Patient?identifier=system%7C00001")
                   .execute();

           client.delete()
                   .resourceConditionalByType("Patient")
                   .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("system", "00001"))
                   .execute();
           // END SNIPPET: deleteConditional
      }
      {
         // START SNIPPET: search
         ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
               .forResource(Patient.class)
               .where(Patient.BIRTHDATE.beforeOrEquals().day("2011-01-01"))
               .and(Patient.CAREPROVIDER.hasChainedProperty(Organization.NAME.matches().value("Health")))
               .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
               .execute();
         // END SNIPPET: search

         // START SNIPPET: searchOr
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.FAMILY.matches().values("Smith", "Smyth"))
               .returnBundle(Bundle.class)
               .execute();
         // END SNIPPET: searchOr

         // START SNIPPET: searchAnd
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .and(Patient.ADDRESS.matches().values("Ontario"))
               .and(Patient.ADDRESS.matches().values("Canada"))
               .returnBundle(Bundle.class)
               .execute();
         // END SNIPPET: searchAnd

         // START SNIPPET: searchCompartment
         response = client.search()
               .forResource(Patient.class)
               .withIdAndCompartment("123", "condition")
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
               .execute();
         // END SNIPPET: searchCompartment

         // START SNIPPET: searchUrl
         String searchUrl = "http://example.com/base/Patient?identifier=foo";
         
         // Search URL can also be a relative URL in which case the client's base
         // URL will be added to it
         searchUrl = "Patient?identifier=foo";
         
         response = client.search()
               .byUrl(searchUrl)
               .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
               .execute();
         // END SNIPPET: searchUrl

         // START SNIPPET: searchSubsetSummary
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
               .summaryMode(SummaryEnum.TRUE)
               .execute();
         // END SNIPPET: searchSubsetSummary

         // START SNIPPET: searchSubsetElements
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
               .elementsSubset("identifier", "name") // only include the identifier and name
               .execute();
         // END SNIPPET: searchSubsetElements

         // START SNIPPET: searchAdv
         response = client.search()
               .forResource(Patient.class)
               .encodedJson()
               .where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
               .and(Patient.BIRTHDATE.after().day("2011-01-01"))
               .withTag("http://acme.org/codes", "needs-review")
               .include(Patient.INCLUDE_ORGANIZATION.asRecursive())
               .include(Patient.INCLUDE_CAREPROVIDER.asNonRecursive())
               .revInclude(Provenance.INCLUDE_TARGET)
               .lastUpdated(new DateRangeParam("2011-01-01", null))
               .sort().ascending(Patient.BIRTHDATE)
               .sort().descending(Patient.NAME).limitTo(123)
               .returnBundle(Bundle.class)
               .execute();
         // END SNIPPET: searchAdv

         // START SNIPPET: searchPost
         response = client.search()
               .forResource("Patient")
               .where(Patient.NAME.matches().value("Tester"))
               .usingStyle(SearchStyleEnum.POST)
               .returnBundle(Bundle.class)
               .execute();
         // END SNIPPET: searchPost

         // START SNIPPET: searchComposite
         response = client.search()
               .forResource("Observation")
               .where(Observation.CODE_VALUE_DATE
                     .withLeft(Observation.CODE.exactly().code("FOO$BAR"))
                     .withRight(Observation.VALUE_DATE.exactly().day("2001-01-01")))
               .returnBundle(Bundle.class)
               .execute();
         // END SNIPPET: searchComposite
      }
      {
         // START SNIPPET: transaction
         List<IResource> resources = new ArrayList<IResource>();
         // .. populate this list - note that you can also pass in a populated
         // Bundle if you want to create one manually ..

         List<IBaseResource> response = client.transaction().withResources(resources).execute();
         // END SNIPPET: transaction
      }

      {
         // START SNIPPET: read
         // search for patient 123
         Patient patient = client.read()
                                 .resource(Patient.class)
                                 .withId("123")
                                 .execute(); 
         // END SNIPPET: read
      }
      {
         // START SNIPPET: vread
          // search for patient 123 (specific version 888)
          Patient patient = client.read()
                                  .resource(Patient.class)
                                  .withIdAndVersion("123", "888")
                                  .execute(); 
         // END SNIPPET: vread
      }
      {
         // START SNIPPET: readabsolute
         // search for patient 123 on example.com
         String url = "http://example.com/fhir/Patient/123";
         Patient patient = client.read()
                                 .resource(Patient.class)
                                 .withUrl(url)
                                 .execute(); 
         // END SNIPPET: readabsolute
      }
      
      {
         // START SNIPPET: etagread
         // search for patient 123
         Patient patient = client.read()
                                 .resource(Patient.class)
                                 .withId("123")
                                 .ifVersionMatches("001").returnNull()
                                 .execute(); 
         if (patient == null) {
            // resource has not changed
         }
         // END SNIPPET: etagread
      }
      
      
      
   }

   @SuppressWarnings("unused")
   public static void history() {
      IGenericClient client = FhirContext.forDstu2().newRestfulGenericClient("");
      {
         ca.uhn.fhir.model.api.Bundle response;
         // START SNIPPET: historyDstu1
         response = client
            .history()
            .onServer()
            .andReturnDstu1Bundle()
            .execute();
         // END SNIPPET: historyDstu1
      }
      {
         ca.uhn.fhir.model.dstu2.resource.Bundle response;
         // START SNIPPET: historyDstu2
         response = client
            .history()
            .onServer()
            .andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
            .execute();
         // END SNIPPET: historyDstu2
      }
      {
         ca.uhn.fhir.model.dstu2.resource.Bundle response;
         // START SNIPPET: historyFeatures
         response = client
            .history()
            .onServer()
            .andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
            .since(new InstantDt("2012-01-01T12:22:32.038Z"))
            .count(100)
            .execute();
         // END SNIPPET: historyFeatures
      }
   }
   
   public static void main(String[] args) {
      paging();
   }
   private static void paging() {
      {
         // START SNIPPET: searchPaging
         FhirContext ctx = FhirContext.forDstu2();
         IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
         
         // Perform a search
         Bundle results = client.search()
               .forResource(Patient.class)
               .where(Patient.NAME.matches().value("Smith"))
               .returnBundle(Bundle.class)
               .execute();
         
         if (results.getLink(Bundle.LINK_NEXT) != null) {

            // load next page
            Bundle nextPage = client.loadPage().next(results).execute();
         }
         // END SNIPPET: searchPaging
      }
   }

   @SuppressWarnings("unused")
   private static void operationHttpGet() {
      // START SNIPPET: operationHttpGet
      // Create a client to talk to the HeathIntersections server
      FhirContext ctx = FhirContext.forDstu2();
      IGenericClient client = ctx.newRestfulGenericClient("http://fhir-dev.healthintersections.com.au/open");
      client.registerInterceptor(new LoggingInterceptor(true));
      
      // Create the input parameters to pass to the server
      Parameters inParams = new Parameters();
      inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
      inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
      
      // Invoke $everything on "Patient/1"
      Parameters outParams = client
         .operation()
         .onInstance(new IdDt("Patient", "1"))
         .named("$everything")
         .withParameters(inParams)
         .useHttpGet() // Use HTTP GET instead of POST
         .execute();
      // END SNIPPET: operationHttpGet
   }

   @SuppressWarnings("unused")
   private static void operation() {
      // START SNIPPET: operation
      // Create a client to talk to the HeathIntersections server
      FhirContext ctx = FhirContext.forDstu2();
      IGenericClient client = ctx.newRestfulGenericClient("http://fhir-dev.healthintersections.com.au/open");
      client.registerInterceptor(new LoggingInterceptor(true));
      
      // Create the input parameters to pass to the server
      Parameters inParams = new Parameters();
      inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
      inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
      
      // Invoke $everything on "Patient/1"
      Parameters outParams = client
         .operation()
         .onInstance(new IdDt("Patient", "1"))
         .named("$everything")
         .withParameters(inParams)
         .execute();
      
      /*
       * Note that the $everything operation returns a Bundle instead 
       * of a Parameters resource. The client operation methods return a
       * Parameters instance however, so HAPI creates a Parameters object
       * with a single parameter containing the value.
       */
      Bundle responseBundle = (Bundle) outParams.getParameter().get(0).getResource();
      
      // Print the response bundle
      System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(responseBundle));
      // END SNIPPET: operation
   }

   @SuppressWarnings("unused")
   private static void operationNoIn() {
      // START SNIPPET: operationNoIn
      // Create a client to talk to the HeathIntersections server
      FhirContext ctx = FhirContext.forDstu2();
      IGenericClient client = ctx.newRestfulGenericClient("http://fhir-dev.healthintersections.com.au/open");
      client.registerInterceptor(new LoggingInterceptor(true));
      
      // Invoke $everything on "Patient/1"
      Parameters outParams = client
         .operation()
         .onInstance(new IdDt("Patient", "1"))
         .named("$everything")
         .withNoParameters(Parameters.class) // No input parameters
         .execute();
      // END SNIPPET: operationNoIn
   }

}
