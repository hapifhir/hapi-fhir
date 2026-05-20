package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSqlBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.QueryChunker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteExpungeSqlBuilderTest {
	@Mock
	private ResourceTableFKProvider myResourceTableFKProvider;
	@Mock
	private JpaStorageSettings myStorageSettings;
	@Mock
	private IIdHelperService<JpaPid> myIdHelper;
	@Mock
	private IResourceLinkDao myResourceLinkDao;
	@Mock
	private PartitionSettings myPartitionSettings;
	@InjectMocks
	private DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilderTest;

	@Test
	public void testQueryChunkingWhenFindingChildResources(){
		//Given: We have a list of pids.
		try (MockedStatic<QueryChunker> queryChunker = Mockito.mockStatic(QueryChunker.class)) {
			JpaPid firstPid = JpaPid.fromIdAndResourceType(1L, "Patient");
			JpaPid secondPid = JpaPid.fromIdAndResourceType(1L, "Patient");
			queryChunker.when(()-> QueryChunker.chunk(anyCollection(), any())).then(invocationOnMock -> null);

			//When: Searching for child resources (i.e. other resources that reference the target PIDs)
			myDeleteExpungeSqlBuilderTest.findResourceLinksWithTargetPidIn(List.of(firstPid), List.of(secondPid), List.of());

			//Verify: That the SQL query is done in chunks.
			queryChunker.verify(() -> QueryChunker.chunk(anyCollection(), any()), times(1));
		}
	}

	@Test
	void testValidateOkToDeleteAndExpunge_whenEnforceReferentialIntegrityDisableForPaths_noException() {
		// Set up
		String path = "Encounter.subject";
		when(myStorageSettings.getEnforceReferentialIntegrityOnDeleteDisableForPaths()).thenReturn(Set.of(path));
		when(myStorageSettings.isEnforceReferentialIntegrityOnDelete()).thenReturn(true);

		ResourceTable sourceRes = new ResourceTable();
		sourceRes.setId(JpaPid.fromIdAndResourceType(20L, "Encounter"));

		ResourceLink resLink = new ResourceLink();
		resLink.setSourceResource(sourceRes);
		resLink.setSourcePath(path);

		List<ResourceLink> conflictResourceLinks = List.of(resLink);
		when(myResourceLinkDao.findWithTargetPidIn(anyList())).thenReturn(conflictResourceLinks);

		Set<JpaPid> pids = Set.of(JpaPid.fromIdAndResourceType(10L, "Patient"));

		// Execute & Verify
		assertDoesNotThrow(() -> {
			myDeleteExpungeSqlBuilderTest.validateOkToDeleteAndExpunge(pids, false, null);
		});
	}

	@Test
	void validateOkToDeleteAndExpunge_RI_disabled_cascade_false_no_conflicts() {
		// RI disabled, cascade=false → early return before any conflict check
		when(myStorageSettings.isEnforceReferentialIntegrityOnDelete()).thenReturn(false);

		Set<JpaPid> pids = new HashSet<>();
		pids.add(JpaPid.fromIdAndResourceType(10L, "Patient"));

		// Should return without throwing — no conflict check performed
		assertThatCode(() -> myDeleteExpungeSqlBuilderTest.validateOkToDeleteAndExpunge(pids, false, null))
				.doesNotThrowAnyException();
	}

	@Test
	void validateOkToDeleteAndExpunge_RI_disabled_cascade_true_conflicts_resolved() {
		// RI disabled, cascade=true, conflicts exist but all sources are resolved in one round
		when(myStorageSettings.isEnforceReferentialIntegrityOnDelete()).thenReturn(false);

		JpaPid patientPid = JpaPid.fromIdAndResourceType(10L, "Patient");
		JpaPid observationPid = JpaPid.fromIdAndResourceType(20L, "Observation");

		// Conflict: Observation refers to Patient
		ResourceTable observationTable = new ResourceTable();
		observationTable.setId(observationPid);
		ResourceLink conflictLink = new ResourceLink();
		conflictLink.setSourceResource(observationTable);
		conflictLink.setSourcePath("Observation.subject");

		// First call returns the conflict; second call (for newly added observation PID) returns nothing
		when(myResourceLinkDao.findWithTargetPidIn(anyList()))
				.thenReturn(List.of(conflictLink))
				.thenReturn(List.of());

		Set<JpaPid> pids = new HashSet<>();
		pids.add(patientPid);

		// All conflicting PIDs should be added to the set and no exception should be thrown
		assertThatCode(() -> myDeleteExpungeSqlBuilderTest.validateOkToDeleteAndExpunge(pids, true, null))
				.doesNotThrowAnyException();
	}

	@Test
	void validateOkToDeleteAndExpunge_RI_disabled_cascade_true_maxRounds_exhausted() {
		// RI disabled, cascade=true, maxRounds exhausted → should NOT throw (Issue 1 fix)
		when(myStorageSettings.isEnforceReferentialIntegrityOnDelete()).thenReturn(false);

		JpaPid patientPid = JpaPid.fromIdAndResourceType(10L, "Patient");
		JpaPid observationPid = JpaPid.fromIdAndResourceType(20L, "Observation");

		// Conflict: Observation refers to Patient — keeps recurring so rounds are exhausted
		ResourceTable observationTable = new ResourceTable();
		observationTable.setId(observationPid);
		ResourceLink conflictLink = new ResourceLink();
		conflictLink.setSourceResource(observationTable);
		conflictLink.setSourcePath("Observation.subject");

		when(myResourceLinkDao.findWithTargetPidIn(anyList())).thenReturn(List.of(conflictLink));

		Set<JpaPid> pids = new HashSet<>();
		pids.add(patientPid);

		// With maxRounds=1, the loop exhausts after one cascade round but RI is disabled → no throw
		assertThatCode(() -> myDeleteExpungeSqlBuilderTest.validateOkToDeleteAndExpunge(pids, true, 1))
				.doesNotThrowAnyException();
	}
}
