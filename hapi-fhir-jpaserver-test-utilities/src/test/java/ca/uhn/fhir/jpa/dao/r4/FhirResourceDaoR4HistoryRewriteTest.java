package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4HistoryRewriteTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4Test.class);

	@Test
	public void testHistoryRewriteNonCurrentVersion() {
		myDaoConfig.setUpdateWithHistoryRewriteEnabled(true);
		String methodName = "testHistoryRewrite";
		String methodNameModified = "testHistoryRewriteDiff";
		String testFamilyName = "Johnson";
		String testGivenName = "Dan";
		String testFamilyNameModified = "Jackson";

		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyName);
		p.setId("Patient/" + id.getIdPart());

		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyName).setGiven(Arrays.asList(new StringType(testGivenName)));
		p.setId("Patient/" + id.getIdPart());

		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertEquals(testFamilyName, p.getName().get(0).getFamily());
		assertThat(p.getIdElement().toString(), endsWith("/_history/3"));

		// execute updates
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/2");

		Patient history2 = myPatientDao.read(id.withVersion("2"));
		ourLog.debug("Patient history 2: {}", history2);
		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodNameModified);
		p.setId("Patient/" + id.getIdPart() + "/_history/1");

		Patient history1 = myPatientDao.read(id.withVersion("1"));
		ourLog.debug("Patient history 1: {}", history1);
		myPatientDao.update(p, mySrd);

		Patient h2 = myPatientDao.read(id.withVersion("2"), mySrd);
		assertEquals(testFamilyNameModified, h2.getName().get(0).getFamily());
		assertThat(h2.getIdElement().toString(), endsWith("/_history/2"));
		assertTrue(Math.abs(h2.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);

		Patient h1 = myPatientDao.read(id.withVersion("1"), mySrd);
		assertEquals(methodNameModified, h1.getIdentifier().get(0).getValue());
		assertThat(h1.getIdElement().toString(), endsWith("/_history/1"));
		assertTrue(Math.abs(h1.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}

	@Test
	public void testHistoryRewriteCurrentVersion() {
		myDaoConfig.setUpdateWithHistoryRewriteEnabled(true);
		String methodName = "testHistoryRewrite";
		String testFamilyName = "Johnson";
		String testGivenName = "Dan";
		String testFamilyNameModified = "Jackson";
		String testGivenNameModified = "Randy";

		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyName);
		p.setId("Patient/" + id.getIdPart());

		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyName).setGiven(Arrays.asList(new StringType(testGivenName)));
		p.setId("Patient/" + id.getIdPart());

		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertEquals(p.getName().get(0).getFamily(), testFamilyName);
		assertThat(p.getIdElement().toString(), endsWith("/_history/3"));

		// execute update
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily(testFamilyNameModified).setGiven(Arrays.asList(new StringType(testGivenNameModified)));
		p.setId("Patient/" + id.getIdPart() + "/_history/3");

		myPatientDao.update(p, mySrd);

		Patient lPatient = myPatientDao.read(id.toVersionless(), mySrd);
		assertEquals(testFamilyNameModified, lPatient.getName().get(0).getFamily());
		assertEquals(testGivenNameModified, lPatient.getName().get(0).getGiven().get(0).getValue());
		assertThat(lPatient.getIdElement().toString(), endsWith("/_history/3"));
		assertTrue(Math.abs(lPatient.getMeta().getLastUpdated().getTime() - new Date().getTime()) < 1000L);
	}
}
