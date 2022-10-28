package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderDstu3ConceptMapTest extends BaseResourceProviderDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderDstu3ConceptMapTest.class);

	private IIdType myConceptMapId;


	@BeforeEach
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

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_3));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
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
	
	@Test
	public void testTranslateWithConceptMapUrlAndVersion() {
		
		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222");
		createConceptMap(url, "v2", "13333", "Target Code 13333");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
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
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());
	}
	
	@Test
	public void testTranslateWithVersionedConceptMapUrl_v2() {

		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222");
		createConceptMap(url, "v2", "13333", "Target Code 13333");
		
		// Call translate with ConceptMap v2.
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		// Should return v2 since v2 specified.
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());

	}

	@Test
	public void testTranslateWithVersionedConceptMapUrl_v1() {

		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222");
		createConceptMap(url, "v2", "13333", "Target Code 13333");
		
		// Call translate with ConceptMap v1.
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v1"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		// Should return v1 since v1 specified.
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("12222", coding.getCode());
		assertEquals("Target Code 12222", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());

	}

	@Test
	public void testTranslateWithVersionedConceptMapUrl_NoVersion() {

		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222");
		createConceptMap(url, "v2", "13333", "Target Code 13333");
		
		// Call translate with no ConceptMap version.
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		// Should return v2 since v2 is the most recently updated version.
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithVersionedConceptMapUrl_NoVersion_null_v1() {

		String url = "http://url";
		createConceptMap(url, null, "12222", "Target Code 12222"); // first version is null
		createConceptMap(url, "v2", "13333", "Target Code 13333");
				 		
		// Call translate with no ConceptMap version.
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		// Should return v2 since v2 is the most recently updated version.
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());
	}

	@Test
	public void testTranslateWithVersionedConceptMapUrl_NoVersion_null_v2() {

		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222"); 
		createConceptMap(url, null, "13333", "Target Code 13333"); // second version is null
		
		// Call translate with no ConceptMap version.
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParams));

		// Should return v2 since v2 is the most recently updated version.
		ParametersParameterComponent param = getParameterByName(respParams, "result");
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = getParameterByName(respParams, "message");
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equal", ((CodeType) part.getValue()).getValueAsString());
		part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());
	}
	
	@Test
	public void testTranslateWithConceptMap_WrongUrl_NoVersion() {
		
		String url = "http://url";
		createConceptMap(url, "v1", "12222", "Target Code 12222");
		createConceptMap(url, "v2", "13333", "Target Code 13333");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType("http://invalid.url.com")); // no exsits url
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		Parameters respParams = ourClient
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

	}
	
	@Test
	public void testTranslateWithReverseConceptMapUrlAndVersion() {
		
		String url = "http://url";
		createReverseConceptMap(url, "v1", "12222", "Source Code 12222");
		createReverseConceptMap(url, "v2", "13333", "Source Code 13333");
				
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = ourClient
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
		assertEquals(2, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Source Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());	
	}
	
	@Test
	public void testTranslateWithReverseConceptMapUrl_NoVersion() {
		
		String url = "http://url";
		createReverseConceptMap(url, "v1", "12222", "Source Code 12222");
		createReverseConceptMap(url, "v2", "13333", "Source Code 13333");
				
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = ourClient
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
		assertEquals(2, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Source Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());	
	}
	
	@Test
	public void testTranslateWithReverseConceptMapUrl_NoVersion_null_v1() {
		
		String url = "http://url";
		createReverseConceptMap(url, null, "12222", "Source Code 12222");
		createReverseConceptMap(url, "v2", "13333", "Source Code 13333");
				
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = ourClient
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
		assertEquals(2, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Source Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());	
	}
	
	@Test
	public void testTranslateWithReverseConceptMapUrl_NoVersion_null_v2() {
		
		String url = "http://url";
		createReverseConceptMap(url, "v1", "12222", "Source Code 12222");
		createReverseConceptMap(url, null, "13333", "Source Code 13333");
				
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

		ourLog.info("Request Parameters:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inParams));

		
		Parameters respParams = ourClient
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
		assertEquals(2, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Source Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL, coding.getSystem());
		assertEquals("Version 1", coding.getVersion());
		part = getPartByName(param, "source");
		assertEquals(url, ((UriType) part.getValue()).getValueAsString());	
	}
	
	private void createConceptMap(String url, String version, String targetCode, String targetDisplay) {
		
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(url).setVersion(version).setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));

		ConceptMapGroupComponent group1 = conceptMap.addGroup();
		group1.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode("11111").setDisplay("Source Code 11111");

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode(targetCode).setDisplay(targetDisplay).setEquivalence(ConceptMapEquivalence.EQUAL);

		IIdType conceptMapId = myConceptMapDao.create(conceptMap, mySrd).getId().toUnqualifiedVersionless();
		conceptMap = myConceptMapDao.read(conceptMapId);
		
		ourLog.info("ConceptMap: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));
	}
	
	private void createReverseConceptMap(String url, String version, String sourceCode, String sourceDisplay) {
		
		//- conceptMap1 v1
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(url).setVersion(version).setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group1 = conceptMap.addGroup();
		group1.setSource(CS_URL).setSourceVersion("Version 1").setTarget(CS_URL_2).setTargetVersion("Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode(sourceCode).setDisplay(sourceDisplay);

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode("11111").setDisplay("11111");
		
		IIdType conceptMapId = myConceptMapDao.create(conceptMap, mySrd).getId().toUnqualifiedVersionless();
		ConceptMap conceptMap1 = myConceptMapDao.read(conceptMapId);
		
		ourLog.info("ConceptMap : \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap1));
	
	}
}
