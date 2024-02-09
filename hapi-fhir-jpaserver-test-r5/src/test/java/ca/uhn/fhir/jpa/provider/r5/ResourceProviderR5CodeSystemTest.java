package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderR5CodeSystemTest extends BaseResourceProviderR5Test {
	private static final String SYSTEM_PARENTCHILD = "http://parentchild";
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5CodeSystemTest.class);
	private Long parentChildCsId;

	private IIdType myCsId;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCsId = myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem parentChildCs = new CodeSystem();
		parentChildCs.setUrl(SYSTEM_PARENTCHILD);
		parentChildCs.setName("Parent Child CodeSystem");
		parentChildCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		parentChildCs.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		parentChildCs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);

		CodeSystem.ConceptDefinitionComponent parentA = parentChildCs.addConcept().setCode("ParentA").setDisplay("Parent A");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA");
		parentChildCs.addConcept().setCode("ParentB").setDisplay("Parent B");

		DaoMethodOutcome parentChildCsOutcome = myCodeSystemDao.create(parentChildCs);
		parentChildCsId = ((ResourceTable)parentChildCsOutcome.getEntity()).getId();

	}

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

		ourLog.debug("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertThat(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue()).isEqualTo(true);
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v1 display");
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("ACME Codes"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testSubsumesOnCodes_Subsumes() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ChildAA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMES.toCode());
	}

	@Test
	public void testSubsumesOnCodings_Subsumes() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMES.toCode());
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

		ourLog.debug("CodeSystem: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
		
		myCodeSystemDao.create(codeSystem, mySrd);
	}

	@Test
	public void testLookupOperationByCoding() throws IOException {

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("ACME Codes"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testValidateCodeFoundByCodeWithId() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		Parameters respParam = myClient.operation().onInstance(myCsId).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue()).isTrue();
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInCode() {
		// First test with no version specified (should return the one and only version defined).
		{
			Parameters respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSN"))
				.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
				.execute();

			String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
			ourLog.info(resp);

			Parameters.ParametersParameterComponent param0 = respParam.getParameter().get(0);
			assertThat(param0.getName()).isEqualTo("name");
			assertThat(((StringType) param0.getValue()).getValue()).isEqualTo("IdentifierType");
			Parameters.ParametersParameterComponent param1 = respParam.getParameter().get(1);
			assertThat(param1.getName()).isEqualTo("version");
			assertThat(((StringType) param1.getValue()).getValue()).isEqualTo("2.9.0");
			Parameters.ParametersParameterComponent param2 = respParam.getParameter().get(2);
			assertThat(param2.getName()).isEqualTo("display");
			assertThat(((StringType) param2.getValue()).getValue()).isEqualTo("Accession ID");
			Parameters.ParametersParameterComponent param3 = respParam.getParameter().get(3);
			assertThat(param3.getName()).isEqualTo("abstract");
			assertThat(((BooleanType) param3.getValue()).getValue()).isEqualTo(false);
		}

		// Repeat with version specified.
		{
			Parameters respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSN"))
				.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
				.andParameter("version", new StringType("2.9.0"))
				.execute();

			String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
			ourLog.info(resp);

			Parameters.ParametersParameterComponent param0 = respParam.getParameter().get(0);
			assertThat(param0.getName()).isEqualTo("name");
			assertThat(((StringType) param0.getValue()).getValue()).isEqualTo("IdentifierType");
			Parameters.ParametersParameterComponent param1 = respParam.getParameter().get(1);
			assertThat(param1.getName()).isEqualTo("version");
			assertThat(((StringType) param1.getValue()).getValue()).isEqualTo("2.9.0");
			Parameters.ParametersParameterComponent param2 = respParam.getParameter().get(2);
			assertThat(param2.getName()).isEqualTo("display");
			assertThat(((StringType) param2.getValue()).getValue()).isEqualTo("Accession ID");
			Parameters.ParametersParameterComponent param3 = respParam.getParameter().get(3);
			assertThat(param3.getName()).isEqualTo("abstract");
			assertThat(((BooleanType) param3.getValue()).getValue()).isEqualTo(false);
		}
	}



}
