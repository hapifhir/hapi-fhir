package ca.uhn.fhir.jpa.provider.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

public class ResourceProviderR5CodeSystemDesignationTest extends BaseResourceProviderR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR5CodeSystemDesignationTest.class);

	private static final String CS_ACME_URL = "http://acme.org";

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs-with-designations-lang.xml");
		myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();
	}
	
	@Test
	public void testLookupWithDisplayLanguage() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8494-7"))
			.andParameter("system", new UriType(CS_ACME_URL))
			.andParameter("displayLanguage",new CodeType("de-AT"))
			.execute();

		String resp = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		
		assertEquals("display", respParam.getParameter().get(0).getName());
		assertEquals(("Systolic blood pressure 12 hour minimum"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		
		assertEquals("abstract", respParam.getParameter().get(1).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(1).getValue()).getValue());

		//-- designationList
		assertEquals(2, designationList.size());
		
		// 1. de-AT:Systolic blood pressure 12 hour minimum
		ParametersParameterComponent designation = designationList.get(0);
		assertEquals("language", designation.getPart().get(0).getName());
		assertEquals("de-AT", designation.getPart().get(0).getValue().toString());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("de-AT:Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

		// 2. Systolic blood pressure 12 hour minimum (no language)
		designation = designationList.get(1);
		assertEquals("language", designation.getPart().get(0).getName());
		assertNull(designation.getPart().get(0).getValue());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

	}

	
	@Test
	public void testLookupWithNonExistLanguage() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8494-7"))
			.andParameter("system", new UriType(CS_ACME_URL))
			.andParameter("displayLanguage",new CodeType("zh-CN"))
			.execute();

		String resp = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		
		assertEquals("display", respParam.getParameter().get(0).getName());
		assertEquals(("Systolic blood pressure 12 hour minimum"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		
		assertEquals("abstract", respParam.getParameter().get(1).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(1).getValue()).getValue());

		//-- designationList
		assertEquals(1, designationList.size());
		
		// 1. Systolic blood pressure 12 hour minimum (no language)
		ParametersParameterComponent designation = designationList.get(0);
		assertEquals("language", designation.getPart().get(0).getName());
		assertNull(designation.getPart().get(0).getValue());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

	}
	
	@Test
	public void testLookupWithoutDisplayLanguage() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8494-7"))
			.andParameter("system", new UriType(CS_ACME_URL))
			.execute();

		String resp = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		
		assertEquals("display", respParam.getParameter().get(0).getName());
		assertEquals(("Systolic blood pressure 12 hour minimum"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		
		assertEquals("abstract", respParam.getParameter().get(1).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(1).getValue()).getValue());

		//-- designationList
		assertEquals(3, designationList.size());
		
		// 1. fr-FR:Systolic blood pressure 12 hour minimum
		ParametersParameterComponent designation = designationList.get(0);
		assertEquals("language", designation.getPart().get(0).getName());
		assertEquals("fr-FR", designation.getPart().get(0).getValue().toString());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("fr-FR:Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

		// 2. de-AT:Systolic blood pressure 12 hour minimum
		designation = designationList.get(1);
		assertEquals("language", designation.getPart().get(0).getName());
		assertEquals("de-AT", designation.getPart().get(0).getValue().toString());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("de-AT:Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

		// 3. Systolic blood pressure 12 hour minimum (no language)
		designation = designationList.get(2);
		assertEquals("language", designation.getPart().get(0).getName());
		assertNull(designation.getPart().get(0).getValue());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());

	}
	private List<ParametersParameterComponent> getDesignations(List<ParametersParameterComponent> parameterList) {
		
		List<ParametersParameterComponent> designationList = new ArrayList<>();

		for (ParametersParameterComponent parameter : parameterList) {
			if ("designation".equals(parameter.getName()))
				designationList.add(parameter);
		}
		return designationList;
		
	}
}
