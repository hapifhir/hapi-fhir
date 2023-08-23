package ca.uhn.hapi.fhir.cdshooks.svc.cr.resolution;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.BaseCrTest;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceR4;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.cql.evaluator.fhir.repository.InMemoryFhirRepository;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CdsCrServiceR4Test extends BaseCrTest {
	private ObjectMapper myObjectMapper;@BeforeEach
	public void loadJson() throws IOException {
		myObjectMapper = new CdsHooksObjectMapperFactory(myFhirContext).newMapper();
	}

	@Test
	public void testR4Params() throws IOException {
		final String rawRequest = ClasspathUtil.loadResource("ASLPCrdServiceRequest.json");
		final CdsServiceRequestJson cdsServiceRequestJson = myObjectMapper.readValue(rawRequest, CdsServiceRequestJson.class);
		final Bundle bundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-ASLPCrd-Content.json");
		final Repository repository = new InMemoryFhirRepository(myFhirContext, bundle);
		final RequestDetails requestDetails = new SystemRequestDetails();
		final IdType planDefinitionId = new IdType(PLAN_DEFINITION_RESOURCE_NAME, "ASLPCrd");
		requestDetails.setId(planDefinitionId);
		final Parameters params = new CdsCrServiceR4(requestDetails, repository).encodeParams(cdsServiceRequestJson);

		assertTrue(params.getParameter().size() == 3);
		assertTrue(params.getParameter("parameters").hasResource());
	}

	@Test
	public void testR4Response() {
		final Bundle bundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-ASLPCrd-Content.json");
		final Repository repository = new InMemoryFhirRepository(myFhirContext, bundle);
		final Bundle responseBundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-ASLPCrd-Response.json");
		final RequestDetails requestDetails = new SystemRequestDetails();
		final IdType planDefinitionId = new IdType(PLAN_DEFINITION_RESOURCE_NAME, "ASLPCrd");
		requestDetails.setId(planDefinitionId);
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsCrServiceR4(requestDetails, repository).encodeResponse(responseBundle);

		assertTrue(cdsServiceResponseJson.getCards().size() == 1);
		assertTrue(!cdsServiceResponseJson.getCards().get(0).getSummary().isEmpty());
		assertTrue(!cdsServiceResponseJson.getCards().get(0).getDetail().isEmpty());
	}
}
