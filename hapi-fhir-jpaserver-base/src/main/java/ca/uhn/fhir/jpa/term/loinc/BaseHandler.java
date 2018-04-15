package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.*;

abstract class BaseHandler implements IRecordHandler {

	private final List<ConceptMap> myConceptMaps;
	private final Map<String, ConceptMap> myIdToConceptMaps = new HashMap<>();
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Map<String, TermConcept> myCode2Concept;

	BaseHandler(Map<String, TermConcept> theCode2Concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		myValueSets = theValueSets;
		myCode2Concept = theCode2Concept;
		myConceptMaps = theConceptMaps;
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
				for (TermConcept next : myCode2Concept.values()) {
					if (next.getCode().equals(theCode)) {
						displayName = next.getDisplay();
					}
				}
			}

			include
				.addConcept()
				.setCode(theCode)
				.setDisplay(displayName);

		}
	}


	void addConceptMapEntry(ConceptMapping theMapping, String theCopyright) {
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
			conceptMap.setPublisher("Regentrief Institute, Inc.");
			conceptMap.addContact()
				.setName("Regentrief Institute, Inc.")
				.addTelecom()
				.setSystem(ContactPoint.ContactPointSystem.URL)
				.setValue("https://loinc.org");
			conceptMap.setCopyright(theCopyright);
			myIdToConceptMaps.put(theMapping.getConceptMapId(), conceptMap);
			myConceptMaps.add(conceptMap);
		} else {
			conceptMap = myIdToConceptMaps.get(theMapping.getConceptMapId());
		}

		if (isNotBlank(theMapping.getCopyright())) {
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
		}
	}

	ValueSet getValueSet(String theValueSetId, String theValueSetUri, String theValueSetName) {
		ValueSet vs;
		if (!myIdToValueSet.containsKey(theValueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(theValueSetId);
			vs.setName(theValueSetName);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			vs.setPublisher("Regenstrief Institute, Inc.");
			vs.addContact()
				.setName("Regenstrief Institute, Inc.")
				.addTelecom()
				.setSystem(ContactPoint.ContactPointSystem.URL)
				.setValue("https://loinc.org");
			vs.setCopyright("This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/");
			myIdToValueSet.put(theValueSetId, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(theValueSetId);
		}
		return vs;
	}


	static class ConceptMapping {

		private String myCopyright;
		private String myConceptMapId;
		private String myConceptMapUri;
		private String myConceptMapName;
		private String mySourceCodeSystem;
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
