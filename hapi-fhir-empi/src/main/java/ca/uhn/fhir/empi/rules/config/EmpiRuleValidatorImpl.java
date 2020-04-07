package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class EmpiRuleValidatorImpl implements IEmpiRuleValidator {

	@Override
	public void validate(EmpiRulesJson theEmpiRulesJson) {
		validateSystemIsUri(theEmpiRulesJson);
	}

	private void validateSystemIsUri(EmpiRulesJson theEmpiRulesJson) {
		try {
			new URI(theEmpiRulesJson.getEnterpriseEIDSystem()).parseServerAuthority();
		} catch (URISyntaxException e) {
			throw new ConfigurationException("Enterprise Identifier System (eidSystem) must be a valid URI");
		}
	}
}
