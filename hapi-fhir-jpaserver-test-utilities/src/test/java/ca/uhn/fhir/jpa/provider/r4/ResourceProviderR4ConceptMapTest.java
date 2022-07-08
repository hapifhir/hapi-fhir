package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderR4ConceptMapTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4ConceptMapTest.class);

	private IIdType myConceptMapId;
	@BeforeEach
	@Transactional
	public void before02() {
		myConceptMapId = myConceptMapDao.create(createConceptMap(), mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testStoreExistingTermConceptMapAndChildren() {
		ConceptMap conceptMap = createConceptMap();

		MethodOutcome methodOutcome = myClient
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

		MethodOutcome methodOutcome = myClient
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

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}


	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne_InBatchOperation() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.BATCH);
		bundle
			.addEntry()
			.getRequest()
			.setMethod(Bundle.HTTPVerb.GET)
			.setUrl("ConceptMap/$translate?system=" + CS_URL + "&code=12345" + "&targetsystem=" + CS_URL_2);

		ourLog.info("Request:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		Bundle respBundle = myClient
			.transaction()
			.withBundle(bundle)
			.execute();

		ourLog.info("Response:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respBundle));

		assertEquals(1, respBundle.getEntry().size());
		Parameters respParams = (Parameters) respBundle.getEntry().get(0).getResource();

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne_InBatchOperation2() throws IOException {
		ConceptMap cm = loadResourceFromClasspath(ConceptMap.class, "/r4/conceptmap.json");
		myConceptMapDao.update(cm);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.BATCH);
		bundle
			.addEntry()
			.getRequest()
			.setMethod(Bundle.HTTPVerb.GET)
			.setUrl("ConceptMap/$translate?url=http://hl7.org/fhir/ConceptMap/CMapHie&system=http://fkcfhir.org/fhir/cs/FMCECCOrderAbbreviation&code=IMed_Janssen&targetsystem=http://fkcfhir.org/fhir/cs/FMCHIEOrderAbbreviation");

		ourLog.info("Request:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		Bundle respBundle = myClient
			.transaction()
			.withBundle(bundle)
			.execute();

		ourLog.info("Response:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respBundle));

		assertEquals(1, respBundle.getEntry().size());
		Parameters respParams = (Parameters) respBundle.getEntry().get(0).getResource();

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParameterByName(respParams, "match");
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equivalent", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("212", coding.getCode());
		assertEquals("COVID-19 Vaccine,vecton-nr,rS-Ad26,PF,0.5mL", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals("http://fkcfhir.org/fhir/cs/FMCHIEOrderAbbreviation", coding.getSystem());
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("BOGUS"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertFalse(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("No Matches found", ((StringType) param.getValue()).getValueAsString());

		assertFalse(hasParameterByName(respParams, "match"));
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesCoding() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version #1
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("coding").setValue(
			new Coding().setSystem(CS_URL).setCode("12345").setVersion("Version 1"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeableConcept() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

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

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

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

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

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

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

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

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

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

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source value set
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("source").setValue(new UriType(VS_URL));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   target value set
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));
		inParams.addParameter().setName("target").setValue(new UriType(VS_URL_2));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithConceptMapUrlAndVersion() {
		
		//- conceptMap1 v1
		ConceptMap conceptMap1 = new ConceptMap();
		conceptMap1.setUrl(CM_URL).setVersion("v1").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group1 = conceptMap1.addGroup();
		group1.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode("11111").setDisplay("Source Code 11111");

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode("12222").setDisplay("Target Code 12222").setEquivalence(ConceptMapEquivalence.EQUAL);
		
		IIdType conceptMapId1 = myConceptMapDao.create(conceptMap1, mySrd).getId().toUnqualifiedVersionless();
		conceptMap1 = myConceptMapDao.read(conceptMapId1);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap1));
	
		//- conceptMap1 v2
		ConceptMap conceptMap2 = new ConceptMap();
		conceptMap2.setUrl(CM_URL).setVersion("v2").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group2 = conceptMap2.addGroup();
		group2.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element2 = group2.addElement();
		element2.setCode("11111").setDisplay("Source Code 11111");

		TargetElementComponent target2 = element2.addTarget();
		target2.setCode("13333").setDisplay("Target Code 13333").setEquivalence(ConceptMapEquivalence.EQUAL);
		
		IIdType conceptMapId2 = myConceptMapDao.create(conceptMap2, mySrd).getId().toUnqualifiedVersionless();
		conceptMap2 = myConceptMapDao.read(conceptMapId2);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap2));

		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CM_URL));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));
		
		
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

	}
	
	
	@Test
	public void testTranslateWithInstance() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onInstance(myConceptMapId)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

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
		assertEquals("Version 2", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
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
		assertEquals("Version 4", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverse() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   target code system
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_4));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("BOGUS"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertFalse(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("No Matches found", ((StringType) param.getValue()).getValueAsString());

		assertFalse(hasParameterByName(respParams, "match"));
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeOnly() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesCoding() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("coding").setValue(
			new Coding().setSystem(CS_URL_2).setCode("34567").setVersion("Version 2"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeableConcept() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version
		 *   reverse = true
		 */
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding().setSystem(CS_URL_2).setCode("34567").setVersion("Version 2"));
		codeableConcept.addCoding(new Coding().setSystem(CS_URL_2).setCode("45678").setVersion("Version 2"));
		codeableConcept.addCoding(new Coding().setSystem(CS_URL_3).setCode("56789").setVersion("Version 4"));
		codeableConcept.addCoding(new Coding().setSystem(CS_URL_3).setCode("67890").setVersion("Version 4"));
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("codeableConcept").setValue(codeableConcept);
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(4, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(2);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("wider", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("23456", coding.getCode());
		assertEquals("Source Code 23456", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(3);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 3", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystem() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystemAndVersion() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   source code system version
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("version").setValue(new StringType("Version 2"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   target code system #1
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source code system
		 *   target code system #4
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_4));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   source value set
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("source").setValue(new UriType(VS_URL_2));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithTargetValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		/*
		 * Provided:
		 *   source code
		 *   target value set
		 *   reverse = true
		 */
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("target").setValue(new UriType(VS_URL));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithReverseConceptMapUrlAndVersion() {
		
		//- conceptMap1 v1
		ConceptMap conceptMap1 = new ConceptMap();
		conceptMap1.setUrl(CM_URL).setVersion("v1").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group1 = conceptMap1.addGroup();
		group1.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode("12222").setDisplay("Source Code 12222");

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode("11111").setDisplay("11111").setEquivalence(ConceptMapEquivalence.EQUAL);
		
		IIdType conceptMapId1 = myConceptMapDao.create(conceptMap1, mySrd).getId().toUnqualifiedVersionless();
		conceptMap1 = myConceptMapDao.read(conceptMapId1);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap1));
	
		//- conceptMap1 v2
		ConceptMap conceptMap2 = new ConceptMap();
		conceptMap2.setUrl(CM_URL).setVersion("v2").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group2 = conceptMap2.addGroup();
		group2.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element2 = group2.addElement();
		element2.setCode("13333").setDisplay("Source Code 13333");

		TargetElementComponent target2 = element2.addTarget();
		target2.setCode("11111").setDisplay("Target Code 11111").setEquivalence(ConceptMapEquivalence.EQUAL);
		
		IIdType conceptMapId2 = myConceptMapDao.create(conceptMap2, mySrd).getId().toUnqualifiedVersionless();
		conceptMap2 = myConceptMapDao.read(conceptMapId2);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap2));

		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CM_URL));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));
		
		
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Source Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
		

	}

	@Test
	public void testTranslateWithReverseAndInstance() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("34567"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = myClient
			.operation()
			.onInstance(myConceptMapId)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(2, getNumberOfParametersByName(respParams, "match"));

		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12345", coding.getCode());
		assertEquals("Source Code 12345", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		param = getParametersByName(respParams, "match").get(1);
		assertEquals(3, param.getPart().size());
		part = getPartByName(param, "equivalence");
		assertEquals("narrower", ((CodeType)part.getValue()).getCode());
		part = getPartByName(param, "concept");
		coding = (Coding) part.getValue();
		assertEquals("78901", coding.getCode());
		assertEquals("Source Code 78901", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_4, coding.getSystem());
		assertEquals("Version 5", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}
}
