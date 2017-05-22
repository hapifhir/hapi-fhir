package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionFlaggedResourceDataDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDstu3SubscriptionTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SubscriptionTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Autowired
	private ISubscriptionFlaggedResourceDataDao mySubscriptionFlaggedResourceDataDao;

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	@Before
	public void beforeEnableSubscription() {
		myDaoConfig.setSubscriptionEnabled(true);
		myDaoConfig.setSubscriptionPurgeInactiveAfterSeconds(60);
	}

	@Test
	public void testSubscriptionGetsPurgedIfItIsNeverActive() throws Exception {
		myDaoConfig.setSubscriptionPurgeInactiveAfterSeconds(1);

		Subscription subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setStatus(SubscriptionStatus.REQUESTED);

		IIdType id = mySubscriptionDao.create(subs, mySrd).getId().toUnqualifiedVersionless();
		mySubscriptionDao.purgeInactiveSubscriptions();
		mySubscriptionDao.read(id, mySrd);

		Thread.sleep(1500);

		myDaoConfig.setSchedulingDisabled(false);
		mySubscriptionDao.purgeInactiveSubscriptions();
		try {
			mySubscriptionDao.read(id, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}
	
	@Before
	public void beforeDisableScheduling() {
		myDaoConfig.setSchedulingDisabled(true);
	}
	

	@Test
	public void testSubscriptionGetsPurgedIfItIsInactive() throws Exception {
		myDaoConfig.setSubscriptionPurgeInactiveAfterSeconds(1);

		Subscription subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setStatus(SubscriptionStatus.REQUESTED);

		IIdType id = mySubscriptionDao.create(subs, mySrd).getId().toUnqualifiedVersionless();
		mySubscriptionDao.purgeInactiveSubscriptions();
		mySubscriptionDao.read(id, mySrd);

		mySubscriptionDao.getUndeliveredResourcesAndPurge(mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(id));
		
		Thread.sleep(1500);

		myDaoConfig.setSchedulingDisabled(false);
		mySubscriptionDao.purgeInactiveSubscriptions();
		try {
			mySubscriptionDao.read(id, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testCreateSubscription() {
		Subscription subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setStatus(SubscriptionStatus.REQUESTED);

		IIdType id = mySubscriptionDao.create(subs, mySrd).getId().toUnqualifiedVersionless();

		TypedQuery<SubscriptionTable> q = myEntityManager.createQuery("SELECT t from SubscriptionTable t WHERE t.mySubscriptionResource.myId = :id", SubscriptionTable.class);
		q.setParameter("id", id.getIdPartAsLong());
		final SubscriptionTable table = q.getSingleResult();

		assertNotNull(table);
		assertNotNull(table.getNextCheck());
		assertEquals(table.getNextCheck(), table.getSubscriptionResource().getPublished().getValue());
		assertEquals(SubscriptionStatus.REQUESTED.toCode(), myEntityManager.find(SubscriptionTable.class, table.getId()).getStatus());
		assertEquals(SubscriptionStatus.REQUESTED, mySubscriptionDao.read(id, mySrd).getStatusElement().getValue());

		subs.setStatus(SubscriptionStatus.ACTIVE);
		mySubscriptionDao.update(subs, mySrd);

		assertEquals(SubscriptionStatus.ACTIVE.toCode(), myEntityManager.find(SubscriptionTable.class, table.getId()).getStatus());
		assertEquals(SubscriptionStatus.ACTIVE, mySubscriptionDao.read(id, mySrd).getStatusElement().getValue());

		mySubscriptionDao.delete(id, mySrd);

		assertNull(myEntityManager.find(SubscriptionTable.class, table.getId()));

		/*
		 * Re-create again
		 */

		subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setId(id);
		subs.setStatus(SubscriptionStatus.REQUESTED);
		mySubscriptionDao.update(subs, mySrd);

		assertEquals(SubscriptionStatus.REQUESTED.toCode(), myEntityManager.createQuery("SELECT t FROM SubscriptionTable t WHERE t.myResId = " + id.getIdPart(), SubscriptionTable.class).getSingleResult().getStatus());
		assertEquals(SubscriptionStatus.REQUESTED, mySubscriptionDao.read(id, mySrd).getStatusElement().getValue());
	}

	@Test
	public void testCreateSubscriptionInvalidCriteria() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("http://foo.com/Observation?AAA=BBB");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("ObservationZZZZ?a=b");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria contains invalid/unsupported resource type: ObservationZZZZ"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.channel.type must be populated"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelType.RESTHOOK);
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.channel.payload must be populated for rest-hook subscriptions"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelType.RESTHOOK);
		subs.getChannel().setPayload("text/html");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Invalid value for Subscription.channel.payload: text/html"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelType.RESTHOOK);
		subs.getChannel().setPayload("application/fhir+xml");
		try {
			mySubscriptionDao.create(subs, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Rest-hook subscriptions must have Subscription.channel.endpoint defined"));
		}
		
		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		assertTrue(mySubscriptionDao.create(subs, mySrd).getId().hasIdPart());

		subs = new Subscription();
		subs.setStatus(SubscriptionStatus.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelType.RESTHOOK);
		subs.getChannel().setPayload("application/fhir+json");
		subs.getChannel().setEndpoint("http://localhost:8080");
		assertTrue(mySubscriptionDao.create(subs, mySrd).getId().hasIdPart());

	}

	@Test
	public void testDeleteSubscriptionWithFlaggedResources() throws Exception {
		myDaoConfig.setSubscriptionPollDelay(0);

		String methodName = "testDeleteSubscriptionWithFlaggedResources";
		Patient p = new Patient();
		p.addName().setFamily(methodName);
		IIdType pId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Subscription subs;

		/*
		 * Create 2 identical subscriptions
		 */

		subs = new Subscription();
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?subject=Patient/" + pId.getIdPart());
		subs.setStatus(SubscriptionStatus.ACTIVE);
		IIdType subsId = mySubscriptionDao.create(subs, mySrd).getId().toUnqualifiedVersionless();
		Long subsPid = mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(subsId);

		assertNull(mySubscriptionTableDao.findOne(subsPid).getLastClientPoll());

		Thread.sleep(100);
		ourLog.info("Before: {}", System.currentTimeMillis());
		assertThat(mySubscriptionFlaggedResourceDataDao.count(), not(greaterThan(0L)));
		assertThat(mySubscriptionTableDao.count(), equalTo(1L));

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(100);

		ourLog.info("After: {}", System.currentTimeMillis());

		mySubscriptionDao.pollForNewUndeliveredResources();
		assertThat(mySubscriptionFlaggedResourceDataDao.count(), greaterThan(0L));
		assertThat(mySubscriptionTableDao.count(), greaterThan(0L));

		/*
		 * Delete the subscription
		 */

		mySubscriptionDao.delete(subsId, mySrd);

		assertThat(mySubscriptionFlaggedResourceDataDao.count(), not(greaterThan(0L)));
		assertThat(mySubscriptionTableDao.count(), not(greaterThan(0L)));

		/*
		 * Delete a second time just to make sure that works
		 */
		mySubscriptionDao.delete(subsId, mySrd);

		/*
		 * Re-create the subscription
		 */

		subs.setId(subsId);
		mySubscriptionDao.update(subs, mySrd).getId();

		assertThat(mySubscriptionFlaggedResourceDataDao.count(), not(greaterThan(0L)));
		assertThat(mySubscriptionTableDao.count(), (greaterThan(0L)));

		/*
		 * Create another resource and make sure it gets flagged
		 */

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(100);

		mySubscriptionDao.pollForNewUndeliveredResources();
		assertThat(mySubscriptionFlaggedResourceDataDao.count(), greaterThan(0L));
		assertThat(mySubscriptionTableDao.count(), greaterThan(0L));

	}

	@Test
	public void testSubscriptionResourcesAppear() throws Exception {
		myDaoConfig.setSubscriptionPollDelay(0);

		String methodName = "testSubscriptionResourcesAppear";
		Patient p = new Patient();
		p.addName().setFamily(methodName);
		IIdType pId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Subscription subs;

		/*
		 * Create 2 identical subscriptions
		 */

		subs = new Subscription();
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?subject=Patient/" + pId.getIdPart());
		subs.setStatus(SubscriptionStatus.ACTIVE);
		Long subsId1 = mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(mySubscriptionDao.create(subs, mySrd).getId());

		subs = new Subscription();
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?subject=Patient/" + pId.getIdPart());
		subs.setStatus(SubscriptionStatus.ACTIVE);
		Long subsId2 = mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(mySubscriptionDao.create(subs, mySrd).getId());

		assertNull(mySubscriptionTableDao.findOne(subsId1).getLastClientPoll());

		Thread.sleep(100);
		ourLog.info("Before: {}", System.currentTimeMillis());

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		IIdType afterId1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		IIdType afterId2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(100);

		ourLog.info("After: {}", System.currentTimeMillis());

		List<IBaseResource> results;
		List<IIdType> resultIds;

		assertEquals(4, mySubscriptionDao.pollForNewUndeliveredResources());
		assertEquals(0, mySubscriptionDao.pollForNewUndeliveredResources());
		
		results = mySubscriptionDao.getUndeliveredResourcesAndPurge(subsId1);
		resultIds = toUnqualifiedVersionlessIds(results);
		assertThat(resultIds, contains(afterId1, afterId2));

		Date lastClientPoll = mySubscriptionTableDao.findOne(subsId1).getLastClientPoll();
		assertNotNull(lastClientPoll);

		mySubscriptionDao.pollForNewUndeliveredResources();
		results = mySubscriptionDao.getUndeliveredResourcesAndPurge(subsId2);
		resultIds = toUnqualifiedVersionlessIds(results);
		assertThat(resultIds, contains(afterId1, afterId2));

		mySubscriptionDao.pollForNewUndeliveredResources();
		results = mySubscriptionDao.getUndeliveredResourcesAndPurge(subsId1);
		resultIds = toUnqualifiedVersionlessIds(results);
		assertThat(resultIds, empty());

		assertNotEquals(lastClientPoll, mySubscriptionTableDao.findOne(subsId1).getLastClientPoll());

		mySubscriptionDao.pollForNewUndeliveredResources();
		results = mySubscriptionDao.getUndeliveredResourcesAndPurge(subsId2);
		resultIds = toUnqualifiedVersionlessIds(results);
		assertThat(resultIds, empty());

		/*
		 * Make sure that reindexing doesn't trigger
		 */
		
		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(100);

		assertEquals(0, mySubscriptionDao.pollForNewUndeliveredResources());

		/*
		 * Update resources on disk
		 */
		IBundleProvider allObs = myObservationDao.search(new SearchParameterMap());
		ourLog.info("Updating {} observations", allObs.size());
		for (IBaseResource next : allObs.getResources(0, allObs.size())) {
			ourLog.info("Updating observation");
			Observation nextObs = (Observation) next;
			nextObs.addPerformer().setDisplay("Some display");
			myObservationDao.update(nextObs, mySrd);
		}

		assertEquals(6, mySubscriptionDao.pollForNewUndeliveredResources());
		assertEquals(0, mySubscriptionDao.pollForNewUndeliveredResources());

	}


	@Test
	public void testSubscriptionResourcesAppear2() throws Exception {
		myDaoConfig.setSubscriptionPollDelay(0);

		String methodName = "testSubscriptionResourcesAppear2";
		Patient p = new Patient();
		p.addName().setFamily(methodName);
		IIdType pId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		IIdType oId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Subscription subs;

		/*
		 * Create 2 identical subscriptions
		 */

		subs = new Subscription();
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?subject=Patient/" + pId.getIdPart());
		subs.setStatus(SubscriptionStatus.ACTIVE);
		Long subsId1 = mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(mySubscriptionDao.create(subs, mySrd).getId());

		assertNull(mySubscriptionTableDao.findOne(subsId1).getLastClientPoll());

		assertEquals(0, mySubscriptionDao.pollForNewUndeliveredResources());
		
		ourLog.info("pId: {}   - oId: {}", pId, oId);
		
		myObservationDao.update(myObservationDao.read(oId, mySrd), mySrd);
		
		assertEquals(1, mySubscriptionDao.pollForNewUndeliveredResources());
		ourLog.info("Between passes");
		assertEquals(0, mySubscriptionDao.pollForNewUndeliveredResources());

		Thread.sleep(100);
		ourLog.info("Before: {}", System.currentTimeMillis());

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(100);

		ourLog.info("After: {}", System.currentTimeMillis());

		assertEquals(2, mySubscriptionDao.pollForNewUndeliveredResources());
		assertEquals(3, mySubscriptionFlaggedResourceDataDao.count());

		Thread.sleep(100);
		
		mySubscriptionDao.pollForNewUndeliveredResources();
		assertEquals(3, mySubscriptionFlaggedResourceDataDao.count());
		
		Thread.sleep(100);
		
		mySubscriptionDao.pollForNewUndeliveredResources();
		assertEquals(3, mySubscriptionFlaggedResourceDataDao.count());

		Thread.sleep(100);
		
		obs = new Observation();
		obs.getSubject().setReferenceElement(pId);
		obs.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		mySubscriptionDao.pollForNewUndeliveredResources();
		assertEquals(4, mySubscriptionFlaggedResourceDataDao.count());
		
		Thread.sleep(100);
		
		mySubscriptionDao.pollForNewUndeliveredResources();
		assertEquals(4, mySubscriptionFlaggedResourceDataDao.count());
	}


}
