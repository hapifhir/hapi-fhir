package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.ThreadPoolUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseHapiFhirDaoTest {

	private static class TestDao extends BaseHapiFhirDao<Patient> {

		@Nullable
		@Override
		protected String getResourceName() {
			return "Patient";
		}

		@Override
		protected TagDefinition getTagOrNull(TransactionDetails theDetails,
														 TagTypeEnum theEnum,
														 String theScheme,
														 String theTerm,
														 String theLabel) {
			// we need to init synchronization due to what
			// the underlying class is doing
			try {
				TransactionSynchronizationManager.initSynchronization();
				return super.getTagOrNull(theDetails, theEnum, theScheme, theTerm, theLabel);
			} finally {
				TransactionSynchronizationManager.clearSynchronization();
			}
		}
	}

	private Logger ourLogger;

	@Mock
	private Appender<ILoggingEvent> myAppender;

	@Mock
	private MemoryCacheService myMemoryCacheService;

	@Mock
	private EntityManager myEntityManager;

	@Mock
	private PlatformTransactionManager myTransactionManager;

	@InjectMocks
	private TestDao myTestDao;

	@BeforeEach
	public void init() {
		ourLogger = (Logger) LoggerFactory.getLogger(BaseHapiFhirDao.class);
		ourLogger.addAppender(myAppender);
	}

	@AfterEach
	public void end() {
		ourLogger.detachAppender(myAppender);
	}

	/**
	 * Returns a mocked criteria builder
	 * @return
	 */
	private CriteriaBuilder getMockedCriteriaBuilder() {
		Predicate pred = mock(Predicate.class);

		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		// lenient due to multiple equal calls with different inputs
		lenient().when(builder.equal(any(), any()))
			.thenReturn(pred);

		return builder;
	}

	/**
	 * Returns a mocked from
	 * @return
	 */
	private Root<TagDefinition> getMockedFrom() {
		Path path = mock(Path.class);

		Root<TagDefinition> from = mock(Root.class);
		// lenient due to multiple get calls with different inputs
		lenient().when(from.get(anyString()))
			.thenReturn(path);
		return from;
	}

	@Test
	public void getTagOrNull_raceCondition_wontUpsertDuplicates() throws InterruptedException, ExecutionException {
		/*
		 * We use this boolean to fake a race condition.
		 * Boolean is used for two reasons:
		 * 1) We don't want an unstable test (so a fake
		 * race condition will ensure the test always executes
		 * exactly as expected... but this just ensures the code
		 * is not buggy, not that race conditions are actually handled)
		 * 2) We want the ability (and confidence!) to know
		 * that a _real_ race condition can be handled. Setting
		 * this boolean false (and potentially tweaking thread count)
		 * gives us this confidence.
		 *
		 * Set this false to try with a real race condition
		 */
		boolean fakeRaceCondition = true;

		// the more threads, the more likely we
		// are to see race conditions.
		// We need a lot to ensure at least 2 threads
		// are in the create method at the same time
		int threads = fakeRaceCondition ? 2 : 30;

		// setup
		TagTypeEnum tagType = TagTypeEnum.TAG;
		String scheme = "http://localhost";
		String term = "code123";
		String label = "hollow world";
		String raceConditionError = "Entity exists; if this is logged, you have race condition issues!";

		TagDefinition tagDefinition = new TagDefinition(tagType,
			scheme,
			term,
			label);

		// mock objects
		CriteriaBuilder builder = getMockedCriteriaBuilder();
		TypedQuery<TagDefinition> query = mock(TypedQuery.class);
		CriteriaQuery<TagDefinition> cq = mock(CriteriaQuery.class);
		Root<TagDefinition> from = getMockedFrom();

		// when
		when(myEntityManager.getCriteriaBuilder())
			.thenReturn(builder);
		when(builder.createQuery(any(Class.class)))
				.thenReturn(cq);
		when(cq.from(any(Class.class)))
				.thenReturn(from);
		when(myEntityManager.createQuery(any(CriteriaQuery.class)))
			.thenReturn(query);
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);
		AtomicInteger getSingleResultInt = new AtomicInteger();
		when(query.getSingleResult())
			.thenAnswer(new Answer<TagDefinition>() {
				private final AtomicInteger count = new AtomicInteger();

				@Override
				public TagDefinition answer(InvocationOnMock invocationOnMock) throws Throwable {
					getSingleResultInt.incrementAndGet();
					if (fakeRaceCondition) {
						// fake
						// ensure the first 2 accesses throw to
						// help fake a race condition (2, or possibly the same,
						// thread failing to access the resource)
						if (count.get() < 2) {
							count.incrementAndGet();
							throw new NoResultException();
						}
					}
					else {
						// real
						if (!atomicBoolean.get()) {
							throw new NoResultException();
						}
					}
					return tagDefinition;
				}
			});
		AtomicInteger persistInt = new AtomicInteger();
		doAnswer(new Answer() {
			private final AtomicInteger count = new AtomicInteger();

			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				persistInt.incrementAndGet();
				if (fakeRaceCondition) {
					// fake
					if (count.get() < 1) {
						count.incrementAndGet();
						return null;
					}
					else {
						throw new EntityExistsException(raceConditionError);
					}
				}
				else {
					// real
					if (!atomicBoolean.getAndSet(true)) {
						// first thread gets null...
						return null;
					} else {
						// all other threads get entity exists exception
						throw new EntityExistsException(raceConditionError);
					}
				}
			}
		}).when(myEntityManager).persist(any(Object.class));

		ourLogger.setLevel(Level.WARN);

		// test
