package ca.uhn.fhir.mdm.api;

import java.util.List;

public interface IMdmClearSvc {
	void removeLinkAndGoldenResources(List<Long> thePidList);
}
