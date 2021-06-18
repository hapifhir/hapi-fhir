package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.MeasureReport.MeasureReportGroupComponent;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CqlMeasureEvaluationR4Test extends BaseCqlR4Test {
	Logger ourLog = LoggerFactory.getLogger(CqlMeasureEvaluationR4Test.class);

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	protected void testMeasureBundle(String theLocation) throws IOException {
		Bundle bundle = parseBundle(theLocation);
		loadBundle(bundle, myRequestDetails);

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
			null, null, null, null, null, null, myRequestDetails);

		compareMeasureReport(expected, actual);
	}

	protected void compareMeasureReport(MeasureReport expected, MeasureReport actual) {
		assertNotNull("expected MeasureReport can not be null", expected);
		assertNotNull("actual MeasureReport can not be null", actual);

		String errorLocator = String.format("Measure: %s, Subject: %s", expected.getMeasure(),
				expected.getSubject().getReference());

		assertEquals(expected.hasGroup(), actual.hasGroup(), errorLocator);
		assertEquals(expected.getGroup().size(), actual.getGroup().size(), errorLocator);

		for (MeasureReportGroupComponent mrgcExpected : expected.getGroup()) {
			Optional<MeasureReportGroupComponent> mrgcActualOptional = actual.getGroup().stream()
			.filter(x -> x.getId() != null && x.getId().equals(mrgcExpected.getId())).findFirst();

			errorLocator = String.format("Measure: %s, Subject: %s, Group: %s", expected.getMeasure(),
					expected.getSubject().getReference(), mrgcExpected.getId());
			assertTrue(errorLocator, mrgcActualOptional.isPresent());

			MeasureReportGroupComponent mrgcActual = mrgcActualOptional.get();

			if (mrgcExpected.getMeasureScore() == null) {
				assertNull(mrgcActual.getMeasureScore(), errorLocator);
			} else {
				assertNotNull(mrgcActual.getMeasureScore());
				Quantity quantityExpected = mrgcExpected.getMeasureScore();
				Quantity quantityActual = mrgcActual.getMeasureScore();

				assertThat(errorLocator, quantityActual.getValue(), Matchers.comparesEqualTo(quantityExpected.getValue()));
			}
		}
	}

	// TODO: In R4 the Subject will not necessarily be a Patient.
	public String getPatientId(MeasureReport measureReport) {
		String[] subjectRefParts = measureReport.getSubject().getReference().split("/");
		String patientId = subjectRefParts[subjectRefParts.length - 1];
		return patientId;
	}

	public String getMeasureId(MeasureReport measureReport) {
		String[] measureRefParts = measureReport.getMeasure().split("/");
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

	// @Test - No test results in this bundle yet
	// public void test_EXM74_102000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM74-10.2.000-bundle.json");
	// }

	@Test
	public void test_EXM105_82000() throws IOException {
		this.testMeasureBundle("r4/connectathon/EXM105-8.2.000-bundle.json");
	}

	@Test
	public void test_EXM108_83000() throws IOException {
		this.testMeasureBundle("r4/connectathon/EXM108-8.3.000-bundle.json");
	}

	// @Test - No test results in this bundle yet
	// public void test_EXM111_91000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM111-9.1.000-bundle.json");
	// }

	// @Test - The test data for the denominator exclusion appears to be invalid
	// public void test_EXM124_82000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM124-8.2.000-bundle.json");
	// }

	@Test
	public void test_EXM124_90000() throws IOException {
		this.testMeasureBundle("r4/connectathon/EXM124-9.0.000-bundle.json");
	}

	@Test
	public void test_EXM125_73000() throws IOException {
		this.testMeasureBundle("r4/connectathon/EXM125-7.3.000-bundle.json");
	}

	@Test
	public void test_EXM130_73000() throws IOException {
		this.testMeasureBundle("r4/connectathon/EXM130-7.3.000-bundle.json");
	}

	// @Test - No test results in this bundle yet
	// public void test_EXM149_92000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM149-9.2.000-bundle.json");
	// }

	// @Test - Missing Adult outpatient encounters Library
	// public void test_EXM153_92000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM153-9.2.000-bundle.json");
	// }

	// @Test - Missing Encounter data for Numerator test
	// public void test_EXM347_43000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM347-4.3.000-bundle.json");
	// }

	// @Test - No test results in this bundle yet
	// public void test_EXM349_210000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM349-2.10.000-bundle.json");
	// }

	// @Test - No test results in this bundle yet
	// public void test_EXM506_22000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM506-2.2.000-bundle.json");
	// }

	// @Test - No test results in this bundle yet
	// public void test_EXM529_10000() throws IOException {
	// 	this.testMeasureBundle("r4/connectathon/EXM529-1.0.000-bundle.json");
	// }
}
