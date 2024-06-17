package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.InternalCodingDt;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HapiFhirRepositoryR4Test extends BaseCrR4TestServer {

	@Autowired
	RestfulServer myRestfulServer;
	private static final String MY_TEST_DATA =
		"ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";

	@BeforeEach
	void setup() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		loadBundle(MY_TEST_DATA);
	}
	@Test
	void repositoryTests(){
		var repository = new HapiFhirRepository(myDaoRegistry, setupRequestDetails(), myRestfulServer);
		//run repository tests
		transactionReadsImmunizationResources(repository);
		repositorySearchForEncounterWithMatchingCode(repository);
		canSearchMoreThan50Patients(repository);
		canSearchWithPagination(repository);
		transactionReadsPatientResources(repository);
		transactionReadsEncounterResources(repository);
		assertTrue(crudTest(repository));
	}


	Boolean crudTest(HapiFhirRepository theRepository) {

		var result = theRepository
			.create(new Patient().addName(new HumanName().setFamily("Test").addGiven("Name1")));
		assertEquals(true, result.getCreated());
		var patient = (Patient) result.getResource();
		assertThat(patient.getName()).hasSize(1);
		assertEquals("Test", patient.getName().get(0).getFamily());
		assertThat(patient.getName().get(0).getGiven()).hasSize(1);
		patient.getName().get(0).addGiven("Name2");
		theRepository.update(patient);
		var updatedPatient = theRepository.read(Patient.class, patient.getIdElement());
		assertEquals(2, updatedPatient.getName().get(0).getGiven().size());
		theRepository.delete(Patient.class, patient.getIdElement());
		var ex = assertThrows(Exception.class,
			() -> theRepository.read(Patient.class, new IdType(patient.getIdElement().getIdPart())));
		return ex.getMessage().contains("Resource was deleted");
	}

	void canSearchMoreThan50Patients(HapiFhirRepository theRepository) {
		var expectedPatientCount = 65;

		ourPagingProvider.setMaximumPageSize(100);
		// get all patient resources posted
		var result = theRepository.search(Bundle.class, Patient.class, withCountParam(100));
		assertEquals(expectedPatientCount, result.getTotal());
		// count all resources in result
		int counter = 0;
		for (Object i : result.getEntry()) {
			counter++;
		}
		// verify all patient resources captured
		assertThat(counter).as("Patient search results don't match available resources").isEqualTo(expectedPatientCount);
	}


	void canSearchWithPagination(HapiFhirRepository theRepository) {

		var result = theRepository.search(Bundle.class, Patient.class, withCountParam(20));
		assertEquals(20, result.getEntry().size());
		var next = result.getLink().get(1);
		assertEquals("next", next.getRelation());
		var nextUrl = next.getUrl();
		var nextResult = theRepository.link(Bundle.class, nextUrl);
		assertEquals(20, nextResult.getEntry().size());
		assertEquals(false,
			result.getEntry().stream().map(e -> e.getResource().getIdPart()).anyMatch(
				i -> nextResult.getEntry().stream().map(e -> e.getResource().getIdPart())
					.toList().contains(i)));
	}


	void transactionReadsPatientResources(HapiFhirRepository theRepository) {
		var expectedPatientCount = 65;
		ourPagingProvider.setMaximumPageSize(100);
		var result = theRepository.search(Bundle.class, Patient.class, withCountParam(100));
		// count all resources in result
		int counter = 0;
		for (Object i : result.getEntry()) {
			counter++;
		}
		// verify all patient resources captured
		assertThat(counter).as("Patient search results don't match available resources").isEqualTo(expectedPatientCount);
	}

	void transactionReadsEncounterResources(HapiFhirRepository theRepository) {
		var expectedEncounterCount = 654;
		ourPagingProvider.setMaximumPageSize(1000);
		var result = theRepository.search(Bundle.class, Encounter.class, withCountParam(1000));
		// count all resources in result
		int counter = 0;
		for (Object i : result.getEntry()) {
			counter++;
		}
		// verify all encounter resources captured
		assertThat(counter).as("Encounter search results don't match available resources").isEqualTo(expectedEncounterCount);
	}

	void repositorySearchForEncounterWithMatchingCode(HapiFhirRepository theRepository) {

		//SearchConverter validation test for repository
		List<IQueryParameterType> codeList = new ArrayList<>();
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("185463005")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("185464004")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("185465003")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("30346009")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("3391000175108")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("37894004")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://snomed.info/sct").setCode("439740005")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99201")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99202")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99203")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99204")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99205")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99212")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99213")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99214")));
		codeList.add(new TokenParam(new InternalCodingDt().setSystem("http://www.ama-assn.org/go/cpt").setCode("99215")));

		// replicate repository searchParam list
		Map<String, List<IQueryParameterType>> searchParams = Map.of("type", codeList, "subject", Collections.singletonList(new ReferenceParam("Patient/numer-EXM130")));

		// replicate search for valueset codes
		var result = theRepository.search(Bundle.class, Encounter.class, searchParams);

		// count all resources in result
		int counter = 0;
		for (Object i : result.getEntry()) {
			counter++;
		}
		// verify patient encounter was the only one found
		assertThat(counter).as("Encounter search results don't match available resources").isEqualTo(1);
	}

	void transactionReadsImmunizationResources(HapiFhirRepository theRepository) {
		var expectedEncounterCount = 638;
		ourPagingProvider.setMaximumPageSize(1000);

		var result = theRepository.search(Bundle.class, Immunization.class, withCountParam(1000));
		// count all resources in result
		int counter = 0;
		for (Object i : result.getEntry()) {
			counter++;
		}
		// verify all immunization resources captured
		assertThat(counter).as("Immunization search results don't match available resources").isEqualTo(expectedEncounterCount);
	}

	Map<String, List<IQueryParameterType>> withEmptySearchParams() {
		return new HashMap<>();
	}

	Map<String, List<IQueryParameterType>> withCountParam(int theCount) {
		var params = withEmptySearchParams();
		params.put(Constants.PARAM_COUNT, Collections.singletonList(new NumberParam(theCount)));
		return params;
	}
}
