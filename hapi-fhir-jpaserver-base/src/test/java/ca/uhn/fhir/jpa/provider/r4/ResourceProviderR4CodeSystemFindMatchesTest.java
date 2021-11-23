package ca.uhn.fhir.jpa.provider.r4;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceProviderR4CodeSystemFindMatchesTest extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemFindMatchesTest.class);
	private static final String CS_ACME_URL = "http://acme.org";
	private IIdType myCodeSystemId;

	@BeforeEach
	@Transactional
	public void before_loadCodeSystem() throws IOException {
		String input = IOUtils.toString(getClass().getResource("/r4/codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirCtx.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemId = myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void operationFindMatches_findYieldsResults_Success() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("find-matches")
			.withParameter(Parameters.class, "system", new UriType(CS_ACME_URL))
			.execute();

		String resp = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		// FIXME KBD - Figure out how to properly test the output of this method before finalizing this implementation!
		assertNotNull(resp);
		// List<Parameters.ParametersParameterComponent> parameterList = respParam.getParameter();
		// assertEquals(1, parameterList.size());

	}
}
