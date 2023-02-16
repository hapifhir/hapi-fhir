package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.cr.config.CrProperties;
import ca.uhn.fhir.cr.r4.measure.CareGapsService;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class CareGapsServiceR4Test extends BaseCrR4Test {
	private static final String periodStartValid = "2019-01-01";
	private static IPrimitiveType<Date> periodStart = new DateDt("2019-01-01");
	private static final String periodEndValid = "2019-12-31";
	private static IPrimitiveType<Date> periodEnd = new DateDt("2019-12-31");
	private static final String subjectPatientValid = "Patient/numer-EXM125";
	private static final String subjectGroupValid = "Group/gic-gr-1";
	private static final String subjectGroupParallelValid = "Group/gic-gr-parallel";
	private static final String statusValid = "open-gap";
	private List<String> status;

	private List<CanonicalType> measureUrls;
	private static final String statusValidSecond = "closed-gap";

	private List<String> measures;
	private static final String measureIdValid = "BreastCancerScreeningFHIR";
	private static final String measureUrlValid = "http://ecqi.healthit.gov/ecqms/Measure/BreastCancerScreeningFHIR";
	private static final String practitionerValid = "gic-pra-1";
	private static final String organizationValid = "gic-org-1";
	private static final String dateInvalid = "bad-date";
	private static final String subjectInvalid = "bad-subject";
	private static final String statusInvalid = "bad-status";
	private static final String subjectReferenceInvalid = "Measure/gic-sub-1";

	Function<RequestDetails, CareGapsService> theCareGapsService;

	CrProperties crProperties;
	@Autowired
	MeasureService measureService;
	Executor executor;

	@BeforeEach
	public void beforeEach() {
		readResource("Alphora-organization.json");
		readResource("AlphoraAuthor-organization.json");
		readResource("numer-EXM125-patient.json");
		status = new ArrayList<>();
		measures = new ArrayList<>();
		measureUrls = new ArrayList<>();

		crProperties = new CrProperties();
		CrProperties.MeasureProperties measureProperties = new CrProperties.MeasureProperties();
		CrProperties.MeasureProperties.MeasureReportConfiguration measureReportConfiguration = new CrProperties.MeasureProperties.MeasureReportConfiguration();
		measureReportConfiguration.setCareGapsReporter("Organization/alphora");
		measureReportConfiguration.setCareGapsCompositionSectionAuthor("Organization/alphora-author");
		measureProperties.setMeasureReport(measureReportConfiguration);
		crProperties.setMeasure(measureProperties);

		executor = Executors.newSingleThreadExecutor();

		//measureService = new MeasureService();

		theCareGapsService = requestDetails -> {
			CareGapsService careGapsService = new CareGapsService(crProperties, measureService, getDaoRegistry(), executor);
			careGapsService.setTheRequestDetails(requestDetails);
			return careGapsService;
		};

	}

	private void beforeEachMeasure() {
		loadBundle("BreastCancerScreeningFHIR-bundle.json");
	}
	private void beforeEachMultipleMeasures() {
		loadBundle("BreastCancerScreeningFHIR-bundle.json");
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
	}

	@Test
	void testMinimalParametersValid() {
		beforeEachMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		);

		assertNotNull(result);
	}

	@Test
	void testPeriodStartNull() {
		beforeEachMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> theCareGapsService.apply(requestDetails).getCareGapsReport(null, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodStartInvalid() {
		beforeEachMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> theCareGapsService.apply(requestDetails).getCareGapsReport(new DateDt("12-21-2025"), periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodEndNull() {
		beforeEachMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, null
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodEndInvalid() {
		beforeEachMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, new DateDt("12-21-2025")
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		));
	}

	@Test
	void testSubjectGroupValid() {
		status.add(statusValid);
		measures.add(measureIdValid);
		readResource("gic-gr-1.json");
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertDoesNotThrow(() -> {
			 theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
				, null
				, subjectGroupValid
				, null
				, null
				, status
				, measures
				, null
				, null
				, null
			);
		});
	}

	@Test
	void testSubjectInvalid() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectInvalid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectReferenceInvalid() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectReferenceInvalid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectAndPractitioner() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, practitionerValid
			, null
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectAndOrganization() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, organizationValid
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.hasParameter("Unsupported configuration"));
	}

	@Test
	void testOrganizationOnly() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, null
			, null
			, organizationValid
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.hasParameter("Unsupported configuration"));
	}

	@Test
	void testPractitionerAndOrganization() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, null
			, practitionerValid
			, organizationValid
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.hasParameter("Unsupported configuration"));
	}

	@Test
	void testPractitionerOnly() {
		status.add(statusValid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, null
			, practitionerValid
			, null
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.hasParameter("Unsupported configuration"));
	}

	@Test
	void testNoMeasure() {
		status.add(statusValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, null
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testStatusInvalid() {
		status.add(statusInvalid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testStatusNull() {
		status.add(statusInvalid);
		measures.add(measureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, null
			, measures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	public void testMeasures() {
		beforeEachMultipleMeasures();
		status.add(statusValid);
		periodStart = new DateDt("2019-01-01");
		measures.add("ColorectalCancerScreeningsFHIR");
		measureUrls.add(new CanonicalType(measureUrlValid));

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectPatientValid
			, null
			, null
			, status
			, measures
			, null
			, measureUrls
			, null
		);

		assertNotNull(result);
	}

	@Test
	void testParallelMultiSubject() {
		beforeEachParallelMeasure();
		status.add(statusValid);
		measures.add(measureIdValid);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = theCareGapsService.apply(requestDetails).getCareGapsReport(periodStart, periodEnd
			, null
			, subjectGroupParallelValid
			, null
			, null
			, status
			, measures
			, null
			, null
			, null
		);

		assertNotNull(result);
	}

	private void beforeEachParallelMeasure() {
		readResource("gic-gr-parallel.json");
		loadBundle("BreastCancerScreeningFHIR-bundle.json");
	}
}
