package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestDstu1Config;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;

@SuppressWarnings("unused")
public class FhirResourceDaoDstu1Test extends BaseJpaTest {
	private static AnnotationConfigApplicationContext ourAppCtx;
	private static FhirContext ourCtx;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Encounter> ourEncounterDao;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu1Test.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}

	@Test
	public void testCreateDuplicateIdFails() {
		String methodName = "testCreateDuplocateIdFailsText";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = ourPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		try {
			ourPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not create entity with ID[" + methodName + "], a resource with this ID already exists"));
		}
	}

	@Test
	public void testCreateNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123");
		try {
			ourPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which contain at least one non-numeric"));
		}
	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new ResourceReferenceDt("Organization/99999999"));
		try {
			ourPatientDao.create(patient, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("99999 not found"));
		}

	}

	@Test
	public void testUpdateRejectsIdWhichPointsToForcedId() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsIdWhichPointsToForcedId01");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId01");
		p1.setId("ABABA");
		IIdType p1id = ourPatientDao.create(p1, mySrd).getId();
		assertEquals("ABABA", p1id.getIdPart());

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsIdWhichPointsToForcedId02");
		p2.addName().addFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId02");
		IIdType p2id = ourPatientDao.create(p2, mySrd).getId();
		long p1longId = p2id.getIdPartAsLong() - 1;

		try {
			ourPatientDao.read(new IdDt("Patient/" + p1longId), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		try {
			p1.setId(new IdDt("Patient/" + p1longId));
			ourPatientDao.update(p1, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which contain at least one non-numeric"));
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		ourAppCtx.close();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourAppCtx = new AnnotationConfigApplicationContext(TestDstu1Config.class);
		ourPatientDao = ourAppCtx.getBean("myPatientDaoDstu1", IFhirResourceDao.class);
		ourObservationDao = ourAppCtx.getBean("myObservationDaoDstu1", IFhirResourceDao.class);
		ourDiagnosticReportDao = ourAppCtx.getBean("myDiagnosticReportDaoDstu1", IFhirResourceDao.class);
		ourDeviceDao = ourAppCtx.getBean("myDeviceDaoDstu1", IFhirResourceDao.class);
		ourOrganizationDao = ourAppCtx.getBean("myOrganizationDaoDstu1", IFhirResourceDao.class);
		ourLocationDao = ourAppCtx.getBean("myLocationDaoDstu1", IFhirResourceDao.class);
		ourEncounterDao = ourAppCtx.getBean("myEncounterDaoDstu1", IFhirResourceDao.class);
		ourCtx = ourAppCtx.getBean(FhirContext.class);
	}

}
