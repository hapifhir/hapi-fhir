package ca.uhn.fhir.jpa.reindex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Batch2DaoSvcImplTest extends BaseJpaR4Test {

	private static final Date PREVIOUS_MILLENNIUM = toDate(LocalDate.of(1999, Month.DECEMBER, 31));
	private static final Date TOMORROW = toDate(LocalDate.now().plusDays(1));
	private static final String URL_PATIENT_EXPUNGE_TRUE = "Patient?_expunge=true";
	private static final String PATIENT = "Patient";

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IHapiTransactionService myIHapiTransactionService ;

	private IBatch2DaoSvc mySubject;


	@BeforeEach
	void beforeEach() {

		mySubject = new Batch2DaoSvcImpl(myResourceTableDao, myMatchUrlService, myDaoRegistry, myFhirContext, myIHapiTransactionService);
	}

	// TODO: LD this test won't work with the nonUrl variant yet:  error:   No existing transaction found for transaction marked with propagation 'mandatory'

	@Test
	void fetchResourcesByUrlEmptyUrl() {
		final InternalErrorException exception =
			assertThrows(
				InternalErrorException.class,
				() -> mySubject.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), "")
					.visitStream(Stream::toList));

		assertEquals("HAPI-2422: this should never happen: URL is missing a '?'", exception.getMessage());
	}

	@Test
	void fetchResourcesByUrlSingleQuestionMark() {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mySubject.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), "?").visitStream(Stream::toList));

		assertEquals("theResourceName must not be blank", exception.getMessage());
	}

	@Test
	void fetchResourcesByUrlNonsensicalResource() {
		final DataFormatException exception = assertThrows(DataFormatException.class, () -> mySubject.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), "Banana?_expunge=true").visitStream(Stream::toList));

		assertEquals("HAPI-1684: Unknown resource name \"Banana\" (this name is not known in FHIR version \"R4\")", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourcesByUrl(int expectedNumResults) {
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		final IResourcePidStream resourcePidList = mySubject.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
			.toList());
		assertIdsEqual(patientIds, actualPatientIds);
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 9, 10, 11, 21, 22, 23, 45})
	void fetchResourcesNoUrl(int expectedNumResults) {
		final int pageSizeWellBelowThreshold = 2;
		final List<IIdType> patientIds = IntStream.range(0, expectedNumResults)
			.mapToObj(num -> createPatient())
			.toList();

		final IResourcePidStream resourcePidList = mySubject.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), null);

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
}
