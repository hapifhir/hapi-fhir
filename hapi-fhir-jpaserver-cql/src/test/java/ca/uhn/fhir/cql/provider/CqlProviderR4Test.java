package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.rp.r4.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ValueSetResourceProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.opencds.cqf.r4.providers.MeasureOperationsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class CqlProviderR4Test extends BaseCqlR4Test implements CqlProviderTestBase {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IFhirResourceDao<Patient> myPatientDao;
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

		myProvider = myCqlProviderLoader.buildR4Provider();

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
	//@Test
	@Disabled
	public void evaluateMeasure() throws IOException {
		loadResource("dstu3/library/library-asf-logic.json", myFhirContext, myDaoRegistry);
		// Load the measure for ASF: Unhealthy Alcohol Use Screening and Follow-up (ASF)
		loadResource("dstu3/measure-asf.json", myFhirContext, myDaoRegistry);
		Bundle result = loadBundle("dstu3/test-patient-6529-data.json");
		assertNotNull(result);
		List<Bundle.BundleEntryComponent> entries = result.getEntry();
		assertThat(entries, hasSize(22));
		assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		assertEquals(entries.get(21).getResponse().getStatus(), "201 Created");

		IdType measureId = new IdType("Measure", "measure-asf");
		String patientIdentifier = "Patient/Patient-6529";
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		// First run to absorb startup costs
		MeasureReport report = myProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
			patientIdentifier, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}

	private Bundle loadBundle(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		Bundle result = (Bundle) mySystemDao.transaction(null, bundle);
		return result;
	}
}
