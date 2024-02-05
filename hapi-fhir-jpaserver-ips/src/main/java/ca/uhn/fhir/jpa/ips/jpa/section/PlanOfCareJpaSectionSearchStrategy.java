package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ResourceType;

public class PlanOfCareJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.CarePlan.name())) {
			theSearchParameterMap.add(
				CarePlan.SP_STATUS,
				new TokenOrListParam()
					.addOr(new TokenParam(
						CarePlan.CarePlanStatus.ACTIVE.getSystem(),
						CarePlan.CarePlanStatus.ACTIVE.toCode()))
					.addOr(new TokenParam(
						CarePlan.CarePlanStatus.ONHOLD.getSystem(),
						CarePlan.CarePlanStatus.ONHOLD.toCode()))
					.addOr(new TokenParam(
						CarePlan.CarePlanStatus.UNKNOWN.getSystem(),
						CarePlan.CarePlanStatus.UNKNOWN.toCode())));
		}
	}

}
