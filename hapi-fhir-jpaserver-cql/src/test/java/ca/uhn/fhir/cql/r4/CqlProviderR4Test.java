package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderR4Test extends BaseCqlR4Test implements CqlProviderTestBase {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	public void testHedisIGEvaluateMeasure() throws IOException {
		IdType libraryId = new IdType("Library", "library-mrp-logic");
		IdType measureId = new IdType("Measure", "measure-asf");
		loadResource("r4/hedis-ig/library-asf-logic.json");
		loadResource("r4/hedis-ig/measure-asf.json");
		Bundle result = loadBundle("dstu3/hedis-ig/test-patient-6529-data.json");

		String measure = "Measure/measure-asf";
		String patient = "Patient/Patient-6529";
		String periodStart = "2000-01-01";
		String periodEnd = "2019-12-31";

		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, measure, "patient",
			patient, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}
}
