package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
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
			TransactionSynchronizationManager.initSynchronization();
			return super.getTagOrNull(theDetails, theEnum, theScheme, theTerm, theLabel);
		}
	}

	@Mock
	private MemoryCacheService myMemoryCacheService;

	@Mock
	private EntityManager myEntityManager;

	@InjectMocks
	private TestDao myTestDao;

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
		// setup
		TagTypeEnum tagType = TagTypeEnum.TAG;
		String scheme = "http://localhost";
		String term = "code123";
		String label = "hollow world";
		TransactionDetails transactionDetails = new TransactionDetails();

		TagDefinition tagDefinition = new TagDefinition(tagType,
			scheme,
			term,
			label);

		// the more threads, the more likely we
		// are to see race conditions
		int threads = 20;

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
			.thenAnswer(new Answer<TagDefinition>() {
				private int count = 0;

				@Override
				public TagDefinition answer(InvocationOnMock invocationOnMock) throws Throwable {
					// this delay is to slow down
					// other threads
					Thread.sleep(500);
					if (count == 0) {
						count++;
						throw new NoResultException();
					}
					return tagDefinition;
				}
			});
		doAnswer(new Answer() {
			private int count = 0;

			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				if (count == 0) {
					count++;
					/*
					 * This delay is to ensure other threads
					 * have time to enter this method at the same time.
					 *
					 * How much time we need depends on speed of computer,
					 * number of threads, etc.
					 *
					 * So half second here is "arbitrary", but hopefully
					 * good enough for failures if getTagOrNull will throw
					 */
					Thread.sleep(500);
					return null;
				}
				else {
					throw new EntityExistsException("Entity exists; if this is logged, you have race condition issues!");
				}
			}
		}).when(myEntityManager).persist(any(Object.class));

		// test
		ExecutorService service = Executors.newFixedThreadPool(threads);
		ConcurrentHashMap<Integer, TagDefinition> outcomes = new ConcurrentHashMap<>();
		ConcurrentHashMap<Integer, Throwable> errors = new ConcurrentHashMap<>();

		AtomicInteger counter = new AtomicInteger();
		for (int i = 0; i < threads; i++) {
				service.submit(() -> {
				try {
					TagDefinition retTag = myTestDao.getTagOrNull(transactionDetails, tagType, scheme, term, label);
					outcomes.put(retTag.hashCode(), retTag);
					counter.incrementAndGet();
				} catch (Exception ex) {
					errors.put(ex.hashCode(), ex);
				}
			});
		}
		service.shutdown();
		// should not take a second per thread.
		// but will take some time, due to the delays above.
		// a second per thread seems like a good threshold.
		Assertions.assertTrue(
			service.awaitTermination(threads, TimeUnit.SECONDS)
		);

		// verify
		Assertions.assertEquals(0, errors.size(),
			errors.values().stream().map(Throwable::getMessage)
				.collect(Collectors.joining(", ")));
		Assertions.assertEquals(1, outcomes.size());
		Assertions.assertEquals(threads, counter.get());
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
