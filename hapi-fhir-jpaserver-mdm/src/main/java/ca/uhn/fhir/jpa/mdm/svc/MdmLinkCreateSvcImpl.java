package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MdmLinkCreateSvcImpl implements IMdmLinkCreateSvc  {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	IMdmSettings myMdmSettings;
	@Autowired
	MessageHelper myMessageHelper;

	@Transactional
	@Override
	public IAnyResource createLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchResultEnum theMatchResult, MdmTransactionContext theMdmContext) {
		String sourceType = myFhirContext.getResourceType(theSourceResource);

		validateCreateLinkRequest(theGoldenResource, theSourceResource, sourceType);

		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long targetId = myIdHelperService.getPidOrThrowException(theSourceResource);

		Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (optionalMdmLink.isPresent()) {
			throw new InvalidRequestException(myMessageHelper.getMessageForPresentLink(theGoldenResource, theSourceResource));
		}

		List<MdmLink> mdmLinks = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(targetId, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() > 0 && theMatchResult == MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(myMessageHelper.getMessageForMultipleGoldenRecords(theSourceResource));
		}

		MdmLink mdmLink = myMdmLinkDaoSvc.getOrCreateMdmLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		if (theMatchResult == null) {
			mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		} else {
			mdmLink.setMatchResult(theMatchResult);
		}
		ourLog.info("Manually creating a " + theGoldenResource.getIdElement().toVersionless() + " to " + theSourceResource.getIdElement().toVersionless() + " mdm link.");
		myMdmLinkDaoSvc.save(mdmLink);

		return theGoldenResource;
	}

	private void validateCreateLinkRequest(IAnyResource theGoldenRecord, IAnyResource theSourceResource, String theSourceType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theSourceType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theSourceType));
		}

		if (!Objects.equals(goldenRecordType, theSourceType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theSourceType));
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenRecord)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnmanagedResource());
		}

		if (!MdmResourceUtil.isMdmAllowed(theSourceResource)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedSourceResource());
		}
	}
}
