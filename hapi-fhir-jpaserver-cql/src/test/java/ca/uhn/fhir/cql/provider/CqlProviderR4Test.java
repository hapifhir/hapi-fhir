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
import org.junit.jupiter.api.Test;
import org.opencds.cqf.r4.providers.MeasureOperationsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
	@Disabled
	//@Test
	public void evaluateColMeasure() throws IOException {
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

	private Bundle loadBundle(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		Bundle result = (Bundle) mySystemDao.transaction(null, bundle);
		return result;
	}
}
