package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.CrR4Test;
import ca.uhn.fhir.cr.common.helper.PartitionHelper;
import ca.uhn.fhir.cr.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.test.utilities.RequestDetailsHelper;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class CqlProviderR4Test extends CrR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);
	private static final IdType measureId = new IdType("Measure", "measure-asf");
	private static final String measure = "Measure/measure-asf";
	private static final String patient = "Patient/Patient-6529";
	private static final String periodStart = "2000-01-01";
	private static final String periodEnd = "2019-12-31";
	private static boolean bundlesLoaded = false;
	protected final RequestDetails myRequestDetails = RequestDetailsHelper.newServletRequestDetails();

	@Autowired
	@RegisterExtension
	protected PartitionHelper myPartitionHelper;

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	public void testHedisIGEvaluateMeasureWithTimeframe() throws IOException {
		loadBundle("ca/uhn/fhir/cr/dstu3/hedis-ig/test-patient-6529-data.json");
		var library = loadResource(ourFhirContext, Library.class, "ca/uhn/fhir/cr/r4/hedis-ig/library-asf-logic.json");
		var measure = loadResource(ourFhirContext, Measure.class, "ca/uhn/fhir/cr/r4/hedis-ig/measure-asf.json");
		daoRegistry.getResourceDao(Library.class).update(library, myRequestDetails);
		daoRegistry.getResourceDao(Measure.class).update(measure, myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(
			myRequestDetails,
			measureId,
			periodStart,
			periodEnd,
			"subject",
			patient,
			null,
			null,
			null,
			null,
			null);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}

	@Test
	public void testHedisIGEvaluateMeasureNoTimeframe() throws IOException {
		loadBundle("ca/uhn/fhir/cr/dstu3/hedis-ig/test-patient-6529-data.json");
		var library = loadResource(ourFhirContext, Library.class, "ca/uhn/fhir/cr/r4/hedis-ig/library-asf-logic.json");
		var measure = loadResource(ourFhirContext, Measure.class, "ca/uhn/fhir/cr/r4/hedis-ig/measure-asf.json");
		daoRegistry.getResourceDao(Library.class).update(library, myRequestDetails);
		daoRegistry.getResourceDao(Measure.class).update(measure, myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(
			myRequestDetails,
			measureId,
			null,
			null,
			"subject",
			patient,
			null,
			null,
			null,
			null,
			null);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}
}
