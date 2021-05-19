package ca.uhn.fhir.jpa.provider.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r5.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r5.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r5.model.Enumerations.ConceptMapRelationship;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProviderR5ConceptMapTest extends BaseResourceProviderR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5ConceptMapTest.class);

	@Test
	public void testTranslateWithConceptMapUrlAndVersion() {
		//- conceptMap1 v1
		ConceptMap conceptMap1 = new ConceptMap();
		conceptMap1.setUrl(CM_URL).setVersion("v1").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group1 = conceptMap1.addGroup();
		group1.setSource(CS_URL + "|" + "Version 1").setTarget(CS_URL_2 + "|" + "Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode("11111").setDisplay("Source Code 11111");

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode("12222").setDisplay("Target Code 12222");
		
		IIdType conceptMapId1 = myConceptMapDao.create(conceptMap1, mySrd).getId().toUnqualifiedVersionless();
		conceptMap1 = myConceptMapDao.read(conceptMapId1);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap1));
	
		//- conceptMap1 v2
		ConceptMap conceptMap2 = new ConceptMap();
		conceptMap2.setUrl(CM_URL).setVersion("v2").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group2 = conceptMap2.addGroup();
		group2.setSource(CS_URL + "|" + "Version 1").setTarget(CS_URL_2 + "|" + "Version 2");

		SourceElementComponent element2 = group2.addElement();
		element2.setCode("11111").setDisplay("Source Code 11111");

		TargetElementComponent target2 = element2.addTarget();
		target2.setCode("13333").setDisplay("Target Code 13333");
		
		IIdType conceptMapId2 = myConceptMapDao.create(conceptMap2, mySrd).getId().toUnqualifiedVersionless();
		conceptMap2 = myConceptMapDao.read(conceptMapId2);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap2));

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CM_URL));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));

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
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);

		assertEquals(3, param.getPart().size());
		
		ParametersParameterComponent part = getPartByName(param, "concept");
		Coding coding = (Coding) part.getValue();
		assertEquals("13333", coding.getCode());
		assertEquals("Target Code 13333", coding.getDisplay());
		assertFalse(coding.getUserSelected());
		assertEquals(CS_URL_2, coding.getSystem());
		assertEquals("Version 2", coding.getVersion());
		
		part = getPartByName(param, "source");
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());

		part = getPartByName(param, "equivalence");
		assertEquals("relatedto", part.getValue().toString());
	}
	
	@Test
	public void testTranslateWithReverseConceptMapUrlAndVersion() {
		
		//- conceptMap1 v1
		ConceptMap conceptMap1 = new ConceptMap();
		conceptMap1.setUrl(CM_URL).setVersion("v1").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group1 = conceptMap1.addGroup();
		group1.setSource(CS_URL + "|" + "Version 1").setTarget(CS_URL_2 + "|" + "Version 2");

		SourceElementComponent element1 = group1.addElement();
		element1.setCode("12222").setDisplay("Source Code 12222");

		TargetElementComponent target1 = element1.addTarget();
		target1.setCode("11111").setDisplay("11111").setRelationship(ConceptMapRelationship.EQUIVALENT);
		
		IIdType conceptMapId1 = myConceptMapDao.create(conceptMap1, mySrd).getId().toUnqualifiedVersionless();
		conceptMap1 = myConceptMapDao.read(conceptMapId1);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap1));
	
		//- conceptMap1 v2
		ConceptMap conceptMap2 = new ConceptMap();
		conceptMap2.setUrl(CM_URL).setVersion("v2").setSource(new UriType(VS_URL)).setTarget(new UriType(VS_URL_2));
		
		ConceptMapGroupComponent group2 = conceptMap2.addGroup();
		group2.setSource(CS_URL + "|" + "Version 1").setTarget(CS_URL_2 + "|" + "Version 2");

		SourceElementComponent element2 = group2.addElement();
		element2.setCode("13333").setDisplay("Source Code 13333");

		TargetElementComponent target2 = element2.addTarget();
		target2.setCode("11111").setDisplay("Target Code 11111").setRelationship(ConceptMapRelationship.EQUIVALENT);
		
		IIdType conceptMapId2 = myConceptMapDao.create(conceptMap2, mySrd).getId().toUnqualifiedVersionless();
		conceptMap2 = myConceptMapDao.read(conceptMapId2);
		
		ourLog.info("ConceptMap: 2 \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap2));

		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CM_URL));
		inParams.addParameter().setName("conceptMapVersion").setValue(new StringType("v2"));
		inParams.addParameter().setName("code").setValue(new CodeType("11111"));
		inParams.addParameter().setName("reverse").setValue(new BooleanType(true));

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
		assertEquals("Matches found", ((StringType) param.getValue()).getValueAsString());

		assertEquals(1, getNumberOfParametersByName(respParams, "match"));
		param = getParametersByName(respParams, "match").get(0);
		assertEquals(3, param.getPart().size());
		ParametersParameterComponent part = getPartByName(param, "equivalence");
		assertEquals("equivalent", ((CodeType) part.getValue()).getCode());
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
}
