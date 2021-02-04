package ca.uhn.fhir.cql.r4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.MeasureReport.MeasureReportGroupPopulationComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;

public class CqlMeasureEvaluationR4Test extends BaseCqlR4Test implements CqlProviderTestBase {

	private static final IdType measureId = new IdType("Measure", "measure-EXM130-7.3.000");
	private static final String periodStart = "2019-01-01";
	private static final String periodEnd = "2019-12-31";

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	public void loadBundles() throws IOException {
		loadBundle("r4/connectathon/EXM130-7.3.000-bundle.json");
	}

	@Test
	public void testExm130PatientNumerator() throws IOException {
		loadBundles();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "patient",
			"numer-EXM130", null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertEquals(new BigDecimal("1.0"), report.getGroupFirstRep().getMeasureScore().getValue());
	}

	@Test
	public void testExm130PatientDenominator() throws IOException {
		loadBundles();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, "patient",
			"denom-EXM130", null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertEquals(new BigDecimal("0.0"), report.getGroupFirstRep().getMeasureScore().getValue());
	}
}
