package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.BaseCrTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscoveryResolutionR4Test extends BaseCrTest {
	@Test
	public void testR4DiscoveryResolution() throws JsonProcessingException {
		loadBundle("Bundle-HelloWorldContent.json");

		final var planDefinition = (PlanDefinition) read(new IdType(PLAN_DEFINITION_RESOURCE_NAME, "hello-world-patient-view"));
		final var cdsServiceJson = new DiscoveryResolutionR4(myDaoRegistry).resolveService(planDefinition);
		final var objectMapper = new CdsHooksObjectMapperFactory(myFhirContext).newMapper();
		// execute
		final String actual = objectMapper.writeValueAsString(cdsServiceJson);
		final String expected = "{\n" +
			"  \"hook\" : \"patient-view\",\n" +
			"  \"title\" : \"Hello World (patient-view)\",\n" +
			"  \"description\" : \"This PlanDefinition defines a simple Hello World recommendation that triggers on patient-view.\",\n" +
			"  \"id\" : \"hello-world-patient-view\",\n" +
			"  \"prefetch\" : {\n" +
			"    \"item1\" : \"Patient?_id=Patient/{{context.patientId}}\"\n" +
			"  }\n" +
			"}";
		assertEquals(expected, actual);
	}

}
