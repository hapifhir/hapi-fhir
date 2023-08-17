package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.config.CareGapsProperties;
import ca.uhn.fhir.cr.r4.measure.CareGapsService;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static javolution.testing.TestContext.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class CareGapsServiceR4Test extends BaseCrR4TestServer {
	private static final String ourPeriodStartValid = "2019-01-01";
	private static IPrimitiveType<Date> ourPeriodStart = new DateDt("2019-01-01");
	private static final String ourPeriodEndValid = "2019-12-31";
	private static IPrimitiveType<Date> ourPeriodEnd = new DateDt("2019-12-31");
	private static final String ourSubjectPatientValid = "Patient/numer-EXM125";
	private static final String ourSubjectGroupValid = "Group/gic-gr-1";
	private static final String ourSubjectGroupParallelValid = "Group/gic-gr-parallel";
	private static final String ourStatusValid = "open-gap";
	private List<String> myStatuses;

	private List<CanonicalType> myMeasureUrls;
	private static final String ourStatusValidSecond = "closed-gap";

	private List<String> myMeasures;
	private static final String ourMeasureIdValid = "BreastCancerScreeningFHIR";
	private static final String ourMeasureUrlValid = "http://ecqi.healthit.gov/ecqms/Measure/BreastCancerScreeningFHIR";
	private static final String ourPractitionerValid = "gic-pra-1";
	private static final String ourOrganizationValid = "gic-org-1";
	private static final String ourDateInvalid = "bad-date";
	private static final String ourSubjectInvalid = "bad-subject";
	private static final String ourStatusInvalid = "bad-status";
	private static final String ourSubjectReferenceInvalid = "Measure/gic-sub-1";

	@Autowired
	Function<RequestDetails, CareGapsService> myCareGapsServiceFactory;

	@Autowired
	CareGapsProperties myCareGapsProperties;
	@Autowired
	MeasureService myMeasureService;

	@Autowired
	EvaluationSettings myEvaluationSettings;

	@Autowired
	CareGapsService myCareGapsService;

	Executor myExecutor;

	@BeforeEach
	public void beforeEach() {
		loadBundle(Bundle.class, "CaregapsAuthorAndReporter.json");
		readAndLoadResource("numer-EXM125-patient.json");
		myStatuses = new ArrayList<>();
		myMeasures = new ArrayList<>();
		myMeasureUrls = new ArrayList<>();

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
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);

		assertNotNull(result);
	}

	@Test
	void testPeriodStartNull() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(null, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodStartInvalid() {
		beforeEachMeasure();
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(new DateDt("12-21-2025"), ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodEndNull() {
		beforeEachMeasure();
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, null
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testPeriodEndInvalid() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(Exception.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, new DateDt("12-21-2025")
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testSubjectGroupValid() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		readAndLoadResource("gic-gr-1.json");
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertDoesNotThrow(() -> {
			 myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
				, null
				, ourSubjectGroupValid
				, null
				, null
				, myStatuses
				, myMeasures
				, null
				, null
				, null
			);
		});
	}

	@Test
	void testSubjectInvalid() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectInvalid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectReferenceInvalid() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectReferenceInvalid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectAndPractitioner() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, ourPractitionerValid
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testSubjectAndOrganization() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, ourOrganizationValid
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testOrganizationOnly() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(NotImplementedOperationException.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, null
			, null
			, ourOrganizationValid
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testPractitionerAndOrganization() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(NotImplementedOperationException.class, () -> myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, null
			, ourPractitionerValid
			, ourOrganizationValid
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testPractitionerOnly() {
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		assertThrows(NotImplementedOperationException.class, () ->  myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, null
			, ourPractitionerValid
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		));
	}

	@Test
	void testNoMeasure() {
		myStatuses.add(ourStatusValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		var result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, null
			, null
			, null
			, null
		);

		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testStatusInvalid() {
		myStatuses.add(ourStatusInvalid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);
		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	void testStatusNull() {
		myStatuses.add(ourStatusInvalid);
		myMeasures.add(ourMeasureIdValid);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		var result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, null
			, myMeasures
			, null
			, null
			, null
		);

		assertTrue(result.getParameter().isEmpty());
	}

	@Test
	public void testMeasures() {
		beforeEachMultipleMeasures();
		myStatuses.add(ourStatusValid);
		ourPeriodStart = new DateDt("2019-01-01");
		myMeasures.add("ColorectalCancerScreeningsFHIR");
		myMeasureUrls.add(new CanonicalType(ourMeasureUrlValid));

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectPatientValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, myMeasureUrls
			, null
		);

		assertNotNull(result);

		//Test to search for how many search parameters are created.
		//only 1 should be created.
		var searchParams = this.myDaoRegistry.getResourceDao("SearchParameter")
			.search(new SearchParameterMap(), requestDetails);

		assertNotNull(searchParams);

		assertEquals(searchParams.getAllResources().size(), 1);
	}

	@Test
	void testParallelMultiSubject() {
		beforeEachParallelMeasure();
		myStatuses.add(ourStatusValid);
		myMeasures.add(ourMeasureIdValid);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		requestDetails.setFhirServerBase("test.com");
		Parameters result = myCareGapsServiceFactory.apply(requestDetails).getCareGapsReport(ourPeriodStart, ourPeriodEnd
			, null
			, ourSubjectGroupParallelValid
			, null
			, null
			, myStatuses
			, myMeasures
			, null
			, null
			, null
		);

		assertNotNull(result);
	}

	private void beforeEachParallelMeasure() {
		readAndLoadResource("gic-gr-parallel.json");
		loadBundle("BreastCancerScreeningFHIR-bundle.json");
	}
}
