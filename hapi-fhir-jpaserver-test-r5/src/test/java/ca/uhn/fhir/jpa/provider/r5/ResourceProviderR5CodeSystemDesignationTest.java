package ca.uhn.fhir.jpa.provider.r5;

import static org.junit.jupiter.api.Assertions.assertNull;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("Systolic blood pressure 12 hour minimum"));

		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(false);

		//-- designationList
		assertThat(designationList).hasSize(2);
		
		// 1. de-AT:Systolic blood pressure 12 hour minimum
		ParametersParameterComponent designation = designationList.get(0);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertThat(designation.getPart().get(0).getValue().toString()).isEqualTo("de-AT");
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("de-AT:Systolic blood pressure 12 hour minimum");

		// 2. Systolic blood pressure 12 hour minimum (no language)
		designation = designationList.get(1);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertNull(designation.getPart().get(0).getValue());
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("Systolic blood pressure 12 hour minimum");

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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("Systolic blood pressure 12 hour minimum"));

		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(false);

		//-- designationList
		assertThat(designationList).hasSize(1);
		
		// 1. Systolic blood pressure 12 hour minimum (no language)
		ParametersParameterComponent designation = designationList.get(0);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertNull(designation.getPart().get(0).getValue());
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("Systolic blood pressure 12 hour minimum");

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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("Systolic blood pressure 12 hour minimum"));

		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(false);

		//-- designationList
		assertThat(designationList).hasSize(3);
		
		// 1. fr-FR:Systolic blood pressure 12 hour minimum
		ParametersParameterComponent designation = designationList.get(0);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertThat(designation.getPart().get(0).getValue().toString()).isEqualTo("fr-FR");
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("fr-FR:Systolic blood pressure 12 hour minimum");

		// 2. de-AT:Systolic blood pressure 12 hour minimum
		designation = designationList.get(1);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertThat(designation.getPart().get(0).getValue().toString()).isEqualTo("de-AT");
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("de-AT:Systolic blood pressure 12 hour minimum");

		// 3. Systolic blood pressure 12 hour minimum (no language)
		designation = designationList.get(2);
		assertThat(designation.getPart().get(0).getName()).isEqualTo("language");
		assertNull(designation.getPart().get(0).getValue());
		assertThat(designation.getPart().get(2).getName()).isEqualTo("value");
		assertThat(designation.getPart().get(2).getValue().toString()).isEqualTo("Systolic blood pressure 12 hour minimum");

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
