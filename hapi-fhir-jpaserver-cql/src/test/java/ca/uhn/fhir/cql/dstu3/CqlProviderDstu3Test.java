package ca.uhn.fhir.cql.dstu3;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderFactory;
import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.ValueSetResourceProvider;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderDstu3Test extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderDstu3Test.class);

	@Autowired
	CqlProviderFactory myCqlProviderFactory;
	@Autowired
	IFhirSystemDao mySystemDao;
	@Autowired
	private LibraryResourceProvider myLibraryResourceProvider;
	@Autowired
	private MeasureResourceProvider myMeasureResourceProvider;
	@Autowired
	private ValueSetResourceProvider myValueSetResourceProvider;
	@Autowired
	private MeasureOperationsProvider myMeasureOperationsProvider;

	@BeforeEach
	public void before() throws IOException {
		// Load terminology for measure tests (HEDIS measures)
		loadBundle("dstu3/hedis-valuesets-bundle.json");

		// Load libraries
		loadResource("dstu3/library/library-fhir-model-definition.json");
		loadResource("dstu3/library/library-fhir-helpers.json");

		// load test data and conversion library for $apply operation tests
		loadResource("dstu3/general-practitioner.json");
		loadResource("dstu3/general-patient.json");
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
	public void evaluatePatientMeasure() throws IOException {
		loadResource("dstu3/library/library-asf-logic.json");
		// Load the measure for ASF: Unhealthy Alcohol Use Screening and Follow-up (ASF)
		loadResource("dstu3/measure-asf.json");
		Bundle result = loadBundle("dstu3/test-patient-6529-data.json");
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
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
			patient, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));

		// Now timed runs
		int runCount = 10;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < runCount; ++i) {
			myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
				patient, null, null, null, null, null, null);
		}

		ourLog.info("Called evaluateMeasure() {} times: average time per call: {}", runCount, sw.formatMillisPerOperation(runCount));
	}

	@Test
	public void evaluatePopulationMeasure() throws IOException {
		loadResource("dstu3/library/library-asf-logic.json");
		// Load the measure for ASF: Unhealthy Alcohol Use Screening and Follow-up (ASF)
		loadResource("dstu3/measure-asf.json");
		loadBundle("dstu3/test-patient-6529-data.json");
		// Add a second patient with the same data
		loadBundle("dstu3/test-patient-9999-x-data.json");

		IdType measureId = new IdType("Measure", "measure-asf");
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		// TODO KBD Why does this call accept a reportType of "population" ? http://hl7.org/fhir/STU3/valueset-measure-report-type.html
		// First run to absorb startup costs
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "population",
			null, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));

		// Now timed runs
		int runCount = 10;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < runCount; ++i) {
			myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "population",
				null, null, null, null, null, null, null);
		}

		ourLog.info("Called evaluateMeasure() {} times: average time per call: {}", runCount, sw.formatMillisPerOperation(runCount));
	}

	// Fails with:
	// java.lang.IllegalArgumentException: Could not load library source for libraries referenced in Measure/Measure/measure-EXM104-FHIR3-8.1.000/_history/1.
	//@Test
	public void testEXM104() throws IOException {
		Bundle result = loadBundle("dstu3/EXM104/valuesets-EXM104_FHIR3-8.1.000-bundle.json");
		result = loadBundle("dstu3/EXM104/library-deps-EXM104_FHIR3-8.1.000-bundle.json");
		result = loadBundle("dstu3/EXM104/tests-denom-EXM104_FHIR3-bundle.json");
		result = loadBundle("dstu3/EXM104/tests-numer-EXM104_FHIR3-bundle.json");
		loadResource("dstu3/EXM104/library-EXM104_FHIR3-8.1.000.json");
		loadResource("dstu3/EXM104/measure-EXM104_FHIR3-8.1.000.json");
		//Bundle result = loadBundle("dstu3/test-patient-6529-data.json");
		//assertNotNull(result);
		//List<Bundle.BundleEntryComponent> entries = result.getEntry();
		//assertThat(entries, hasSize(22));
		//assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		//assertEquals(entries.get(21).getResponse().getStatus(), "201 Created");

		IdType measureId = new IdType("Measure", "measure-EXM104-FHIR3-8.1.000");
		//String patient = "Patient/Patient-6529";
		//String periodStart = "2003-01-01";
		//String periodEnd = "2003-12-31";

		// First run to absorb startup costs
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, null, null, null, null,
			null, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));

	}

	// Fails with:
	//	java.lang.IllegalArgumentException: [MATGlobalCommonFunctions_FHIR3-4.0.000[40:74, 40:92]Timezone keyword is only valid in 1.3 or lower,
	//	MATGlobalCommonFunctions_FHIR3-4.0.000[223:19, 223:53]Could not resolve membership operator for terminology target of the retrieve.,
	//	MATGlobalCommonFunctions_FHIR3-4.0.000[229:21, 229:90]Could not resolve membership operator for terminology target of the retrieve.,
	//	MATGlobalCommonFunctions_FHIR3-4.0.000[40:74, 40:92]Timezone keyword is only valid in 1.3 or lower,
	//	MATGlobalCommonFunctions_FHIR3-4.0.000[223:19, 223:53]Could not resolve membership operator for terminology target of the retrieve.,
	//	MATGlobalCommonFunctions_FHIR3-4.0.000[229:21, 229:90]Could not resolve membership operator for terminology target of the retrieve.,
	//	TJCOverall_FHIR3-3.6.000[74:18, 74:37]Could not resolve call to operator ToDate with signature (System.DateTime).]
	//	at org.opencds.cqf.common.evaluation.LibraryLoader.loadLibrary(LibraryLoader.java:86)
	//	at org.opencds.cqf.common.evaluation.LibraryLoader.resolveLibrary(LibraryLoader.java:63)
	//	at org.opencds.cqf.common.evaluation.LibraryLoader.load(LibraryLoader.java:105)
	//	at org.opencds.cqf.dstu3.helpers.LibraryHelper.loadLibraries(LibraryHelper.java:61)
	//	at org.opencds.cqf.dstu3.evaluation.MeasureEvaluationSeed.setup(MeasureEvaluationSeed.java:57)
	//	at org.opencds.cqf.dstu3.providers.MeasureOperationsProvider.evaluateMeasure(MeasureOperationsProvider.java:184)
	//	at ca.uhn.fhir.cql.dstu3.CqlProviderDstu3Test.testConnectathonExample_EXM104(CqlProviderDstu3Test.java:238)
	//@Test
	public void testConnectathonExample_EXM104() throws IOException {
		// git clone git@github.com:DBCG/connectathon.git
		Bundle bundle = loadBundle("dstu3/Connectathon/EXM104_FHIR3-8.1.000/EXM104_FHIR3-8.1.000-files/library-deps-EXM104_FHIR3-8.1.000-bundle.json");
		loadResource("dstu3/Connectathon/EXM104_FHIR3-8.1.000/EXM104_FHIR3-8.1.000-files/library-EXM104_FHIR3-8.1.000.json");
		bundle = loadBundle("dstu3/Connectathon/EXM104_FHIR3-8.1.000/EXM104_FHIR3-8.1.000-bundle.json");
		int numFilesLoaded = loadDataFromDirectory("dstu3/Connectathon/EXM104_FHIR3-8.1.000/EXM104_FHIR3-8.1.000-files");
		assertEquals(numFilesLoaded, 6);
		ourLog.info("Data imported successfully!");
		assertNotNull(bundle);
		List<Bundle.BundleEntryComponent> entries = bundle.getEntry();
		assertThat(entries, hasSize(35));
		assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		assertEquals(entries.get(34).getResponse().getStatus(), "201 Created");

		IdType measureId = new IdType("Measure", "measure-EXM104-FHIR3-8.1.000");
		String patient = "Patient/numer-EXM104-FHIR3";

		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, null, null, null, null,
			patient, null, null, null, null, null, null);
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
	}

	private int loadDataFromDirectory(String theDirectoryName) throws IOException {
		int count = 0;
		ourLog.info("Reading files in directory: {}", theDirectoryName);
		ClassPathResource dir = new ClassPathResource(theDirectoryName);
		Collection<File> files = FileUtils.listFiles(dir.getFile(), null, false);
		ourLog.info("{} files found.", files.size());
		for (File file : files) {
			String filename = file.getAbsolutePath();
			ourLog.info("Processing filename '{}'", filename);
			if (filename.endsWith(".cql") || filename.contains("expectedresults")) {
				// Ignore .cql and expectedresults files
				ourLog.info("Ignoring file: '{}'", filename);
			} else if (filename.endsWith(".json")) {
				if (filename.contains("bundle")) {
					loadBundle(filename);
				} else {
					loadResource(filename);
				}
				count++;
			} else {
				ourLog.info("Ignoring file: '{}'", filename);
			}
		}
		return count;
	}

	private Bundle loadBundle(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		Bundle result = (Bundle) mySystemDao.transaction(null, bundle);
		return result;
	}
}
