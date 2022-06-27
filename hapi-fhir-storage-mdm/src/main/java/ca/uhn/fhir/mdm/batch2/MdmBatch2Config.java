package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

import static ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx.MDM_CLEAR_JOB_BEAN_NAME;

@Configuration
@Import({MdmClearAppCtx.class})
public class MdmBatch2Config {
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;

	@Autowired
	ApplicationContext myApplicationContext;

	@PostConstruct
	public void start() {
		JobDefinition jobDefinition = myApplicationContext.getBean(MDM_CLEAR_JOB_BEAN_NAME, JobDefinition.class);
		myJobDefinitionRegistry.addJobDefinitionIfNotRegistered(jobDefinition);
	}
}
