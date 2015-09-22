package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import javax.persistence.TypedQuery;

import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class FhirResourceDaoDstu2SubscriptionTest extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2SubscriptionTest.class);

	@Test
	public void testCreateSubscriptionInvalidCriteria() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);
		subs.setCriteria("Observation");
		try {
			mySubscriptionDao.create(subs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);
		subs.setCriteria("http://foo.com/Observation?AAA=BBB");
		try {
			mySubscriptionDao.create(subs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria must be in the form \"{Resource Type}?[params]\""));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);
		subs.setCriteria("ObservationZZZZ?a=b");
		try {
			mySubscriptionDao.create(subs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.criteria contains invalid/unsupported resource type: ObservationZZZZ"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		try {
			mySubscriptionDao.create(subs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Subscription.channel.type must be populated on this server"));
		}

		subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);
		subs.setCriteria("Observation?identifier=123");
		subs.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
		assertTrue(mySubscriptionDao.create(subs).getId().hasIdPart());

	}

	@Before
	public void beforeEnableSubscription() {
		myDaoConfig.setSubscriptionEnabled(true);
	}
	
	@Test
	public void testSubscriptionResourcesAppear() {
		String methodName = "testSubscriptionResourcesAppear";
		Patient p = new Patient();
		p.addName().addFamily(methodName);
		IIdType pId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReference(pId);
		obs.setStatus(ObservationStatusEnum.FINAL);
		IIdType beforeId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		Subscription subs = new Subscription();
		subs.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
		subs.setCriteria("Observation?subject=Patient/123");
		IIdType id = mySubscriptionDao.create(subs).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReference(pId);
		obs.setStatus(ObservationStatusEnum.FINAL);
		IIdType afterId1 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReference(pId);
		obs.setStatus(ObservationStatusEnum.FINAL);
		IIdType afterId2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		mySubscriptionDao.pollForNewUndeliveredResources();
	}

	@Test
	public void testCreateSubscription() {
		Subscription subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
		subs.setStatus(SubscriptionStatusEnum.REQUESTED);

		IIdType id = mySubscriptionDao.create(subs).getId().toUnqualifiedVersionless();

		TypedQuery<SubscriptionTable> q = myEntityManager.createQuery("SELECT t from SubscriptionTable t WHERE t.mySubscriptionResource.myId = :id", SubscriptionTable.class);
		q.setParameter("id", id.getIdPartAsLong());
		final SubscriptionTable table = q.getSingleResult();

		assertNotNull(table);
		assertNotNull(table.getNextCheck());
		assertEquals(table.getNextCheck(), table.getSubscriptionResource().getPublished().getValue());
		assertEquals(SubscriptionStatusEnum.REQUESTED, myEntityManager.find(SubscriptionTable.class, table.getId()).getStatus());
		assertEquals(SubscriptionStatusEnum.REQUESTED, mySubscriptionDao.read(id).getStatusElement().getValueAsEnum());

		subs.setStatus(SubscriptionStatusEnum.ACTIVE);
		mySubscriptionDao.update(subs);

		assertEquals(SubscriptionStatusEnum.ACTIVE, myEntityManager.find(SubscriptionTable.class, table.getId()).getStatus());
		assertEquals(SubscriptionStatusEnum.ACTIVE, mySubscriptionDao.read(id).getStatusElement().getValueAsEnum());

		mySubscriptionDao.delete(id);

		assertNull(myEntityManager.find(SubscriptionTable.class, table.getId()));

		/*
		 * Re-create again
		 */

		subs = new Subscription();
		subs.setCriteria("Observation?subject=Patient/123");
		subs.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
		subs.setId(id);
		mySubscriptionDao.update(subs);

		assertEquals(SubscriptionStatusEnum.REQUESTED, myEntityManager.createQuery("SELECT t FROM SubscriptionTable t WHERE t.myResId = " + id.getIdPart(), SubscriptionTable.class).getSingleResult().getStatus());
		assertEquals(SubscriptionStatusEnum.REQUESTED, mySubscriptionDao.read(id).getStatusElement().getValueAsEnum());
	}

}
