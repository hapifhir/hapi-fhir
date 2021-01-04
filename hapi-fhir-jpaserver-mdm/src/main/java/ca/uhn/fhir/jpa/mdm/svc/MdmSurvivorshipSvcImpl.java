package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IBase;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {

	@Override
	public <T extends IBase> void applySurvivorshipRulesToGoldenResource(T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		// survivorship logic placeholder
	}
}
