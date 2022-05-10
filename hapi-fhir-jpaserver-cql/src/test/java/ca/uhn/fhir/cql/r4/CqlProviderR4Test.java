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
	private static final IdType measureId = new IdType("Measure", "measure-asf");
	private static final String measure = "Measure/measure-asf";
	private static final String patient = "Patient/Patient-6529";
	private static final String periodStart = "2000-01-01";
	private static final String periodEnd = "2019-12-31";
	private static boolean bundlesLoaded = false;

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	public synchronized void loadBundles() throws IOException {
		if (!bundlesLoaded) {
			Bundle result = loadBundle("dstu3/hedis-ig/test-patient-6529-data.json");
			bundlesLoaded = true;
		}
	}

	@Test
	public void testHedisIGEvaluateMeasureWithTimeframe() throws IOException {
		loadBundles();
		loadResource("r4/hedis-ig/library-asf-logic.json", myRequestDetails);
		loadResource("r4/hedis-ig/measure-asf.json", myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, measure, "patient",
			patient, null, null, null, null, null, null, myRequestDetails);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}

	@Test
	public void testHedisIGEvaluateMeasureNoTimeframe() throws IOException {
		loadBundles();
		loadResource("r4/hedis-ig/library-asf-logic.json", myRequestDetails);
		loadResource("r4/hedis-ig/measure-asf.json", myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, null, null, measure, "patient",
			patient, null, null, null, null, null, null, myRequestDetails);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}
}
