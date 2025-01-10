package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Batch2DaoSvcImplTest extends BaseJpaR4Test {
	private static final Date PREVIOUS_MILLENNIUM = toDate(LocalDate.of(1999, Month.DECEMBER, 31));
	private static final Date TOMORROW = toDate(LocalDate.now().plusDays(1));

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IHapiTransactionService myIHapiTransactionService ;
	private IBatch2DaoSvc mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new Batch2DaoSvcImpl(myResourceTableDao, myResourceLinkDao,  myMatchUrlService, myDaoRegistry, myFhirContext, myIHapiTransactionService);
	}

	@Test
	void fetchResourceIds_ByUrlInvalidUrl() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "Patient");
		final InternalErrorException exception = assertThrows(InternalErrorException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("HAPI-2422: this should never happen: URL is missing a '?'", exception.getMessage());
	}

	@Test
	void fetchResourceIds_ByUrlSingleQuestionMark() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "?");
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("theResourceName must not be blank", exception.getMessage());
	}

	@Test
	void fetchResourceIds_ByUrlNonsensicalResource() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "Banana?_expunge=true");
		final DataFormatException exception = assertThrows(DataFormatException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("HAPI-1684: Unknown resource name \"Banana\" (this name is not known in FHIR version \"R4\")", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourceIds_ByUrl(int expectedNumResults) {
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), "Patient?_expunge=true");

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
			.toList());
		assertIdsEqual(patientIds, actualPatientIds);
	}

	@Test
	public void fetchResourceIds_ByUrl_WithData() {
		// Setup
		createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChange();

		// Start of resources within range
		Date start = new Date();
		sleepUntilTimeChange();
		Long patientId1 = createPatient(withActiveFalse()).getIdPartAsLong();
		createObservation(withObservationCode("http://foo", "bar"));
		createObservation(withObservationCode("http://foo", "bar"));
		sleepUntilTimeChange();
		Long patientId2 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChange();
		Date end = new Date();
		// End of resources within range

		createObservation(withObservationCode("http://foo", "bar"));
		createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChange();

		// Execute
		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(start, end, null, "Patient?active=false");

		// Verify
		List<TypedResourcePid> typedResourcePids = queryStream.visitStream(Stream::toList);

		assertThat(typedResourcePids)
				.hasSize(2)
				.containsExactly(
						new TypedResourcePid("Patient", patientId1),
						new TypedResourcePid("Patient", patientId2));

		assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(1);
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourceIds_NoUrl(int expectedNumResults) {
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		// at the moment there is no Prod use-case for noUrl use-case
		// reindex will always have urls as well (see https://github.com/hapifhir/hapi-fhir/issues/6179)
		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), null);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList());
		assertIdsEqual(patientIds, actualPatientIds);
	}

	private static void assertIdsEqual(List<IIdType> expectedResourceIds, List<? extends IIdType> actualResourceIds) {
		assertThat(actualResourceIds).hasSize(expectedResourceIds.size());

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

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", " "})
	public void fetchResourceIds_NoUrl_WithData(String theMissingUrl) {
		// Setup
		createPatient(withActiveFalse());
		sleepUntilTimeChange();

		Date start = new Date();
		Long id0 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChange();
		Long id1 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChange();
		Long id2 = createObservation(withObservationCode("http://foo", "bar")).getIdPartAsLong();
		sleepUntilTimeChange();

		Date end = new Date();
		sleepUntilTimeChange();
		createPatient(withActiveFalse());

		// Execute
		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(start, end, null, theMissingUrl);

		// Verify
		List<TypedResourcePid> typedPids = queryStream.visitStream(Stream::toList);
		assertThat(typedPids)
				.hasSize(3)
				.containsExactly(
						new TypedResourcePid("Patient", id0),
						new TypedResourcePid("Patient", id1),
						new TypedResourcePid("Observation", id2));

		assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(1);
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", " "})
	public void fetchResourceIds_NoUrl_NoData(String theMissingUrl) {
		// Execute
		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, theMissingUrl);

		// Verify
		List<TypedResourcePid> typedPids = queryStream.visitStream(Stream::toList);

		assertThat(typedPids).isEmpty();
		assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(1);
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}
}
