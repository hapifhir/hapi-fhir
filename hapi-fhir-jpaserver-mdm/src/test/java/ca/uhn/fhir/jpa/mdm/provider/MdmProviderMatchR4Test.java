package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.mdm.api.MdmConstants;
import com.google.common.collect.Ordering;
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

		Bundle result = myMdmProviderR4.match(newJane);
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

		Bundle result = myMdmProviderR4.serverMatch(newMedication, new StringType("Medication"));
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

		Bundle result = myMdmProviderR4.serverMatch(newJane, new StringType("Patient"));
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

		Bundle result = myMdmProviderR4.match(newJane);
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

		Bundle result = myMdmProviderR4.match(paul);
		assertEquals(0, result.getEntry().size());
	}

	@Test
	public void testMatchWithEmptySearchParamCandidates() throws Exception {
		setMdmRuleJson("mdm/empty-candidate-search-params.json");
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = myMdmProviderR4.match(newJane);
		assertEquals(1, result.getEntry().size());
		assertEquals(createdJane.getId(), result.getEntryFirstRep().getResource().getId());
	}
}
