package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestCrConfig.class})
public abstract class BaseCrTest {
	public static final String PLAN_DEFINITION_RESOURCE_NAME = "PlanDefinition";
	protected static final String TEST_ADDRESS = "http://test:8000/fhir";

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected CdsCrSettings myCdsCrSettings;
}