//		ExecutorService service = Executors.newFixedThreadPool(threads);
		ConcurrentHashMap<Integer, TagDefinition> outcomes = new ConcurrentHashMap<>();
		ConcurrentHashMap<Integer, Throwable> errors = new ConcurrentHashMap<>();

		ThreadPoolTaskExecutor executor = ThreadPoolUtil.newThreadPool(threads, threads * 2, "test-");

		AtomicInteger counter = new AtomicInteger();

		CountDownLatch latch = new CountDownLatch(threads);
		Runnable task = () -> {
			latch.countDown();
			try {
				TagDefinition retTag = myTestDao.getTagOrNull(new TransactionDetails(), tagType, scheme, term, label);
				outcomes.put(retTag.hashCode(), retTag);
				counter.incrementAndGet();
			} catch (Exception ex) {
				errors.put(ex.hashCode(), ex);
			}
		};

		ArrayList<Future> futures = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			futures.add(executor.submit(task));
		}
		for (Future f : futures) {
			f.get();
		}
		AsyncUtil.awaitLatchAndIgnoreInterrupt(latch, (long) threads, TimeUnit.SECONDS);

//		try {
//			ArrayList<Future> futures = new ArrayList<>();
//			for (int i = 0; i < threads; i++) {
//				futures.add(service.submit(task));
//			}
//			for (Future f : futures) {
//				f.get();
//			}
//		} finally {
//			service.shutdown();
//		}
//		// should not take a second per thread.
//		// but will take some time, due to the delays above.
//		// a second per thread seems like a good threshold.
//		Assertions.assertTrue(
//			service.awaitTermination(threads, TimeUnit.SECONDS)
//		);

		Assertions.assertEquals(threads + 1, getSingleResultInt.get(), "Not enough gets " + getSingleResultInt.get());
		Assertions.assertEquals(threads, persistInt.get(), "Not enough persists " + persistInt.get());

		// verify
		Assertions.assertEquals(1, outcomes.size());
		Assertions.assertEquals(threads, counter.get());
		Assertions.assertEquals(0, errors.size(),
			errors.values().stream().map(Throwable::getMessage)
				.collect(Collectors.joining(", ")));

		// verify we logged some race conditions
		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender, Mockito.atLeastOnce())
			.doAppend(captor.capture());
		assertTrue(captor.getAllValues().get(0).getMessage()
			.contains(raceConditionError));
	}

	@Test
	public void getTagOrNull_failingForever_throwsInternalErrorAndLogsWarnings() {
		// setup
		TagTypeEnum tagType = TagTypeEnum.TAG;
		String scheme = "http://localhost";
		String term = "code123";
		String label = "hollow world";
		TransactionDetails transactionDetails = new TransactionDetails();
		String exMsg = "Hi there";
		String readError = "No read for you";

		ourLogger.setLevel(Level.WARN);

		// mock objects
		CriteriaBuilder builder = getMockedCriteriaBuilder();
		TypedQuery<TagDefinition> query = mock(TypedQuery.class);
		CriteriaQuery<TagDefinition> cq = mock(CriteriaQuery.class);
		Root<TagDefinition> from = getMockedFrom();

		// when
		when(myEntityManager.getCriteriaBuilder())
			.thenReturn(builder);
		when(builder.createQuery(any(Class.class)))
			.thenReturn(cq);
		when(cq.from(any(Class.class)))
			.thenReturn(from);
		when(myEntityManager.createQuery(any(CriteriaQuery.class)))
			.thenReturn(query);
		when(query.getSingleResult())
			.thenThrow(new NoResultException(readError));
		doThrow(new RuntimeException(exMsg))
			.when(myEntityManager).persist(any(Object.class));

		// test
		try {
			myTestDao.getTagOrNull(transactionDetails, tagType, scheme, term, label);
			fail();
		} catch (Exception ex) {
			// verify
			assertTrue(ex.getMessage().contains("Tag get/create failed after 10 attempts with error(s): " + exMsg));

			ArgumentCaptor<ILoggingEvent> appenderCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
			verify(myAppender, Mockito.times(10))
				.doAppend(appenderCaptor.capture());
			List<ILoggingEvent> events = appenderCaptor.getAllValues();
			assertEquals(10, events.size());
			for (int i = 0; i < 10; i++) {
				String actualMsg = events.get(i).getMessage();
				assertEquals(
					"Tag read/write failed: "
						+ exMsg
						+ ". "
						+ "This is not a failure on its own, "
						+ "but could be useful information in the result of an actual failure.",
					actualMsg
				);
			}
		}
	}

	////////// Static access tests

	@Test
	public void cleanProvenanceSourceUri() {
		assertEquals("", BaseHapiFhirDao.cleanProvenanceSourceUri(null));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc"));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc#def"));
		assertEquals("abc", BaseHapiFhirDao.cleanProvenanceSourceUri("abc#def#ghi"));
	}
}
