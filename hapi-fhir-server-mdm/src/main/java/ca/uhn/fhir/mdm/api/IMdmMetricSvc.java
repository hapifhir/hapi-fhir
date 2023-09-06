package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.parameters.MdmGenerateMetricParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IMdmMetricSvc {

	/**
	 * Generates the metrics for the provided parameters.
	 * This implementation is persistence dependent.
	 */
	MdmMetrics generateMetrics(MdmGenerateMetricParameters theParameters);
}
