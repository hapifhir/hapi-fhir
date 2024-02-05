package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.ResourceType;

public class ImmunizationsJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (ResourceType.Immunization.name().equals(theIpsSectionContext.getResourceType())) {
			theSearchParameterMap.setSort(new SortSpec(Immunization.SP_DATE).setOrder(SortOrderEnum.DESC));
			theSearchParameterMap.addInclude(Immunization.INCLUDE_MANUFACTURER);
		}
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Immunization.name())) {
			Immunization immunization = (Immunization) theCandidate;
			if (immunization.getStatus() == Immunization.ImmunizationStatus.ENTEREDINERROR) {
				return false;
			}
		}

		return true;
	}
}
