package ca.uhn.fhir.cr;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.helper.ResourceLoader;
import ca.uhn.fhir.cr.config.CrDstu3Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.parser.IParser;
import io.specto.hoverfly.junit.dsl.HoverflyDsl;
import io.specto.hoverfly.junit.dsl.StubServiceBuilder;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

@ContextConfiguration(classes = { TestCrConfig.class, CrDstu3Config.class })
public class CrDstu3Test extends BaseJpaDstu3Test implements ResourceLoader {
	protected static final FhirContext ourFhirContext = FhirContext.forDstu3Cached();
	private static IParser parser = ourFhirContext.newJsonParser().setPrettyPrint(true);
	private static String hoverfly_address = "test-address.com";

	@Autowired
	protected DaoRegistry daoRegistry;

	@Override
	public DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourFhirContext;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	@ClassRule
	public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
		service(hoverfly_address)
			.get("/fhir/metadata")
			.willReturn(success(getCapabilityStatement().toString(), "application/json"))
	));

	public IParser getFhirParser() {
		return parser;
	}

	public StubServiceBuilder mockNotFound(String resource) {
		OperationOutcome outcome = new OperationOutcome();
		outcome.getText().setStatusAsString("generated");
		outcome.getIssueFirstRep().setSeverity(OperationOutcome.IssueSeverity.ERROR).setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(resource);

		return mockFhirRead(resource, outcome, 404);
	}

	public StubServiceBuilder mockFhirRead(Resource resource) {
		String resourcePath = "/" + resource.fhirType() + "/" + resource.getId();
		return mockFhirRead(resourcePath, resource);
	}

	public StubServiceBuilder mockFhirRead(String path, Resource resource) {
		return mockFhirRead(path, resource, 200);
	}

	public StubServiceBuilder mockFhirRead(String path, Resource resource, int statusCode) {
		return service(hoverfly_address).get(path)
			.willReturn(HoverflyDsl.response()
				.status(statusCode)
				.body(parser.encodeResourceToString(resource))
				.header("Content-Type", "application/json"));
	}

	public StubServiceBuilder mockFhirSearch(String path, String query, String value, Resource... resources) {
		return service(hoverfly_address).get(path).queryParam(query, value)
			.willReturn(success(parser.encodeResourceToString(makeBundle(resources)), "application/json"));
	}

	public List<StubServiceBuilder> mockValueSet(String theId, String theUrl) {
		var valueSet = (ValueSet) read(new IdType("ValueSet", theId));
		return Arrays.asList(
			mockFhirSearch("/fhir/ValueSet", "url", String.format("%s/%s", theUrl, theId), valueSet),
			mockFhirRead(String.format("/fhir/ValueSet/%s/$expand", theId), valueSet)
		);
	}

	public StubServiceBuilder mockFhirPost(String path, Resource resource) {
		return service(hoverfly_address).post(path).body(parser.encodeResourceToString(resource))
			.willReturn(success());
	}

	public static CapabilityStatement getCapabilityStatement() {
		CapabilityStatement metadata = new CapabilityStatement();
		metadata.setFhirVersion("3.0.2");
		return metadata;
	}

	public Bundle makeBundle(List<? extends Resource> resources) {
		return makeBundle(resources.toArray(new Resource[resources.size()]));
	}

	public Bundle makeBundle(Resource... resources) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.SEARCHSET);
		bundle.setTotal(resources != null ? resources.length : 0);
		if (resources != null) {
			for (Resource l : resources) {
				bundle.addEntry().setResource(l).setFullUrl("/" + l.fhirType() + "/" + l.getId());
			}
		}
		return bundle;
	}
}
