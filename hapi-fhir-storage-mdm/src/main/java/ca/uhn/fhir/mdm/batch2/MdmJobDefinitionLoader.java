package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitJobParameters;

public class MdmJobDefinitionLoader {
	public MdmJobDefinitionLoader(JobDefinitionRegistry theJobDefinitionRegistry,
											JobDefinition<MdmClearJobParameters> theClearJobDefinition,
											JobDefinition<MdmSubmitJobParameters> theSubmitJobDefinition) {

		theJobDefinitionRegistry.addJobDefinitionIfNotRegistered(theClearJobDefinition);
		theJobDefinitionRegistry.addJobDefinitionIfNotRegistered(theSubmitJobDefinition);
	}
}
