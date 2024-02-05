package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.ResourceType;

public class FunctionalStatusJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.ClinicalImpression.name())) {
			ClinicalImpression clinicalImpression = (ClinicalImpression) theCandidate;
			if (clinicalImpression.getStatus() == ClinicalImpression.ClinicalImpressionStatus.INPROGRESS ||
				clinicalImpression.getStatus() == ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR) {
				return false;
			}
		}
		return true;
	}
}
