package ca.uhn.fhir.cql.dstu3;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.util.BundleUtil;

public class CqlMeasureEvaluationDstu3Test extends BaseCqlDstu3Test {
	Logger ourLog = LoggerFactory.getLogger(CqlMeasureEvaluationDstu3Test.class);

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	protected void testMeasureBundle(String theLocation) throws IOException {
		Bundle bundle = parseBundle(theLocation);
		loadBundle(bundle);

		List<Measure> measures = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Measure.class);
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
		String periodStart = this.getPeriodStart(expected);
		String periodEnd = this.getPeriodEnd(expected);

		this.ourLog.info("Measure: %s, Patient: %s, Start: %s, End: %s", measureId, patientId, periodStart, periodEnd);

		MeasureReport actual = this.myMeasureOperationsProvider.evaluateMeasure(new IdType("Measure", measureId),
				periodStart, periodEnd, null,
				// TODO: These are all individual reports
				"patient", patientId,
				// TODO: Generalize these parameters into a Parameters resource
				null, null, null, null, null, null);

		compareMeasureReport(expected, actual);
	}

	protected void compareMeasureReport(MeasureReport expected, MeasureReport actual) {
		assertNotNull("expected MeasureReport can not be null", expected);
		assertNotNull("actual MeasureReport can not be null", actual);

		String errorLocator = String.format("Measure: %s, Subject: %s", expected.getMeasure(),
				expected.getPatient().getReference());

		assertEquals(expected.hasGroup(), actual.hasGroup(), errorLocator);
		assertEquals(expected.getGroup().size(), actual.getGroup().size(), errorLocator);

		for (MeasureReportGroupComponent mrgcExpected : expected.getGroup()) {
			Optional<MeasureReportGroupComponent> mrgcActualOptional = actual.getGroup().stream()
					.filter(x -> x.getId().equals(mrgcExpected.getId())).findFirst();

			errorLocator = String.format("Measure: %s, Subject: %s, Group: %s", expected.getMeasure(),
					expected.getPatient().getReference(), mrgcExpected.getId());
			assertTrue(errorLocator, mrgcActualOptional.isPresent());

			MeasureReportGroupComponent mrgcActual = mrgcActualOptional.get();

			if (mrgcExpected.getMeasureScore() == null) {
				assertNull(mrgcActual.getMeasureScore(), errorLocator);
			} else {
				assertNotNull(mrgcActual.getMeasureScore());
				BigDecimal decimalExpected = mrgcExpected.getMeasureScore();
				BigDecimal decimalActual = mrgcActual.getMeasureScore();

				assertThat(errorLocator, decimalActual, Matchers.comparesEqualTo(decimalExpected));
			}
		}
	}

	public String getPatientId(MeasureReport measureReport) {
		String[] subjectRefParts = measureReport.getPatient().getReference().split("/");
		String patientId = subjectRefParts[subjectRefParts.length - 1];
		return patientId;
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

	// As of 2/11/2021, all the DSTU3 bundles in the Connectathon IG are out of date
	// and can't be posted
	// @Test
	// public void test_EXM117_83000() throws IOException {
	// 	this.testMeasureBundle("dstu3/connectathon/EXM117_FHIR3-8.3.000-bundle.json");
	// }
}
