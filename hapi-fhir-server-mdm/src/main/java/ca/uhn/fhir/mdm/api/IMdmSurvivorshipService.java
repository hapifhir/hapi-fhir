package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Service that applies survivorship rules on target and golden resources.
 */
public interface IMdmSurvivorshipService {

	/**
	 * Applies survivorship rules to merge fields from the specified target resource to the golden resource
	 *
	 * @param theTargetResource        Target resource to merge fields from
	 * @param theGoldenResource        Golden resource to merge fields into
	 * @param theMdmTransactionContext Current transaction context
	 * @param <T>                      Resource type to apply the survivorship rules to
	 */
	<T extends IBase> void applySurvivorshipRulesToGoldenResource(T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext);
}
