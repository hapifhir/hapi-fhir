package ca.uhn.fhir.cql.dstu3;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderFactory;
import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderDstu3Test extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderDstu3Test.class);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	CqlProviderFactory myCqlProviderFactory;
	@Autowired
	private MeasureOperationsProvider myMeasureOperationsProvider;

	@BeforeEach
	public void before() throws IOException {
		// Load terminology for measure tests (HEDIS measures)
		loadBundle("dstu3/hedis-ig/hedis-valuesets-bundle.json");

		// Load libraries
		loadResource("dstu3/hedis-ig/library/library-fhir-model-definition.json", myRequestDetails);
		loadResource("dstu3/hedis-ig/library/library-fhir-helpers.json", myRequestDetails);
	}

	/*
		See dstu3/library-asf-cql.txt to see the cql encoded within library-asf-logic.json
		See dstu3/library-asf-elm.xml to see the elm encoded within library-asf-logic.json
		To help explain what's being measured here.  Specifically how to interpret the contents of library-asf-logic.json.
		From https://www.ncqa.org/wp-content/uploads/2020/02/20200212_17_ASF.pdf
		• ValueSet: "Alcohol Counseling and Treatment": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1437'
		• ValueSet: "Alcohol Screening": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1337'
		• ValueSet: "Alcohol use disorder": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1339'
		• ValueSet: "Dementia": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1074'
		• Diagnosis: Alcohol Use Disorder (2.16.840.1.113883.3.464.1004.1339)
		• Diagnosis: Dementia (2.16.840.1.113883.3.464.1004.1074)
		• Encounter, Performed: Hospice Encounter (2.16.840.1.113883.3.464.1004.1761)
		• Intervention, Order: Hospice Intervention (2.16.840.1.113883.3.464.1004.1762)
		• Intervention, Performed: Alcohol Counseling or Other Follow Up Care
		(2.16.840.1.113883.3.464.1004.1437)
		• Intervention, Performed: Hospice Intervention (2.16.840.1.113883.3.464.1004.1762)
		Direct Reference Codes:
		• Assessment, Performed: How often have you had five or more drinks in one day during the past year
		[Reported] (LOINC version 2.63 Code 88037-7)
		• Assessment, Performed: How often have you had four or more drinks in one day during the past year
		[Reported] (LOINC version 2.63 Code 75889-6)
		• Assessment, Performed: Total score [AUDIT-C] (LOINC version 2.63 Code 75626-2)
	 */
	@Test
	public void testHedisIGEvaluatePatientMeasure() throws IOException {
		loadResource("dstu3/hedis-ig/library/library-asf-logic.json", myRequestDetails);
		// Load the measure for ASF: Unhealthy Alcohol Use Screening and Follow-up (ASF)
		loadResource("dstu3/hedis-ig/measure-asf.json", myRequestDetails);
		Bundle result = loadBundle("dstu3/hedis-ig/test-patient-6529-data.json");
		assertNotNull(result);
		List<Bundle.BundleEntryComponent> entries = result.getEntry();
		assertThat(entries, hasSize(22));
		assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		assertEquals(entries.get(21).getResponse().getStatus(), "201 Created");

		IdType measureId = new IdType("Measure", "measure-asf");
		String patient = "Patient/Patient-6529";
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		// First run to absorb startup costs
		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
			patient, null, null, null, null, null, null, myRequestDetails);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));

		// Now timed runs
		int runCount = 10;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < runCount; ++i) {
			myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
				patient, null, null, null, null, null, null, myRequestDetails);
		}

		ourLog.info("Called evaluateMeasure() {} times: average time per call: {}", runCount, sw.formatMillisPerOperation(runCount));
		assertTrue(myPartitionHelper.wasCalled());
	}

	@Test
	public void testHedisIGEvaluatePopulationMeasure() throws IOException {
		loadResource("dstu3/hedis-ig/library/library-asf-logic.json", myRequestDetails);
		// Load the measure for ASF: Unhealthy Alcohol Use Screening and Follow-up (ASF)
		loadResource("dstu3/hedis-ig/measure-asf.json", myRequestDetails);
		loadBundle("dstu3/hedis-ig/test-patient-6529-data.json");
		// Add a second patient with the same data
		loadBundle("dstu3/hedis-ig/test-patient-9999-x-data.json");

		IdType measureId = new IdType("Measure", "measure-asf");
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		// First run to absorb startup costs
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "population",
			null, null, null, null, null, null, null, myRequestDetails);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));

		// Now timed runs
		int runCount = 10;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < runCount; ++i) {
			myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "population",
				null, null, null, null, null, null, null, myRequestDetails);
		}

		ourLog.info("Called evaluateMeasure() {} times: average time per call: {}", runCount, sw.formatMillisPerOperation(runCount));
	}
}
