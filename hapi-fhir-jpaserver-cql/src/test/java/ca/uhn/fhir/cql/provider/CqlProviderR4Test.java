package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.rp.r4.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ValueSetResourceProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.r4.providers.MeasureOperationsProvider;
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

@Disabled
public class CqlProviderR4Test extends BaseCqlR4Test implements CqlProviderTestBase {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	CqlProviderFactory myCqlProviderFactory;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IFhirSystemDao mySystemDao;
	@Autowired
	private LibraryResourceProvider myLibraryResourceProvider;
	@Autowired
	private MeasureResourceProvider myMeasureResourceProvider;
	@Autowired
	private ValueSetResourceProvider myValueSetResourceProvider;

	MeasureOperationsProvider myProvider;

	@BeforeEach
	public void before() throws IOException {
		// FIXME KBD Can we find a way to remove these?
		myMeasureResourceProvider.setDao(myDaoRegistry.getResourceDao("Measure"));
		myLibraryResourceProvider.setDao(myDaoRegistry.getResourceDao("Library"));
		myValueSetResourceProvider.setDao(myDaoRegistry.getResourceDao("ValueSet"));

		myProvider = (MeasureOperationsProvider) myCqlProviderFactory.getMeasureOperationsProvider();

		// Load terminology for measure tests (HEDIS measures)
		loadBundle("dstu3/hedis-valuesets-bundle.json");

		// Load libraries
		loadResource("dstu3/library/library-fhir-model-definition.json", myFhirContext, myDaoRegistry);
		loadResource("dstu3/library/library-fhir-helpers.json", myFhirContext, myDaoRegistry);


		// load test data and conversion library for $apply operation tests
		loadResource("dstu3/general-practitioner.json", myFhirContext, myDaoRegistry);
		loadResource("dstu3/general-patient.json", myFhirContext, myDaoRegistry);
	}

	// FIXME KBD
	//@Disabled
	//@Test
	public void evaluateMeasureEXM130() throws IOException {
		// Colorectal Cancer Screening - http://hl7.org/fhir/us/davinci-deqm/2020Sep/Measure-measure-exm130-example.html
		loadResource("r4/EXM130/library-fhir-model-definition.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-fhir-helpers.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-FHIRHelpers-4.0.0.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-EXM130_FHIR4-7.2.000.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-deps-EXM130_FHIR4-7.2.000-bundle.json", myFhirContext, myDaoRegistry);
		//loadResource("r4/EXM130/valuesets-EXM130_FHIR4-7.2.000-bundle.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-matglobalcommonfunctions-fhir.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-MATGlobalCommonFunctions-FHIR4-4.0.000.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-hospice-fhir.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-AdultOutpatientEncounters-FHIR4-1.1.000.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/library-SupplementalDataElements-FHIR4-1.0.0.json", myFhirContext, myDaoRegistry);
		loadResource("r4/EXM130/measure-EXM130_FHIR4-7.2.000.json", myFhirContext, myDaoRegistry);

		loadBundle("dstu3/test-patient-6529-data.json");

		IdType measureId = new IdType("Measure", "measure-EXM130-FHIR4-7.2.000");
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";
		String patient = "Patient/Patient-6529";
		MeasureReport measureReport = myProvider.evaluateMeasure(measureId, null,
			null, null, "patient", patient, null, null,
			null, null, null, null);
		assertThat(measureReport.getGroup(), hasSize(1));
		assertThat(measureReport.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : measureReport.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(measureReport));
	}

	//@Test
	// Fails with: ca.uhn.fhir.parser.DataFormatException: Invalid JSON content detected, missing required element: 'resourceType'
	public void evaluateMeasureEXM130R4() throws IOException {
		// https://github.com/projecttacoma/synthea/blob/abacus/src/main/resources/modules/EXM130-8.0.000-r4.json
		loadResource("r4/EXM130/EXM130-8.0.000-r4.json", myFhirContext, myDaoRegistry);

		loadBundle("dstu3/test-patient-6529-data.json");

		IdType measureId = new IdType("Measure", "measure-EXM130-FHIR4-7.2.000");
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";
		String patient = "Patient/Patient-6529";
		MeasureReport measureReport = myProvider.evaluateMeasure(measureId, null,
			null, null, "patient", patient, null, null,
			null, null, null, null);
		assertThat(measureReport.getGroup(), hasSize(1));
		assertThat(measureReport.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : measureReport.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(measureReport));
	}

	//@Test
	// Fails with: ca.uhn.fhir.rest.server.exceptions.InvalidRequestException: Unable to process request, this server does not know how to handle resources of type null - Can handle: [Account, etc. etc...
	public void testSubmitDataCOL() throws IOException {
		// http://hl7.org/fhir/us/davinci-deqm/Parameters-col-submit-collect-obs.json.html
		loadResource("r4/COL/col-submit-collect-obs.json", myFhirContext, myDaoRegistry);
		ourLog.info("Data imported successfully!");
	}

	//@Test
	// Fails with: ca.uhn.fhir.rest.server.exceptions.InvalidRequestException: Unable to process request, this server does not know how to handle resources of type null - Can handle: [Account, etc. etc...
	public void testDaVinciExample_BundleSingleIndvColObsReport() throws IOException {
		// http://hl7.org/fhir/us/davinci-deqm/downloads.html#examples
		loadResource("r4/DaVinciExamples/Bundle-single-indv-col-obs-report.json", myFhirContext, myDaoRegistry);
		ourLog.info("Data imported successfully!");
	}

	@Test
	public void testConnectathonExample_EXM104() throws IOException {
		// git clone git@github.com:DBCG/connectathon.git
		Bundle bundle = loadBundle("r4/Connectathon/EXM104-8.2.000/EXM104-8.2.000-bundle.json");
		ourLog.info("Data imported successfully!");
		assertNotNull(bundle);
		List<Bundle.BundleEntryComponent> entries = bundle.getEntry();
		assertThat(entries, hasSize(40));
		assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		assertEquals(entries.get(39).getResponse().getStatus(), "201 Created");
	}

	private Bundle loadBundle(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		Bundle result = (Bundle) mySystemDao.transaction(null, bundle);
		return result;
	}
}
