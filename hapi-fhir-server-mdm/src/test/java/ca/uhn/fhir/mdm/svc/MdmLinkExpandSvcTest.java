package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmLinkExpandSvcTest {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkExpandSvcTest.class);

	private static final int PARTITION_A = 1;
	private static final int PARTITION_B = 2;
	private static final int PARTITION_GOLDEN = 3;
	private static final JpaPid JPA_PID_PARTITION_A_1 = JpaPid.fromId(123L);
	private static final JpaPid JPA_PID_PARTITION_B = JpaPid.fromId(456L);
	private static final JpaPid JPA_PID_PARTITION_A_2 = JpaPid.fromId(789L);
	private static final JpaPid JPA_PID_PARTITION_DEFAULT = JpaPid.fromId(111L);
	private static final JpaPid JPA_PID_PARTITION_GOLDEN = JpaPid.fromId(999L);
	private static final Set<JpaPid> ALL_PIDS = Set.of(JPA_PID_PARTITION_A_1, JPA_PID_PARTITION_B, JPA_PID_PARTITION_A_2, JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_DEFAULT);
	private static final MdmPidTuple<JpaPid> JPA_PID_MDM_PID_TUPLE_1 = MdmPidTuple.fromGoldenAndSourceAndPartitionIds(JPA_PID_PARTITION_GOLDEN, PARTITION_GOLDEN, JPA_PID_PARTITION_A_1, PARTITION_A);
	private static final MdmPidTuple<JpaPid> JPA_PID_MDM_PID_TUPLE_2 = MdmPidTuple.fromGoldenAndSourceAndPartitionIds(JPA_PID_PARTITION_GOLDEN, PARTITION_GOLDEN, JPA_PID_PARTITION_B, PARTITION_B);
	private static final MdmPidTuple<JpaPid> JPA_PID_MDM_PID_TUPLE_3 = MdmPidTuple.fromGoldenAndSourceAndPartitionIds(JPA_PID_PARTITION_GOLDEN, PARTITION_GOLDEN, JPA_PID_PARTITION_A_2, PARTITION_A);
	private static final MdmPidTuple<JpaPid> JPA_PID_MDM_PID_TUPLE_4 = MdmPidTuple.fromGoldenAndSourceAndPartitionIds(JPA_PID_PARTITION_GOLDEN, PARTITION_GOLDEN, JPA_PID_PARTITION_DEFAULT, null);

	@Mock
	private IMdmLinkDao<JpaPid,?> myIMdmLinkDao;

	@Mock
	private IIdHelperService<?> myIdHelperService;

	@InjectMocks
	private MdmLinkExpandSvc mySubject;

	@BeforeEach
	public void beforeEach() {
		mySubject.myMdmLinkDao = myIMdmLinkDao;
		mySubject.myIdHelperService = myIdHelperService;
	}


	void beforeEachExpand() {
		final Answer<Set<String>> answer = invocation -> {
			final Set<IResourcePersistentId<?>> param = invocation.getArgument(0);
			return param.stream()
				.filter(JpaPid.class::isInstance)
				.map(JpaPid.class::cast)
				.map(pid -> Long.toString(pid.getId()))
				.collect(Collectors.toUnmodifiableSet());
		};

		when(myIdHelperService.translatePidsToFhirResourceIds(any()))
			.thenAnswer(answer);
	}

	private static Stream<Arguments> partitionsAndExpectedPids() {
		return Stream.of(
			Arguments.of(RequestPartitionId.allPartitions(), ALL_PIDS),
			Arguments.of(RequestPartitionId.defaultPartition(), Collections.singleton(JPA_PID_PARTITION_DEFAULT)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A)), Set.of(JPA_PID_PARTITION_A_1, JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_B)), Collections.singleton(JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_GOLDEN)), Collections.singleton(JPA_PID_PARTITION_GOLDEN)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A, PARTITION_B)), Set.of(JPA_PID_PARTITION_A_1, JPA_PID_PARTITION_A_2, JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, null)), Set.of(JPA_PID_PARTITION_A_1, JPA_PID_PARTITION_A_2, JPA_PID_PARTITION_B, JPA_PID_PARTITION_DEFAULT)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, PARTITION_GOLDEN, null)), ALL_PIDS)
		);
	}

	@ParameterizedTest
	@MethodSource("partitionsAndExpectedPids")
	void expandMdmBySourceResourcePid(RequestPartitionId theRequestPartitionId, Set<JpaPid> theExpectedJpaPids) {
		beforeEachExpand();

		final JpaPid jpaPid = JpaPid.fromId(123L);
		when(myIMdmLinkDao.expandPidsBySourcePidAndMatchResult(jpaPid, MdmMatchResultEnum.MATCH)).thenReturn(List.of(
			JPA_PID_MDM_PID_TUPLE_1,
			JPA_PID_MDM_PID_TUPLE_1,
			JPA_PID_MDM_PID_TUPLE_1,
			JPA_PID_MDM_PID_TUPLE_1,
			JPA_PID_MDM_PID_TUPLE_2,
			JPA_PID_MDM_PID_TUPLE_2,
			JPA_PID_MDM_PID_TUPLE_2,
			JPA_PID_MDM_PID_TUPLE_2,
			JPA_PID_MDM_PID_TUPLE_3,
			JPA_PID_MDM_PID_TUPLE_3,
			JPA_PID_MDM_PID_TUPLE_3,
			JPA_PID_MDM_PID_TUPLE_3,
			JPA_PID_MDM_PID_TUPLE_3,
			JPA_PID_MDM_PID_TUPLE_4,
			JPA_PID_MDM_PID_TUPLE_4,
			JPA_PID_MDM_PID_TUPLE_4,
			JPA_PID_MDM_PID_TUPLE_4)
		);

		final Set<String> resolvedPids = mySubject.expandMdmBySourceResourcePid(theRequestPartitionId, jpaPid);

		assertThat(resolvedPids).as(String.format("expected: %s, actual: %s", theExpectedJpaPids, resolvedPids)).isEqualTo(toPidStrings(theExpectedJpaPids));
	}

	@ParameterizedTest
	@MethodSource("partitionsAndExpectedPids")
	void expandMdmByGoldenResourcePid(RequestPartitionId theRequestPartitionId, Set<JpaPid> theExpectedJpaPids) {
		beforeEachExpand();

		when(myIMdmLinkDao.expandPidsByGoldenResourcePidAndMatchResult(any(), any()))
			.thenReturn(List.of(JPA_PID_MDM_PID_TUPLE_1, JPA_PID_MDM_PID_TUPLE_2, JPA_PID_MDM_PID_TUPLE_3, JPA_PID_MDM_PID_TUPLE_4));
		final JpaPid jpaPid = JpaPid.fromId(123L);
		final Set<String> resolvedPids = mySubject.expandMdmByGoldenResourcePid(theRequestPartitionId, jpaPid);

		assertThat(resolvedPids).as(String.format("expected: %s, actual: %s", theExpectedJpaPids, resolvedPids)).isEqualTo(toPidStrings(theExpectedJpaPids));
	}

	private static Stream<Arguments> partitionsAndTuples() {
		return Stream.of(
			Arguments.of(RequestPartitionId.allPartitions(), JPA_PID_MDM_PID_TUPLE_1, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_A_1)),
			Arguments.of(RequestPartitionId.defaultPartition(), JPA_PID_MDM_PID_TUPLE_1, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A)), JPA_PID_MDM_PID_TUPLE_1, Collections.singleton(JPA_PID_PARTITION_A_1)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_B)), JPA_PID_MDM_PID_TUPLE_1, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_GOLDEN)), JPA_PID_MDM_PID_TUPLE_1, Collections.singleton(JPA_PID_PARTITION_GOLDEN)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A, PARTITION_B)), JPA_PID_MDM_PID_TUPLE_1, Collections.singleton(JPA_PID_PARTITION_A_1)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, null)), JPA_PID_MDM_PID_TUPLE_1, Collections.singleton(JPA_PID_PARTITION_A_1)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, PARTITION_GOLDEN, null)), JPA_PID_MDM_PID_TUPLE_1, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_A_1)),
			Arguments.of(RequestPartitionId.allPartitions(), JPA_PID_MDM_PID_TUPLE_2, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.defaultPartition(), JPA_PID_MDM_PID_TUPLE_2, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A)), JPA_PID_MDM_PID_TUPLE_2, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_B)), JPA_PID_MDM_PID_TUPLE_2, Collections.singleton(JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_GOLDEN)), JPA_PID_MDM_PID_TUPLE_2, Collections.singleton(JPA_PID_PARTITION_GOLDEN)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A, PARTITION_B)), JPA_PID_MDM_PID_TUPLE_2, Collections.singleton(JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, null)), JPA_PID_MDM_PID_TUPLE_2, Collections.singleton(JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, PARTITION_GOLDEN, null)), JPA_PID_MDM_PID_TUPLE_2, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_B)),
			Arguments.of(RequestPartitionId.allPartitions(), JPA_PID_MDM_PID_TUPLE_3, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.defaultPartition(), JPA_PID_MDM_PID_TUPLE_3, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A)), JPA_PID_MDM_PID_TUPLE_3, Collections.singleton(JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_B)), JPA_PID_MDM_PID_TUPLE_3, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_GOLDEN)), JPA_PID_MDM_PID_TUPLE_3, Collections.singleton(JPA_PID_PARTITION_GOLDEN)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A, PARTITION_B)), JPA_PID_MDM_PID_TUPLE_3, Collections.singleton(JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, null)), JPA_PID_MDM_PID_TUPLE_3, Collections.singleton(JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, PARTITION_GOLDEN, null)), JPA_PID_MDM_PID_TUPLE_3, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_A_2)),
			Arguments.of(RequestPartitionId.allPartitions(), JPA_PID_MDM_PID_TUPLE_4, Set.of(JPA_PID_PARTITION_GOLDEN, JPA_PID_PARTITION_DEFAULT)),
			Arguments.of(RequestPartitionId.defaultPartition(), JPA_PID_MDM_PID_TUPLE_4, Collections.singleton(JPA_PID_PARTITION_DEFAULT)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A)), JPA_PID_MDM_PID_TUPLE_4, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_B)), JPA_PID_MDM_PID_TUPLE_4, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_GOLDEN)), JPA_PID_MDM_PID_TUPLE_4, Collections.singleton(JPA_PID_PARTITION_GOLDEN)),
			Arguments.of(RequestPartitionId.fromPartitionIds(List.of(PARTITION_A, PARTITION_B)), JPA_PID_MDM_PID_TUPLE_4, Collections.emptySet()),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, null)), JPA_PID_MDM_PID_TUPLE_4, Collections.singleton(JPA_PID_PARTITION_DEFAULT)),
			Arguments.of(RequestPartitionId.fromPartitionIds(Arrays.asList(PARTITION_A, PARTITION_B, PARTITION_GOLDEN, null)), JPA_PID_MDM_PID_TUPLE_4, Set.of(JPA_PID_PARTITION_DEFAULT, JPA_PID_PARTITION_GOLDEN))
		);
	}

	@ParameterizedTest
	@MethodSource("partitionsAndTuples")
	void flattenTuple(RequestPartitionId theRequestPartitionId, MdmPidTuple<JpaPid> theTuple, Set<JpaPid> theExpectedResourceIds) {
		assertEquals(theExpectedResourceIds, MdmLinkExpandSvc.flattenTuple(theRequestPartitionId, theTuple));
	}

	@Nonnull
	private Set<String> toPidStrings(Set<JpaPid> theTheExpectedJpaPids) {
		return theTheExpectedJpaPids.stream().map(JpaPid::getId).map(id -> Long.toString(id)).collect(Collectors.toUnmodifiableSet());
	}
}
