package ca.uhn.fhir.cr.dstu3;


import ca.uhn.fhir.cr.BaseCrDstu3TestServer;

import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.MeasureReport.MeasureReportGroupComponent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ContextConfiguration(classes = {TestCrDstu3Config.class})
public class CrDstu3MeasureOperationProviderIT extends BaseCrDstu3TestServer {
	private static final Logger ourLog = LoggerFactory.getLogger(CrDstu3MeasureOperationProviderIT.class);

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;
	private final SystemRequestDetails mySrd = new SystemRequestDetails();

	protected void testMeasureBundle(String theLocation) {
		var bundle = loadBundle(theLocation);

		var measures = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Measure.class);
		if (measures == null || measures.isEmpty()) {
			throw new IllegalArgumentException(String.format("No measures found for Bundle %s", theLocation));
		}

		List<MeasureReport> reports = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, MeasureReport.class);
		if (reports == null || reports.isEmpty()) {
			throw new IllegalArgumentException(String.format("No measure reports found for Bundle %s", theLocation));
		}

		for (MeasureReport report : reports) {
			testMeasureReport(report);
		}
	}

	protected void testMeasureReport(MeasureReport expected) {
		String measureId = this.getMeasureId(expected);
		String patientId = this.getPatientId(expected);
		String periodStart = "2019-01-01";//this.getPeriodStart(expected);
		String periodEnd = "2019-12-31";//this.getPeriodEnd(expected);

		ourLog.info("Measure: {}, Patient: {}, Start: {}, End: {}", measureId, patientId, periodStart, periodEnd);

		MeasureReport actual = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", measureId),
			periodStart,
			periodEnd,
			"patient",
			patientId,
			null, null, null, null, null, mySrd);

		compareMeasureReport(expected, actual);
	}

	protected void compareMeasureReport(MeasureReport expected, MeasureReport actual) {
		assertNotNull("expected MeasureReport can not be null", expected);
		assertNotNull("actual MeasureReport can not be null", actual);

		String errorLocator = String.format("Measure: %s, Subject: %s", expected.getMeasure().getReference(),
			expected.getPatient().getReference());

		assertEquals(expected.hasGroup(), actual.hasGroup(), errorLocator);
		assertEquals(expected.getGroup().size(), actual.getGroup().size(), errorLocator);

		for (MeasureReportGroupComponent mrgcExpected : expected.getGroup()) {
			Optional<MeasureReportGroupComponent> mrgcActualOptional = actual.getGroup().stream()
				.filter(x -> x.getId() != null && x.getId().equals(mrgcExpected.getIdentifier().getValue())).findFirst();

			errorLocator = String.format("Measure: %s, Subject: %s, Group: %s", expected.getMeasure().getReference(),
				expected.getPatient().getReference(), mrgcExpected.getIdentifier().getValue());
			assertTrue(errorLocator, mrgcActualOptional.isPresent());

			MeasureReportGroupComponent mrgcActual = mrgcActualOptional.get();

			if (mrgcExpected.getMeasureScore() == null) {
				assertNull(mrgcActual.getMeasureScore(), errorLocator);
			} else {
				assertNotNull(errorLocator, mrgcActual.getMeasureScore());
				BigDecimal decimalExpected = mrgcExpected.getMeasureScore();
				BigDecimal decimalActual = mrgcActual.getMeasureScore();

				assertThat(errorLocator, decimalActual, Matchers.comparesEqualTo(decimalExpected));
			}
		}
	}

	public String getPatientId(MeasureReport measureReport) {
		String[] subjectRefParts = measureReport.getPatient().getReference().split("/");
		String patientId = subjectRefParts[subjectRefParts.length - 1];
		return "Patient/" + patientId;
	}

	public String getMeasureId(MeasureReport measureReport) {
		String[] measureRefParts = measureReport.getMeasure().getReference().split("/");
		String measureId = measureRefParts[measureRefParts.length - 1];
		return measureId;
	}

	public String getPeriodStart(MeasureReport measureReport) {
		Date periodStart = measureReport.getPeriod().getStart();
		if (periodStart != null) {
			return toDateString(periodStart);
		}
		return null;
	}

	public String getPeriodEnd(MeasureReport measureReport) {
		Date periodEnd = measureReport.getPeriod().getEnd();
		if (periodEnd != null) {
			return toDateString(periodEnd);
		}
		return null;
	}

	public String toDateString(Date date) {
		return new DateTimeType(date).getValueAsString();
	}

	@Test
	public void test_EXM124_FHIR3_72000() throws IOException {
		ourPagingProvider.setDefaultPageSize(100);
		ourPagingProvider.setMaximumPageSize(100);
		this.testMeasureBundle("ca/uhn/fhir/cr/dstu3/connectathon/EXM124-FHIR3-7.2.000-bundle.json");
	}

	@Test
	public void test_EXM104_FHIR3_81000() throws IOException {
		ourPagingProvider.setDefaultPageSize(100);
		ourPagingProvider.setMaximumPageSize(100);
		this.testMeasureBundle("ca/uhn/fhir/cr/dstu3/connectathon/EXM104-FHIR3-8.1.000-bundle.json");
	}

	@Test
	void testMeasureEvaluate() throws IOException {
		loadBundle("Exm105Fhir3Measure.json");
		ourPagingProvider.setDefaultPageSize(100);
		ourPagingProvider.setMaximumPageSize(100);
		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/denom-EXM105-FHIR3",
			null,
			"2019-12-12",
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}

	// This test is failing because the Dstu3MeasureProcessor in the evaluator is not checking the additionalData bundle for the patient
	//@Test
	void testMeasureEvaluateWithAdditionalData() throws IOException {
		loadBundle("Exm105FhirR3MeasurePartBundle.json");
		ourPagingProvider.setDefaultPageSize(100);
		ourPagingProvider.setMaximumPageSize(100);
		var additionalData = readResource(Bundle.class, "Exm105FhirR3MeasureAdditionalData.json");

		var patient = "Patient/denom-EXM105-FHIR3";
		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			patient,
			null,
			"2019-12-12",
			null,
			additionalData,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
		assertEquals(patient, returnMeasureReport.getPatient().getReference());
	}

	@Test
	void testMeasureEvaluateWithTerminology() throws IOException {
		loadBundle("Exm105Fhir3Measure.json");
		ourPagingProvider.setDefaultPageSize(100);
		ourPagingProvider.setMaximumPageSize(100);
		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"patient",
			"Patient/denom-EXM105-FHIR3",
			null,
			null,
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}
}
