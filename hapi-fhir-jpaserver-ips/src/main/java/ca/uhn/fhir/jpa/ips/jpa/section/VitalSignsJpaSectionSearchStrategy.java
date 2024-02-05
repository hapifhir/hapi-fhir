package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ResourceType;

public class VitalSignsJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
			theSearchParameterMap.add(
				Observation.SP_CATEGORY,
				new TokenOrListParam()
					.addOr(new TokenParam(
						"http://terminology.hl7.org/CodeSystem/observation-category",
						"vital-signs")));
		}
	}

	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
			// code filtering not yet applied
			Observation observation = (Observation) theCandidate;
			if (observation.getStatus() == Observation.ObservationStatus.CANCELLED ||
				observation.getStatus() == Observation.ObservationStatus.ENTEREDINERROR ||
				observation.getStatus() == Observation.ObservationStatus.PRELIMINARY) {
				return false;
			}
		}

		return true;
	}
}
