package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.JpaPid;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
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
	@Mock
	IIdHelperService myIdHelperService;
	@Mock
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Mock
	IMdmSettings myMdmSettings;
	@Mock
	MessageHelper myMessageHelper;
	@Mock
	MdmPartitionHelper myMdmPartitionHelper;

	@InjectMocks
	MdmLinkCreateSvcImpl myMdmLinkCreateSvc = new MdmLinkCreateSvcImpl();

	@Test
	public void testCreateLink() {
		ArgumentCaptor<IMdmLink> mdmLinkCaptor = ArgumentCaptor.forClass(IMdmLink.class);

		when(myMdmLinkDaoSvc.save(mdmLinkCaptor.capture())).thenReturn(new MdmLink());

		Patient goldenPatient = new Patient().setActive(true);
		MdmResourceUtil.setMdmManaged(goldenPatient);
		MdmResourceUtil.setGoldenResource(goldenPatient);

		Patient sourcePatient = new Patient();
		MdmTransactionContext ctx = new MdmTransactionContext();

		myMdmLinkCreateSvc.createLink(goldenPatient, sourcePatient, MdmMatchResultEnum.MATCH, ctx);

		IMdmLink mdmLink = mdmLinkCaptor.getValue();

		assertEquals(MdmLinkSourceEnum.MANUAL, mdmLink.getLinkSource());
		assertEquals("Patient", mdmLink.getMdmSourceType());

	}

	@BeforeEach
	public void setup() {
		JpaPid goldenId = new JpaPid(1L);
		JpaPid sourceId = new JpaPid(2L);
		when(myIdHelperService.getPidOrThrowException(any()))
			.thenReturn(goldenId, sourceId);
		when(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(any(ResourcePersistentId.class), any(ResourcePersistentId.class))).thenReturn(Optional.empty());
		when(myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(any(ResourcePersistentId.class), any())).thenReturn(new ArrayList<>());

		MdmLink resultMdmLink = new MdmLink();
		resultMdmLink.setGoldenResourcePersistenceId(goldenId).setSourcePersistenceId(sourceId);

		when(myMdmLinkDaoSvc.getOrCreateMdmLinkByGoldenResourceAndSourceResource(any(), any())).thenReturn(resultMdmLink);


		when(myMdmSettings.isSupportedMdmType(any())).thenReturn(true);
	}
}
