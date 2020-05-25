package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeleteConflictServiceTest.SpringConfig.class})
public class DeleteConflictServiceTest {

	@MockBean
	private DeleteConflictFinderService myDeleteConflictFinderService;
	/**
	 * This is needed, don't remove
	 */
	@SuppressWarnings("unused")
	@MockBean
	private IResourceLinkDao myResourceLinkDao;
	@SuppressWarnings("unused")
	@MockBean
	private FhirContext myFhirContext;
	@MockBean
	@SuppressWarnings("unused")
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private DeleteConflictService myDeleteConflictService;

	@Test
	public void noInterceptorTwoConflictsDoesntRetry() {
		ResourceTable entity = new ResourceTable();
		DeleteConflictList deleteConflicts = new DeleteConflictList();

		List<ResourceLink> list = new ArrayList<>();
		ResourceLink link = new ResourceLink();
		link.setSourceResource(entity);
		list.add(link);
		when(myDeleteConflictFinderService.findConflicts(any(), anyInt())).thenReturn(list);
		int retryCount = myDeleteConflictService.validateOkToDelete(deleteConflicts, entity, false, null, new TransactionDetails());
		assertEquals(0, retryCount);
	}

	static class SpringConfig {
		@Bean
		DeleteConflictService myDeleteConflictService() {
			return new DeleteConflictService();
		}

		@Bean
		DaoConfig myDaoConfig() {
			return new DaoConfig();
		}

		@Bean
		PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}
	}
}
