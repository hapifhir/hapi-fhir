

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

@Ignore
public class DemoTest {

    private static final String serverBase = "http://localhost:8580/hapi-fhir-jaxrsserver-example/jaxrs-demo/";
    private static IGenericClient client;

    //START SNIPPET: client
    @BeforeClass
    public static void setUpOnce() {
        final FhirContext ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient(serverBase);
        client.setEncoding(EncodingEnum.JSON);
    }

    //END SNIPPET: client

    /** Search/Query - Type */
    @Test
    public void findUsingGenericClientBySearch() {
        // Perform a search
        final ca.uhn.fhir.model.api.Bundle results = client.search().forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("SHORTNAME", "TOYS")).execute();
        System.out.println(results.getEntries().get(0));
        assertEquals(results.getEntries().size(), 1);
    }

    /** Search - Multi-valued Parameters (ANY/OR) */
    @Test
    public void findUsingGenericClientBySearchWithMultiValues() {
        final ca.uhn.fhir.model.api.Bundle response = client.search().forResource(Patient.class)
                .where(Patient.ADDRESS.matches().values("Toronto")).and(Patient.ADDRESS.matches().values("Ontario"))
                .and(Patient.ADDRESS.matches().values("Canada"))
                .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("SHORTNAME", "TOYS")).execute();
        System.out.println(response.getEntries().get(0));
    }

    /** Search - Paging */
    @Test
    public void findWithPaging() {
        // Perform a search
        for(int i =  0 ; i < 10 ; i++) {
            testCreatePatient();
        }
        final Bundle results = client.search().forResource(Patient.class).limitTo(8).returnBundle(Bundle.class).execute();
        System.out.println(results.getEntry().size());

        if (results.getLink(Bundle.LINK_NEXT) != null) {

            // load next page
            final Bundle nextPage = client.loadPage().next(results).execute();
            System.out.println(nextPage.getEntry().size());
        }
    }
    
    /** Search using other query options */
    public void testOther() {
        //missing
    }
    
    /** */ 
    @Test
    public void testSearchPost() {
        Bundle response = client.search()
                .forResource("Patient")
                .usingStyle(SearchStyleEnum.POST)
                .returnBundle(Bundle.class)
                .execute();
        assertTrue(response.getEntry().size() > 0);
    }
    
    /** Search - Compartments */
    @Test
    public void testSearchCompartements() {
        Bundle response = client.search()
                .forResource(Patient.class)
                .withIdAndCompartment("1", "condition")
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();
        assertTrue(response.getEntry().size() > 0);
    }
    
    /** Search - Subsetting (_summary and _elements) */
    @Test
    @Ignore
    public void testSummary() {
    Object response = client.search()
            .forResource(Patient.class)
            .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
            .execute();
    }
    
    @Test
    public void testCreatePatient() {
        final Patient existing = new Patient();
        existing.setId((IdDt) null);
        existing.getNameFirstRep().addFamily("Created Patient 54");
        client.setEncoding(EncodingEnum.XML);
        final MethodOutcome results = client.create().resource(existing).execute();
        System.out.println(results.getId());
        final Bundle bundle = (Bundle) results.getResource();
        final Patient patient = (Patient) bundle.getEntryFirstRep().getResource();
        System.out.println(patient);
        assertNotNull(client.read(patient.getId()));
        client.setEncoding(EncodingEnum.JSON);
    }
    
    
    /** Conditional Creates */
    @Test
    public void testConditionalCreate() {
        final Patient existing = new Patient();
        existing.setId((IdDt) null);
        existing.getNameFirstRep().addFamily("Created Patient 54");
        client.setEncoding(EncodingEnum.XML);
        final MethodOutcome results = client.create().resource(existing).execute();
        System.out.println(results.getId());
        final Bundle bundle = (Bundle) results.getResource();
        final Patient patient = (Patient) bundle.getEntryFirstRep().getResource();

        client.create()
        .resource(patient)
        .conditional()
        .where(Patient.IDENTIFIER.exactly().identifier(patient.getIdentifierFirstRep()))
        .execute();
    }
    
    
    /** Find By Id */
    @Test
    public void findUsingGenericClientById() {
        final Patient results = client.read(Patient.class, "1");
        assertTrue(results.getIdentifier().toString().contains("THOR"));
    }

    @Test
    public void testUpdateById() {
        final Patient existing = client.read(Patient.class, "1");
        final List<HumanNameDt> name = existing.getName();
        name.get(0).addSuffix("The Second");
        existing.setName(name);
        client.setEncoding(EncodingEnum.XML);
        final MethodOutcome results = client.update("1", existing);
    }

    @Test
    public void testDeletePatient() {
        final Patient existing = new Patient();
        existing.getNameFirstRep().addFamily("Created Patient XYZ");
        final MethodOutcome results = client.create().resource(existing).execute();
        System.out.println(results.getId());
        final Bundle bundle = (Bundle) results.getResource();
        final Patient patient = (Patient) bundle.getEntryFirstRep().getResource();
        client.delete(Patient.class, patient.getId());
        try {
            assertNotNull(client.read(patient.getId()));
        }
        catch (final ResourceNotFoundException e) {
            assertEquals(e.getStatusCode(), Constants.STATUS_HTTP_404_NOT_FOUND);
        }
    }
    
    /** Transaction - Server     */
    @Test
    public void testTransaction() {
        ca.uhn.fhir.model.api.Bundle bundle = new ca.uhn.fhir.model.api.Bundle();
        BundleEntry entry = bundle.addEntry();
        final Patient existing = new Patient();
        existing.getNameFirstRep().addFamily("Created with bundle");
        entry.setResource(existing);
        
        BoundCodeDt<BundleEntryTransactionMethodEnum> theTransactionOperation = 
                new BoundCodeDt(
                        BundleEntryTransactionMethodEnum.VALUESET_BINDER, 
                        BundleEntryTransactionMethodEnum.POST);
        entry.setTransactionMethod(theTransactionOperation);
        ca.uhn.fhir.model.api.Bundle response = client.transaction().withBundle(bundle).execute();
    }
    
    /** Conformance - Server */
    @Test
    public void testConformance() {
        final Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();
        System.out.println(conf.getRest().get(0).getResource().get(0).getType());
        System.out.println(conf.getRest().get(0).getResource().get(1).getType());
    }    
    
    /** Extended Operations */
 // Create a client to talk to the HeathIntersections server
    @Test
    public void testExtendedOperations() {
        client.registerInterceptor(new LoggingInterceptor(true));
         
        // Create the input parameters to pass to the server
        Parameters inParams = new Parameters();
        inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
        inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
        inParams.addParameter().setName("dummy").setValue(new StringDt("myAwesomeDummyValue"));
         
        // Invoke $everything on "Patient/1"
        Parameters outParams = client
           .operation()
           .onInstance(new IdDt("Patient", "1"))
           .named("$last")
           .withParameters(inParams)
           //.useHttpGet() // Use HTTP GET instead of POST
           .execute();
        String resultValue = outParams.getParameter().get(0).getValue().toString();
        System.out.println(resultValue);
        assertEquals("expected but found : "+ resultValue, resultValue.contains("myAwesomeDummyValue"), true);
    }
    
    @Test
    public void testExtendedOperationsUsingGet() {
        client.registerInterceptor(new LoggingInterceptor(true));
        
        // Create the input parameters to pass to the server
        Parameters inParams = new Parameters();
        inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
        inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
        inParams.addParameter().setName("dummy").setValue(new StringDt("myAwesomeDummyValue"));
        
        // Invoke $everything on "Patient/1"
        Parameters outParams = client
                .operation()
                .onInstance(new IdDt("Patient", "1"))
                .named("$last")
                .withParameters(inParams)
                .useHttpGet() // Use HTTP GET instead of POST
                .execute();
        String resultValue = outParams.getParameter().get(0).getValue().toString();
        System.out.println(resultValue);
        assertEquals("expected but found : "+ resultValue, resultValue.contains("myAwesomeDummyValue"), true);
    }
    
    
    

    @Test
    public void testFindUnknownPatient() {
        try {
            final Patient existing = client.read(Patient.class, "999955541264");
        }
        catch (final ResourceNotFoundException e) {
            e.printStackTrace();
            assertEquals(e.getStatusCode(), 404);
        }
    }

    @Test
    public void testVRead() {
        final Patient patient = client.vread(Patient.class, "1", "1");
        System.out.println(patient);
    }

}
