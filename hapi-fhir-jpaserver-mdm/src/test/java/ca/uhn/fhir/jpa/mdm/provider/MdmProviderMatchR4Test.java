package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.MdmConstants;
import com.google.common.collect.Ordering;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmProviderMatchR4Test extends BaseProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmProviderMatchR4Test.class);

	public static final String NAME_GIVEN_JANET = NAME_GIVEN_JANE + "t";

	@Override
	@BeforeEach
	public void before() {
		super.before();
	}

	@Test
	public void testMatch() throws Exception {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.match(newJane, new SystemRequestDetails());
		assertEquals(1, result.getEntry().size());

		Bundle.BundleEntryComponent entry0 = result.getEntry().get(0);
		assertEquals(createdJane.getId(), entry0.getResource().getId());

		Bundle.BundleEntrySearchComponent searchComponent = entry0.getSearch();
		assertEquals(Bundle.SearchEntryMode.MATCH, searchComponent.getMode());

		assertEquals(2.0 / 3.0, searchComponent.getScore().doubleValue(), 0.01);
		Extension matchGradeExtension = searchComponent.getExtensionByUrl(MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE);
		assertNotNull(matchGradeExtension);
		assertEquals(MatchGrade.CERTAIN.toCode(), matchGradeExtension.getValue().toString());
	}

	@Test
	public void testMedicationMatch() throws Exception {
		createDummyOrganization();


		Medication medication = buildMedication("Organization/mfr");
		Medication createdMedication = createMedication(medication);
		Medication newMedication = buildMedication("Organization/mfr");

		Bundle result = (Bundle) myMdmProvider.serverMatch(newMedication, new StringType("Medication"), new SystemRequestDetails());
		assertEquals(1, result.getEntry().size());

		Bundle.BundleEntryComponent entry0 = result.getEntry().get(0);
		assertEquals(createdMedication.getId(), entry0.getResource().getId());

		Bundle.BundleEntrySearchComponent searchComponent = entry0.getSearch();
		assertEquals(Bundle.SearchEntryMode.MATCH, searchComponent.getMode());

		//Since there is only
		assertEquals(1.0 / 1.0, searchComponent.getScore().doubleValue(), 0.01);
		Extension matchGradeExtension = searchComponent.getExtensionByUrl(MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE);
		assertNotNull(matchGradeExtension);
		assertEquals(MatchGrade.CERTAIN.toCode(), matchGradeExtension.getValue().toString());

	}


	@Test
	public void testServerLevelMatch() throws Exception {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.serverMatch(newJane, new StringType("Patient"), new SystemRequestDetails());
		assertEquals(1, result.getEntry().size());

		Bundle.BundleEntryComponent entry0 = result.getEntry().get(0);
		assertEquals(createdJane.getId(), entry0.getResource().getId());

		Bundle.BundleEntrySearchComponent searchComponent = entry0.getSearch();
		assertEquals(Bundle.SearchEntryMode.MATCH, searchComponent.getMode());

		assertEquals(2.0 / 3.0, searchComponent.getScore().doubleValue(), 0.01);
		Extension matchGradeExtension = searchComponent.getExtensionByUrl(MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE);
		assertNotNull(matchGradeExtension);
		assertEquals(MatchGrade.CERTAIN.toCode(), matchGradeExtension.getValue().toString());
	}

	@Test
	public void testMatchOrder() throws Exception {
		Patient jane0 = buildJanePatient();
		Patient createdJane1 = createPatient(jane0);

		Patient jane1 = buildPatientWithNameAndId(NAME_GIVEN_JANET, JANE_ID);
		jane1.setActive(true);
		Patient createdJane2 = createPatient(jane1);

		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.match(newJane, new SystemRequestDetails());
		assertEquals(2, result.getEntry().size());

		Bundle.BundleEntryComponent entry0 = result.getEntry().get(0);
		assertTrue(jane0.getId().equals(((Patient) entry0.getResource()).getId()), "First match should be Jane");
		Bundle.BundleEntryComponent entry1 = result.getEntry().get(1);
		assertTrue(jane1.getId().equals(((Patient) entry1.getResource()).getId()), "Second match should be Janet");

		List<Double> scores = result.getEntry()
			.stream()
			.map(bec -> bec.getSearch().getScore().doubleValue())
			.collect(Collectors.toList());
		assertTrue(Ordering.<Double>natural().reverse().isOrdered(scores), "Match scores must be descending");
	}

	@Test
	public void testMismatch() throws Exception {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);

		Patient paul = buildPaulPatient();
		paul.setActive(true);

		Bundle result = (Bundle) myMdmProvider.match(paul, new SystemRequestDetails());
		assertEquals(0, result.getEntry().size());
	}

	@Test
	public void testMatchWithEmptySearchParamCandidates() throws Exception {
		setMdmRuleJson("mdm/empty-candidate-search-params.json");
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.match(newJane, new SystemRequestDetails());
		assertEquals(1, result.getEntry().size());
		assertEquals(createdJane.getId(), result.getEntryFirstRep().getResource().getId());
	}

	@Test
	public void testMatchWithCoarseDateGranularity() throws Exception {
		setMdmRuleJson("mdm/coarse-birthdate-mdm-rules.json");

		String granularPatient = """
			{
			    "resourceType": "Patient",
			    "active": true,
			    "name": [
			        {
			            "family": "PETERSON",
			            "given": [
			                "GARY",
			                "D"
			            ]
			        }
			    ],
			    "telecom": [
			        {
			            "system": "phone",
			            "value": "100100100",
			            "use": "home"
			        }
			    ],
			    "gender": "male",
			    "birthDate": "1991-10-10",
			    "address": [
			        {
			            "state": "NY",
			            "postalCode": "12313"
			        }
			    ]
			}""";
		IBaseResource iBaseResource = myFhirContext.newJsonParser().parseResource(granularPatient);
		createPatient((Patient) iBaseResource);

		String coarsePatient = """
			{
			    "resourceType": "Patient",
			    "active": true,
			    "name": [
			        {
			            "family": "PETERSON",
			            "given": [
			                "GARY",
			                "D"
			            ]
			        }
			    ],
			    "telecom": [
			        {
			            "system": "phone",
			            "value": "100100100",
			            "use": "home"
			        }
			    ],
			    "gender": "male",
			    "birthDate": "1991-10",
			    "address": [
			        {
			            "state": "NY",
			            "postalCode": "12313"
			        }
			    ]
			}""";

		IBaseResource coarseResource = myFhirContext.newJsonParser().parseResource(coarsePatient);
		Bundle result = (Bundle) myMdmProvider.match((Patient) coarseResource, new SystemRequestDetails());
		assertEquals(1, result.getEntry().size());
	}

	@Test
	public void testNicknameMatch() throws IOException {
		setMdmRuleJson("mdm/nickname-mdm-rules.json");

		String formalPatientJson = """
			{
			    "resourceType": "Patient",
			    "active": true,
			    "name": [
			        {
			            "family": "PETERSON",
			            "given": [
			                "Gregory"
			            ]
			        }
			    ],
			    "gender": "male"
			}""";
		Patient formalPatient = (Patient) myFhirContext.newJsonParser().parseResource(formalPatientJson);
		createPatient(formalPatient);

		String noMatchPatientJson = """
			{
			    "resourceType": "Patient",
			    "active": true,
			    "name": [
			        {
			            "family": "PETERSON",
			            "given": [
			                "Bob"
			            ]
			        }
			    ],
			    "gender": "male"
			}""";
		Patient noMatchPatient = (Patient) myFhirContext.newJsonParser().parseResource(noMatchPatientJson);
		createPatient(noMatchPatient);
		{
			Bundle result = (Bundle) myMdmProvider.match(noMatchPatient, new SystemRequestDetails());
			assertEquals(0, result.getEntry().size());
		}

		String nickPatientJson = """
			{
			    "resourceType": "Patient",
			    "active": true,
			    "name": [
			        {
			            "family": "PETERSON",
			            "given": [
			                "Greg"
			            ]
			        }
			    ],
			    "gender": "male"
			}""";

		{
			Patient nickPatient = (Patient) myFhirContext.newJsonParser().parseResource(nickPatientJson);
			Bundle result = (Bundle) myMdmProvider.match(nickPatient, new SystemRequestDetails());
			assertEquals(1, result.getEntry().size());
		}
	}
}
