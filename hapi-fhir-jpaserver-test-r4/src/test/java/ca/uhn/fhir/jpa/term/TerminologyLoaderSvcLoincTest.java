package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincDocumentOntologyHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincImagingDocumentCodeHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincUniversalOrderSetHandler;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_MAKE_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TerminologyLoaderSvcLoincTest extends BaseLoaderTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcLoincTest.class);
	private TermLoaderSvcImpl mySvc;

	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor_267_first;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor_267_second;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor_268;
	@Captor
	private ArgumentCaptor<List<ValueSet>> myValueSetsCaptor_267_first;
	@Captor
	private ArgumentCaptor<List<ValueSet>> myValueSetsCaptor_267_second;
	@Captor
	private ArgumentCaptor<List<ValueSet>> myValueSetsCaptor_268;
	@Captor
	private ArgumentCaptor<List<ConceptMap>> myConceptMapCaptor_267_first;
	@Captor
	private ArgumentCaptor<List<ConceptMap>> myConceptMapCaptor_267_second;
	@Captor
	private ArgumentCaptor<List<ConceptMap>> myConceptMapCaptor_268;
	@Captor
	private ArgumentCaptor<RequestDetails> myRequestDetailsCaptor;
	private ZipCollectionBuilder myFiles;
	@Mock
	private ITermDeferredStorageSvc myTermDeferredStorageSvc;

	public static final String expectedLoincCopyright = "This material contains content from LOINC (http://loinc.org). LOINC is copyright ©1995-2021, Regenstrief Institute, Inc. and the Logical Observation Identifiers Names and Codes (LOINC) Committee and is available at no cost under the license at http://loinc.org/license. LOINC® is a registered United States trademark of Regenstrief Institute, Inc.";
	public static final String partMappingsExternalCopyright = "The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";
	public static final String expectedWhoExternalCopyrightNotice = "Copyright © 2006 World Health Organization. Used with permission. Publications of the World Health Organization can be obtained from WHO Press, World Health Organization, 20 Avenue Appia, 1211 Geneva 27, Switzerland (tel: +41 22 791 2476; fax: +41 22 791 4857; email: bookorders@who.int). Requests for permission to reproduce or translate WHO publications – whether for sale or for noncommercial distribution – should be addressed to WHO Press, at the above address (fax: +41 22 791 4806; email: permissions@who.int). The designations employed and the presentation of the material in this publication do not imply the expression of any opinion whatsoever on the part of the World Health Organization concerning the legal status of any country, territory, city or area or of its authorities, or concerning the delimitation of its frontiers or boundaries. Dotted lines on maps represent approximate border lines for which there may not yet be full agreement. The mention of specific companies or of certain manufacturers’ products does not imply that they are endorsed or recommended by the World Health Organization in preference to others of a similar nature that are not mentioned. Errors and omissions excepted, the names of proprietary products are distinguished by initial capital letters. All reasonable precautions have been taken by WHO to verify the information contained in this publication. However, the published material is being distributed without warranty of any kind, either express or implied. The responsibility for the interpretation and use of the material lies with the reader. In no event shall the World Health Organization be liable for damages arising from its use.";

	@BeforeEach
	public void before() {
		mySvc = TermLoaderSvcImpl.withoutProxyCheck(myTermDeferredStorageSvc, myTermCodeSystemStorageSvc);
		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadLoincWithSplitPartLink() throws Exception {
		TermTestUtil.addLoincMandatoryFilesToZip(myFiles);
		verifyLoadLoinc();
	}

	@Test
	public void testLoadLoincWithSinglePartLink() throws Exception {
		TermTestUtil.addLoincMandatoryFilesAndSinglePartLinkToZip(myFiles);
		verifyLoadLoinc();
	}

	@Test
	public void testLoadLoincWithMandatoryFilesOnly() throws Exception {
		TermTestUtil.addLoincMandatoryFilesWithoutTop2000ToZip(myFiles);
		verifyLoadLoinc(false, false);
	}

	@Test
	public void testLoadLoincInvalidPartLinkFiles() throws IOException {

		// Missing all PartLinkFiles
		TermTestUtil.addBaseLoincMandatoryFilesToZip(myFiles, true);
		myFiles.addFileZip("/loinc/", LOINC_UPLOAD_PROPERTIES_FILE.getCode());

		try {
			mySvc.loadLoinc(myFiles.getFiles(), mySrd);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(863) + "Could not find any of the PartLink files: [AccessoryFiles/PartFile/LoincPartLink_Primary.csv, AccessoryFiles/PartFile/LoincPartLink_Supplementary.csv] nor [AccessoryFiles/PartFile/LoincPartLink.csv]");
		}

		// Missing LoincPartLink_Supplementary
		myFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		try {
			mySvc.loadLoinc(myFiles.getFiles(), mySrd);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(863) + "Could not find any of the PartLink files: [AccessoryFiles/PartFile/LoincPartLink_Supplementary.csv] nor [AccessoryFiles/PartFile/LoincPartLink.csv]");
		}

		// Both Split and Single PartLink files
		myFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
		myFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_DEFAULT.getCode());
		try {
			mySvc.loadLoinc(myFiles.getFiles(), mySrd);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(863) + "Only either the single PartLink file or the split PartLink files can be present. Found both the single PartLink file, AccessoryFiles/PartFile/LoincPartLink.csv, and the split PartLink files: [AccessoryFiles/PartFile/LoincPartLink_Primary.csv, AccessoryFiles/PartFile/LoincPartLink_Supplementary.csv]");
		}

	}

	@Test
	public void testLoadLoincWithConsumerNameAndLinguisticVariants() throws Exception {
		TermTestUtil.addLoincMandatoryFilesAndConsumerNameAndLinguisticVariants(myFiles);
		verifyLoadLoinc(false, true);
	}


	private void verifyLoadLoinc() {
		verifyLoadLoinc(true, false);
	}

	private void verifyLoadLoinc(boolean theIncludeTop2000, boolean theIncludeConsumerNameAndLinguisticVariants) {
		// Actually do the load
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();
		Map<String, ValueSet> valueSets = extractValueSets();
		Map<String, ConceptMap> conceptMaps = extractConceptMaps();

		ConceptMap conceptMap;
		TermConcept code;
		ValueSet vs;
		ConceptMap.ConceptMapGroupComponent group;

		// Normal LOINC code
		code = concepts.get("10013-1");
		assertThat(code.getCode()).isEqualTo("10013-1");
		// Coding Property
		assertThat(code.getCodingProperties("PROPERTY").get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties("PROPERTY").get(0).getCode()).isEqualTo("LP6802-5");
		assertThat(code.getCodingProperties("PROPERTY").get(0).getDisplay()).isEqualTo("Elpot");
		// String Property
		assertThat(code.getStringProperty("CLASSTYPE")).isEqualTo("2");
		assertThat(code.getDisplay()).isEqualTo("R' wave amplitude in lead I");
		// Coding Property from Part File
		assertThat(code.getCodingProperties("TIME_ASPCT").get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties("TIME_ASPCT").get(0).getCode()).isEqualTo("LP6960-1");
		assertThat(code.getCodingProperties("TIME_ASPCT").get(0).getDisplay()).isEqualTo("Pt");
		// Code with component that has a divisor
		code = concepts.get("17788-1");
		assertThat(code.getCode()).isEqualTo("17788-1");

		// LOINC code with answer
		code = concepts.get("61438-8");
		assertThat(code.getStringProperties("answer-list")).containsExactly("LL1000-0");

		// LOINC code with 3rd party copyright
		code = concepts.get("47239-9");
		assertThat(code.getStringProperty("EXTERNAL_COPYRIGHT_NOTICE")).isEqualTo(expectedWhoExternalCopyrightNotice);

		// Answer list
		code = concepts.get("LL1001-8");
		assertThat(code.getCode()).isEqualTo("LL1001-8");
		assertThat(code.getDisplay()).isEqualTo("PhenX05_14_30D freq amts");

		// Answer list code
		code = concepts.get("LA13834-9");
		assertThat(code.getCode()).isEqualTo("LA13834-9");
		assertThat(code.getDisplay()).isEqualTo("1-2 times per week");
		assertThat(code.getSequence().intValue()).isEqualTo(3);

		// Answer list code with link to answers-for
		code = concepts.get("LL1000-0");
		assertThat(code.getStringProperties("answers-for")).containsExactly("61438-8");

		// AnswerList valueSet
		vs = valueSets.get("LL1001-8");
		assertThat(vs.getVersion()).isEqualTo("Beta.1");
		assertThat(vs.getIdentifier().get(0).getSystem()).isEqualTo("urn:ietf:rfc:3986");
		assertThat(vs.getIdentifier().get(0).getValue()).isEqualTo("urn:oid:1.3.6.1.4.1.12009.10.1.166");
		assertThat(vs.getName()).isEqualTo("PhenX05_14_30D freq amts");
		assertThat(vs.getUrl()).isEqualTo("http://loinc.org/vs/LL1001-8");
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(7);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("LA6270-8");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("Never");
		assertThat(vs.getVersion()).isEqualTo("Beta.1");

		// External AnswerList
		vs = valueSets.get("LL1892-0");
		assertThat(vs.getCompose().getIncludeFirstRep().getConcept()).isEmpty();
		assertThat(vs.getVersion()).isEqualTo("Beta.1");

		// Part
		code = concepts.get("LP101394-7");
		assertThat(code.getCode()).isEqualTo("LP101394-7");
		assertThat(code.getDisplay()).isEqualTo("adjusted for maternal weight");

		// Part Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_ID);
		assertThat(conceptMap.getSource()).isNull();
		assertThat(conceptMap.getTarget()).isNull();
		assertThat(conceptMap.getUrl()).isEqualTo(LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_URI);
		assertThat(conceptMap.getCopyright()).isEqualTo(expectedLoincCopyright + " " + partMappingsExternalCopyright);
		assertThat(conceptMap.getVersion()).isEqualTo("Beta.1");
		assertThat(conceptMap.getGroup()).hasSize(1);
		group = conceptMap.getGroup().get(0);
		assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(group.getSourceVersion()).isNull();
		assertThat(group.getTarget()).isEqualTo(ITermLoaderSvc.SCT_URI);
		assertThat(group.getTargetVersion()).isEqualTo("http://snomed.info/sct/900000000000207008/version/20170731");
		assertThat(group.getElement().get(0).getCode()).isEqualTo("LP18172-4");
		assertThat(group.getElement().get(0).getDisplay()).isEqualTo("Interferon.beta");
		assertThat(group.getElement().get(0).getTarget()).hasSize(1);
		assertThat(group.getElement().get(0).getTarget().get(0).getCode()).isEqualTo("420710006");
		assertThat(group.getElement().get(0).getTarget().get(0).getDisplay()).isEqualTo("Interferon beta (substance)");

		// Document Ontology ValueSet
		vs = valueSets.get(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_ID);
		assertThat(vs.getName()).isEqualTo(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_NAME);
		assertThat(vs.getUrl()).isEqualTo(LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_URI);
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(3);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("11488-4");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("Consult note");
		assertThat(vs.getVersion()).isNull();

		// Document ontology parts
		code = concepts.get("11488-4");
		assertThat(code.getCodingProperties("document-kind")).hasSize(1);
		assertThat(code.getCodingProperties("document-kind").get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties("document-kind").get(0).getCode()).isEqualTo("LP173418-7");
		assertThat(code.getCodingProperties("document-kind").get(0).getDisplay()).isEqualTo("Note");

		// RSNA Playbook ValueSet
		vs = valueSets.get(LoincRsnaPlaybookHandler.RSNA_CODES_VS_ID);
		assertThat(vs.getName()).isEqualTo(LoincRsnaPlaybookHandler.RSNA_CODES_VS_NAME);
		assertThat(vs.getUrl()).isEqualTo(LoincRsnaPlaybookHandler.RSNA_CODES_VS_URI);
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(3);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("17787-3");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("NM Thyroid gland Study report");
		assertThat(vs.getVersion()).isNull();

		// RSNA Playbook Code Parts - Region Imaged
		code = concepts.get("17787-3");
		String propertyName = "rad-anatomic-location-region-imaged";
		assertThat(code.getCodingProperties(propertyName)).hasSize(1);
		assertThat(code.getCodingProperties(propertyName).get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties(propertyName).get(0).getCode()).isEqualTo("LP199995-4");
		assertThat(code.getCodingProperties(propertyName).get(0).getDisplay()).isEqualTo("Neck");
		// RSNA Playbook Code Parts - Imaging Focus
		code = concepts.get("17787-3");
		propertyName = "rad-anatomic-location-imaging-focus";
		assertThat(code.getCodingProperties(propertyName)).hasSize(1);
		assertThat(code.getCodingProperties(propertyName).get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties(propertyName).get(0).getCode()).isEqualTo("LP206648-0");
		assertThat(code.getCodingProperties(propertyName).get(0).getDisplay()).isEqualTo("Thyroid gland");
		// RSNA Playbook Code Parts - Modality Type
		code = concepts.get("17787-3");
		propertyName = "rad-modality-modality-type";
		assertThat(code.getCodingProperties(propertyName)).hasSize(1);
		assertThat(code.getCodingProperties(propertyName).get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties(propertyName).get(0).getCode()).isEqualTo("LP208891-4");
		assertThat(code.getCodingProperties(propertyName).get(0).getDisplay()).isEqualTo("NM");

		// RSNA Playbook - LOINC Part -> RadLex RID Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID);
		assertThat(conceptMap.getUrl()).isEqualTo(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_URI);
		assertThat(conceptMap.getVersion()).isEqualTo("Beta.1");
		assertThat(conceptMap.getName()).isEqualTo(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_NAME);
		assertThat(conceptMap.getGroup()).hasSize(1);
		group = conceptMap.getGroupFirstRep();
		// all entries have the same source and target so these should be null
		assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(group.getSourceVersion()).isNull();
		assertThat(group.getTarget()).isEqualTo(LoincRsnaPlaybookHandler.RID_CS_URI);
		assertThat(group.getElement().get(0).getCode()).isEqualTo("LP199995-4");
		assertThat(group.getElement().get(0).getDisplay()).isEqualTo("Neck");
		assertThat(group.getElement().get(0).getTarget()).hasSize(1);
		assertThat(group.getElement().get(0).getTarget().get(0).getCode()).isEqualTo("RID7488");
		assertThat(group.getElement().get(0).getTarget().get(0).getDisplay()).isEqualTo("neck");
		assertThat(group.getElement().get(0).getTarget().get(0).getEquivalence()).isEqualTo(Enumerations.ConceptMapEquivalence.EQUAL);

		// RSNA Playbook - LOINC Term -> RadLex RPID Mappings
		conceptMap = conceptMaps.get(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID);
		assertThat(conceptMap.getUrl()).isEqualTo(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_URI);
		assertThat(conceptMap.getVersion()).isEqualTo("Beta.1");
		assertThat(conceptMap.getName()).isEqualTo(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_NAME);
		assertThat(conceptMap.getGroup()).hasSize(1);
		group = conceptMap.getGroupFirstRep();
		// all entries have the same source and target so these should be null
		assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(group.getSourceVersion()).isNull();
		assertThat(group.getTarget()).isEqualTo(LoincRsnaPlaybookHandler.RPID_CS_URI);
		assertThat(group.getElement().get(0).getCode()).isEqualTo("24531-6");
		assertThat(group.getElement().get(0).getDisplay()).isEqualTo("US Retroperitoneum");
		assertThat(group.getElement().get(0).getTarget()).hasSize(1);
		assertThat(group.getElement().get(0).getTarget().get(0).getCode()).isEqualTo("RPID2142");
		assertThat(group.getElement().get(0).getTarget().get(0).getDisplay()).isEqualTo("US Retroperitoneum");
		assertThat(group.getElement().get(0).getTarget().get(0).getEquivalence()).isEqualTo(Enumerations.ConceptMapEquivalence.EQUAL);

		if (theIncludeTop2000) {
			// TOP 2000 - US
			vs = valueSets.get(LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_ID);
			assertThat(LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_NAME).isEqualTo(vs.getName());
			assertThat(LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_URI).isEqualTo(vs.getUrl());
			assertThat(vs.getCompose().getInclude()).hasSize(1);
			assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(9);
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("2160-0");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("Creatinine [Mass/volume] in Serum or Plasma");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(1).getCode()).isEqualTo("718-7");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(1).getDisplay()).isEqualTo("Hemoglobin [Mass/volume] in Blood");
			assertThat(vs.getVersion()).isNull();

			// TOP 2000 - SI
			vs = valueSets.get(LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_ID);
			assertThat(LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_NAME).isEqualTo(vs.getName());
			assertThat(LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_URI).isEqualTo(vs.getUrl());
			assertThat(vs.getCompose().getInclude()).hasSize(1);
			assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(9);
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("14682-9");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("Creatinine [Moles/volume] in Serum or Plasma");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(1).getCode()).isEqualTo("718-7");
			assertThat(vs.getCompose().getInclude().get(0).getConcept().get(1).getDisplay()).isEqualTo("Hemoglobin [Mass/volume] in Blood");
			assertThat(vs.getVersion()).isNull();
		}

		// Universal lab order VS
		vs = valueSets.get(LoincUniversalOrderSetHandler.VS_ID_BASE);
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(9);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("42176-8");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("1,3 beta glucan [Mass/volume] in Serum");
		assertThat(vs.getVersion()).isNull();

		// All LOINC codes
		assertThat(valueSets).containsKey(LOINC_ALL_VALUESET_ID);
		vs = valueSets.get(LOINC_ALL_VALUESET_ID);
		assertThat(vs.getUrl()).isEqualTo("http://loinc.org/vs");
		assertThat(vs.getVersion()).isNull();
		assertThat(vs.getName()).isEqualTo("All LOINC codes");
		assertThat(vs.getStatus()).isEqualTo(Enumerations.PublicationStatus.ACTIVE);
		assertThat(vs.hasDate()).isTrue();
		assertThat(vs.getPublisher()).isEqualTo("Regenstrief Institute, Inc.");
		assertThat(vs.getDescription()).isEqualTo("A value set that includes all LOINC codes");
		assertThat(vs.getCopyright()).isEqualTo(expectedLoincCopyright);
		assertThat(vs.hasCompose()).isTrue();
		assertThat(vs.getCompose().hasInclude()).isTrue();
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getVersion()).isNull();

		// IEEE Medical Device Codes
		conceptMap = conceptMaps.get(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_ID);
		ourLog.debug(FhirContext.forR4Cached().newXmlParser().setPrettyPrint(true).encodeResourceToString(conceptMap));
		assertThat(conceptMap.getName()).isEqualTo(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_NAME);
		assertThat(conceptMap.getUrl()).isEqualTo(LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_URI);
		assertThat(conceptMap.getVersion()).isEqualTo("Beta.1");
		assertThat(conceptMap.getGroup()).hasSize(1);
		assertThat(conceptMap.getGroup().get(0).getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(conceptMap.getGroup().get(0).getTarget()).isEqualTo(ITermLoaderSvc.IEEE_11073_10101_URI);
		assertThat(conceptMap.getGroup().get(0).getElement()).hasSize(7);
		assertThat(conceptMap.getGroup().get(0).getElement().get(4).getCode()).isEqualTo("14749-6");
		assertThat(conceptMap.getGroup().get(0).getElement().get(4).getDisplay()).isEqualTo("Glucose [Moles/volume] in Serum or Plasma");
		assertThat(conceptMap.getGroup().get(0).getElement().get(4).getTarget()).hasSize(2);
		assertThat(conceptMap.getGroup().get(0).getElement().get(4).getTarget().get(0).getCode()).isEqualTo("160196");
		assertThat(conceptMap.getGroup().get(0).getElement().get(4).getTarget().get(0).getDisplay()).isEqualTo("MDC_CONC_GLU_VENOUS_PLASMA");

		// Imaging Document Codes
		vs = valueSets.get(LoincImagingDocumentCodeHandler.VS_ID_BASE);
		assertThat(vs.getUrl()).isEqualTo(LoincImagingDocumentCodeHandler.VS_URI);
		assertThat(vs.getName()).isEqualTo(LoincImagingDocumentCodeHandler.VS_NAME);
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(9);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("11525-3");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay()).isEqualTo("US Pelvis Fetus for pregnancy");

		// Group - Parent
		vs = valueSets.get("LG100-4");
		ourLog.debug(FhirContext.forR4Cached().newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		assertThat(vs.getName()).isEqualTo("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>");
		assertThat(vs.getUrl()).isEqualTo("http://loinc.org/vs/LG100-4");
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getValueSet()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getValueSet().get(0).getValueAsString()).isEqualTo("http://loinc.org/vs/LG1695-8");

		// Group - Child
		vs = valueSets.get("LG1695-8");
		ourLog.debug(FhirContext.forR4Cached().newXmlParser().setPrettyPrint(true).encodeResourceToString(vs));
		assertThat(vs.getName()).isEqualTo("1,4-Dichlorobenzene|MCnc|Pt|ANYBldSerPl");
		assertThat(vs.getUrl()).isEqualTo("http://loinc.org/vs/LG1695-8");
		assertThat(vs.getCompose().getInclude()).hasSize(1);
		assertThat(vs.getCompose().getInclude().get(0).getConcept()).hasSize(2);
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(0).getCode()).isEqualTo("17424-3");
		assertThat(vs.getCompose().getInclude().get(0).getConcept().get(1).getCode()).isEqualTo("13006-2");

		// Consumer Name
		if (theIncludeConsumerNameAndLinguisticVariants) {
		    code = concepts.get("61438-8");
			assertThat(code.getDesignations()).hasSize(8);
		    TermTestUtil.verifyConsumerName(code.getDesignations(), "Consumer Name 61438-8");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "de-AT", "Entlassungsbrief Ärztlich","Ergebnis","Zeitpunkt","{Setting}","Dokument","Dermatologie","DOC.ONTOLOGY","de shortname","de long common name","de related names 2","de linguistic variant display name");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "fr-CA", "Cellules de Purkinje cytoplasmique type 2 , IgG","Titre","Temps ponctuel","Sérum","Quantitatif","Immunofluorescence","Sérologie","","","","");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "zh-CN", "血流速度.收缩期.最大值","速度","时间点","大脑中动脉","定量型","超声.多普勒","产科学检查与测量指标.超声","","", "Cereb 动态 可用数量表示的;定量性;数值型;数量型;连续数值型标尺 大脑（Cerebral） 时刻;随机;随意;瞬间 术语\"cerebral\"指的是主要由中枢半球（大脑皮质和基底神经节）组成的那部分脑结构 流 流量;流速;流体 血;全血 血流量;血液流量 速度(距离/时间);速率;速率(距离/时间)","");
		    code = concepts.get("17787-3");
			assertThat(code.getDesignations()).hasSize(5);
		    TermTestUtil.verifyConsumerName(code.getDesignations(), "Consumer Name 17787-3");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "de-AT", "","","","","","","","","","CoV OC43 RNA ql/SM P","Coronavirus OC43 RNA ql. /Sondermaterial PCR");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "fr-CA", "Virus respiratoire syncytial bovin","Présence-Seuil","Temps ponctuel","XXX","Ordinal","Culture spécifique à un microorganisme","Microbiologie","","","","");
		    TermTestUtil.verifyLinguisticVariant(code.getDesignations(), "zh-CN", "血流速度.收缩期.最大值","速度","时间点","二尖瓣^胎儿","定量型","超声.多普勒","产科学检查与测量指标.超声","","","僧帽瓣 动态 可用数量表示的;定量性;数值型;数量型;连续数值型标尺 时刻;随机;随意;瞬间 流 流量;流速;流体 胎;超系统 - 胎儿 血;全血 血流量;血液流量 速度(距离/时间);速率;速率(距离/时间)","");
		}
	}

	@Test
	public void testLoadLoincMultipleVersions() throws IOException {

		// Load LOINC marked as version 2.67

		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(2)).storeNewCodeSystemVersion(mySystemCaptor_267_first.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor_267_first.capture(), myConceptMapCaptor_267_first.capture());
		List<CodeSystem> loincCSResources = mySystemCaptor_267_first.getAllValues();
		assertThat(loincCSResources).hasSize(2);
		assertThat(loincCSResources.get(0).getVersion()).isEqualTo("2.67");
		assertThat(loincCSResources.get(1).getVersion()).isNull();

		List<List<ValueSet>> loincVS_resourceLists = myValueSetsCaptor_267_first.getAllValues();
		assertThat(loincVS_resourceLists).hasSize(2);
		List<ValueSet> loincVS_resources = loincVS_resourceLists.get(0);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1-2.67");
			} else {
				assertThat(loincVS.getVersion()).isEqualTo("2.67");
			}
		}
		loincVS_resources = loincVS_resourceLists.get(1);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1");
			} else {
				assertThat(loincVS.getVersion()).isNull();
			}
		}

		List<List<ConceptMap>> loincCM_resourceLists = myConceptMapCaptor_267_first.getAllValues();
		assertThat(loincCM_resourceLists).hasSize(2);
		List<ConceptMap> loincCM_resources = loincCM_resourceLists.get(0);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1-2.67");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isEqualTo("2.67");
		}
		loincCM_resources = loincCM_resourceLists.get(1);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isNull();
		}

		reset(myTermCodeSystemStorageSvc);

		// Update LOINC marked as version 2.67
		myFiles = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(2)).storeNewCodeSystemVersion(mySystemCaptor_267_second.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor_267_second.capture(), myConceptMapCaptor_267_second.capture());
		loincCSResources = mySystemCaptor_267_second.getAllValues();
		assertThat(loincCSResources).hasSize(2);
		assertThat(loincCSResources.get(0).getVersion()).isEqualTo("2.67");
		assertThat(loincCSResources.get(1).getVersion()).isNull();

		loincVS_resourceLists = myValueSetsCaptor_267_second.getAllValues();
		assertThat(loincVS_resourceLists).hasSize(2);
		loincVS_resources = loincVS_resourceLists.get(0);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1-2.67");
			} else {
				assertThat(loincVS.getVersion()).isEqualTo("2.67");
			}
		}
		loincVS_resources = loincVS_resourceLists.get(1);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1");
			} else {
				assertThat(loincVS.getVersion()).isNull();
			}
		}

		loincCM_resourceLists = myConceptMapCaptor_267_second.getAllValues();
		assertThat(loincCM_resourceLists).hasSize(2);
		loincCM_resources = loincCM_resourceLists.get(0);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1-2.67");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isEqualTo("2.67");
		}
		loincCM_resources = loincCM_resourceLists.get(1);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isNull();
		}

		reset(myTermCodeSystemStorageSvc);

		// Load LOINC marked as version 2.68
		myFiles = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v268_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(2)).storeNewCodeSystemVersion(mySystemCaptor_268.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor_268.capture(), myConceptMapCaptor_268.capture());
		loincCSResources = mySystemCaptor_268.getAllValues();
		assertThat(loincCSResources).hasSize(2);
		assertThat(loincCSResources.get(0).getVersion()).isEqualTo("2.68");
		assertThat(loincCSResources.get(1).getVersion()).isNull();

		loincVS_resourceLists = myValueSetsCaptor_268.getAllValues();
		assertThat(loincVS_resourceLists).hasSize(2);
		loincVS_resources = loincVS_resourceLists.get(0);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1-2.68");
			} else {
				assertThat(loincVS.getVersion()).isEqualTo("2.68");
			}
		}
		loincVS_resources = loincVS_resourceLists.get(1);
		for (ValueSet loincVS : loincVS_resources) {
			if (loincVS.getId().startsWith("LL1000-0") || loincVS.getId().startsWith("LL1001-8") || loincVS.getId().startsWith("LL1892-0")) {
				assertThat(loincVS.getVersion()).isEqualTo("Beta.1");
			} else {
				assertThat(loincVS.getVersion()).isNull();
			}
		}

		loincCM_resourceLists = myConceptMapCaptor_268.getAllValues();
		assertThat(loincCM_resourceLists).hasSize(2);
		loincCM_resources = loincCM_resourceLists.get(0);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1-2.68");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isEqualTo("2.68");
		}
		loincCM_resources = loincCM_resourceLists.get(1);
		for (ConceptMap loincCM : loincCM_resources) {
			assertThat(loincCM.getVersion()).isEqualTo("Beta.1");
			assertThat(loincCM.getGroup()).hasSize(1);
			ConceptMap.ConceptMapGroupComponent group = loincCM.getGroup().get(0);
			assertThat(group.getSource()).isEqualTo(ITermLoaderSvc.LOINC_URI);
			assertThat(group.getSourceVersion()).isNull();
		}

	}

	@Test
	public void testLoadLoincMissingMandatoryFiles() throws IOException {
		myFiles.addFileZip("/loinc/", LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		myFiles.addFileZip("/loinc/", LOINC_GROUP_FILE_DEFAULT.getCode());

		// Actually do the load
		try {
			mySvc.loadLoinc(myFiles.getFiles(), mySrd);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Could not find the following mandatory files in input:");
			assertThat(e.getMessage()).contains("Loinc.csv");
			assertThat(e.getMessage()).contains("MultiAxialHierarchy.csv");
		}
	}


	@Test
	public void testLoadLoincMultiaxialHierarchySupport() throws Exception {
		TermTestUtil.addLoincMandatoryFilesToZip(myFiles);

		// Actually do the load
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();

		TermConcept code;

		// Normal LOINC code
		code = concepts.get("10013-1");
		assertThat(code.getCode()).isEqualTo("10013-1");
		assertThat(code.getCodingProperties("PROPERTY").get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties("PROPERTY").get(0).getCode()).isEqualTo("LP6802-5");
		assertThat(code.getCodingProperties("PROPERTY").get(0).getDisplay()).isEqualTo("Elpot");
		assertThat(code.getCodingProperties("PROPERTY").get(0).getSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(code.getCodingProperties("PROPERTY").get(0).getCode()).isEqualTo("LP6802-5");
		assertThat(code.getCodingProperties("PROPERTY").get(0).getDisplay()).isEqualTo("Elpot");
		assertThat(code.getStringProperty("CLASSTYPE")).isEqualTo("2");
		assertThat(code.getDisplay()).isEqualTo("R' wave amplitude in lead I");

		// Codes with parent and child properties
		code = concepts.get("LP31755-9");
		assertThat(code.getCode()).isEqualTo("LP31755-9");
		List<TermConceptProperty> properties = new ArrayList<>(code.getProperties());
		assertThat(properties).hasSize(1);
		assertThat(properties.get(0).getKey()).isEqualTo("child");
		assertThat(properties.get(0).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(0).getValue()).isEqualTo("LP14559-6");
		assertThat(properties.get(0).getDisplay()).isEqualTo("Microorganism");
		assertThat(code.getParents()).isEmpty();
		assertThat(code.getChildren()).hasSize(1);

		TermConcept childCode = code.getChildren().get(0).getChild();
		assertThat(childCode.getCode()).isEqualTo("LP14559-6");
		assertThat(childCode.getDisplay()).isEqualTo("Microorganism");

		properties = new ArrayList<>(childCode.getProperties());
		assertThat(properties).hasSize(2);
		assertThat(properties.get(0).getKey()).isEqualTo("parent");
		assertThat(properties.get(0).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(0).getValue()).isEqualTo(code.getCode());
		assertThat(properties.get(0).getDisplay()).isEqualTo(code.getDisplay());
		assertThat(properties.get(1).getKey()).isEqualTo("child");
		assertThat(properties.get(1).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(1).getValue()).isEqualTo("LP98185-9");
		assertThat(properties.get(1).getDisplay()).isEqualTo("Bacteria");
		assertThat(childCode.getParents()).hasSize(1);
		assertThat(childCode.getChildren()).hasSize(1);
		assertThat(new ArrayList<>(childCode.getParents()).get(0).getParent().getCode()).isEqualTo(code.getCode());

		TermConcept nestedChildCode = childCode.getChildren().get(0).getChild();
		assertThat(nestedChildCode.getCode()).isEqualTo("LP98185-9");
		assertThat(nestedChildCode.getDisplay()).isEqualTo("Bacteria");

		properties = new ArrayList<>(nestedChildCode.getProperties());
		assertThat(properties).hasSize(2);
		assertThat(properties.get(0).getKey()).isEqualTo("parent");
		assertThat(properties.get(0).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(0).getValue()).isEqualTo(childCode.getCode());
		assertThat(properties.get(0).getDisplay()).isEqualTo(childCode.getDisplay());
		assertThat(properties.get(1).getKey()).isEqualTo("child");
		assertThat(properties.get(1).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(1).getValue()).isEqualTo("LP14082-9");
		assertThat(properties.get(1).getDisplay()).isEqualTo("Bacteria");
		assertThat(nestedChildCode.getParents()).hasSize(1);
		assertThat(nestedChildCode.getChildren()).hasSize(1);
		assertThat(new ArrayList<>(nestedChildCode.getParents()).get(0).getParent().getCode()).isEqualTo(childCode.getCode());

		TermConcept doublyNestedChildCode = nestedChildCode.getChildren().get(0).getChild();
		assertThat(doublyNestedChildCode.getCode()).isEqualTo("LP14082-9");
		assertThat(doublyNestedChildCode.getDisplay()).isEqualTo("Bacteria");

		properties = new ArrayList<>(doublyNestedChildCode.getProperties());
		assertThat(properties).hasSize(4);
		assertThat(properties.get(0).getKey()).isEqualTo("parent");
		assertThat(properties.get(0).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(0).getValue()).isEqualTo(nestedChildCode.getCode());
		assertThat(properties.get(0).getDisplay()).isEqualTo(nestedChildCode.getDisplay());
		assertThat(properties.get(1).getKey()).isEqualTo("child");
		assertThat(properties.get(1).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(1).getValue()).isEqualTo("LP52258-8");
		assertThat(properties.get(1).getDisplay()).isEqualTo("Bacteria | Body Fluid");
		assertThat(properties.get(2).getKey()).isEqualTo("child");
		assertThat(properties.get(2).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(2).getValue()).isEqualTo("LP52260-4");
		assertThat(properties.get(2).getDisplay()).isEqualTo("Bacteria | Cerebral spinal fluid");
		assertThat(properties.get(3).getKey()).isEqualTo("child");
		assertThat(properties.get(3).getCodeSystem()).isEqualTo(ITermLoaderSvc.LOINC_URI);
		assertThat(properties.get(3).getValue()).isEqualTo("LP52960-9");
		assertThat(properties.get(3).getDisplay()).isEqualTo("Bacteria | Cervix");
		assertThat(doublyNestedChildCode.getParents()).hasSize(1);
		assertThat(doublyNestedChildCode.getChildren()).hasSize(3);
		assertThat(new ArrayList<>(doublyNestedChildCode.getParents()).get(0).getParent().getCode()).isEqualTo(nestedChildCode.getCode());
		assertThat(doublyNestedChildCode.getChildren().get(0).getChild().getCode()).isEqualTo("LP52258-8");
		assertThat(doublyNestedChildCode.getChildren().get(1).getChild().getCode()).isEqualTo("LP52260-4");
		assertThat(doublyNestedChildCode.getChildren().get(2).getChild().getCode()).isEqualTo("LP52960-9");
	}




	@Nested
	public class LoadLoincCurrentVersion {
		private TermLoaderSvcImpl testedSvc;
		private final Properties testProps = new Properties();

		@Mock private final LoadedFileDescriptors mockFileDescriptors = mock(LoadedFileDescriptors.class);
		@SuppressWarnings("unchecked")
		@Mock private final List<ITermLoaderSvc.FileDescriptor> mockFileDescriptorList = mock(List.class);
		@Mock private final ITermCodeSystemStorageSvc mockCodeSystemStorageSvc = mock(ITermCodeSystemStorageSvc.class);
		private final RequestDetails requestDetails = new ServletRequestDetails();


		@BeforeEach
		void beforeEach() {
			testedSvc = spy(mySvc);
			doReturn(testProps).when(testedSvc).getProperties(any(), eq(LOINC_UPLOAD_PROPERTIES_FILE.getCode()));
			requestDetails.setOperation(JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM);
		}


		@Test
		public void testDontMakeCurrentVersion() throws IOException {
			TermTestUtil.addLoincMandatoryFilesToZip(myFiles);
			testProps.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "false");
			testProps.put(LOINC_CODESYSTEM_VERSION.getCode(), "27.0");

			testedSvc.loadLoinc(myFiles.getFiles(), requestDetails);

			verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(
				any(CodeSystem.class), any(TermCodeSystemVersion.class), myRequestDetailsCaptor.capture(), any(), any());

			myRequestDetailsCaptor.getAllValues().forEach( rd ->
				assertFalse(rd.getUserData() == null ||
					(boolean) requestDetails.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE))
			);
		}


		@Test
		public void testMakeCurrentVersionPropertySet() {
			testProps.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "true");
			testProps.put(LOINC_CODESYSTEM_VERSION.getCode(), "27.0");
			doReturn(mockFileDescriptors).when(testedSvc).getLoadedFileDescriptors(mockFileDescriptorList);
			doReturn(mock(UploadStatistics.class)).when(testedSvc).processLoincFiles(
				eq(mockFileDescriptors), eq(requestDetails), eq(testProps), any());

			testedSvc.loadLoinc(mockFileDescriptorList, requestDetails);

			boolean isMakeCurrent = requestDetails.getUserData() == null ||
				(boolean) requestDetails.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE);
			assertThat(isMakeCurrent).isTrue();
		}


		@Test
		public void testMakeCurrentVersionByDefaultPropertySet() {
			testProps.put(LOINC_CODESYSTEM_VERSION.getCode(), "27.0");
			doReturn(mockFileDescriptors).when(testedSvc).getLoadedFileDescriptors(mockFileDescriptorList);
			doReturn(mock(UploadStatistics.class)).when(testedSvc).processLoincFiles(
				eq(mockFileDescriptors), eq(requestDetails), eq(testProps), any());

			testedSvc.loadLoinc(mockFileDescriptorList, requestDetails);

			boolean isMakeCurrent = requestDetails.getUserData() == null ||
				(boolean) requestDetails.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE);
			assertThat(isMakeCurrent).isTrue();
		}


		@Test
		public void testDontMakeCurrentVersionPropertySet() {
			testProps.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "false");
			testProps.put(LOINC_CODESYSTEM_VERSION.getCode(), "27.0");
			doReturn(mockFileDescriptors).when(testedSvc).getLoadedFileDescriptors(mockFileDescriptorList);
			doReturn(mock(UploadStatistics.class)).when(testedSvc).processLoincFiles(
				eq(mockFileDescriptors), eq(requestDetails), eq(testProps), any());

			testedSvc.loadLoinc(mockFileDescriptorList, requestDetails);

			boolean isMakeCurrent = requestDetails.getUserData() == null ||
				(boolean) requestDetails.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE);
			assertThat(isMakeCurrent).isFalse();
		}


		@Test
		public void testNoVersionAndNoMakeCurrentThrows() {
			testProps.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "false");
			doReturn(mockFileDescriptors).when(testedSvc).getLoadedFileDescriptors(mockFileDescriptorList);

			InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
				() -> testedSvc.loadLoinc(mockFileDescriptorList, mySrd) );

			assertThat(thrown.getMessage()).isEqualTo(Msg.code(864) + "'" + LOINC_CODESYSTEM_VERSION.getCode() + "' property is required when '" +
				LOINC_CODESYSTEM_MAKE_CURRENT.getCode() + "' property is 'false'");
		}

	}


}
