package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.PartitionHelper;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
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
public class CqlProviderR4Test extends BaseCrR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);
	private static final IdType MEASURE_ID = new IdType("Measure", "measure-asf");
	private static final String PATIENT_ID = "Patient/Patient-6529";
	private static final String PERIOD_START = "2000-01-01";
	private static final String PERIOD_END = "2019-12-31";
	private static final boolean ourBundlesLoaded = false;
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
		myDaoRegistry.getResourceDao(Library.class).update(library, myRequestDetails);
		myDaoRegistry.getResourceDao(Measure.class).update(measure, myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(
			MEASURE_ID,
			PERIOD_START,
			PERIOD_END,
			"subject",
			PATIENT_ID,
			null,
			null,
			null,
			null,
			null,
			myRequestDetails);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		//assertThat(report.getGroup().get(0).getPopulation(), hasSize(3)); WIP on Practitioner
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}

	@Test
	public void testHedisIGEvaluateMeasureNoTimeframe() throws IOException {
		loadBundle("ca/uhn/fhir/cr/dstu3/hedis-ig/test-patient-6529-data.json");
		var library = loadResource(ourFhirContext, Library.class, "ca/uhn/fhir/cr/r4/hedis-ig/library-asf-logic.json");
		var measure = loadResource(ourFhirContext, Measure.class, "ca/uhn/fhir/cr/r4/hedis-ig/measure-asf.json");
		myDaoRegistry.getResourceDao(Library.class).update(library, myRequestDetails);
		myDaoRegistry.getResourceDao(Measure.class).update(measure, myRequestDetails);

		myPartitionHelper.clear();
		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(
			MEASURE_ID,
			null,
			null,
			"subject",
			PATIENT_ID,
			null,
			null,
			null,
			null,
			null,
			myRequestDetails);

		// Assert it worked
		assertTrue(myPartitionHelper.wasCalled());
		assertThat(report.getGroup(), hasSize(1));
		//assertThat(report.getGroup().get(0).getPopulation(), hasSize(3)); WIP on Practitioner
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}
}
