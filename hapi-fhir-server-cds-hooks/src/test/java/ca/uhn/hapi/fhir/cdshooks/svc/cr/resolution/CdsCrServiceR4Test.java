package ca.uhn.hapi.fhir.cdshooks.svc.cr.resolution;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.BaseCrTest;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceR4;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.codesystems.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.repository.InMemoryFhirRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CdsCrServiceR4Test extends BaseCrTest {
	private ObjectMapper myObjectMapper;
	private ICdsConfigService myCdsConfigService;

	@BeforeEach
	public void loadJson() throws IOException {
		myObjectMapper = new CdsHooksObjectMapperFactory(myFhirContext).newMapper();
		myCdsConfigService = new CdsConfigServiceImpl(myFhirContext, myObjectMapper, myCdsCrSettings, null, null, null);
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
		final Parameters params = new CdsCrServiceR4(requestDetails, repository, myCdsConfigService).encodeParams(cdsServiceRequestJson);

		assertEquals(2, params.getParameter().size());
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
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsCrServiceR4(requestDetails, repository, myCdsConfigService).encodeResponse(responseBundle);

		assertEquals(1, cdsServiceResponseJson.getCards().size());
		assertFalse(cdsServiceResponseJson.getCards().get(0).getSummary().isEmpty());
		assertFalse(cdsServiceResponseJson.getCards().get(0).getDetail().isEmpty());
	}

	@Test
	@Disabled // Disabled until the CDS on FHIR specification details how to map system actions.
	public void testSystemActionResponse() {
		final Bundle bundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-DischargeInstructionsPlan-Content.json");
		final Repository repository = new InMemoryFhirRepository(myFhirContext, bundle);
		final Bundle responseBundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "Bundle-DischargeInstructionsPlan-Response.json");
		final RequestDetails requestDetails = new SystemRequestDetails();
		final IdType planDefinitionId = new IdType(PLAN_DEFINITION_RESOURCE_NAME, "DischargeInstructionsPlan");
		requestDetails.setId(planDefinitionId);
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsCrServiceR4(requestDetails, repository, myCdsConfigService).encodeResponse(responseBundle);

		assertEquals(1, cdsServiceResponseJson.getServiceActions().size());
		assertEquals(ActionType.CREATE, cdsServiceResponseJson.getServiceActions().get(0).getType());
		assertNotNull(cdsServiceResponseJson.getServiceActions().get(0).getResource());
	}
}
