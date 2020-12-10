package ca.uhn.fhir.cql.dstu3.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.InMemoryLibraryResourceProvider;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.parser.IParser;
import com.jamesmurty.utils.XMLBuilder2;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Contributor;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.MarkdownType;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jsoup.Jsoup;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.opencds.cqf.tooling.measure.stu3.CodeTerminologyRef;
import org.opencds.cqf.tooling.measure.stu3.CqfMeasure;
import org.opencds.cqf.tooling.measure.stu3.TerminologyRef;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HQMFProvider {

	private static Map<String, String> measureTypeValueSetMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("PROCESS", "Process");
			put("OUTCOME", "Outcome");
			put("STRUCTURE", "Structure");
			put("PATIENT-REPORTED-OUTCOME", "Patient Reported Outcome");
			put("COMPOSITE", "Composite");
		}
	};

	private static Map<String, String> measureScoringValueSetMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("PROPOR", "Proportion");
			put("RATIO", "Ratio");
			put("CONTINUOUS-VARIABLE", "Continuous Variable");
			put("COHORT", "Cohort");
		}
	};

	public static class CodeMapping {
		public CodeMapping(String code, String displayName, String criteriaName, String criteriaExtension) {
			this.code = code;
			this.displayName = displayName;
			this.criteriaName = criteriaName;
			this.criteriaExtension = criteriaExtension;
		}

		public String code;
		public String displayName;
		public String criteriaName;
		public String criteriaExtension;

	}

	public static Map<String, CodeMapping> measurePopulationValueSetMap = new HashMap<String, CodeMapping>() {
		private static final long serialVersionUID = 1L;

		{
			put("initial-population", new CodeMapping("IPOP", "Initial Population", "initialPopulationCriteria", "initialPopulation"));
			put("numerator", new CodeMapping("NUMER", "Numerator", "numeratorCriteria", "numerator"));
			put("numerator-exclusion", new CodeMapping("NUMEX", "Numerator Exclusion", "numeratorExclusionCriteria", "numeratorExclusions"));
			put("denominator", new CodeMapping("DENOM", "Denominator", "denominatorCriteria", "denominator"));
			put("denominator-exclusion", new CodeMapping("DENEX", "Denominator Exclusion", "denominatorExclusionCriteria", "denominatorExclusions"));
			put("denominator-exception", new CodeMapping("DENEXCEP", "Denominator Exception", "denominatorExceptionCriteria", "denominatorExceptions"));
			// TODO: Figure out what the codes for these are (MPOP, MPOPEX, MPOPEXCEP are guesses)
			put("measure-population", new CodeMapping("MPOP", "Measure Population", "measurePopulationCriteria", "measurePopulation"));
			put("measure-population-exclusion", new CodeMapping("MPOPEX", "Measure Population Exclusion", "measurePopulationExclusionCriteria", "measurePopulationExclusions"));
			put("measure-observation", new CodeMapping("MOBS", "Measure Observation", "measureObservationCriteria", "measureObservations"));
		}
	};

	public String generateHQMF(CqfMeasure m) {
		XMLBuilder2 xml = createQualityMeasureDocumentElement(m);
		this.addResponsibleParties(xml, m);
		this.addDefinitions(xml, m);

		String primaryLibraryGuid = UUID.randomUUID().toString();
		String primaryLibraryName = this.addRelatedDocuments(xml, m, primaryLibraryGuid);
		this.addControlVariables(xml, m);
		this.addSubjectOfs(xml, m);
		this.addComponentOfs(xml, m);
		this.addComponents(xml, m, primaryLibraryGuid, primaryLibraryName);
		return writeDocument(xml.getDocument());
	}

	private XMLBuilder2 createQualityMeasureDocumentElement(CqfMeasure m) {
		// HQMF expects a unique Id and a version independent Id.
		String id = this.stripHistory(m.getId());
		String setId = this.resolveSetId(m);

		XMLBuilder2 builder = XMLBuilder2.create("QualityMeasureDocument").ns("urn:hl7-org:v3")
			.ns("cql-ext", "urn:hhs-cql:hqmf-n1-extensions:v1")
			.ns("xsi", "http://www.w3.org/2001/XMLSchema-instance")
			.elem("typeId").a("extension", "POQM_HD000001UV02").a("root", "2.16.840.1.113883.1.3").up()
			.elem("templateId").elem("item").a("extension", "2018-05-01").a("root", "2.16.840.1.113883.10.20.28.1.2").up().up()
			.elem("id").a("root", id).up().up().elem("code")
			.a("code", "57024-2").a("codeSystem", "2.16.840.1.113883.6.1").elem("displayName")
			.a("value", "Health Quality Measure Document").up().up()
			.elem("title").a("value", m.hasTitle() ? m.getTitle() : "None").up()
			.elem("text").a("value", m.hasDescription() ? m.getDescription() : "None").up()
			.elem("statusCode").a("code", "COMPLETED").up()
			.elem("setId").a("root", setId).up()
			.elem("versionNumber").a("value", m.hasVersion() ? m.getVersion() : "None").up()
			.root();

		return builder;
	}

	// Looks for the MAT set id. If not found, returns the measure name.
	private String resolveSetId(CqfMeasure m) {
		Identifier id = this.getIdentifierFor(m, "hqmf-set-id");
		if (id != null && id.hasValue() && !id.getValue().isEmpty()) {
			return id.getValue();
		}

		return m.getName();
	}

	private void addDefinitions(XMLBuilder2 xml, CqfMeasure m) {

		if (m.hasTerminology())
		{
			for (TerminologyRef t : m.getTerminology()) {
				if (t.getType() == TerminologyRef.TerminologyRefType.VALUESET) {
					this.addValueSet(xml, t);
				}
			}

			for (TerminologyRef t : m.getTerminology()) {
				if (t.getType() == TerminologyRef.TerminologyRefType.CODE) {
					this.addDirectReferenceCode(xml, (CodeTerminologyRef)t);
				}
			}
		}
	}

	private void addValueSet(XMLBuilder2 xml, TerminologyRef t) {
		xml.root().elem("definition").elem("valueSet").a("classCode", "OBS").a("moodCode", "DEF").elem("id")
			.a("root", t.getId()).up()
			.elem("title").a("value", t.getName());
	}

	private void addDirectReferenceCode(XMLBuilder2 xml, CodeTerminologyRef t) {
		XMLBuilder2 temp = xml.root().elem("definition").elem("cql-ext:code").a("code", t.getId()).a("codeSystem", t.getcodeSystemId()).a("codeSystemName", t.getcodeSystemName());
		if (t.getdisplayName() != null) {
			temp.elem("displayName").a("value", t.getdisplayName()).up();
		}
	}

	private String stripHistory(String id) {
		if (id.contains("/_history")) {
			id = id.substring(0, id.indexOf("/_history"));
		}

		return id;
	}

	// Returns the name of the primary library once the documents are added.
	private String addRelatedDocuments(XMLBuilder2 xml, CqfMeasure m, String primaryLibraryGuid) {
		String primaryLibraryId = m.getLibraryFirstRep().getReference();
		String primaryLibraryName = null;

		for (Library l : m.getLibraries()) {
			String guid = UUID.randomUUID().toString();
			String name = l.getName();
			String version = l.getVersion();
			String id = this.stripHistory(l.getId());
			if (id.equals(primaryLibraryId)) {
				primaryLibraryName = name;
				guid = primaryLibraryGuid;
			}

			this.addRelatedDocument(xml, name + "-" + version, guid);
		}

		return primaryLibraryName;
	}

	// Returns the random guid assigned to a document
	private void addRelatedDocument(XMLBuilder2 xml, String name, String guid) {
		xml.root().elem("relatedDocument").a("typeCode", "COMP").elem("expressionDocument").elem("id").a("root", guid)
			.up().elem("text").a("mediaType", "text/cql").elem("reference").a("value", name + ".cql").up()
			.elem("translation").a("mediaType", "application/elm+xml").elem("reference").a("value", name + ".xml")
			.up().up().elem("translation").a("mediaType", "application/elm+json").elem("reference")
			.a("value", name + ".json");
	}

	private void addControlVariables(XMLBuilder2 xml, CqfMeasure m) {
		// TODO: These are parameters?

		// Measure Period
		// TODO: Same as effective period?
		if (m.hasEffectivePeriod() && m.getEffectivePeriod().hasStart()) {
			this.addMeasurePeriod(xml, m.getEffectivePeriod());
		}
	}

	private void addMeasurePeriod(XMLBuilder2 xml, Period p) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		xml.root().elem("controlVariable").elem("measurePeriod").elem("id").a("extension", "measureperiod")
			.a("root", "cfe828b5-ce93-4354-b137-77b0aeb41dd6").up().elem("code").a("code", "MSRTP")
			.a("codeSystem", "2.16.840.1.113883.5.4").elem("originalText").a("value", "Measurement Period").up()
			.up().elem("value").a("xsi:type", "PIVL_TS").elem("phase").a("highClosed", "true")
			.a("lowClosed", "true").elem("low").a("value", sdf.format(p.getStart())).up().elem("width")
			.a("unit", "a").a("value", "1").a("xsi:type", "PQ").up().up().elem("period").a("unit", "a")
			.a("value", "1");
	}

	private Identifier getIdentifierFor(CqfMeasure m, String identifierCode) {
		for (Identifier i : m.getIdentifier())
		{
			if (i.hasType())
			{
				if(i.getType().getCodingFirstRep().getCode().equalsIgnoreCase(identifierCode))
				{
					return i;
				}
			}
		}

		return null;
	}

	private void addSubjectOfs(XMLBuilder2 xml, CqfMeasure m) {
		String codeSystem = "2.16.840.1.113883.5.4";

		// TODO: What to do with the NCQA Identifier?

		// CMS Identifier
		Identifier cms = this.getIdentifierFor(m, "CMS");
		if (cms!= null) {
			this.addMeasureAttributeWithNullAndText(xml, "OTH","eCQM Identifier", "text/plain", cms.getValue());
		}

		// NQF Identifer
		Identifier nqf = this.getIdentifierFor(m, "NQF");
		if (nqf != null) {
			this.addMeasureAttributeWithNullAndText(xml, "OTH","NQF Number", "text/plain", nqf.getValue());
		}

		// Copyright
		this.addMeasureAttributeWithCodeAndTextValue(xml, "COPY", codeSystem, "Copyright", "text/plain",
			m.hasCopyright() ? m.getCopyright() : "None");

		// Disclaimer
		this.addMeasureAttributeWithCodeAndTextValue(xml, "DISC", codeSystem, "Disclaimer", "text/plain",
			m.hasDisclaimer() ? m.getDisclaimer() : "None");

		// Measure Scoring
		if (m.hasScoring()) {
			Coding scoring = m.getScoring().getCodingFirstRep();
			String measureScoringCode = scoring.getCode().toUpperCase();

			// TODO: How do we fix this mapping?
			if (measureScoringCode.startsWith("PROPOR")) {
				measureScoringCode = "PROPOR";
			}

			this.addMeasureAttributeWithCodeAndCodeValue(xml, "MSRSCORE", codeSystem, "Measure Scoring",
				measureScoringCode, "2.16.840.1.113883.1.11.20367",
				measureScoringValueSetMap.get(measureScoringCode));
		}

		// Measure Type
		if (m.hasType()) {
			Coding measureType = m.getTypeFirstRep().getCoding().get(0);
			String measureTypeCode = measureType.getCode().toUpperCase();
			this.addMeasureAttributeWithCodeAndCodeValue(xml, "MSRTYPE", codeSystem, "Measure Type", measureTypeCode,
				"2.16.840.1.113883.1.11.20368", measureTypeValueSetMap.get(measureTypeCode));
		}

		// Risk Adjustment
		this.addMeasureAttributeWithCodeAndTextValue(xml, "MSRADJ", codeSystem, "Risk Adjustment", "text/plain",
			m.hasRiskAdjustment() ? m.getRiskAdjustment() : "None");

		// Rate Aggregation
		this.addMeasureAttributeWithCodeAndTextValue(xml, "MSRAGG", codeSystem, "Rate Aggregation", "text/plain",
			m.hasRateAggregation() ? m.getRateAggregation() : "None");

		// Rationale
		this.addMeasureAttributeWithCodeAndTextValue(xml, "RAT", codeSystem, "Rationale", "text/plain",
			m.hasRationale() ? m.getRationale() : "None");

		// Clinical Recommendation Statement
		this.addMeasureAttributeWithCodeAndTextValue(xml, "CRS", codeSystem, "Clinical Recommendation Statement",
			"text/plain", m.hasClinicalRecommendationStatement() ? m.getClinicalRecommendationStatement() : "None");

		// Improvement Notation
		this.addMeasureAttributeWithCodeAndTextValue(xml, "IDUR", codeSystem, "Improvement Notation", "text/plain",
			m.hasImprovementNotation() ? m.getImprovementNotation() : "None");

		// Reference (citations)
		if (m.hasRelatedArtifact()) {
			for (RelatedArtifact r : m.getRelatedArtifact()) {
				if (r.hasType() && r.getType() == RelatedArtifact.RelatedArtifactType.CITATION) {
					this.addMeasureAttributeWithCodeAndTextValue(xml, "REF", codeSystem, "Reference", "text/plain",
						r.getCitation());
				}
			}
		}

		// Definition
		if (m.hasDefinition()) {
			for (MarkdownType mt : m.getDefinition()) {
				this.addMeasureAttributeWithCodeAndTextValue(xml, "DEF", codeSystem, "Definition", "text/markdown",
					mt.asStringValue());
			}
		} else {
			this.addMeasureAttributeWithCodeAndTextValue(xml, "DEF", codeSystem, "Definition", "text/plain", "None");
		}

		// Guidance
		this.addMeasureAttributeWithCodeAndTextValue(xml, "GUIDE", codeSystem, "Guidance", "text/plain",
			m.hasGuidance() ? m.getGuidance() : "None");

		// Transmission Format
		this.addMeasureAttributeWithCodeAndTextValue(xml, "TRANF", codeSystem, "Transmission Format", "text/plain",
			"TBD");

		// TODO: Groups - It seems the HQMF measure supports descriptions for only  one group, the FHIR measure has descriptions per group
		if (m.hasGroup()) {
			Measure.MeasureGroupComponent mgc = m.getGroupFirstRep();
			this.addGroupMeasureAttributes(xml, codeSystem, mgc);
		}

		// TODO: Stratification - The HQMF measure has a description of the stratification, the FHIR measure does not.

		// TODO: Supplemental Data Elements - The HQMF measure has a description of the elements, the FHIR measure does not.
	}

	private void addGroupMeasureAttributes(XMLBuilder2 xml, String codeSystem, Measure.MeasureGroupComponent mgc) {
		for  (Map.Entry<String, CodeMapping> entry : measurePopulationValueSetMap.entrySet()) {
			String key = entry.getKey();
			Measure.MeasureGroupPopulationComponent mgpc = GetPopulationForKey(key, mgc);
			if (mgpc != null) {
				this.addMeasureAttributeWithCodeAndTextValue(xml,
					entry.getValue().code, codeSystem, entry.getValue().displayName,
					"text/plain", mgpc.hasDescription() ? mgpc.getDescription() : "None");
			}
		}
	}

	private Measure.MeasureGroupPopulationComponent GetPopulationForKey(String key, Measure.MeasureGroupComponent mgc) {
		for (Measure.MeasureGroupPopulationComponent mgpc : mgc.getPopulation()) {
			String mgpcIdentifier = mgpc.getCode().getCoding().get(0).getCode();
			if (key.equals(mgpcIdentifier)) {
				return mgpc;
			}
		}

		return null;
	}

	private void addMeasureAttributeWithCodeAndTextValue(XMLBuilder2 xml, String code, String codeSystem, String displayName, String mediaType, String value) {
		XMLBuilder2 temp = this.addMeasureAttribute(xml);
		this.addMeasureAttributeCode(temp, code, codeSystem, displayName);
		this.addMeasureAttributeValue(temp, mediaType, value, "ED");
	}

	private void addMeasureAttributeWithCodeAndCodeValue(XMLBuilder2 xml, String code, String codeSystem, String displayName, String valueCode, String valueCodeSystem, String valueDisplayName) {
		XMLBuilder2 temp = this.addMeasureAttribute(xml);
		this.addMeasureAttributeCode(temp, code, codeSystem, displayName);
		this.addMeasureAttributeValue(temp, valueCode, valueCodeSystem, "CD", valueDisplayName);
	}

	private void addMeasureAttributeWithNullAndText(XMLBuilder2 xml, String nullFlavor, String originalText, String mediaType, String value) {
		XMLBuilder2 temp = this.addMeasureAttribute(xml);
		this.addMeasureAttributeCode(temp, nullFlavor, originalText);
		this.addMeasureAttributeValue(temp, mediaType, value, "ED");
	}

	private XMLBuilder2 addMeasureAttribute(XMLBuilder2 xml) {
		return xml.root().elem("subjectOf")
			.elem("measureAttribute");
	}

	private void addMeasureAttributeCode(XMLBuilder2 xml, String code, String codeSystem, String displayName) {
		xml.elem("code").a("code", code).a("codeSystem", codeSystem).elem("displayName").a("value", displayName).up().up();
	}

	private void addMeasureAttributeCode(XMLBuilder2 xml, String nullFlavor, String originalText) {
		xml.elem("code").a("nullFlavor", nullFlavor).elem("originalText").a("value", originalText).up().up();
	}

	private void addMeasureAttributeValue(XMLBuilder2 xml, String code, String codeSystem, String xsiType, String displayName) {
		xml.elem("value").a("code", code).a("codeSystem", codeSystem).a("xsi:type", xsiType).elem("displayName").a("value", displayName).up().up();
	}

	private void addMeasureAttributeValue(XMLBuilder2 xml, String mediaType, String value, String xsiType) {
		xml.elem("value").a("mediaType", mediaType).a("value", value).a("xsi:type", xsiType).up();
	}

	private void addComponentOfs(XMLBuilder2 xml, CqfMeasure m) {
		// TODO: Where's the quality measure set? Hedis?
		String qualityMeasureSetId = "a0f96a17-36f0-46d4-bbd5-ad265d81bc95";

		xml.root().elem("componentOf").elem("qualityMeasureSet").a("classCode", "ACT")
			.elem("id").a("root", qualityMeasureSetId).up()
			.elem("title").a("value", "None");
	}

	private void addComponents(XMLBuilder2 xml, CqfMeasure m, String documentGuid, String documentName) {
		this.addDataCriteriaSection(xml, m);
		this.addPopulationCriteriaSection(xml, m, documentGuid, documentName);
	}


	private void addDataCriteriaSection(XMLBuilder2 xml, CqfMeasure m) {
		this.addDataCriteriaHeader(xml);

		// if (m.hasDataCriteria()) {
		//     for (StringType s : m.getDataCriteria())
		//     {

		//     }
		// }
	}

	private XMLBuilder2 addDataCriteriaHeader(XMLBuilder2 xml) {
		return xml.root().elem("component")
			.elem("dataCriteriaSection")
			.elem("templateId")
			.elem("item").a("extension","2018-05-01").a("root", "2.16.840.1.113883.10.20.28.2.6").up().up()
			.elem("code").a("code","57025-9").a("codeSystem","2.16.840.1.113883.6.1").up()
			.elem("title").a("value", "Data Criteria Section").up()
			.elem("text").up();
	}

	// Unlike other functions, this function expects the xml builder to be located
	// at the correct spot. It's also expected to reset the xmlBuilder to the correct spot.
	// private void addDataCriteriaEntry(XMLBuilder2 xml, String localVariableName, String criteriaName, String classCode, String itemExtension, String itemRoot,
	//     String idExtension, String idRoot, String code, String codeSystem, String codeSystemName, String codeDisplayName, String title, String statusCode, String valueSet) {
	//     xml.elem("entry").a("typeCode", "DRIV")
	//         .elem("localVariableName").a("value", localVariableName).up()
	//         .elem(criteriaName).a("classCode", classCode).a("moodCode", "EVN").up()
	//         .elem("templateId").elem("item").a("extension", itemExtension).a("root", itemRoot).up().up()
	//         .elem("id").a("extension", idExtension).a("root", idRoot).up()
	//         .elem("code").a("code", code).a("codeSystem", codeSystem).a("codeSystemName", codeSystemName)
	//             .elem("displayName").a("value", codeDisplayName).up().up()
	//         .elem("title").a("value", title).up()
	//         .elem("statusCode").a("code", statusCode).up()
	//         .elem("value").a("valueSet", valueSet).a("xsi:type", "CD").up().up();
	// }

	private void addPopulationCriteriaSection(XMLBuilder2 xml, CqfMeasure m, String documentGuid, String documentName) {
		if (m.hasGroup()) {
			for (int i = 0; i < m.getGroup().size(); i++) {
				String criteriaName = "PopulationCriteria_" + (i + 1);
				String criteriaRoot = UUID.randomUUID().toString();
				XMLBuilder2 readyForComponents = this.addPopulationCriteriaHeader(xml, criteriaName, criteriaRoot);
				Measure.MeasureGroupComponent mgc = m.getGroupFirstRep();
				for (Measure.MeasureGroupPopulationComponent mgpc : mgc.getPopulation()) {
					String key = mgpc.getCode().getCoding().get(0).getCode();
					CodeMapping mapping = measurePopulationValueSetMap.get(key);
					this.addPopulationCriteriaComponentCriteria(readyForComponents, mapping.criteriaName, mapping.criteriaExtension, mapping.code, documentName + ".\"" + mgpc.getCriteria() + "\"", documentGuid);
				}

				if (m.hasSupplementalData()) {
					for (Measure.MeasureSupplementalDataComponent sde : m.getSupplementalData()) {
						this.addPopulationCriteriaComponentSDE(readyForComponents, UUID.randomUUID().toString(), documentName + ".\"" + sde.getCriteria() + "\"", documentGuid);
					}
				}
			}
		}
	}

	// Unlike other functions, this function expects the xml builder to be located
	// at the correct spot.
	private void addPopulationCriteriaComponentCriteria(XMLBuilder2 xml, String criteriaName, String criteriaIdExtension,
																		 String code, String criteriaReferenceIdExtension, String criteriaReferenceIdRoot) {
		xml.elem("component").a("typeCode", "COMP")
			.elem(criteriaName).a("classCode", "OBS").a("moodCode", "EVN")
			.elem("id").a("extension", criteriaIdExtension).a("root", UUID.randomUUID().toString()).up()
			.elem("code").a("code", code).a("codeSystem", "2.16.840.1.113883.5.4").a("codeSystemName", "Act Code").up()
			.elem("precondition").a("typeCode", "PRCN")
			.elem("criteriaReference").a("classCode", "OBS").a("moodCode", "EVN")
			.elem("id").a("extension", criteriaReferenceIdExtension).a("root", criteriaReferenceIdRoot).up().up().up().up().up();
	}

	// Unlike other functions, this function expects the xml builder to be located
	// at the correct spot.
	private void addPopulationCriteriaComponentSDE(XMLBuilder2 xml, String sdeIdRoot, String criteriaReferenceIdExtension, String criteriaReferenceIdRoot) {
		xml.elem("component").a("typeCode", "COMP")
			.elem("cql-ext:supplementalDataElement")
			.elem("id").a("extension", "Supplemental Data Elements").a("root", sdeIdRoot).up()
			.elem("code").a("code", "SDE").a("codeSystem", "2.16.840.1.113883.5.4").a("codeSystemName", "Act Code").up()
			.elem("precondition").a("typeCode", "PRCN")
			.elem("criteriaReference").a("classCode", "OBS").a("moodCode", "EVN")
			.elem("id").a("extension", criteriaReferenceIdExtension).a("root", criteriaReferenceIdRoot).up().up().up().up().up();
	}

	private XMLBuilder2 addPopulationCriteriaHeader(XMLBuilder2 xml, String criteriaName, String criteriaRoot) {
		return xml.root().elem("component")
			.elem("populationCriteriaSection")
			.elem("templateId")
			.elem("item").a("extension","2017-08-01").a("root", "2.16.840.1.113883.10.20.28.2.7").up().up()
			.elem("id").a("extension", criteriaName).a("root", criteriaRoot).up()
			.elem("code").a("code","57026-7").a("codeSystem","2.16.840.1.113883.6.1").up()
			.elem("title").a("value", "Population Criteria Section").up()
			.elem("text").up();
	}

	private void addResponsibleParties(XMLBuilder2 xml, CqfMeasure m) {

		List<Contributor> contributors = m.getContributor();
		if (contributors != null) {
			for (Contributor c : contributors)
			{
				// TODO: Hard-coded NCQA's OID
				if (c.getType() == Contributor.ContributorType.AUTHOR) {
					this.addResponsibleParty(xml, "author", "2.16.840.1.113883.3.464", c.getName());
				}
			}
		}

		// TODO: Hard-coded NCQA's OID
		if (m.getPublisher() != null) {
			this.addResponsibleParty(xml, "publisher", "2.16.840.1.113883.3.464", m.getPublisher());
		}


		// Add verifier
		// TODO: Not present on the FHIR resource - need an extension?
		// TODO: Hard-coded to National Quality Forum
		this.addResponsibleParty(xml, "verifier", "2.16.840.1.113883.3.560", "National Quality Forum");

	}

	private void addResponsibleParty(XMLBuilder2 xml, String type, String oid, String name) {
		xml.root().elem(type)
			.elem("responsibleParty").a("classCode", "ASSIGNED")
			.elem("representedResponsibleOrganization").a("classCode","ORG").a("determinerCode","INSTANCE")
			.elem("id")
			.elem("item").a("root", oid).up().up()
			.elem("name")
			.elem("item")
			.elem("part").a("value", name);

	}

	private String writeDocument(Document d) {
		try {
			DOMSource source = new DOMSource(d);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			transformer.transform(source, result);

			return writer.toString();
		}
		catch (Exception e) {
			return null;
		}
	}

	// private boolean validateHQMF(String xml) {
	//     try {
	//         return this.validateXML(this.loadHQMFSchema(), xml);
	//     }
	//     catch (SAXException e) {
	//         return false;
	//     }
	// }

	// private boolean validateXML(Schema schema, String xml){
	//     try {
	//         Validator validator = schema.newValidator();
	//         validator.validate(new StreamSource(new StringReader(xml)));
	//     } catch (IOException | SAXException e) {
	//         System.out.println("Exception: " + e.getMessage());
	//         return false;
	//     }
	//     return true;
	// }

	// private Schema loadHQMFSchema() throws SAXException {
	//     SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	//     URL hqmfSchema = ClassLoader.getSystemClassLoader().getResource("hqmf/schemas/EMeasure_N1.xsd");
	//     return factory.newSchema(hqmfSchema);
	// }

	// args[0] == relative path to json measure -> i.e. measure/measure-demo.json (optional)
	// args[1] == path to resource output -> i.e. library/library-demo.json(optional)
	// args[2] == path to hqmf output -> i.e. hqmf.xml(optional)
	// args[3] == path to narrative output -> i.e. output.html(optional)
	public static void main(String[] args) {

		try {
			List<String> strings = Arrays.asList(
				"hqmf/examples/input/measure-ann.json",
				"hqmf/examples/input/library-common.json",
				"hqmf/examples/input/library-ann.json"
			);

			List<Path> paths = strings.stream().map(x -> Paths.get(toUri(x))).collect(Collectors.toList());

			// Path pathToLibrary = Paths.get(HQMFProvider.class.getClassLoader().getResource("narratives/examples/library/CMS146.json").toURI());
			Path pathToOutput = Paths.get("src/main/resources/hqmf/hqmf.xml").toAbsolutePath();
			Path pathToNarrativeOutput = Paths.get("src/main/resources/narratives/output.html").toAbsolutePath();

			Path pathToProp = Paths.get(
				NarrativeProvider.class.getClassLoader().getResource("narratives/narrative.properties").toURI());

			if (args.length >= 4) {
				pathToNarrativeOutput = Paths.get(new URI(args[3]));
			}

			if (args.length >= 3) {
				pathToOutput = Paths.get(new URI(args[2]));
			}

			// if (args.length >= 2) {
			//     pathToLibrary = Paths.get(new URI(args[1]));
			// }

			// if (args.length >= 1) {
			//     pathToMeasure = Paths.get(new URI(args[0]));
			// }

			HQMFProvider provider = new HQMFProvider();
			DataRequirementsProvider dataRequirementsProvider = new DataRequirementsProvider();
			NarrativeProvider narrativeProvider = new NarrativeProvider(pathToProp.toUri().toString());;

			FhirContext context = FhirContext.forDstu3();

			//IParser parser = pathToMeasure.toString().endsWith("json") ? context.newJsonParser() : context.newXmlParser();

			IParser parser = context.newJsonParser();

			List<IBaseResource> resources = paths.stream()
				.map(x -> toReader(x))
				.filter(x -> x != null)
				.map(x -> parser.parseResource(x))
				.collect(Collectors.toList());

			Measure measure = (Measure)resources.stream().filter(x -> (x instanceof Measure)).findFirst().get();
			List<Library> libraries = resources.stream().filter(x -> (x instanceof Library)).map(x -> (Library)x).collect(Collectors.toList());

			LibraryResolutionProvider<Library> lrp =
				new InMemoryLibraryResourceProvider<Library>(libraries, x -> x.getIdElement().getIdPart(), x -> x.getName(), x -> x.getVersion());

			CqfMeasure cqfMeasure = dataRequirementsProvider.createCqfMeasure(measure, lrp);

			String result = provider.generateHQMF(cqfMeasure);

			PrintWriter writer = new PrintWriter(new File(pathToOutput.toString()), "UTF-8");
			writer.println(result);
			writer.println();
			writer.close();



			Narrative narrative = narrativeProvider.getNarrative(context, cqfMeasure);
			String narrativeContent = narrative.getDivAsString();

			Path pathToHTML = Paths.get(toUri("narratives/templates/hqmf.html"));
			org.jsoup.nodes.Document htmlDoc = Jsoup.parse(pathToHTML.toFile(), "UTF-8");

			htmlDoc.title(measure.getName());
			htmlDoc.body().html(narrativeContent);

			writer = new PrintWriter(new File(pathToNarrativeOutput.toString()), "UTF-8");
			writer.write(htmlDoc.outerHtml());
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

	public static Reader toReader(Path p) {
		try {
			return new FileReader(p.toFile());
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static URI toUri(String s) {
		try {
			return HQMFProvider.class.getClassLoader().getResource(s).toURI();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

