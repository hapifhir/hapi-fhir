package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
@ExtendWith(MockitoExtension.class)
class MdmLoaderTest {
	@Mock
	IMdmSettings myMdmSettings;

	@Mock
	MdmProviderLoader myMdmProviderLoader;

	@Mock
	MdmSubscriptionLoader myMdmSubscriptionLoader;

	@Mock
	MdmRuleValidator myMdmRuleValidator;

	@InjectMocks
	MdmLoader myMdmLoader;

	/**
	 * Custom algorithms register in @PostConstruct, so their names can only be checked once the context is
	 * refreshed. The match only rules can name them as well as the MDM rules can.
	 */
	@Test
	void updateSubscriptions_matchOnlyRulesConfigured_validatesTheirAlgorithmRegistrations() {
		MdmRulesJson rules = new MdmRulesJson();
		MdmRulesJson matchOnlyRules = new MdmRulesJson();
		when(myMdmSettings.isEnabled()).thenReturn(true);
		when(myMdmSettings.getMode()).thenReturn(MdmModeEnum.MATCH_ONLY);
		when(myMdmSettings.getMdmRules()).thenReturn(rules);
		when(myMdmSettings.getMatchOnlyMdmRules()).thenReturn(matchOnlyRules);

		myMdmLoader.updateSubscriptions();

		verify(myMdmRuleValidator).validateAlgorithmRegistrations(rules);
		verify(myMdmRuleValidator).validateAlgorithmRegistrations(matchOnlyRules);
	}
}
