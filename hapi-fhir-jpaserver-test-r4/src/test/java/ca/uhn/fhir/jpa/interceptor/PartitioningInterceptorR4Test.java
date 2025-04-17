package ca.uhn.fhir.jpa.interceptor;

import static ca.uhn.fhir.interceptor.model.RequestPartitionId.defaultPartition;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.r4.BasePartitioningR4Test;
import ca.uhn.fhir.jpa.interceptor.ex.PartitionInterceptorReadAllPartitions;
import ca.uhn.fhir.jpa.interceptor.ex.PartitionInterceptorReadPartitionsBasedOnScopes;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static ca.uhn.fhir.jpa.dao.r4.PartitioningSqlR4Test.assertLocalDateFromDbMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitioningInterceptorR4Test extends BasePartitioningR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PartitioningInterceptorR4Test.class);

	@Test
	public void testCrossPartitionUpdate() {
		// setup
		String id = "RED";
		mySrd.setRestOperationType(RestOperationTypeEnum.UPDATE);
		mySrd.setServletRequest(new MockHttpServletRequest());
		AtomicInteger readIndex = new AtomicInteger();
		AtomicInteger writeIndex = new AtomicInteger();

		Subscription subscription = new Subscription();
		subscription.setId("Subscription/" + id);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.addExtension(
			HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION,
			new BooleanType(true)
		);
		subscription.setCriteria("[*]");
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		subscription.setChannel(subscriptionChannelComponent);

		// set up partitioning for subscriptions
		mySubscriptionSettings.setCrossPartitionSubscriptionEnabled(true);

		// register interceptors that return different partition ids
		MySubscriptionReadInterceptor readInterceptor = new MySubscriptionReadInterceptor();
		MySubscriptionWriteInterceptor writeInterceptor = new MySubscriptionWriteInterceptor();
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
		readInterceptor.setObjectConsumer((obj) -> readIndex.getAndIncrement());
		writeInterceptor.setObjectConsumer((ojb) -> writeIndex.getAndIncrement());
		myInterceptorRegistry.registerInterceptor(readInterceptor);
		myInterceptorRegistry.registerInterceptor(writeInterceptor);

		try {

			// run test
			IFhirResourceDao<Subscription> dao = myDaoRegistry.getResourceDao(Subscription.class);
			DaoMethodOutcome outcome = dao.update(subscription, mySrd);

			// verify
			assertNotNull(outcome);
			assertEquals(id, outcome.getResource().getIdElement().getIdPart());
			assertEquals(0, readIndex.get()); // should be no read interactions
			assertEquals(2, writeIndex.get());
		} finally {
			myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
			myInterceptorRegistry.unregisterInterceptorsIf(t->t instanceof MySubscriptionReadInterceptor);
			myInterceptorRegistry.unregisterInterceptorsIf(t->t instanceof MySubscriptionWriteInterceptor);
		}
	}

	@Test
	public void testCreateNonPartionableResourceWithPartitionDate() {
		addNextTargetPartitionForCreate(defaultPartition(LocalDate.of(2021, 2, 22)));

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo");
		myStructureDefinitionDao.create(sd, mySrd);

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			LocalDate expectedDate = LocalDate.of(2021, 2, 22);
			assertEquals(1, resources.size());
			assertEquals(null, resources.get(0).getPartitionId().getPartitionId());
			assertLocalDateFromDbMatches(expectedDate, resources.get(0).getPartitionId().getPartitionDate());
		});
	}

	@Test
	public void testCreateNonPartionableResourceWithNullPartitionReturned() {
		addNextTargetPartitionForCreate(RequestPartitionId.defaultPartition());

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo");
		myStructureDefinitionDao.create(sd, mySrd);

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			assertEquals(1, resources.size());
			assertEquals(null, resources.get(0).getPartitionId().getPartitionId());
		});
	}

	@Test
	public void testCreateNonPartionableResourceWithDisallowedPartitionReturned() {
		addNextTargetPartitionForCreate(RequestPartitionId.fromPartitionId(1));

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo");
		try {
			myStructureDefinitionDao.create(sd, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1318) + "Resource type StructureDefinition can not be partitioned", e.getMessage());
		}
	}

	/**
	 * Should fail if no interceptor is registered for the READ pointcut
	 */
	@Test
	public void testSearch_NoReadInterceptor() {
		try {
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			myPatientDao.search(map);
			fail();
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(1319) + "No interceptor provided a value for pointcuts: [STORAGE_PARTITION_IDENTIFY_ANY, STORAGE_PARTITION_IDENTIFY_READ]", e.getMessage());
		}
	}

	@Test
	public void testSearch_InterceptorForAllPartitions() {
		IIdType patientIdNull = createPatient(withCreatePartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withCreatePartition(1), withActiveTrue());
		IIdType patientId2 = createPatient(withCreatePartition(2), withActiveTrue());


		PartitionInterceptorReadAllPartitions interceptor = new PartitionInterceptorReadAllPartitions();
		myInterceptorRegistry.registerInterceptor(interceptor);
		try {
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids).containsExactly(patientIdNull, patientId1, patientId2);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testSearch_InterceptorWithScopes() {
		assertNoRemainingPartitionIds();
		createPatient(withCreatePartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withCreatePartition(1), withActiveTrue());
		createPatient(withCreatePartition(2), withActiveTrue());
		assertNoRemainingPartitionIds();

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		when(mySrd.getServletRequest()).thenReturn(servletRequest);

		when(servletRequest.getAttribute(eq("ca.cdr.servletattribute.session.oidc.approved_scopes"))).thenReturn(Sets.newHashSet("partition-PART-1"));

		PartitionInterceptorReadPartitionsBasedOnScopes interceptor = new PartitionInterceptorReadPartitionsBasedOnScopes();
		myInterceptorRegistry.registerInterceptor(interceptor);
		try {
			// Load once to ensure that the partition name is resolved
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			myPatientDao.search(map, mySrd);

			// Do the load for real
			myCaptureQueriesListener.clear();
			IBundleProvider results = myPatientDao.search(map, mySrd);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids).containsExactly(patientId1);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	private void addCreatePartition(Integer thePartitionId, LocalDate thePartitionDate) {
		Validate.notNull(thePartitionId);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(thePartitionId, thePartitionDate);
		addNextInterceptorCreateResult(requestPartitionId);
	}

	private void addCreateDefaultPartition() {
		addNextInterceptorCreateResult(defaultPartition());
	}

	public void createRequestId() {
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
	}

	@Interceptor
	public static class MyWriteInterceptor {


		private final List<RequestPartitionId> myCreateRequestPartitionIds = new ArrayList<>();

		public void addCreatePartition(RequestPartitionId theRequestPartitionId) {
			myCreateRequestPartitionIds.add(theRequestPartitionId);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
			assertNotNull(theResource);
			RequestPartitionId retVal = myCreateRequestPartitionIds.remove(0);
			ourLog.info("Returning partition for create: {}", retVal);
			return retVal;
		}

		public void assertNoRemainingIds() {
			assertThat(myCreateRequestPartitionIds).isEmpty();
		}

	}

	@Interceptor
	public static class MySubscriptionReadInterceptor {

		private Consumer<Object> myObjectConsumer;

		public void setObjectConsumer(Consumer<Object> theConsumer) {
			myObjectConsumer = theConsumer;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId identifyForRead(ReadPartitionIdRequestDetails theReadDetails, RequestDetails theRequestDetails) {
			if (myObjectConsumer != null) {
				myObjectConsumer.accept(theReadDetails);
			}
			return RequestPartitionId.allPartitions();
		}

	}

	@Interceptor
	public static class MySubscriptionWriteInterceptor {
		private Consumer<Object> myObjectConsumer;

		public void setObjectConsumer(Consumer<Object> theConsumer) {
			myObjectConsumer = theConsumer;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
			assertNotNull(theResource);
			if (myObjectConsumer != null) {
				myObjectConsumer.accept(theResource);
			}
			// doesn't matter; just not allPartitions
			return defaultPartition(LocalDate.of(2021, 2, 22));
		}
	}
}
