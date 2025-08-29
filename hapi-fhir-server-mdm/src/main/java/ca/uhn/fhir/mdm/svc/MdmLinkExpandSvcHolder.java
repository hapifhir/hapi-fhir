package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmLinkExpandSvcHolder {

	private static volatile IMdmLinkExpandSvc instance;

	private IMdmSettings myMdmSettings;

	@Autowired
	private IMdmLinkDao myMdmLinkDao;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	public MdmLinkExpandSvcHolder() {}

	public IMdmLinkExpandSvc getInstance() {
		if (instance == null) {
			synchronized (MdmLinkExpandSvcHolder.class) {
				if (instance == null) {
					instance = createInstance();
				}
			}
		}
		return instance;
	}

	private IMdmLinkExpandSvc createInstance() {
		if (myMdmSettings == null) {
			throw new IllegalStateException("IMdmSettings must be set before calling getInstance()");
		}
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

	public void setMyMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
	}
}
