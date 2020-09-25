package ca.uhn.fhir.jpa.provider.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProviderR5CodeSystemTest extends BaseResourceProviderR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5CodeSystemTest.class);

	@Test
	public void testValidateCodeWithUrlAndVersion_v1() {
		
		String url = "http://url";
		createCodeSystem(url, "v1", "1", "Code v1 display");
		createCodeSystem(url, "v2", "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("version").setValue(new StringType("v1"));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));
		inParams.addParameter().setName("display").setValue(new StringType("Code v1 display"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v1 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	private void createCodeSystem(String url, String version, String code, String display) {
		
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(url).setVersion(version);

		ConceptDefinitionComponent concept1 = codeSystem.addConcept();
		concept1.setCode("1000").setDisplay("Code Dispaly 1000");

		ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode(code).setDisplay(display);

		ConceptDefinitionComponent concept2 = codeSystem.addConcept();
		concept2.setCode("2000").setDisplay("Code Dispaly 2000");

		ourLog.info("CodeSystem: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
		
		myCodeSystemDao.create(codeSystem, mySrd);
	}
}
