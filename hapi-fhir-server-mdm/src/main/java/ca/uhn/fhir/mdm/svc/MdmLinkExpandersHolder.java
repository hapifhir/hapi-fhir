package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.util.EIDHelper;

public class MdmLinkExpandersHolder {

	private IMdmSettings myMdmSettings;

	private IMdmLinkExpandSvc myLinkExpandSvcInstanceToUse;

	private IBulkExportMDMResourceExpander myBulkExportMDMResourceExpanderInstanceToUse;

	private final IMdmLinkExpandSvc myMdmLinkExpandSvc;

	private final MdmEidMatchOnlyLinkExpandSvc myMdmEidMatchOnlyLinkExpandSvc;

	private final BulkExportMDMResourceExpander myBulkExportMDMResourceExpander;

	private final BulkExportMDMEidMatchOnlyResourceExpander myBulkExportMDMEidMatchOnlyResourceExpander;

	private final FhirContext myFhirContext;

	public MdmLinkExpandersHolder(
			IMdmLinkExpandSvc theMdmLinkExpandSvc,
			MdmEidMatchOnlyLinkExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			BulkExportMDMResourceExpander theBulkExportMDMResourceExpander,
			BulkExportMDMEidMatchOnlyResourceExpander theBulkExportMDMEidMatchOnlyResourceExpander,
			FhirContext theFhirContext) {
		myMdmLinkExpandSvc = theMdmLinkExpandSvc;
		myMdmEidMatchOnlyLinkExpandSvc = theMdmEidMatchOnlyLinkExpandSvc;
		myBulkExportMDMResourceExpander = theBulkExportMDMResourceExpander;
		myBulkExportMDMEidMatchOnlyResourceExpander = theBulkExportMDMEidMatchOnlyResourceExpander;
		myFhirContext = theFhirContext;
	}

	public IMdmLinkExpandSvc getLinkExpandSvcInstance() {
		if (myLinkExpandSvcInstanceToUse != null) {
			return myLinkExpandSvcInstanceToUse;
		}

		myLinkExpandSvcInstanceToUse = determineLinkExpandSvsInstanceToUse();

		return myLinkExpandSvcInstanceToUse;
	}

	public IBulkExportMDMResourceExpander getBulkExportMDMResourceExpanderInstance() {
		if (myBulkExportMDMResourceExpanderInstanceToUse != null) {
			return myBulkExportMDMResourceExpanderInstanceToUse;
		}

		myBulkExportMDMResourceExpanderInstanceToUse = determineBulkExportMDMResourceExpanderInstanceToUse();

		return myBulkExportMDMResourceExpanderInstanceToUse;
	}

	public IBulkExportMDMResourceExpander determineBulkExportMDMResourceExpanderInstanceToUse() {
		if (isMatchOnlyEidMode()) {
			return myBulkExportMDMEidMatchOnlyResourceExpander;
		} else {
			return myBulkExportMDMResourceExpander;
		}
	}

	private boolean isMatchOnlyEidMode() {
		assert myMdmSettings != null;
		boolean isMatchOnly = myMdmSettings.getMode() == MdmModeEnum.MATCH_ONLY;
		boolean hasEidSystems = false;
		if (myMdmSettings.getMdmRules() != null) {
			hasEidSystems = myMdmSettings.getMdmRules().getEnterpriseEIDSystems() != null
					&& !myMdmSettings.getMdmRules().getEnterpriseEIDSystems().isEmpty();
		}
		return isMatchOnly && hasEidSystems;
	}

	private IMdmLinkExpandSvc determineLinkExpandSvsInstanceToUse() {
		if (isMatchOnlyEidMode()) {
			myMdmEidMatchOnlyLinkExpandSvc.setMyEidHelper(new EIDHelper(myFhirContext, myMdmSettings));
			return myMdmEidMatchOnlyLinkExpandSvc;
		} else {
			return myMdmLinkExpandSvc;
		}
	}

	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
		myLinkExpandSvcInstanceToUse = determineLinkExpandSvsInstanceToUse();
		myBulkExportMDMResourceExpanderInstanceToUse = determineBulkExportMDMResourceExpanderInstanceToUse();
	}
}
