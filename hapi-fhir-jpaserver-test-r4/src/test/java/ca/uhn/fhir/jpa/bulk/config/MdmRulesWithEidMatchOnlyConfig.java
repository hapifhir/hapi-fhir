package ca.uhn.fhir.jpa.bulk.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.bulk.mdm.MdmClearHelperSvcImpl;
import ca.uhn.fhir.jpa.dao.mdm.JpaMdmLinkImplFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDaoJpaImpl;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class MdmRulesWithEidMatchOnlyConfig {
	public static final  String TEST_PATIENT_EID_SYS = "http://patient-eid-sys";

	@Bean
	@Primary //Override any random test mdm settings that are setup above.
	public IMdmSettings mdmSettings(IMdmRuleValidator theMdmRuleValidator) {
		MdmSettings mdmSettings = new MdmSettings(theMdmRuleValidator);
		mdmSettings.setEnabled(true);
		mdmSettings.setMdmMode(MdmModeEnum.MATCH_ONLY);
		MdmRulesJson rules = new MdmRulesJson();
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystem("Patient", TEST_PATIENT_EID_SYS);
		mdmSettings.setMdmRules(rules);
		return mdmSettings;
	}

	@Bean
	public MdmSearchExpansionSvc mdmSearchExpansionSvc() {
		return new MdmSearchExpansionSvc();
	}

	@Bean
	public IMdmLinkDao<JpaPid, MdmLink> mdmLinkDao() {
		return new MdmLinkDaoJpaImpl();
	}

	@Bean
	public IMdmLinkImplFactory<MdmLink> mdmLinkImplFactory() {
		return new JpaMdmLinkImplFactory();
	}

	@Bean
	public IMdmLinkExpandSvc mdmLinkExpandSvc(
			EIDHelper theEidHelper,
			IMdmSettings theMdmSettings,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IIdHelperService theIdHelperService) {
		return new MdmEidMatchOnlyExpandSvc(theDaoRegistry, theFhirContext, theIdHelperService, theEidHelper);
	}

	@Bean
	public EIDHelper eidHelper(FhirContext theFhirContext, IMdmSettings theMdmSettings) {
		return new EIDHelper(theFhirContext, theMdmSettings);
	}

	@Bean
	public IMdmClearHelperSvc<JpaPid> mdmClearHelperSvc(IDeleteExpungeSvc<JpaPid> theDeleteExpungeSvc) {
		return new MdmClearHelperSvcImpl(theDeleteExpungeSvc);
	}
}
