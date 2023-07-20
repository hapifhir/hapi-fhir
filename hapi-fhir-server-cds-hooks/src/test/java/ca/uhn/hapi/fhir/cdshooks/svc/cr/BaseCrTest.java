package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IResourceLoader;
import ca.uhn.fhir.cr.config.BaseRepositoryConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestCrConfig.class})
public abstract class BaseCrTest extends BaseResourceProviderR4Test implements IResourceLoader {
	public static final String PLAN_DEFINITION_RESOURCE_NAME = "PlanDefinition";
	protected static final String TEST_ADDRESS = "http://test:9001/fhir";

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	protected RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestServer);
		requestDetails.setFhirServerBase(TEST_ADDRESS);
		return requestDetails;
	}

}
