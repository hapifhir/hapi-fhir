package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmLinkCreateSvcImplTest {
	@SuppressWarnings("unused")
	@Spy
	FhirContext myFhirContext = FhirContext.forR4();
	@SuppressWarnings("rawtypes")
	@Mock
	IIdHelperService myIdHelperService;
	@SuppressWarnings("rawtypes")
	@Mock
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Mock
	IMdmSettings myMdmSettings;
	@Mock
	MessageHelper myMessageHelper;
	@Mock
	MdmPartitionHelper myMdmPartitionHelper;

	@Mock
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@Mock
	IMdmModelConverterSvc myIMdmModelConverterSvc;

	@InjectMocks
	MdmLinkCreateSvcImpl myMdmLinkCreateSvc = new MdmLinkCreateSvcImpl();

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void testCreateLink() {
		ArgumentCaptor<IMdmLink> mdmLinkCaptor = ArgumentCaptor.forClass(IMdmLink.class);

		when(myMdmLinkDaoSvc.save(mdmLinkCaptor.capture())).thenReturn(new MdmLink());

		Patient goldenPatient = new Patient().setActive(true);
		MdmResourceUtil.setMdmManaged(goldenPatient);
		MdmResourceUtil.setGoldenResource(goldenPatient);

		Patient sourcePatient = new Patient();
		MdmTransactionContext ctx = new MdmTransactionContext();

		MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
		params.setGoldenResource(goldenPatient);
		params.setSourceResource(sourcePatient);
		params.setMatchResult(MdmMatchResultEnum.MATCH);
		params.setMdmContext(ctx);
		params.setRequestDetails(new SystemRequestDetails());
		myMdmLinkCreateSvc.createLink(params);

		IMdmLink mdmLink = mdmLinkCaptor.getValue();

		assertEquals(MdmLinkSourceEnum.MANUAL, mdmLink.getLinkSource());
		assertEquals("Patient", mdmLink.getMdmSourceType());

	}

	@SuppressWarnings({"unchecked"})
	@BeforeEach
	public void setup() {
		JpaPid goldenId = JpaPid.fromId(1L);
		JpaPid sourceId = JpaPid.fromId(2L);
		when(myIdHelperService.getPidOrThrowException(any()))
			.thenReturn(goldenId, sourceId);
		when(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(any(BaseResourcePersistentId.class), any(BaseResourcePersistentId.class))).thenReturn(Optional.empty());
		when(myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(any(BaseResourcePersistentId.class), any())).thenReturn(new ArrayList<>());

		MdmLink resultMdmLink = new MdmLink();
		resultMdmLink.setGoldenResourcePersistenceId(goldenId).setSourcePersistenceId(sourceId);

		when(myMdmLinkDaoSvc.getOrCreateMdmLinkByGoldenResourceAndSourceResource(any(), any())).thenReturn(resultMdmLink);


		when(myMdmSettings.isSupportedMdmType(any())).thenReturn(true);
	}
}
