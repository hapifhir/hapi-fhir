package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpiMessageFilteringSvc {

	@Autowired
	private IEmpiSettings empiSettings;

	@Autowired
	EmpiSearchParamSvc myEmpiSearchParamSvc;

	public boolean shouldBeProcessed(ResourceModifiedMessage theMsg) {
		List<EmpiResourceSearchParamJson> candidateSearchParams = empiSettings.getEmpiRules().getCandidateSearchParams();
		List<String> valuesFromResourceForSearchParam = myEmpiSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam);

	}
}
