package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.provider.BaseLinkR4Test;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;

import static ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus.DEFAULT_PAGE_SIZE;
import static ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus.MAX_PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

public class MdmControllerSvcImplTest extends BaseLinkR4Test {
	@Autowired
	IMdmControllerSvc myMdmControllerSvc;

	@SpyBean
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Test
	public void testSurvivorshipIsCalledOnMatchingToTheSameGoldenResource() {

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		assertLinkCount(1);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		Patient patient = createPatientAndUpdateLinksOnPartition(buildFrankPatient(), requestPartitionId);

		getGoldenResourceFromTargetResource(patient);

		MdmLink link = myMdmLinkDaoSvc.findMdmLinkBySource(patient).get();
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		saveLink(link);
		assertEquals(MdmLinkSourceEnum.AUTO, link.getLinkSource());
		assertLinkCount(2);

		Page<MdmLinkJson> resultPage = myMdmControllerSvc.queryLinks(null, myPatientId.getIdElement().getValue(), null, null,
			new MdmTransactionContext(MdmTransactionContext.OperationType.QUERY_LINKS),
			new MdmPageRequest((Integer) null, null, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE),
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(1)));

		assertEquals(resultPage.getContent().size(), 1);

		assertEquals(resultPage.getContent().get(0).getSourceId(), patient.getIdElement().getResourceType() + "/" + patient.getIdElement().getIdPart());

		Mockito.verify(myRequestPartitionHelperSvc, Mockito.atLeastOnce()).validateHasPartitionPermissions(any(), eq("Patient"), argThat(new PartitionIdMatcher(requestPartitionId)));
	}

	@Test
	public void testMdmDuplicateGoldenResource() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		assertLinkCount(1);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		Patient patient = createPatientAndUpdateLinksOnPartition(buildFrankPatient(), requestPartitionId);

		getGoldenResourceFromTargetResource(patient);

		MdmLink link = myMdmLinkDaoSvc.findMdmLinkBySource(patient).get();
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		saveLink(link);
		assertEquals(MdmLinkSourceEnum.AUTO, link.getLinkSource());
		assertLinkCount(2);

		Page<MdmLinkJson> resultPage = myMdmControllerSvc.getDuplicateGoldenResources(null,
			new MdmPageRequest((Integer) null, null, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE),
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(1)));

		assertEquals(resultPage.getContent().size(), 1);

		assertEquals(resultPage.getContent().get(0).getSourceId(), patient.getIdElement().getResourceType() + "/" + patient.getIdElement().getIdPart());

		Mockito.verify(myRequestPartitionHelperSvc, Mockito.atLeastOnce()).validateHasPartitionPermissions(any(), eq("Patient"), argThat(new PartitionIdMatcher(requestPartitionId)));
	}

	private class PartitionIdMatcher implements ArgumentMatcher<RequestPartitionId> {
		private RequestPartitionId myRequestPartitionId;

		PartitionIdMatcher(RequestPartitionId theRequestPartitionId){
			myRequestPartitionId = theRequestPartitionId;
		}

		@Override
		public boolean matches(RequestPartitionId theRequestPartitionId) {
			return myRequestPartitionId.getPartitionIds().equals(theRequestPartitionId.getPartitionIds());
		}
	}

}
