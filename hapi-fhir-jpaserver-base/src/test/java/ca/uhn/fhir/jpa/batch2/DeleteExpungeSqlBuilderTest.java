package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSqlBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.QueryChunker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.times;

public class DeleteExpungeSqlBuilderTest {
	@Autowired
	private ResourceTableFKProvider theResourceTableFKProvider;
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	private IIdHelperService<JpaPid> myIdHelper;
	@Mock
	private IResourceLinkDao theResourceLinkDao;
	private PartitionSettings thePartitionSettings = new PartitionSettings();
	private DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilderTest;

	@BeforeEach
	public void beforeEach() {
		myDeleteExpungeSqlBuilderTest = new DeleteExpungeSqlBuilder(
			theResourceTableFKProvider,
			myStorageSettings,
			myIdHelper,
			theResourceLinkDao,
			thePartitionSettings
		);
	}

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
}
