package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

public class ResourceProviderDstu3ConceptMapTest extends BaseResourceProviderDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderDstu3ConceptMapTest.class);

	private IIdType myConceptMapId;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Before
	@Transactional
	public void before02() {
		myConceptMapId = myConceptMapDao.create(createConceptMap(), mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testStoreExistingTermConceptMapAndChildren() {
		ConceptMap conceptMap = createConceptMap();

		MethodOutcome methodOutcome = ourClient
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMap.getUrl()))
			.execute();

		assertNull(methodOutcome.getCreated());
		assertEquals("1", methodOutcome.getId().getVersionIdPart());
	}

	@Test
	public void testStoreUpdatedTermConceptMapAndChildren() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroupFirstRep().getElementFirstRep().setCode("UPDATED_CODE");

		MethodOutcome methodOutcome = ourClient
			.update()
			.resource(conceptMap)
			.conditional()
			.where(ConceptMap.URL.matches().value(conceptMap.getUrl()))
			.execute();

		assertNull(methodOutcome.getCreated());
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found!", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}
}
