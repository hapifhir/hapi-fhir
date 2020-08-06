package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class EmpiResourceFilteringSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiSettings empiSettings;
	@Autowired
	EmpiSearchParamSvc myEmpiSearchParamSvc;
	@Autowired
	FhirContext myFhirContext;

	/**
	 * Given a message from the EMPI Channel, determine whether or not EMPI processing should occur on the payload.
	 * EMPI processing should occur iff for any {@link EmpiResourceSearchParamJson) Search Param, the resource contains a value.
	 *
	 * If the payload has no attributes that appear in the Candidate Search Params, processing should be skipped, as there is not
	 * sufficient information to perform meaningful EMPI processing. (For example, how can EMPI processing occur on a patient that has _no_ attributes?)
	 *
	 * @param theResource the resource that you wish to check against EMPI rules.
	 *
	 * @return whether or not EMPI processing should proceed
	 */
	public boolean shouldBeProcessed(IAnyResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		List<EmpiResourceSearchParamJson> candidateSearchParams = empiSettings.getEmpiRules().getCandidateSearchParams();

		boolean containsValueForSomeSearchParam = candidateSearchParams.stream()
			.filter(csp -> myEmpiSearchParamSvc.searchParamTypeIsValidForResourceType(csp.getResourceType(), resourceType))
			.flatMap(csp -> csp.getSearchParams().stream())
			.map(searchParam -> myEmpiSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam))
			.anyMatch(valueList -> !valueList.isEmpty());

		ourLog.debug("Is {} suitable for EMPI processing? : {}", theResource.getId(), containsValueForSomeSearchParam);
		return containsValueForSomeSearchParam;
	}
}
