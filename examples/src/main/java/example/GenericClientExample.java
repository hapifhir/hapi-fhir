package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.method.SearchStyleEnum;

public class GenericClientExample {

   public static void simpleExample() {
      // START SNIPPET: simple
      FhirContext ctx = new FhirContext();
      String serverBase = "http://fhirtest.uhn.ca/base";
      IGenericClient client = ctx.newRestfulGenericClient(serverBase);

      // Perform a search
      Bundle results = client
            .search()
            .forResource(Patient.class)
            .where(Patient.FAMILY.matches().value("duck"))
            .execute();

      System.out.println("Found " + results.size() + " patients named 'duck'");
      // END SNIPPET: simple
   }

   @SuppressWarnings("unused")
   public static void fluentSearch() {
      FhirContext ctx = new FhirContext();
      IGenericClient client = ctx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
      {
         // START SNIPPET: create
         Patient patient = new Patient();
         // ..populate the patient object..
         patient.addIdentifier("urn:system", "12345");
         patient.addName().addFamily("Smith").addGiven("John");

         // Invoke the server create method (and send pretty-printed JSON
         // encoding to the server
         // instead of the default which is non-pretty printed XML)
         client
            .create()
            .resource(patient)
            .prettyPrint()
            .encodedJson()
            .execute();
         // END SNIPPET: create
      }
      {
         // START SNIPPET: update
         Patient patient = new Patient();
         // ..populate the patient object..
         patient.addIdentifier("urn:system", "12345");
         patient.addName().addFamily("Smith").addGiven("John");

         // To update a resource, it should have an ID set (if the resource
         // object
         // comes from the results of a previous read or search, it will already
         // have one though)
         patient.setId("Patient/123");

         // Invoke the server create method (and send pretty-printed JSON
         // encoding to the server
         // instead of the default which is non-pretty printed XML)
         client
            .update()
            .resource(patient)
            .execute();
         // END SNIPPET: update
      }
      {
         // START SNIPPET: conformance
         // Retrieve the server's conformance statement and print its
         // description
         BaseConformance conf = client.conformance();
         System.out.println(conf.getDescriptionElement().getValue());
         // END SNIPPET: conformance
      }
      {
         // START SNIPPET: delete
         // Retrieve the server's conformance statement and print its
         // description
         BaseOperationOutcome outcome = client.delete().resourceById(new IdDt("Patient", "1234")).execute();

         // outcome may be null if the server didn't return one
         if (outcome != null) {
            System.out.println(outcome.getIssueFirstRep().getDetailsElement().getValue());
         }
         // END SNIPPET: delete
      }
      {
         // START SNIPPET: search
         Bundle response = client.search()
               .forResource(Patient.class)
               .where(Patient.BIRTHDATE.beforeOrEquals().day("2011-01-01"))
               .and(Patient.PROVIDER.hasChainedProperty(Organization.NAME.matches().value("Health")))
               .execute();
         // END SNIPPET: search

         // START SNIPPET: searchOr
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.FAMILY.matches().values("Smith", "Smyth"))
               .execute();
         // END SNIPPET: searchOr

         // START SNIPPET: searchAnd
         response = client.search()
               .forResource(Patient.class)
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .and(Patient.ADDRESS.matches().values("Ontario"))
               .and(Patient.ADDRESS.matches().values("Canada"))
               .execute();
         // END SNIPPET: searchAnd

         // START SNIPPET: searchCompartment
         response = client.search()
               .forResource(Patient.class)
               .withIdAndCompartment("123", "condition")
               .where(Patient.ADDRESS.matches().values("Toronto"))
               .execute();
         // END SNIPPET: searchCompartment

         // START SNIPPET: searchAdv
         response = client.search()
               .forResource(Patient.class)
               .encodedJson()
               .where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
               .and(Patient.BIRTHDATE.after().day("2011-01-01"))
               .include(Patient.INCLUDE_MANAGINGORGANIZATION)
               .sort().ascending(Patient.BIRTHDATE)
               .sort().descending(Patient.NAME).limitTo(123)
               .execute();
         // END SNIPPET: searchAdv

         // START SNIPPET: searchPost
         response = client.search()
               .forResource("Patient")
               .where(Patient.NAME.matches().value("Tester"))
               .usingStyle(SearchStyleEnum.POST)
               .execute();
         // END SNIPPET: searchPost

         // START SNIPPET: searchComposite
         response = client.search()
               .forResource("Observation")
               .where(Observation.NAME_VALUE_DATE
                     .withLeft(Observation.NAME.exactly().code("FOO$BAR"))
                     .withRight(Observation.VALUE_DATE.exactly().day("2001-01-01")))
               .execute();
         // END SNIPPET: searchComposite

         // START SNIPPET: searchPaging
         if (response.getLinkNext().isEmpty() == false) {

            // load next page
            Bundle nextPage = client.loadPage().next(response).execute();
         }
         // END SNIPPET: searchPaging
      }
      {
         // START SNIPPET: transaction
         List<IResource> resources = new ArrayList<IResource>();
         // .. populate this list - note that you can also pass in a populated
         // Bundle if you want to create one manually ..

         List<IResource> response = client.transaction().withResources(resources).execute();
         // END SNIPPET: transaction
      }

      {
         // START SNIPPET: read
         IdDt id = new IdDt("Patient", "123");
         Patient patient = client.read(Patient.class, id); // search for patient 123
         // END SNIPPET: read
      }
      {
         // START SNIPPET: vread
         IdDt id = new IdDt("Patient", "123", "888");
         Patient patient = client.vread(Patient.class, id); // search for version 888 of patient 123
         // END SNIPPET: vread
      }
      {
         // START SNIPPET: readabsolute
         IdDt id = new IdDt("http://example.com/fhir/Patient/123");
         Patient patient = client.read(Patient.class, id); // search for patient 123 on example.com
         // END SNIPPET: readabsolute
      }
      
      
      
      
   }

   public static void main(String[] args) {
      fluentSearch();
   }

}
