package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.hapi.fhir.batch2.test.IJobPartitionProviderTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaJobPartitionProviderTest implements IJobPartitionProviderTest {
	@Mock
	private FhirContext myFhirContext;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private MatchUrlService myMatchUrlService;
	@Mock
	private IPartitionLookupSvc myPartitionLookupSvc;
	@InjectMocks
	private JpaJobPartitionProvider myJobPartitionProvider;

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public IRequestPartitionHelperSvc getRequestPartitionHelper() {
		return myRequestPartitionHelperSvc;
	}

	@Override
	public MatchUrlService getMatchUrlService() {
		return myMatchUrlService;
	}

	@Override
	public JpaJobPartitionProvider getJobPartitionProvider() {
		return myJobPartitionProvider;
	}

	@Test
	public void getPartitionedUrls_someUrlsAssociatedWithAllPartitions_returnsCorrectly() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		List<RequestPartitionId> partitionIds = List.of(
				RequestPartitionId.fromPartitionIds(1),
				RequestPartitionId.fromPartitionIds(2)
		);

		setupResourceNameUrlWithPartition(requestDetails, "Patient", RequestPartitionId.allPartitions());
		setupResourceNameUrlWithPartition(requestDetails, "Observation", RequestPartitionId.fromPartitionId(1));
		setupPartitions(partitionIds);

		List<String> urls = List.of("Patient?", "Observation?");
		List<PartitionedUrl> partitionedUrls = new ArrayList<>();
		partitionIds.forEach(p -> partitionedUrls.add(new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(p)));
		partitionedUrls.add(new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionIds(1)));
		verifyGetPartitionedUrls(requestDetails, urls, partitionedUrls);
	}

	private void setupPartitions(List<RequestPartitionId> thePartitionIds) {
		List<PartitionEntity> partitionEntities = new ArrayList<>();
		thePartitionIds.forEach(partitionId -> {
			PartitionEntity entity = mock(PartitionEntity.class);
			when(entity.toRequestPartitionId()).thenReturn(partitionId);
			partitionEntities.add(entity);
		});
		when(myPartitionLookupSvc.listPartitions()).thenReturn(partitionEntities);
	}
}