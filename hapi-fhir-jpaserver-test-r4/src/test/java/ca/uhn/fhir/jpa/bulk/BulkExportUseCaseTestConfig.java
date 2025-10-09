package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static ca.uhn.fhir.jpa.bulk.BulkExportUseCaseTest.TEST_PATIENT_EID_SYS;

public class BulkExportUseCaseTestConfig {

	@Autowired
	private IMdmRuleValidator myMdmRulesValidator;

	@Bean
	public IMdmSettings mdmSettings() {
		MdmSettings mdmSettings = new MdmSettings(myMdmRulesValidator);
		mdmSettings.setEnabled(true);
		mdmSettings.setMdmMode(MdmModeEnum.MATCH_ONLY);
		MdmRulesJson rules = new MdmRulesJson();
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystem("Patient", TEST_PATIENT_EID_SYS);
		mdmSettings.setMdmRules(rules);
		return mdmSettings;
	}

	@Bean
	public IMdmRuleValidator mdmRuleValidator(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRetriever) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever);
	}
}
