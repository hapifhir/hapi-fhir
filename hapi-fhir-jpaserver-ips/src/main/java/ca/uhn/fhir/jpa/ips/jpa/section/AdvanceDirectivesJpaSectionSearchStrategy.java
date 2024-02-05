package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.ResourceType;

public class AdvanceDirectivesJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (ResourceType.Consent.name().equals(theIpsSectionContext.getResourceType())) {
			theSearchParameterMap.add(
				Consent.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						Consent.ConsentState.ACTIVE.getSystem(),
						Consent.ConsentState.ACTIVE.toCode())));
		}
	}

}
