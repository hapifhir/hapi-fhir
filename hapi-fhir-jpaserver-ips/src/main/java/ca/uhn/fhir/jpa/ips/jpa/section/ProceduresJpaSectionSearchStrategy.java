package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.ResourceType;

public class ProceduresJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Procedure.name())) {
			Procedure proc = (Procedure) theCandidate;
			if (proc.getStatus() == Procedure.ProcedureStatus.ENTEREDINERROR ||
				proc.getStatus() == Procedure.ProcedureStatus.NOTDONE) {
				return false;
			}
		}

		return true;
	}
}
