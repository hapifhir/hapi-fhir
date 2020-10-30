package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderDstu3Test extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderDstu3Test.class);

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

	private EvaluationProviderFactory evaluationProviderFactory;
	MeasureOperationsProvider myProvider;

	@BeforeEach
	public void before() throws IOException {
		myProvider = myCqlProviderLoader.buildDstu3Provider();

		// Load terminology for measure tests (HEDIS measures)

		// FIXME KBD why won't this load?
		loadBundle("measure-terminology-bundle.json");

		// load test data and conversion library for $apply operation tests
		loadResource("general-practitioner.json");
		loadResource("general-patient.json");
		loadResource("general-fhirhelpers-3.json");
	}

	@Test
	public void evaluateMeasure() {
		Patient patient = new Patient();
		// FIXME KBD add something to patient we want to measure
		IIdType patientId = myPatientDao.create(patient).getId().toVersionless();

		Patient patientInstance = myDaoRegistry.getResourceDao(Patient.class).read(patientId);

		// FIXME KBD
		String periodStart = "0";
		String periodEnd = StringUtils.defaultToString(System.currentTimeMillis());
		String subject = "Patient";
		MeasureReport measureReport = myProvider.evaluateMeasure((IdType) patientId.toVersionless(), periodStart,
			periodEnd, null, "patient", subject, null, null,
			null, null, null, null);
		assertNotNull(measureReport);
		// FIXME KBD assert on stuff in measureReport
	}

	@Test
	public void evaluatePatientBundleMeasure() throws IOException {
		Bundle result = loadBundle("patient-measure-test-bundle.json");
		assertNotNull(result);
		List<Bundle.BundleEntryComponent> entries = result.getEntry();
		assertThat(entries, hasSize(24));
		assertEquals(entries.get(0).getResponse().getStatus(), "201 Created");
		assertEquals(entries.get(23).getResponse().getStatus(), "201 Created");

		IdType measureId = new IdType("Measure", "measure-asf");
		String patient = "Patient/Patient-6529";
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		MeasureReport report =
			runInTransaction(() ->
				myProvider.evaluateMeasure(measureId, periodStart, periodEnd, null, null,
					patient, null, null, null, null, null, null));

		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
	}

	private Bundle loadBundle(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		Bundle result = (Bundle) mySystemDao.transaction(null, bundle);
		return result;
	}

	private String stringFromResource(String theLocation) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theLocation);
		return IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
	}

	private IBaseResource loadResource(String theLocation) throws IOException {
		String json = stringFromResource(theLocation);
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(json);
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resource.getIdElement().getResourceType());
		dao.update(resource);
		return resource;
	}

}
