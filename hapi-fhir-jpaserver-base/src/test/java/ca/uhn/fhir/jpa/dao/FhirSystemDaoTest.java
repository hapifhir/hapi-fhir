package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class FhirSystemDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoTest.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static Date ourTestStarted;
	private static IFhirSystemDao ourSystemDao;

	@Test
	public void testPersistWithSimpleLink() {
		Patient patient = new Patient();
		patient.setId(new IdDt("Patient/testPersistWithSimpleLinkP01"));
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP01");
		patient.addName().addFamily("Tester").addGiven("Joe");

		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/testPersistWithSimpleLinkP01"));

		ourSystemDao.transaction(Arrays.asList((IResource) patient, obs));

		long patientId = Long.parseLong(patient.getId().getUnqualifiedId());
		long patientVersion = Long.parseLong(patient.getId().getUnqualifiedVersionId());
		long obsId = Long.parseLong(obs.getId().getUnqualifiedId());
		long obsVersion = Long.parseLong(obs.getId().getUnqualifiedVersionId());

		assertThat(patientId, greaterThan(0L));
		assertEquals(patientVersion, 1L);
		assertThat(obsId, greaterThan(patientId));
		assertEquals(obsVersion, 1L);

		// Try to search
		
		List<Observation> obsResults = ourObservationDao.search(Observation.SP_NAME, new IdentifierDt("urn:system", "testPersistWithSimpleLinkO01"));
		assertEquals(1, obsResults.size());

		List<Patient> patResults = ourPatientDao.search(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "testPersistWithSimpleLinkP01"));
		assertEquals(1, obsResults.size());

		IdDt foundPatientId = patResults.get(0).getId();
		ResourceReferenceDt subject = obs.getSubject();
		assertEquals(foundPatientId.getUnqualifiedId(), subject.getResourceId().getUnqualifiedId());

		// Update
		
		patient = patResults.get(0);
		obs = obsResults.get(0);
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP02");
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO02");

		ourSystemDao.transaction(Arrays.asList((IResource) patient, obs));

		long patientId2 = Long.parseLong(patient.getId().getUnqualifiedId());
		long patientVersion2 = Long.parseLong(patient.getId().getUnqualifiedVersionId());
		long obsId2 = Long.parseLong(obs.getId().getUnqualifiedId());
		long obsVersion2 = Long.parseLong(obs.getId().getUnqualifiedVersionId());

		assertEquals(patientId, patientId2);
		assertEquals(patientVersion2, 2L);
		assertEquals(obsId, obsId2);
		assertEquals(obsVersion2, 2L);

	}

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourTestStarted = new Date();
		ourCtx = new ClassPathXmlApplicationContext("fhir-jpabase-spring-test-config.xml");
		ourPatientDao = ourCtx.getBean("myPatientDao", IFhirResourceDao.class);
		ourObservationDao = ourCtx.getBean("myObservationDao", IFhirResourceDao.class);
		ourDiagnosticReportDao = ourCtx.getBean("myDiagnosticReportDao", IFhirResourceDao.class);
		ourDeviceDao = ourCtx.getBean("myDeviceDao", IFhirResourceDao.class);
		ourOrganizationDao = ourCtx.getBean("myOrganizationDao", IFhirResourceDao.class);
		ourLocationDao = ourCtx.getBean("myLocationDao", IFhirResourceDao.class);
		ourSystemDao = ourCtx.getBean("mySystemDao", IFhirSystemDao.class);
	}

}
