package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoR4CreateTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CreateTest.class);

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
	}

	@Test
	public void testCreateResourceWithKoreanText() throws IOException {
		String input = loadClasspath("/r4/bug832-korean-text.xml");
		Patient p = myFhirCtx.newXmlParser().parseResource(Patient.class, input);
		String id = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_FAMILY, new StringParam("김"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(id));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("준"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(id));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("준수"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(id));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("수")); // rightmost character only
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());

	}

	@Test
	public void testCreateWithUuidServerResourceStrategy() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		assertThat(id.getIdPart(), matchesPattern("[a-z0-9]{8}-.*"));

		p = myPatientDao.read(id);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

	}

	@Test
	public void testCreateWithUuidServerResourceStrategy_ClientIdNotAllowed() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		assertThat(id.getIdPart(), matchesPattern("[a-z0-9]{8}-.*"));

		p = myPatientDao.read(id);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

	}

	/**
	 * See #1352
	 */
	@Test
	public void testCreateWithSampledDataInObservation() {
		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		SampledData sampledData = new SampledData();
		sampledData.setData("2 3 4 5 6");
		o.setValue(sampledData);
		assertTrue(myObservationDao.create(o).getCreated());
	}

	@Test
	public void testCreateWithClientAssignedIdDisallowed() {
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.setId("AAA");
		p.addName().setFamily("FAM");
		try {
			myPatientDao.update(p);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("No resource exists on this server resource with ID[AAA], and client-assigned IDs are not enabled.", e.getMessage());
		}
	}

	@Test
	public void testCreateWithClientAssignedIdPureNumeric() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.SEQUENTIAL_NUMERIC);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);

		// Create a server assigned ID
		Patient p = new Patient();
		p.setActive(true);
		IIdType id0 = myPatientDao.create(p).getId();
		long firstClientAssignedId = id0.getIdPartAsLong();
		long newId = firstClientAssignedId + 2L;

		// Read it back
		p = myPatientDao.read(new IdType("Patient/" + firstClientAssignedId));
		assertEquals(true, p.getActive());

		// Not create a client assigned numeric ID
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("1", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

		// Update it
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM2");
		id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("2", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM2", p.getNameFirstRep().getFamily());

		// Try to create another server-assigned. This should fail since we have a
		// a conflict.
		p = new Patient();
		p.setActive(false);
		try {
			myPatientDao.create(p).getId();
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		ourLog.info("ID0: {}", id0);
		ourLog.info("ID1: {}", id1);
	}

	@Test
	public void testCreateWithClientAssignedIdPureNumericServerIdUuid() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);

		// Create a server assigned ID
		Patient p = new Patient();
		p.setActive(true);
		IIdType id0 = myPatientDao.create(p).getId();

		// Read it back
		p = myPatientDao.read(id0.toUnqualifiedVersionless());
		assertEquals(true, p.getActive());

		// Pick an ID that was already used as an internal PID
		Long newId = runInTransaction(() -> myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromNewest(
			PageRequest.of(0, 1),
			DateUtils.addDays(new Date(), -1),
			DateUtils.addDays(new Date(), 1)
		).getContent().get(0));

		// Not create a client assigned numeric ID
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("1", id1.getVersionIdPart());

		// Read it back
		p = myPatientDao.read(id1);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

		// Update it
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM2");
		id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("2", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM2", p.getNameFirstRep().getFamily());

		// Try to create another server-assigned. This should fail since we have a
		// a conflict.
		p = new Patient();
		p.setActive(false);
		IIdType id2 = myPatientDao.create(p).getId();

		ourLog.info("ID0: {}", id0);
		ourLog.info("ID1: {}", id1);
		ourLog.info("ID2: {}", id2);
	}


	@Test
	public void testTransactionCreateWithUuidResourceStrategy() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);

		Organization org = new Organization();
		org.setId(IdType.newRandomUuid());
		org.setName("ORG");

		Patient p = new Patient();
		p.setId(IdType.newRandomUuid());
		p.addName().setFamily("FAM");
		p.setActive(true);
		p.setBirthDateElement(new DateType("2011-01-01"));
		p.getManagingOrganization().setReference(org.getId());

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(org)
			.setFullUrl(org.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);
		input.addEntry()
			.setResource(p)
			.setFullUrl(p.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		Bundle output = mySystemDao.transaction(mySrd, input);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry().get(0).getResponse().getLocation(), matchesPattern("Organization/[a-z0-9]{8}-.*"));
		assertThat(output.getEntry().get(1).getResponse().getLocation(), matchesPattern("Patient/[a-z0-9]{8}-.*"));


	}

	@Test
	public void testTagsInContainedResourcesPreserved() {
		Patient p = new Patient();
		p.setActive(true);

		Organization o = new Organization();
		o.getMeta().addTag("http://foo", "bar", "FOOBAR");
		p.getManagingOrganization().setResource(o);

		ourLog.info("Input: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(id);

		ourLog.info("Output: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		Organization org = (Organization) p.getManagingOrganization().getResource();
		assertEquals("#1", org.getId());
		assertEquals(1, org.getMeta().getTag().size());

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
