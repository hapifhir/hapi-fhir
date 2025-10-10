package ca.uhn.fhir.jpa.bulk.config;

import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MdmRulesWithEidMatchOnlyConfig {
	public static final  String TEST_PATIENT_EID_SYS = "http://patient-eid-sys";

	@Bean
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
}
