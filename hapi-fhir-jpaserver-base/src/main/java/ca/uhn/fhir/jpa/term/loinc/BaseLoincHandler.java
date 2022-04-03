package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.*;

public abstract class BaseLoincHandler implements IZipContentsHandlerCsv {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseLoincHandler.class);

	/**
	 * This is <b>NOT</b> the LOINC CodeSystem URI! It is just
	 * the website URL to LOINC.
	 */
	public static final String LOINC_WEBSITE_URL = "https://loinc.org";
	public static final String REGENSTRIEF_INSTITUTE_INC = "Regenstrief Institute, Inc.";
	private final List<ConceptMap> myConceptMaps;
	private final Map<String, ConceptMap> myIdToConceptMaps = new HashMap<>();
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Map<String, TermConcept> myCode2Concept;
	protected final Properties myUploadProperties;
	protected String myLoincCopyrightStatement;

	BaseLoincHandler(Map<String, TermConcept> theCode2Concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		this(theCode2Concept, theValueSets, theConceptMaps, theUploadProperties, null);
	}

	BaseLoincHandler(Map<String, TermConcept> theCode2Concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		myValueSets = theValueSets;
		myValueSets.forEach(t -> myIdToValueSet.put(t.getId(), t));
		myCode2Concept = theCode2Concept;
		myConceptMaps = theConceptMaps;
		myConceptMaps.forEach(t -> myIdToConceptMaps.put(t.getId(), t));
		myUploadProperties = theUploadProperties;
		myLoincCopyrightStatement = theCopyrightStatement;
	}

	void addCodeAsIncludeToValueSet(ValueSet theVs, String theCodeSystemUrl, String theCode, String theDisplayName) {
		ValueSet.ConceptSetComponent include = null;
		for (ValueSet.ConceptSetComponent next : theVs.getCompose().getInclude()) {
			if (next.getSystem().equals(theCodeSystemUrl)) {
				include = next;
				break;
			}
		}
		if (include == null) {
			include = theVs.getCompose().addInclude();
			include.setSystem(theCodeSystemUrl);
			if (StringUtils.isNotBlank(theVs.getVersion())) {
				include.setVersion(theVs.getVersion());
			}
		}

		boolean found = false;
		for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
			if (next.getCode().equals(theCode)) {
				found = true;
			}
		}
		if (!found) {

			String displayName = theDisplayName;
			if (isBlank(displayName)) {
				TermConcept concept = myCode2Concept.get(theCode);
				if (concept != null) {
					displayName = concept.getDisplay();
				}
			}

			include
				.addConcept()
				.setCode(theCode)
				.setDisplay(displayName);

		}
	}


	void addConceptMapEntry(ConceptMapping theMapping, String theExternalCopyright) {
		if (isBlank(theMapping.getSourceCode())) {
			return;
		}
		if (isBlank(theMapping.getTargetCode())) {
			return;
		}

		ConceptMap conceptMap;
		if (!myIdToConceptMaps.containsKey(theMapping.getConceptMapId())) {
			conceptMap = new ConceptMap();
			conceptMap.setId(theMapping.getConceptMapId());
			conceptMap.setUrl(theMapping.getConceptMapUri());
			conceptMap.setName(theMapping.getConceptMapName());
			conceptMap.setVersion(theMapping.getConceptMapVersion());
			conceptMap.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			conceptMap.addContact()
				.setName(REGENSTRIEF_INSTITUTE_INC)
				.addTelecom()
				.setSystem(ContactPoint.ContactPointSystem.URL)
				.setValue(LOINC_WEBSITE_URL);

			String copyright = theExternalCopyright;
			if (!copyright.contains("LOINC")) {
				copyright = myLoincCopyrightStatement +
					(myLoincCopyrightStatement.endsWith(".") ? " " : ". ") + copyright;
			}
			conceptMap.setCopyright(copyright);

			myIdToConceptMaps.put(theMapping.getConceptMapId(), conceptMap);
			myConceptMaps.add(conceptMap);
		} else {
			conceptMap = myIdToConceptMaps.get(theMapping.getConceptMapId());
		}

		if (isBlank(theMapping.getCopyright())) {
			conceptMap.setCopyright(theMapping.getCopyright());
		}

		ConceptMap.SourceElementComponent source = null;
		ConceptMap.ConceptMapGroupComponent group = null;

		for (ConceptMap.ConceptMapGroupComponent next : conceptMap.getGroup()) {
			if (next.getSource().equals(theMapping.getSourceCodeSystem())) {
				if (next.getTarget().equals(theMapping.getTargetCodeSystem())) {
					if (!defaultString(theMapping.getTargetCodeSystemVersion()).equals(defaultString(next.getTargetVersion()))) {
						continue;
					}
					group = next;
					break;
				}
			}
		}
		if (group == null) {
			group = conceptMap.addGroup();
			group.setSource(theMapping.getSourceCodeSystem());
			group.setSourceVersion(theMapping.getSourceCodeSystemVersion());
			group.setTarget(theMapping.getTargetCodeSystem());
			group.setTargetVersion(defaultIfBlank(theMapping.getTargetCodeSystemVersion(), null));
		}

		for (ConceptMap.SourceElementComponent next : group.getElement()) {
			if (next.getCode().equals(theMapping.getSourceCode())) {
				source = next;
			}
		}
		if (source == null) {
			source = group.addElement();
			source.setCode(theMapping.getSourceCode());
			source.setDisplay(theMapping.getSourceDisplay());
		}

		boolean found = false;
		for (ConceptMap.TargetElementComponent next : source.getTarget()) {
			if (next.getCode().equals(theMapping.getTargetCode())) {
				found = true;
			}
		}
		if (!found) {
			source
				.addTarget()
				.setCode(theMapping.getTargetCode())
				.setDisplay(theMapping.getTargetDisplay())
				.setEquivalence(theMapping.getEquivalence());
		} else {
			ourLog.info("Not going to add a mapping from [{}/{}] to [{}/{}] because one already exists", theMapping.getSourceCodeSystem(), theMapping.getSourceCode(), theMapping.getTargetCodeSystem(), theMapping.getTargetCode());
		}
	}

	ValueSet getValueSet(String theValueSetId, String theValueSetUri, String theValueSetName, String theVersionPropertyName) {

		String version;
		String codeSystemVersion = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		if (isNotBlank(theVersionPropertyName)) {
			if (codeSystemVersion != null) {
				version = myUploadProperties.getProperty(theVersionPropertyName) + "-" + codeSystemVersion;
			} else {
				version = myUploadProperties.getProperty(theVersionPropertyName);
			}
		} else {
			version = codeSystemVersion;
		}

		ValueSet vs;
		if (!myIdToValueSet.containsKey(theValueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(theValueSetId);
			vs.setVersion(version);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			vs.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			vs.addContact()
				.setName(REGENSTRIEF_INSTITUTE_INC)
				.addTelecom()
				.setSystem(ContactPoint.ContactPointSystem.URL)
				.setValue(LOINC_WEBSITE_URL);
			vs.setCopyright(myLoincCopyrightStatement);
			myIdToValueSet.put(theValueSetId, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(theValueSetId);
		}

		if (isBlank(vs.getName()) && isNotBlank(theValueSetName)) {
			vs.setName(theValueSetName);
		}

		return vs;
	}


	static class ConceptMapping {

		private String myCopyright;
		private String myConceptMapId;
		private String myConceptMapUri;
		private String myConceptMapVersion;
		private String myConceptMapName;
		private String mySourceCodeSystem;
		private String mySourceCodeSystemVersion;
		private String mySourceCode;
		private String mySourceDisplay;
		private String myTargetCodeSystem;
		private String myTargetCode;
		private String myTargetDisplay;
		private Enumerations.ConceptMapEquivalence myEquivalence;
		private String myTargetCodeSystemVersion;

		String getConceptMapId() {
			return myConceptMapId;
		}

		ConceptMapping setConceptMapId(String theConceptMapId) {
			myConceptMapId = theConceptMapId;
			return this;
		}

		String getConceptMapName() {
			return myConceptMapName;
		}

		ConceptMapping setConceptMapName(String theConceptMapName) {
			myConceptMapName = theConceptMapName;
			return this;
		}

		String getConceptMapUri() {
			return myConceptMapUri;
		}

		ConceptMapping setConceptMapUri(String theConceptMapUri) {
			myConceptMapUri = theConceptMapUri;
			return this;
		}

		String getConceptMapVersion() {
			return myConceptMapVersion;
		}

		ConceptMapping setConceptMapVersion(String theConceptMapVersion) {
			myConceptMapVersion = theConceptMapVersion;
			return this;
		}

		String getCopyright() {
			return myCopyright;
		}

		ConceptMapping setCopyright(String theCopyright) {
			myCopyright = theCopyright;
			return this;
		}

		Enumerations.ConceptMapEquivalence getEquivalence() {
			return myEquivalence;
		}

		ConceptMapping setEquivalence(Enumerations.ConceptMapEquivalence theEquivalence) {
			myEquivalence = theEquivalence;
			return this;
		}

		String getSourceCode() {
			return mySourceCode;
		}

		ConceptMapping setSourceCode(String theSourceCode) {
			mySourceCode = theSourceCode;
			return this;
		}

		String getSourceCodeSystem() {
			return mySourceCodeSystem;
		}

		ConceptMapping setSourceCodeSystem(String theSourceCodeSystem) {
			mySourceCodeSystem = theSourceCodeSystem;
			return this;
		}

		String getSourceCodeSystemVersion() {
			return mySourceCodeSystemVersion;
		}

		ConceptMapping setSourceCodeSystemVersion(String theSourceCodeSystemVersion) {
			mySourceCodeSystemVersion = theSourceCodeSystemVersion;
			return this;
		}

		String getSourceDisplay() {
			return mySourceDisplay;
		}

		ConceptMapping setSourceDisplay(String theSourceDisplay) {
			mySourceDisplay = theSourceDisplay;
			return this;
		}

		String getTargetCode() {
			return myTargetCode;
		}

		ConceptMapping setTargetCode(String theTargetCode) {
			myTargetCode = theTargetCode;
			return this;
		}

		String getTargetCodeSystem() {
			return myTargetCodeSystem;
		}

		ConceptMapping setTargetCodeSystem(String theTargetCodeSystem) {
			myTargetCodeSystem = theTargetCodeSystem;
			return this;
		}

		String getTargetCodeSystemVersion() {
			return myTargetCodeSystemVersion;
		}

		ConceptMapping setTargetCodeSystemVersion(String theTargetCodeSystemVersion) {
			myTargetCodeSystemVersion = theTargetCodeSystemVersion;
			return this;
		}

		String getTargetDisplay() {
			return myTargetDisplay;
		}

		ConceptMapping setTargetDisplay(String theTargetDisplay) {
			myTargetDisplay = theTargetDisplay;
			return this;
		}

	}
}
