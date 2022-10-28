package ca.uhn.fhir.jpa.provider.r4;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceProviderR4CodeSystemDesignationTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemDesignationTest.class);

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

		String resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have de-AT and default language
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		// should be de-AT and default
		assertEquals(2, designationList.size()); 
		verifyDesignationDeAT(designationList.get(0));
		verifyDesignationNoLanguage(designationList.get(1));
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

		String resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have default language only
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		// should be default only
		assertEquals(1, designationList.size()); 
		verifyDesignationNoLanguage(designationList.get(0));

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

		String resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have all languages and the default language
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		// designation should be fr-FR, De-AT and default
		assertEquals(3, designationList.size()); 
		verifyDesignationfrFR(designationList.get(0));
		verifyDesignationDeAT(designationList.get(1));
		verifyDesignationNoLanguage(designationList.get(2));

	}
	
	@Test
	public void testLookupWithDisplayLanguageCaching() {
		
		//-- first call with de-AT
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8494-7"))
			.andParameter("system", new UriType(CS_ACME_URL))
			.andParameter("displayLanguage",new CodeType("de-AT"))
			.execute();

		String resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have de-AT and default language
		List<ParametersParameterComponent> parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		List<ParametersParameterComponent> designationList = getDesignations(parameterList);
		// should be de-AT and default
		assertEquals(2, designationList.size()); 
		verifyDesignationDeAT(designationList.get(0));
		verifyDesignationNoLanguage(designationList.get(1));

		//-- second call with zh-CN (not-exist)
		respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8494-7"))
				.andParameter("system", new UriType(CS_ACME_URL))
				.andParameter("displayLanguage",new CodeType("zh-CN"))
				.execute();

		resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have default language only
		parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		designationList = getDesignations(parameterList);
		// should be default only
		assertEquals(1, designationList.size()); 
		verifyDesignationNoLanguage(designationList.get(0));
		
		//-- third call with no language
		respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8494-7"))
				.andParameter("system", new UriType(CS_ACME_URL))
				.execute();

		resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have all languages and the default language
		parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		designationList = getDesignations(parameterList);
		// designation should be fr-FR, De-AT and default
		assertEquals(3, designationList.size()); 
		verifyDesignationfrFR(designationList.get(0));
		verifyDesignationDeAT(designationList.get(1));
		verifyDesignationNoLanguage(designationList.get(2));
		
		//-- forth call with fr-FR
		respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8494-7"))
				.andParameter("system", new UriType(CS_ACME_URL))
				.andParameter("displayLanguage",new CodeType("fr-FR"))
				.execute();

		resp = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		//-- The designations should have fr-FR languages and the default language
		parameterList = respParam.getParameter();
		
		verifyParameterList(parameterList);
		designationList = getDesignations(parameterList);
		// designation should be fr-FR, default
		assertEquals(2, designationList.size()); 
		verifyDesignationfrFR(designationList.get(0));
		verifyDesignationNoLanguage(designationList.get(1));
	}
	
	
	private void verifyParameterList(List<ParametersParameterComponent> parameterList) {
		assertEquals("display", parameterList.get(0).getName());
		assertEquals(("Systolic blood pressure 12 hour minimum"),
				((StringType) parameterList.get(0).getValue()).getValue());

		assertEquals("abstract", parameterList.get(1).getName());
		assertEquals(false, ((BooleanType) parameterList.get(1).getValue()).getValue());
	}
	
	private void verifyDesignationfrFR(ParametersParameterComponent designation) {
		assertEquals("language", designation.getPart().get(0).getName());
		assertEquals("fr-FR", designation.getPart().get(0).getValue().toString());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("fr-FR:Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());
	}
	
	private void verifyDesignationDeAT(ParametersParameterComponent designation) {
		assertEquals("language", designation.getPart().get(0).getName());
		assertEquals("de-AT", designation.getPart().get(0).getValue().toString());
		assertEquals("value", designation.getPart().get(2).getName());
		assertEquals("de-AT:Systolic blood pressure 12 hour minimum", designation.getPart().get(2).getValue().toString());
	}
	
	private void verifyDesignationNoLanguage(ParametersParameterComponent designation) {
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
