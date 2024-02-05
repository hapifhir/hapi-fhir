package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.ResourceType;

public class MedicalDevicesJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (ResourceType.DeviceUseStatement.name().equals(theIpsSectionContext.getResourceType())) {
			theSearchParameterMap.addInclude(DeviceUseStatement.INCLUDE_DEVICE);
		}
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.DeviceUseStatement.name())) {
			DeviceUseStatement deviceUseStatement = (DeviceUseStatement) theCandidate;
			if (deviceUseStatement.getStatus() == DeviceUseStatement.DeviceUseStatementStatus.ENTEREDINERROR) {
				return false;
			}
		}
		return true;
	}
}
