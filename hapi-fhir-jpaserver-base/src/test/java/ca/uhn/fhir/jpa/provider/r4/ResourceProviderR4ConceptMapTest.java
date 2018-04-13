package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResourceProviderR4ConceptMapTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4ConceptMapTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParameterByName(respParams, "match");
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("BOGUS"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertFalse(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("No matches found!", ((StringType) param.getValue()).getValueAsString());

		assertFalse(hasParameterByName(respParams, "match"));
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(3, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesCoding() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version #1
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("coding").setValue(
			new Coding().setSystem(CS_URL).setCode("12345").setVersion("Version 1"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParameterByName(respParams, "match");
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeableConcept() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system versions #1 and #3
		 */
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding().setSystem(CS_URL).setCode("12345").setVersion("Version 1"));
		codeableConcept.addCoding(new Coding().setSystem(CS_URL).setCode("23456").setVersion("Version 1"));
		codeableConcept.addCoding(new Coding().setSystem(CS_URL).setCode("12345").setVersion("Version 3"));
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("codeableConcept").setValue(codeableConcept);

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(4, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("45678", coding.getCode());
		assertEquals("Target Code 45678", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(3);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(3, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version #1
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("version").setValue(new StringType("Version 1"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParameterByName(respParams, "match");
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version #3
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("version").setValue(new StringType("Version 3"));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   target code system #2
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParameterByName(respParams, "match");
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   target code system #3
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source value set
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("source").setValue(new UriType(VS_URL));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(3, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   target value set
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("target").setValue(new UriType(VS_URL_2));

		ourLog.info("Request Parameters:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
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

		assertEquals(3, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("56789", coding.getCode());
		assertEquals("Target Code 56789", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("67890", coding.getCode());
		assertEquals("Target Code 67890", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_3, coding.getSystem());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	private static int getNumberOfParametersByName(Parameters theParameters, String theName) {
		int retVal = 0;

		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				retVal++;
			}
		}

		return retVal;
	}

	private static ParametersParameterComponent getParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return param;
			}
		}

		return new ParametersParameterComponent();
	}

	private static List<ParametersParameterComponent> getParametersByName(Parameters theParameters, String theName) {
		List<ParametersParameterComponent> params = new ArrayList<>();
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				params.add(param);
			}
		}

		return params;
	}

	private static ParametersParameterComponent getPartByName(ParametersParameterComponent theParameter, String theName) {
		for (ParametersParameterComponent part : theParameter.getPart()) {
			if (part.getName().equals(theName)) {
				return part;
			}
		}

		return new ParametersParameterComponent();
	}

	private static boolean hasParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return true;
			}
		}

		return false;
	}
}
