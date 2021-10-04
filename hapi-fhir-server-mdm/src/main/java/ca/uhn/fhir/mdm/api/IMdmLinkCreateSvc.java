package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IMdmLinkCreateSvc {
	IAnyResource createLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmTransactionContext theMdmContext);
}
