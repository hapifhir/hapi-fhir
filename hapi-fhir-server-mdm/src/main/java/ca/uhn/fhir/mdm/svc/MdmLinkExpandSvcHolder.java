package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.util.EIDHelper;

public class MdmLinkExpandSvcHolder {

	private IMdmLinkExpandSvc myInstance;

	private IMdmSettings myMdmSettings;

	private final EIDHelper myEidHelper;

	private final IMdmLinkExpandSvc myMdmLinkExpandSvc;

	private final MdmEidMatchOnlyLinkExpandSvc myMdmEidMatchOnlyLinkExpandSvc;

	public MdmLinkExpandSvcHolder(
			IMdmSettings theMdmSettings,
			IMdmLinkExpandSvc theMdmLinkExpandSvc,
			MdmEidMatchOnlyLinkExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			EIDHelper theEidHelper) {
		myMdmSettings = theMdmSettings;
		myMdmLinkExpandSvc = theMdmLinkExpandSvc;
		myMdmEidMatchOnlyLinkExpandSvc = theMdmEidMatchOnlyLinkExpandSvc;
		myEidHelper = theEidHelper;
	}

	public IMdmLinkExpandSvc getInstance() {
		if (myInstance != null) {
			return myInstance;
		}

		myInstance = determineInstanceToUse();

		return myInstance;
	}

	private IMdmLinkExpandSvc determineInstanceToUse() {
		boolean isMatchOnly = myMdmSettings.getMode() == MdmModeEnum.MATCH_ONLY;
		boolean hasEidSystems = false;
		if (myMdmSettings.getMdmRules() != null) {
			hasEidSystems = myMdmSettings.getMdmRules().getEnterpriseEIDSystems() != null
					&& !myMdmSettings.getMdmRules().getEnterpriseEIDSystems().isEmpty();
		}
		if (isMatchOnly && hasEidSystems) {
			return myMdmEidMatchOnlyLinkExpandSvc;
		} else {
			return myMdmLinkExpandSvc;
		}
	}

	/*
		private IMdmLinkExpandSvc createInstance() {

			boolean isMatchOnly = myMdmSettings.getMode() == MdmModeEnum.MATCH_ONLY;
			boolean hasEidSystems = false;
			if (myMdmSettings.getMdmRules() != null) {
				hasEidSystems = myMdmSettings.getMdmRules().getEnterpriseEIDSystems() != null
						&& !myMdmSettings.getMdmRules().getEnterpriseEIDSystems().isEmpty();
			}
			if (isMatchOnly && hasEidSystems) {
				EIDHelper myEidHelper = new EIDHelper(myFhirContext, myMdmSettings);
				return new MdmEidMatchOnlyLinkExpandSvc(myDaoRegistry, myEidHelper);
			} else {
				return new MdmLinkExpandSvc(myMdmLinkDao, myIdHelperService);
			}
		}

	*/
	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
		myEidHelper.setMdmSettings(myMdmSettings);
		myInstance = determineInstanceToUse();
	}
}
