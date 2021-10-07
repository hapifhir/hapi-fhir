package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.annotation.Nullable;

public interface IMdmLinkCreateSvc {
	IAnyResource createLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchResultEnum theMatchResult, MdmTransactionContext theMdmContext);
}
