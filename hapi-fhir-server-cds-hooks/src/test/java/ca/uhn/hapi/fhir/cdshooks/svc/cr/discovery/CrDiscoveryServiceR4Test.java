package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.BaseCrTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.repository.InMemoryFhirRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrDiscoveryServiceR4Test extends BaseCrTest {
	@Test
	public void testR4DiscoveryService() throws JsonProcessingException {
		Bundle bundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-ASLPCrd-Content.json");
		Repository repository = new InMemoryFhirRepository(myFhirContext, bundle);

		final IdType planDefinitionId = new IdType(PLAN_DEFINITION_RESOURCE_NAME, "ASLPCrd");
		final CdsServiceJson cdsServiceJson = new CrDiscoveryServiceR4(planDefinitionId, repository).resolveService();
		final ObjectMapper objectMapper = new CdsHooksObjectMapperFactory(myFhirContext).newMapper();
		// execute
		final String actual = objectMapper.writeValueAsString(cdsServiceJson);
		final String expected = "{\n" +
			"  \"hook\" : \"order-sign\",\n" +
			"  \"title\" : \"ASLPCrd Workflow\",\n" +
			"  \"description\" : \"An example workflow for the CRD step of DaVinci Burden Reduction.\",\n" +
			"  \"id\" : \"ASLPCrd\",\n" +
			"  \"prefetch\" : {\n" +
			"    \"item1\" : \"Patient?_id=Patient/{{context.patientId}}\",\n" +
			"    \"item2\" : \"ServiceRequest?patient=Patient/{{context.patientId}}\",\n" +
			"    \"item3\" : \"Condition?patient=Patient/{{context.patientId}}&code=http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes|ASLP.A1.DE19\",\n" +
			"    \"item4\" : \"Condition?patient=Patient/{{context.patientId}}&code=http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes|ASLP.A1.DE18\",\n" +
			"    \"item5\" : \"Observation?subject=Patient/{{context.patientId}}&code=http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes|ASLP.A1.DE19\"\n" +
			"  }\n" +
			"}";
		assertEquals(expected, actual);
	}

}
