package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.config.CrR4Config;
import ca.uhn.fhir.cr.r4.TestCrR4Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import io.specto.hoverfly.junit.dsl.HoverflyDsl;
import io.specto.hoverfly.junit.dsl.StubServiceBuilder;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.ClassRule;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;


@ContextConfiguration(classes = {TestCrR4Config.class, CrR4Config.class})
public abstract class BaseCrR4Test extends BaseResourceProviderR4Test implements IResourceLoader {
	protected static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	protected static final IParser ourParser = ourFhirContext.newJsonParser().setPrettyPrint(true);
	protected static final String TEST_ADDRESS = "http://test:9001/fhir";
	@ClassRule
	public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
		service(TEST_ADDRESS)
			.get("/metadata")
			.willReturn(success(getCapabilityStatement().toString(), "application/json"))
	));

	public static CapabilityStatement getCapabilityStatement() {
		CapabilityStatement metadata = new CapabilityStatement();
		metadata.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		return metadata;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourFhirContext;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	public StubServiceBuilder mockNotFound(String theResource) {
		OperationOutcome outcome = new OperationOutcome();
		outcome.getText().setStatusAsString("generated");
		outcome.getIssueFirstRep().setSeverity(OperationOutcome.IssueSeverity.ERROR).setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(theResource);

		return mockFhirRead(theResource, outcome, 404);
	}

	public StubServiceBuilder mockFhirRead(Resource theResource) {
		String resourcePath = "/" + theResource.fhirType() + "/" + theResource.getId();
		return mockFhirRead(resourcePath, theResource);
	}

	public StubServiceBuilder mockFhirRead(String thePath, Resource theResource) {
		return mockFhirRead(thePath, theResource, 200);
	}

	public StubServiceBuilder mockFhirRead(String thePath, Resource theResource, int theStatusCode) {
		return service(TEST_ADDRESS).get(thePath)
			.willReturn(HoverflyDsl.response()
				.status(theStatusCode)
				.body(ourParser.encodeResourceToString(theResource))
				.header("Content-Type", "application/json"));
	}

	public StubServiceBuilder mockFhirSearch(String thePath, String theQuery, String theValue, Resource... theResources) {
		return service(TEST_ADDRESS).get(thePath).queryParam(theQuery, theValue)
			.willReturn(success(ourParser.encodeResourceToString(makeBundle(theResources)), "application/json"));
	}

	public List<StubServiceBuilder> mockValueSet(String theId, String theUrl) {
		var valueSet = (ValueSet) read(new IdType("ValueSet", theId));
		return Arrays.asList(
			mockFhirSearch("/fhir/ValueSet", "url", String.format("%s/%s", theUrl, theId), valueSet),
			mockFhirRead(String.format("/fhir/ValueSet/%s/$expand", theId), valueSet)
		);
	}

	public StubServiceBuilder mockFhirPost(String thePath, Resource theResource) {
		return service(TEST_ADDRESS).post(thePath).body(ourParser.encodeResourceToString(theResource))
			.willReturn(success());
	}

	public Bundle makeBundle(List<? extends Resource> theResources) {
		return makeBundle(theResources.toArray(new Resource[theResources.size()]));
	}

	public Bundle makeBundle(Resource... theResources) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.SEARCHSET);
		bundle.setTotal(theResources != null ? theResources.length : 0);
		if (theResources != null) {
			for (Resource l : theResources) {
				bundle.addEntry().setResource(l).setFullUrl("/" + l.fhirType() + "/" + l.getId());
			}
		}
		return bundle;
	}

	protected RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestServer);
		requestDetails.setFhirServerBase(TEST_ADDRESS);
		return requestDetails;
	}
}
