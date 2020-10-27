package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmpiProviderMatchR4Test extends BaseProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(EmpiProviderMatchR4Test.class);

	@Override
	@BeforeEach
	public void before() {
		super.before();
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testMatch() throws Exception {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = myEmpiProviderR4.match(newJane);
		assertEquals(1, result.getEntry().size());

		Bundle.BundleEntryComponent entry0 = result.getEntry().get(0);
		assertEquals(createdJane.getId(), entry0.getResource().getId());

		Bundle.BundleEntrySearchComponent searchComponent = entry0.getSearch();
		assertEquals(Bundle.SearchEntryMode.MATCH, searchComponent.getMode());

		assertEquals(2.0/3.0, searchComponent.getScore().doubleValue(), 0.01);
		Extension matchGradeExtension = searchComponent.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/match-grade");
		assertNotNull(matchGradeExtension);
		assertEquals(MatchGrade.CERTAIN.toCode(), matchGradeExtension.getValue().toString());
	}

	@Test
	public void testMismatch() throws Exception {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);

		Patient paul = buildPaulPatient();
		paul.setActive(true);

		Bundle result = myEmpiProviderR4.match(paul);
		assertEquals(0, result.getEntry().size());
	}

	@Test
	public void testMatchWithEmptySearchParamCandidates() throws Exception {
		setEmpiRuleJson("empi/empty-candidate-search-params.json");
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = myEmpiProviderR4.match(newJane);
		assertEquals(1, result.getEntry().size());
		assertEquals(createdJane.getId(), result.getEntryFirstRep().getResource().getId());
	}
}
