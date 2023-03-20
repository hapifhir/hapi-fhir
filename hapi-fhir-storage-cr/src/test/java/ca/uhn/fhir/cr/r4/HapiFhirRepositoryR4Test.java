package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HapiFhirRepositoryR4Test extends BaseCrR4Test {
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";

	private RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestServer);
		requestDetails.setFhirServerBase(ourServerBase);
		return requestDetails;
	}

	@Test
	void crudTest() {
		var requestDetails = setupRequestDetails();
		var repository = new HapiFhirRepository(myDaoRegistry, requestDetails, ourRestServer);
		var result = repository.create(new Patient().addName(new HumanName().setFamily("Test").addGiven("Name1")));
		assertEquals(true, result.getCreated());
		var patient = (Patient) result.getResource();
		assertEquals(1, patient.getName().size());
		assertEquals("Test", patient.getName().get(0).getFamily());
		assertEquals(1, patient.getName().get(0).getGiven().size());
		patient.getName().get(0).addGiven("Name2");
		repository.update(patient);
		var updatedPatient = repository.read(Patient.class, patient.getIdElement());
		assertEquals(2, updatedPatient.getName().get(0).getGiven().size());
		repository.delete(Patient.class, patient.getIdElement());
		var ex = assertThrows(Exception.class, () -> repository.read(Patient.class, new IdType(patient.getIdElement().getIdPart())));
		assertTrue(ex.getMessage().contains("Resource was deleted"));
	}

	@Test
	void canSearchMoreThan50Patients() {
		loadBundle(MY_TEST_DATA);
		var expectedPatientCount = 63;
		ourPagingProvider.setMaximumPageSize(100);
		var repository = new HapiFhirRepository(myDaoRegistry, withMaxPageSize(100), ourRestServer);
		// get all patient resources posted
		var result = repository.search(Bundle.class, Patient.class, withEmptySearchParams());
		assertEquals(expectedPatientCount, result.getTotal());
		// count all resources in result
		int counter = 0;
		for (var e: result.getEntry()) {
			counter++;
		}
		//verify all patient resources captured
		assertEquals(expectedPatientCount, counter, "Patient search results don't match available resources");
	}

	@Test
	void canSearchWithPagination() {
		loadBundle(MY_TEST_DATA);

		var requestDetails = withMaxPageSize(20);
		requestDetails.setCompleteUrl("http://localhost:44465/fhir/context/Patient?_count=20");
		var repository = new HapiFhirRepository(myDaoRegistry, requestDetails, ourRestServer);
		var result = repository.search(Bundle.class, Patient.class, withEmptySearchParams());
		assertEquals(20, result.getEntry().size());
		var next = result.getLink().get(1);
		assertEquals("next", next.getRelation());
		var nextUrl = next.getUrl();
		var nextResult = repository.link(Bundle.class, nextUrl);
		assertEquals(20, nextResult.getEntry().size());
		assertEquals(false, result.getEntry().stream().map(e -> e.getResource().getIdPart()).anyMatch(i -> nextResult.getEntry().stream().map(e -> e.getResource().getIdPart()).collect(Collectors.toList()).contains(i)));
	}

	@Test
	void transactionReadsPatientResources() {
		var expectedPatientCount = 63;
		var theBundle = readResource(Bundle.class, MY_TEST_DATA);
		ourPagingProvider.setMaximumPageSize(100);
		var repository = new HapiFhirRepository(myDaoRegistry, withMaxPageSize(100), ourRestServer);
		repository.transaction(theBundle);
		var result = repository.search(Bundle.class, Patient.class, withEmptySearchParams());
		// count all resources in result
		int counter = 0;
		for (Object i: result.getEntry()) {
			counter++;
		}
		//verify all patient resources captured
		assertEquals(expectedPatientCount, counter, "Patient search results don't match available resources");
	}

	@Test
	void transactionReadsEncounterResources() {
		var expectedEncounterCount = 652;
		var theBundle = readResource(Bundle.class, MY_TEST_DATA);
		ourPagingProvider.setMaximumPageSize(1000);
		var repository = new HapiFhirRepository(myDaoRegistry, withMaxPageSize(1000), ourRestServer);
		repository.transaction(theBundle);
		var result = repository.search(Bundle.class, Encounter.class, withEmptySearchParams());
		// count all resources in result
		int counter = 0;
		for (Object i: result.getEntry()) {
			counter++;
		}
		//verify all encounter resources captured
		assertEquals(expectedEncounterCount, counter, "Encounter search results don't match available resources");
	}

	@Test
	void transactionReadsImmunizationResources() {
		var expectedEncounterCount = 638;
		var theBundle = readResource(Bundle.class, MY_TEST_DATA);
		ourPagingProvider.setMaximumPageSize(1000);
		var repository = new HapiFhirRepository(myDaoRegistry, withMaxPageSize(1000), ourRestServer);
		repository.transaction(theBundle);
		var result = repository.search(Bundle.class, Immunization.class, withEmptySearchParams());
		// count all resources in result
		int counter = 0;
		for (Object i: result.getEntry()) {
			counter++;
		}
		//verify all immunization resources captured
		assertEquals(expectedEncounterCount, counter, "Immunization search results don't match available resources");
	}


	RequestDetails withMaxPageSize(int theMax) {
		var requestDetails = setupRequestDetails();
		Map<String, String[]> params = new HashMap<>();
		params.put(Constants.PARAM_COUNT, new String[] { Integer.toString(theMax) });
		requestDetails.setParameters(params);
		return requestDetails;
	}

	Map<String, List<IQueryParameterType>> withEmptySearchParams () {return new HashMap<>();}
}
