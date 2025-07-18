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

import java.util.List;
import java.util.Set;

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
}
