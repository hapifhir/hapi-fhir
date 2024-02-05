package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.ResourceType;

public class AllergyIntoleranceJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.AllergyIntolerance.name())) {
			AllergyIntolerance allergyIntolerance = (AllergyIntolerance) theCandidate;
			if (allergyIntolerance
				.getClinicalStatus()
				.hasCoding(
					"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical",
					"inactive") ||
				allergyIntolerance
					.getClinicalStatus()
					.hasCoding(
						"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical",
						"resolved") ||
				allergyIntolerance
					.getVerificationStatus()
					.hasCoding(
						"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification",
						"entered-in-error")) {
				return false;
			}
		}

		return true;
	}
}
