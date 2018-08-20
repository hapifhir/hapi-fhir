package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.loinc.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TerminologyLoaderSvcLoincTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcLoincTest.class);
	private TerminologyLoaderSvcImpl mySvc;

	@Mock
	private IHapiTerminologySvc myTermSvc;

	@Mock
	private IHapiTerminologySvcDstu3 myTermSvcDstu3;

	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor;
	@Mock
	private RequestDetails details;
	@Captor
	private ArgumentCaptor<List<ValueSet>> myValueSetsCaptor;
	@Captor
	private ArgumentCaptor<List<ConceptMap>> myConceptMapCaptor;
	private ZipCollectionBuilder myFiles;


	@Before
	public void before() {
		mySvc = new TerminologyLoaderSvcImpl();
		mySvc.setTermSvcForUnitTests(myTermSvc);
		mySvc.setTermSvcDstu3ForUnitTest(myTermSvcDstu3);

		myFiles = new ZipCollectionBuilder();
	}

	private Map<String, ConceptMap> extractConceptMaps() {
		Map<String, ConceptMap> conceptMaps = new HashMap<>();
		for (ConceptMap next : myConceptMapCaptor.getAllValues().get(0)) {
			conceptMaps.put(next.getId(), next);
		}
		return conceptMaps;
	}

	private Map<String, TermConcept> extractConcepts() {
		Map<String, TermConcept> concepts = new HashMap<>();
		for (TermConcept next : myCsvCaptor.getValue().getConcepts()) {
			concepts.put(next.getCode(), next);
		}
		return concepts;
	}

	private Map<String, ValueSet> extractValueSets() {
		Map<String, ValueSet> valueSets = new HashMap<>();
		for (ValueSet next : myValueSetsCaptor.getValue()) {
			valueSets.put(next.getId(), next);
		}
		return valueSets;
	}

	@Test
	public void testLoadLoinc() throws Exception {
		addLoincMandatoryFilesToZip(myFiles);

		// Actually do the load
		mySvc.loadLoinc(myFiles.getFiles(), details);

		verify(myTermSvcDstu3, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();
		Map<String, ValueSet> valueSets = extractValueSets();
		Map<String, ConceptMap> conceptMaps = extractConceptMaps();

		ConceptMap conceptMap;
		TermConcept code;
		ValueSet vs;
		ConceptMap.ConceptMapGroupComponent group;

		// Normal loinc code
		code = concepts.get("10013-1");
		assertEquals("10013-1", code.getCode());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, code.getCodingProperties("PROPERTY").get(0).getSystem());
		assertEquals("LP6802-5", code.getCodingProperties("PROPERTY").get(0).getCode());
		assertEquals("Elpot", code.getCodingProperties("PROPERTY").get(0).getDisplay());
		assertEquals("EKG.MEAS", code.getStringProperty("CLASS"));
		assertEquals("R' wave amplitude in lead I", code.getDisplay());

		// Code with component that has a divisor
		code = concepts.get("17788-1");
		assertEquals("17788-1", code.getCode());
		assertEquals(1, code.getCodingProperties("COMPONENT").size());
		assertEquals("http://loinc.org", code.getCodingProperties("COMPONENT").get(0).getSystem());
		assertEquals("LP19258-0", code.getCodingProperties("COMPONENT").get(0).getCode());

		// Loinc code with answer
		code = concepts.get("61438-8");
		assertThat(code.getStringProperties("answer-list"), contains("LL1000-0"));

		// Answer list
		code = concepts.get("LL1001-8");
		assertEquals("LL1001-8", code.getCode());
		assertEquals("PhenX05_14_30D freq amts", code.getDisplay());

		// Answer list code
		code = concepts.get("LA13834-9");
		assertEquals("LA13834-9", code.getCode());
		assertEquals("1-2 times per week", code.getDisplay());
		assertEquals(3, code.getSequence().intValue());

		// Answer list code with link to answers-for
		code = concepts.get("LL1000-0");
		assertThat(code.getStringProperties("answers-for"), contains("61438-8"));

		// AnswerList valueSet
		vs = valueSets.get("LL1001-8");
		assertEquals("Beta.1", vs.getVersion());
		assertEquals("urn:ietf:rfc:3986", vs.getIdentifier().get(0).getSystem());
		assertEquals("urn:oid:1.3.6.1.4.1.12009.10.1.166", vs.getIdentifier().get(0).getValue());
		assertEquals("PhenX05_14_30D freq amts", vs.getName());
		assertEquals("http://loinc.org/vs/LL1001-8", vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(7, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals("LA6270-8", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("Never", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

		// External AnswerList
		vs = valueSets.get("LL1892-0");
		assertEquals(0, vs.getCompose().getIncludeFirstRep().getConcept().size());

		// Part
		code = concepts.get("LP101394-7");
		assertEquals("LP101394-7", code.getCode());
		assertEquals("adjusted for maternal weight", code.getDisplay());

		// Part Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_ID);
		assertEquals(null, conceptMap.getSource());
		assertEquals(null, conceptMap.getTarget());
		assertEquals(LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_URI, conceptMap.getUrl());
		assertEquals("This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/. The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.", conceptMap.getCopyright());
		assertEquals("Beta.1", conceptMap.getVersion());
		assertEquals(1, conceptMap.getGroup().size());
		group = conceptMap.getGroup().get(0);
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, group.getSource());
		assertEquals(IHapiTerminologyLoaderSvc.SCT_URI, group.getTarget());
		assertEquals("http://snomed.info/sct/900000000000207008/version/20170731", group.getTargetVersion());
		assertEquals("LP18172-4", group.getElement().get(0).getCode());
		assertEquals("Interferon.beta", group.getElement().get(0).getDisplay());
		assertEquals(1, group.getElement().get(0).getTarget().size());
		assertEquals("420710006", group.getElement().get(0).getTarget().get(0).getCode());
		assertEquals("Interferon beta (substance)", group.getElement().get(0).getTarget().get(0).getDisplay());

		// Document Ontology ValueSet
		vs = valueSets.get(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_ID);
		assertEquals(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_NAME, vs.getName());
		assertEquals(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_URI, vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals(3, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("11488-4", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("Consult note", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

		// Document ontology parts
		code = concepts.get("11488-4");
		assertEquals(1, code.getCodingProperties("document-kind").size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, code.getCodingProperties("document-kind").get(0).getSystem());
		assertEquals("LP173418-7", code.getCodingProperties("document-kind").get(0).getCode());
		assertEquals("Note", code.getCodingProperties("document-kind").get(0).getDisplay());

		// RSNA Playbook ValueSet
		vs = valueSets.get(LoincRsnaPlaybookHandler.RSNA_CODES_VS_ID);
		assertEquals(LoincRsnaPlaybookHandler.RSNA_CODES_VS_NAME, vs.getName());
		assertEquals(LoincRsnaPlaybookHandler.RSNA_CODES_VS_URI, vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(3, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals("17787-3", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("NM Thyroid gland Study report", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

		// RSNA Playbook Code Parts - Region Imaged
		code = concepts.get("17787-3");
		String propertyName = "rad-anatomic-location-region-imaged";
		assertEquals(1, code.getCodingProperties(propertyName).size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, code.getCodingProperties(propertyName).get(0).getSystem());
		assertEquals("LP199995-4", code.getCodingProperties(propertyName).get(0).getCode());
		assertEquals("Neck", code.getCodingProperties(propertyName).get(0).getDisplay());
		// RSNA Playbook Code Parts - Imaging Focus
		code = concepts.get("17787-3");
		propertyName = "rad-anatomic-location-imaging-focus";
		assertEquals(1, code.getCodingProperties(propertyName).size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, code.getCodingProperties(propertyName).get(0).getSystem());
		assertEquals("LP206648-0", code.getCodingProperties(propertyName).get(0).getCode());
		assertEquals("Thyroid gland", code.getCodingProperties(propertyName).get(0).getDisplay());
		// RSNA Playbook Code Parts - Modality Type
		code = concepts.get("17787-3");
		propertyName = "rad-modality-modality-type";
		assertEquals(1, code.getCodingProperties(propertyName).size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, code.getCodingProperties(propertyName).get(0).getSystem());
		assertEquals("LP208891-4", code.getCodingProperties(propertyName).get(0).getCode());
		assertEquals("NM", code.getCodingProperties(propertyName).get(0).getDisplay());

		// RSNA Playbook - LOINC Part -> RadLex RID Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID);
		assertEquals(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_URI, conceptMap.getUrl());
		assertEquals(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_NAME, conceptMap.getName());
		assertEquals(1, conceptMap.getGroup().size());
		group = conceptMap.getGroupFirstRep();
		// all entries have the same source and target so these should be null
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, group.getSource());
		assertEquals(LoincRsnaPlaybookHandler.RID_CS_URI, group.getTarget());
		assertEquals("LP199995-4", group.getElement().get(0).getCode());
		assertEquals("Neck", group.getElement().get(0).getDisplay());
		assertEquals(1, group.getElement().get(0).getTarget().size());
		assertEquals("RID7488", group.getElement().get(0).getTarget().get(0).getCode());
		assertEquals("neck", group.getElement().get(0).getTarget().get(0).getDisplay());
		assertEquals(Enumerations.ConceptMapEquivalence.EQUAL, group.getElement().get(0).getTarget().get(0).getEquivalence());

		// RSNA Playbook - LOINC Term -> RadLex RPID Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID);
		assertEquals(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_URI, conceptMap.getUrl());
		assertEquals(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_NAME, conceptMap.getName());
		assertEquals(1, conceptMap.getGroup().size());
		group = conceptMap.getGroupFirstRep();
		// all entries have the same source and target so these should be null
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, group.getSource());
		assertEquals(LoincRsnaPlaybookHandler.RPID_CS_URI, group.getTarget());
		assertEquals("24531-6", group.getElement().get(0).getCode());
		assertEquals("US Retroperitoneum", group.getElement().get(0).getDisplay());
		assertEquals(1, group.getElement().get(0).getTarget().size());
		assertEquals("RPID2142", group.getElement().get(0).getTarget().get(0).getCode());
		assertEquals("US Retroperitoneum", group.getElement().get(0).getTarget().get(0).getDisplay());
		assertEquals(Enumerations.ConceptMapEquivalence.EQUAL, group.getElement().get(0).getTarget().get(0).getEquivalence());

		// TOP 2000 - US
		vs = valueSets.get(LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_ID);
		assertEquals(vs.getName(), LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_NAME);
		assertEquals(vs.getUrl(), LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_URI);
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals(9, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("2160-0", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("Creatinine [Mass/volume] in Serum or Plasma", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());
		assertEquals("718-7", vs.getCompose().getInclude().get(0).getConcept().get(1).getCode());
		assertEquals("Hemoglobin [Mass/volume] in Blood", vs.getCompose().getInclude().get(0).getConcept().get(1).getDisplay());

		// TOP 2000 - SI
		vs = valueSets.get(LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_ID);
		assertEquals(vs.getName(), LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_NAME);
		assertEquals(vs.getUrl(), LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_URI);
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals(9, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("14682-9", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("Creatinine [Moles/volume] in Serum or Plasma", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());
		assertEquals("718-7", vs.getCompose().getInclude().get(0).getConcept().get(1).getCode());
		assertEquals("Hemoglobin [Mass/volume] in Blood", vs.getCompose().getInclude().get(0).getConcept().get(1).getDisplay());

		// Universal lab order VS
		vs = valueSets.get(LoincUniversalOrderSetHandler.VS_ID);
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals(9, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("42176-8", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("1,3 beta glucan [Mass/volume] in Serum", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

		// IEEE Medical Device Codes
		conceptMap = conceptMaps.get(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_ID);
		ourLog.debug(FhirContext.forR4().newXmlParser().setPrettyPrint(true).encodeResourceToString(conceptMap));
		assertEquals(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_NAME, conceptMap.getName());
		assertEquals(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_URI, conceptMap.getUrl());
		assertEquals(1, conceptMap.getGroup().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, conceptMap.getGroup().get(0).getSource());
		assertEquals(IHapiTerminologyLoaderSvc.IEEE_11073_10101_URI, conceptMap.getGroup().get(0).getTarget());
		assertEquals(7, conceptMap.getGroup().get(0).getElement().size());
		assertEquals("14749-6", conceptMap.getGroup().get(0).getElement().get(4).getCode());
		assertEquals("Glucose [Moles/volume] in Serum or Plasma", conceptMap.getGroup().get(0).getElement().get(4).getDisplay());
		assertEquals(2, conceptMap.getGroup().get(0).getElement().get(4).getTarget().size());
		assertEquals("160196", conceptMap.getGroup().get(0).getElement().get(4).getTarget().get(0).getCode());
		assertEquals("MDC_CONC_GLU_VENOUS_PLASMA", conceptMap.getGroup().get(0).getElement().get(4).getTarget().get(0).getDisplay());

		// Imaging Document Codes
		vs = valueSets.get(LoincImagingDocumentCodeHandler.VS_ID);
		assertEquals(LoincImagingDocumentCodeHandler.VS_URI, vs.getUrl());
		assertEquals(LoincImagingDocumentCodeHandler.VS_NAME, vs.getName());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URI, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals(9, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("11525-3", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("US Pelvis Fetus for pregnancy", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

		// Group - Parent
		vs = valueSets.get("LG100-4");
		ourLog.info(FhirContext.forR4().newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		assertEquals("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>", vs.getName());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(1, vs.getCompose().getInclude().get(0).getValueSet().size());
		assertEquals("http://loinc.org/vs/LG1695-8", vs.getCompose().getInclude().get(0).getValueSet().get(0).getValueAsString());

		// Group - Child
		vs = valueSets.get("LG1695-8");
		ourLog.info(FhirContext.forR4().newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		assertEquals("1,4-Dichlorobenzene|MCnc|Pt|ANYBldSerPl", vs.getName());
		assertEquals("http://loinc.org/vs/LG1695-8", vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(2, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals("17424-3", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("13006-2", vs.getCompose().getInclude().get(0).getConcept().get(1).getCode());
	}

	@Test
	@Ignore
	public void testLoadLoincMandatoryFilesOnly() throws IOException {
		addLoincMandatoryFilesToZip(myFiles);

		// Actually do the load
		mySvc.loadLoinc(myFiles.getFiles(), details);

		verify(myTermSvcDstu3, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();
		Map<String, ValueSet> valueSets = extractValueSets();
		Map<String, ConceptMap> conceptMaps = extractConceptMaps();

		// Normal loinc code
		TermConcept code = concepts.get("10013-1");
		assertEquals("10013-1", code.getCode());

		// No valuesets or conceptmaps get created
		assertThat(valueSets.keySet(), empty());
		assertThat(conceptMaps.keySet(), empty());

	}

	@Test
	public void testLoadLoincMissingMandatoryFiles() throws IOException {
		myFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_UPLOAD_PROPERTIES_FILE);
		myFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_GROUP_FILE);

		// Actually do the load
		try {
			mySvc.loadLoinc(myFiles.getFiles(), details);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Could not find the following mandatory files in input:"));
			assertThat(e.getMessage(), containsString("Loinc.csv"));
			assertThat(e.getMessage(), containsString("MultiAxialHierarchy.csv"));
		}
	}


	public static void addLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles) throws IOException {
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_UPLOAD_PROPERTIES_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_GROUP_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_GROUP_TERMS_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_PARENT_GROUP_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_FILE, TerminologyLoaderSvcImpl.LOINC_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_HIERARCHY_FILE, TerminologyLoaderSvcImpl.LOINC_HIERARCHY_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_ANSWERLIST_FILE, TerminologyLoaderSvcImpl.LOINC_ANSWERLIST_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_ANSWERLIST_LINK_FILE, TerminologyLoaderSvcImpl.LOINC_ANSWERLIST_LINK_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_PART_FILE, TerminologyLoaderSvcImpl.LOINC_PART_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_PART_LINK_FILE, TerminologyLoaderSvcImpl.LOINC_PART_LINK_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_PART_RELATED_CODE_MAPPING_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_DOCUMENT_ONTOLOGY_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_RSNA_PLAYBOOK_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_CSV);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_IMAGING_DOCUMENT_CODES_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE);
		theFiles.addFileZip("/loinc/", TerminologyLoaderSvcImpl.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
