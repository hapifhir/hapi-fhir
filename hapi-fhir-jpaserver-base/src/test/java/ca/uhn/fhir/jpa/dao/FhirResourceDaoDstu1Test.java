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
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
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

@SuppressWarnings("unused")
public class FhirResourceDaoDstu1Test  extends BaseJpaTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Encounter> ourEncounterDao;
	private static FhirContext ourFhirCtx;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu1Test.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;

	@Test
	public void testCreateDuplicateIdFails() {
		String methodName = "testCreateDuplocateIdFailsText";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		try {
			ourPatientDao.create(p);
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
			ourPatientDao.create(p);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which begin with a non-numeric"));
		}
	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new ResourceReferenceDt("Patient/99999999"));
		try {
			ourPatientDao.create(patient);
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
		IIdType p1id = ourPatientDao.create(p1).getId();
		assertEquals("ABABA", p1id.getIdPart());

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsIdWhichPointsToForcedId02");
		p2.addName().addFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId02");
		IIdType p2id = ourPatientDao.create(p2).getId();
		long p1longId = p2id.getIdPartAsLong() - 1;

		try {
			ourPatientDao.read(new IdDt("Patient/" + p1longId));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		try {
			p1.setId(new IdDt("Patient/" + p1longId));
			ourPatientDao.update(p1);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which begin with a non-numeric"));
		}

	}

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourCtx = new ClassPathXmlApplicationContext("hapi-fhir-server-resourceproviders-dstu1.xml", "fhir-jpabase-spring-test-config.xml");
		ourPatientDao = ourCtx.getBean("myPatientDaoDstu1", IFhirResourceDao.class);
		ourObservationDao = ourCtx.getBean("myObservationDaoDstu1", IFhirResourceDao.class);
		ourDiagnosticReportDao = ourCtx.getBean("myDiagnosticReportDaoDstu1", IFhirResourceDao.class);
		ourDeviceDao = ourCtx.getBean("myDeviceDaoDstu1", IFhirResourceDao.class);
		ourOrganizationDao = ourCtx.getBean("myOrganizationDaoDstu1", IFhirResourceDao.class);
		ourLocationDao = ourCtx.getBean("myLocationDaoDstu1", IFhirResourceDao.class);
		ourEncounterDao = ourCtx.getBean("myEncounterDaoDstu1", IFhirResourceDao.class);
		ourFhirCtx = ourCtx.getBean(FhirContext.class);
	}

}
