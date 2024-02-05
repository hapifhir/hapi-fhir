package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ResourceType;

public class IllnessHistoryJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Condition.name())) {
			Condition prob = (Condition) theCandidate;
			if (prob.getVerificationStatus()
				.hasCoding(
					"http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
				return false;
			}

			if (prob.getClinicalStatus()
				.hasCoding(
					"http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive") ||
				prob.getClinicalStatus()
					.hasCoding(
						"http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved") ||
				prob.getClinicalStatus()
					.hasCoding(
						"http://terminology.hl7.org/CodeSystem/condition-clinical", "remission")) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}
}
