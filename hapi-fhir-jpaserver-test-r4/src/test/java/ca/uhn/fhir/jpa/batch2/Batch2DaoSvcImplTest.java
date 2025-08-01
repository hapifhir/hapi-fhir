package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Batch2DaoSvcImplTest extends BaseJpaR4Test {
	private static final Date PREVIOUS_MILLENNIUM = toDate(LocalDate.of(1999, Month.DECEMBER, 31));
	private static final Date TOMORROW = toDate(LocalDate.now().plusDays(1));

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IHapiTransactionService myIHapiTransactionService;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	private IBatch2DaoSvc mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new Batch2DaoSvcImpl(myResourceTableDao, myResourceLinkDao,  myMatchUrlService, myDaoRegistry, myFhirContext, myIHapiTransactionService, myPartitionSettings, mySearchBuilderFactory);
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

		assertEquals("HAPI-2742: Conditional URL does not include a resource type, but includes parameters which require a resource type", exception.getMessage());
	}

	@Test
	void fetchResourceIds_WithNoResourceType() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "?_id=abc");
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("HAPI-2742: Conditional URL does not include a resource type, but includes parameters which require a resource type", exception.getMessage());
	}

	@Test
	void fetchResourceIds_ByUrlNonsensicalResource() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "Banana?_expunge=true");
		final DataFormatException exception = assertThrows(DataFormatException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("HAPI-1684: Unknown resource name \"Banana\" (this name is not known in FHIR version \"R4\")", exception.getMessage());
	}

	@Test
	void fetchResourceIds_ByUrlNonsensicalParam() {
		IResourcePidStream stream = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, null, "Patient?banana=true");
		final InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> stream.visitStream(Stream::toList));

		assertEquals("HAPI-0488: Failed to parse match URL[Patient?banana=true] - Resource type Patient does not have a parameter with name: banana", exception.getMessage());
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

	@ParameterizedTest
	@MethodSource("metaSearchParamProvider")
	void fetchResourceIds_ByUrlMetaParamsOnServerBase_succeeds(String theSearchParam, String theParamValue) {
		// Given
		Patient matchPatient = buildResource("Patient",
			withId("p-match"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		Patient noMatchPatient = buildResource("Patient",
			withId("p-no-match"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		DaoMethodOutcome patientMatchOutcome = doUpdateResourceAndReturnOutcome(matchPatient);
		IdType idMatchPatient = new IdType(patientMatchOutcome.getPersistentId().getResourceType(), (Long) patientMatchOutcome.getPersistentId().getId());

		doUpdateResourceAndReturnOutcome(noMatchPatient);

		// When
		String theUrl = "?" + theSearchParam + "=" + theParamValue;
		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), theUrl);
		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList());

		// Then
		assertIdsEqual(List.of(idMatchPatient), actualPatientIds);
	}

	private static Stream<Arguments> metaSearchParamProvider() {
		return Stream.of(
			Arguments.of(Constants.PARAM_TAG, "tag-match"),
			Arguments.of(Constants.PARAM_PROFILE, "http://example.org/profile-match"),
			Arguments.of(Constants.PARAM_SECURITY, "security-match")
		);
	}

	@Test
	void fetchResourceIds_ByUrlLastUpdatedParamOnServerBase_succeeds() {
		IBaseResource matchPatient = buildResource("Patient",
			withId("p-match"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		IBaseResource noMatchPatient = buildResource("Patient",
			withId("p-no-match"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		DaoMethodOutcome patientMatchOutcome = doUpdateResourceAndReturnOutcome(matchPatient);
		IdType idMatchPatient = new IdType(patientMatchOutcome.getPersistentId().getResourceType(), (Long) patientMatchOutcome.getPersistentId().getId());
		Date lastUpdated = patientMatchOutcome.getResource().getMeta().getLastUpdated();

		sleepAtLeast(1000);
		BaseDateTimeDt d = new DateTimeDt().setValue(lastUpdated);

		String theUrl = "?" + Constants.PARAM_LASTUPDATED + "=eq" + d.getValueAsString();
		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), theUrl);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList());
		assertIdsEqual(List.of(idMatchPatient), actualPatientIds);
	}

	// Test the other common search parameters that are not supported by querying withput resource type (ie. ?_param)
	// Most of these params are more complex and involve search parameters that don't apply to every resource
	// The _id search parameter won't work since a unique resource is identified by type, id, and partition
	@ParameterizedTest
	@ValueSource(strings = {
		Constants.PARAM_ID,
		Constants.PARAM_TEXT,
		Constants.PARAM_CONTENT,
		Constants.PARAM_LIST,
		Constants.PARAM_QUERY
	})
	void fetchResourceIds_ByUrlMetaParamsOnServerBase_failsWithRequiresResourceType(String theSearchParam) {
		// Given
		String theUrl = "?" + theSearchParam + "=abc";
		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), theUrl);

		// When
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId())).toList()));

		// Then
		assertThat(exception.getMessage()).isEqualTo("HAPI-2742: Conditional URL does not include a resource type, but includes parameters which require a resource type");
	}

	@ParameterizedTest
	@MethodSource("reindexDeletedResourcesBySearchUrlParams")
	void fetchResourceIds_ByUrlDeletedParamOnServerBase_succeeds(SearchIncludeDeletedEnum theIncludeDeleted, String theLastUpdatedParam) {
		// Given
		IBaseResource matchPatient = buildResource("Patient",
			withId("p-match"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		IBaseResource matchPatientDeleted = buildResource("Patient",
			withId("p-match-del"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		IBaseResource noMatchPatient = buildResource("Patient",
			withId("p-no-match"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		IBaseResource noMatchPatientDeleted = buildResource("Patient",
			withId("p-no-match-del"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		DaoMethodOutcome patientMatchOutcome = doUpdateResourceAndReturnOutcome(matchPatient);
		IdType idBeforeDate = new IdType(patientMatchOutcome.getPersistentId().getResourceType(), (Long) patientMatchOutcome.getPersistentId().getId());

		DaoMethodOutcome patientMatchOutcomeDel = doUpdateResourceAndReturnOutcome(matchPatientDeleted);
		IdType idDeletedBeforeDate = new IdType(patientMatchOutcomeDel.getPersistentId().getResourceType(), (Long) patientMatchOutcomeDel.getPersistentId().getId());
		deleteResource(new IdType("Patient", "p-match-del"));

		BaseDateTimeDt d = new DateTimeDt().setValue(new Date());
		sleepAtLeast(1000);

		DaoMethodOutcome patientNoMatchOutcome = doUpdateResourceAndReturnOutcome(noMatchPatient);
		IdType idAfterDate = new IdType(patientNoMatchOutcome.getPersistentId().getResourceType(), (Long) patientNoMatchOutcome.getPersistentId().getId());

		DaoMethodOutcome patientNoMatchOutcomeDel = doUpdateResourceAndReturnOutcome(noMatchPatientDeleted);
		IdType idDeletedAfterDate = new IdType(patientNoMatchOutcomeDel.getPersistentId().getResourceType(), (Long) patientNoMatchOutcomeDel.getPersistentId().getId());
		deleteResource(new IdType("Patient", "p-no-match-del"));

		StringBuilder theAdditionalSearchUrlParams = new StringBuilder();
		if (theLastUpdatedParam != null) {
			theAdditionalSearchUrlParams.append(String.format(theLastUpdatedParam, d.getValueAsString()));
		}

		String theFullUrl = "?" + Constants.PARAM_INCLUDE_DELETED + "=" + theIncludeDeleted.getCode() + theAdditionalSearchUrlParams;
		ourLog.info("Reindexing with URL: " + theFullUrl);

		// When
		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), theFullUrl);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList());

		// Then
		List<IIdType> expectedMatchIds = new ArrayList<>();

		switch(theIncludeDeleted) {
			case EXCLUSIVE -> {
				expectedMatchIds.add(idDeletedBeforeDate);
				if (theLastUpdatedParam == null) {
					expectedMatchIds.add(idDeletedAfterDate);
				}
			}
			case NEVER -> {
				expectedMatchIds.add(idBeforeDate);
				if (theLastUpdatedParam == null) {
					expectedMatchIds.add(idAfterDate);
				}
			}
			case BOTH -> {
				expectedMatchIds.addAll(List.of(idBeforeDate, idDeletedBeforeDate));
				if (theLastUpdatedParam == null) {
					expectedMatchIds.addAll(List.of(idAfterDate, idDeletedAfterDate));
				}
			}
		}

		assertIdsEqual(expectedMatchIds, actualPatientIds);
	}

	// Try the _includeDeleted param with and without the _lastUpdated param
	private static Stream<Arguments> reindexDeletedResourcesBySearchUrlParams() {
		String lastUpdatedParamSuffix = "&" + Constants.PARAM_LASTUPDATED + "=le%s";

		return Arrays.stream(SearchIncludeDeletedEnum.values()).flatMap(theDeletedMode ->
			Stream.of(
				Arguments.of(theDeletedMode, lastUpdatedParamSuffix),
				Arguments.of(theDeletedMode, null)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("reindexDeletedResourcesBySearchUrlParamsOnResourceType")
	public void fetchResourceIds_ByUrlDeletedParamOnResourceType_succeeds(SearchIncludeDeletedEnum theIncludeDeleted, String theLastUpdatedParam, String theIdParam) {
		// Given
		IBaseResource matchPatient = buildResource("Patient",
			withId("p-match"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		IBaseResource matchPatientDeleted = buildResource("Patient",
			withId("p-match-del"),
			withTag("http://example.com", "tag-match"),
			withProfile("http://example.org/profile-match"),
			withSecurity("http://example.com", "security-match"));

		IBaseResource noMatchPatient = buildResource("Patient",
			withId("p-no-match"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		IBaseResource noMatchPatientDeleted = buildResource("Patient",
			withId("p-no-match-del"),
			withTag("http://example.com", "tag-no-match"),
			withProfile("http://example.org/profile-no-match"),
			withSecurity("http://example.com", "security-no-match"));

		IBaseResource observationBeforeDate = buildResource("Observation",
			withId("obsA"),
			withEffectiveDate("2021-01-01T00:00:00"));

		IBaseResource observationAfterDate = buildResource("Observation",
			withId("obsB"),
			withEffectiveDate("2021-01-01T00:00:00"));

		doUpdateResource(observationBeforeDate);

		DaoMethodOutcome patientMatchOutcome = doUpdateResourceAndReturnOutcome(matchPatient);
		IdType idBeforeDate = new IdType(patientMatchOutcome.getPersistentId().getResourceType(), (Long) patientMatchOutcome.getPersistentId().getId());

		DaoMethodOutcome patientMatchOutcomeDel = doUpdateResourceAndReturnOutcome(matchPatientDeleted);
		IdType idDeletedBeforeDate = new IdType(patientMatchOutcomeDel.getPersistentId().getResourceType(), (Long) patientMatchOutcomeDel.getPersistentId().getId());
		deleteResource(new IdType("Patient", "p-match-del"));

		BaseDateTimeDt date = new DateTimeDt().setValue(new Date());
		sleepAtLeast(1000);

		doUpdateResource(observationAfterDate);

		doUpdateResourceAndReturnOutcome(noMatchPatient);
		doUpdateResourceAndReturnOutcome(noMatchPatientDeleted);
		deleteResource(new IdType("Patient", "p-no-match-del"));

		// When
		String theFullUrl = "Patient?_includeDeleted=" + theIncludeDeleted.getCode() + buildAdditionalSearchParams(theIncludeDeleted, theLastUpdatedParam, theIdParam, date);

		final IResourcePidStream resourcePidList = mySvc.fetchResourceIdStream(PREVIOUS_MILLENNIUM, TOMORROW, RequestPartitionId.defaultPartition(), theFullUrl);

		final List<? extends IIdType> actualPatientIds =
			resourcePidList.visitStream(s-> s.map(typePid -> new IdDt(typePid.resourceType, (Long) typePid.id.getId()))
				.toList());

		// Then
		switch(theIncludeDeleted) {
			case EXCLUSIVE -> assertIdsEqual(List.of(idDeletedBeforeDate), actualPatientIds);
			case NEVER -> assertIdsEqual(List.of(idBeforeDate), actualPatientIds);
			case BOTH -> {
				if (theIdParam != null) {
					assertIdsEqual(List.of(idDeletedBeforeDate), actualPatientIds);
				} else {
					assertIdsEqual(List.of(idBeforeDate, idDeletedBeforeDate), actualPatientIds);
				}
			}
		}
	}

	private String buildAdditionalSearchParams(SearchIncludeDeletedEnum theIncludeDeleted, String theLastUpdatedParam, String theIdParam, BaseDateTimeDt date) {
		StringBuilder theAdditionalSearchUrlParams = new StringBuilder();

		if (theLastUpdatedParam != null) {
			theAdditionalSearchUrlParams.append(String.format(theLastUpdatedParam, date.getValueAsString()));
		}
		if (theIdParam != null) {
			String theIdToUse = theIncludeDeleted.equals(SearchIncludeDeletedEnum.NEVER) ? "p-match" : "p-match-del";
			theAdditionalSearchUrlParams.append(String.format(theIdParam, theIdToUse));
		}
		return theAdditionalSearchUrlParams.toString();
	}

	// Matrix of all supported search params (_lastUpdated, _id) with valid values of the _includeDeleted param
	private static Stream<Arguments> reindexDeletedResourcesBySearchUrlParamsOnResourceType() {
		String lastUpdatedParamSuffix = "&" + Constants.PARAM_LASTUPDATED + "=le%s";
		String idParamSuffix = "&" + Constants.PARAM_ID + "=%s";

		return Arrays.stream(SearchIncludeDeletedEnum.values()).flatMap(theDeletedMode ->
			Stream.of(
				Arguments.of(theDeletedMode, lastUpdatedParamSuffix, null),
				Arguments.of(theDeletedMode, null, idParamSuffix),
				Arguments.of(theDeletedMode, lastUpdatedParamSuffix, idParamSuffix)
			));
	}

}
