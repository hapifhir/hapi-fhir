package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx.MDM_CLEAR_JOB_BEAN_NAME;
import static ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx.MDM_SUBMIT_JOB_BEAN_NAME;

public class MdmJobDefinitionLoader implements ApplicationContextAware {
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private ApplicationContext myApplicationContext;

	public MdmJobDefinitionLoader(JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		addJobDefinitions();
	}

	private void addJobDefinitions() {
		JobDefinition clearJobDefinition = myApplicationContext.getBean(MDM_CLEAR_JOB_BEAN_NAME, JobDefinition.class);
		myJobDefinitionRegistry.addJobDefinitionIfNotRegistered(clearJobDefinition);
		JobDefinition submitJobDefinition = myApplicationContext.getBean(MDM_SUBMIT_JOB_BEAN_NAME, JobDefinition.class);
		myJobDefinitionRegistry.addJobDefinitionIfNotRegistered(submitJobDefinition);
	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myApplicationContext = theApplicationContext;
	}
}
