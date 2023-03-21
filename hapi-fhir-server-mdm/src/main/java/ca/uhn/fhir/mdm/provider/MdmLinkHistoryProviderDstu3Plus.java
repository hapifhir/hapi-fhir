package ca.uhn.fhir.mdm.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevisionJson;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MdmLinkHistoryProviderDstu3Plus extends BaseMdmProvider {
	private static final Logger ourLog = getLogger(MdmLinkHistoryProviderDstu3Plus.class);

	private final IMdmControllerSvc myMdmControllerSvc;

	public MdmLinkHistoryProviderDstu3Plus(FhirContext theFhirContext, IMdmControllerSvc theMdmControllerSvc) {
		super(theFhirContext);
		myMdmControllerSvc = theMdmControllerSvc;
	}

	@Operation(name = ProviderConstants.MDM_LINK_HISTORY, idempotent = true)
	public IBaseParameters historyLinks(@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theMdmGoldenResourceIds,
													@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theResourceIds,
													ServletRequestDetails theRequestDetails) {
		validateMdmLinkHistoryParameters(theMdmGoldenResourceIds, theResourceIds);

		final List<String> goldenResourceIdsToUse = convertToStringsIfNotNull(theMdmGoldenResourceIds);
		final List<String> resourceIdsToUse = convertToStringsIfNotNull(theResourceIds);

		final IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);

		final MdmHistorySearchParameters mdmHistorySearchParameters = new MdmHistorySearchParameters()
			.setGoldenResourceIds(goldenResourceIdsToUse)
			.setSourceIds(resourceIdsToUse);

		final List<MdmLinkWithRevisionJson> mdmLinkRevisionsFromSvc = myMdmControllerSvc.queryLinkHistory(mdmHistorySearchParameters, theRequestDetails);

		parametersFromMdmLinkRevisions(retVal, mdmLinkRevisionsFromSvc);

		return retVal;
	}
}
