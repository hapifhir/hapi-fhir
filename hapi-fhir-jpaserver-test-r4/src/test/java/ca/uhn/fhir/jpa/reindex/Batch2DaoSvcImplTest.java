package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class Batch2DaoSvcImplTest extends BaseJpaR4Test {

	private static final Date PREVIOUS_MILLENNIUM = toDate(LocalDate.of(1999, Month.DECEMBER, 31));
	private static final Date TOMORROW = toDate(LocalDate.now().plusDays(1));
	private static final String URL_PATIENT_EXPUNGE_TRUE = "Patient?_expunge=true";
	private static final String PATIENT = "Patient";
	private static final int INTERNAL_SYNCHRONOUS_SEARCH_SIZE = 10;

	@Autowired
	private JpaStorageSettings myJpaStorageSettings;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IHapiTransactionService myIHapiTransactionService ;

	private DaoRegistry mySpiedDaoRegistry;

	private IBatch2DaoSvc mySubject;

	@BeforeEach
	void beforeEach() {
		myJpaStorageSettings.setInternalSynchronousSearchSize(INTERNAL_SYNCHRONOUS_SEARCH_SIZE);

		mySpiedDaoRegistry = spy(myDaoRegistry);

		mySubject = new Batch2DaoSvcImpl(myResourceTableDao, myMatchUrlService, mySpiedDaoRegistry, myFhirContext, myIHapiTransactionService, myJpaStorageSettings);
	}

	// TODO: LD this test won't work with the nonUrl variant yet:  error:   No existing transaction found for transaction marked with propagation 'mandatory'

	@Test
	void fetchResourcesByUrlEmptyUrl() {
		final InternalErrorException exception = assertThrows(InternalErrorException.class, () -> mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), ""));

		assertEquals("HAPI-2422: this should never happen: URL is missing a '?'", exception.getMessage());
	}

	@Test
	void fetchResourcesByUrlSingleQuestionMark() {
		final InternalErrorException exception = assertThrows(InternalErrorException.class, () -> mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), "?"));

		assertEquals("HAPI-2223: theResourceName must not be blank", exception.getMessage());
	}

	@Test
	void fetchResourcesByUrlNonsensicalResource() {
		final InternalErrorException exception = assertThrows(InternalErrorException.class, () -> mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), "Banana?_expunge=true"));

		assertEquals("HAPI-2223: HAPI-1684: Unknown resource name \"Banana\" (this name is not known in FHIR version \"R4\")", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourcesByUrl(int expectedNumResults) {
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.getTypedResourcePids()
				.stream()
				.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList();
		assertIdsEqual(patientIds, actualPatientIds);

		verify(mySpiedDaoRegistry, times(getExpectedNumOfInvocations(expectedNumResults))).getResourceDao(PATIENT);
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourcesNoUrl(int expectedNumResults) {
		final int pageSizeWellBelowThreshold = 2;
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, pageSizeWellBelowThreshold, RequestPartitionId.defaultPartition(), null);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.getTypedResourcePids()
				.stream()
				.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList();
		assertIdsEqual(patientIds, actualPatientIds);
	}

	private int getExpectedNumOfInvocations(int expectedNumResults) {
		final int maxResultsPerQuery = INTERNAL_SYNCHRONOUS_SEARCH_SIZE + 1;
		final int division = expectedNumResults / maxResultsPerQuery;
		return division + 1;
	}

	private static void assertIdsEqual(List<IIdType> expectedResourceIds, List<? extends IIdType> actualResourceIds) {
		assertEquals(expectedResourceIds.size(), actualResourceIds.size());

		for (int index = 0; index < expectedResourceIds.size(); index++) {
			final IIdType expectedIdType = expectedResourceIds.get(index);
			final IIdType actualIdType = actualResourceIds.get(index);

			assertEquals(expectedIdType.getResourceType(), actualIdType.getResourceType());
			assertEquals(expectedIdType.getIdPartAsLong(), actualIdType.getIdPartAsLong());
		}
	}

	@Nonnull
	private static Date toDate(LocalDate theLocalDate) {
		return Date.from(theLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
