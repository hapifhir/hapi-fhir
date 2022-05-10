package ca.uhn.fhir.jpa.term.custom;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.LoadedFileDescriptors;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomTerminologySet {

	private final int mySize;
	private final List<TermConcept> myRootConcepts;

	/**
	 * Constructor for an empty object
	 */
	public CustomTerminologySet() {
		this(0, new ArrayList<>());
	}

	/**
	 * Constructor
	 */
	private CustomTerminologySet(int theSize, List<TermConcept> theRootConcepts) {
		mySize = theSize;
		myRootConcepts = theRootConcepts;
	}

	public TermConcept addRootConcept(String theCode) {
		return addRootConcept(theCode, null);
	}

	public TermConcept addRootConcept(String theCode, String theDisplay) {
		Validate.notBlank(theCode, "theCode must not be blank");
		Validate.isTrue(myRootConcepts.stream().noneMatch(t -> t.getCode().equals(theCode)), "Already have code %s", theCode);
		TermConcept retVal = new TermConcept();
		retVal.setCode(theCode);
		retVal.setDisplay(theDisplay);
		myRootConcepts.add(retVal);
		return retVal;
	}


	public int getSize() {
		return mySize;
	}

	public TermCodeSystemVersion toCodeSystemVersion() {
		TermCodeSystemVersion csv = new TermCodeSystemVersion();

		for (TermConcept next : myRootConcepts) {
			csv.getConcepts().add(next);
		}

		populateVersionToChildCodes(csv, myRootConcepts);

		return csv;
	}

	private void populateVersionToChildCodes(TermCodeSystemVersion theCsv, List<TermConcept> theConcepts) {
		for (TermConcept next : theConcepts) {
			next.setCodeSystemVersion(theCsv);
			populateVersionToChildCodes(theCsv, next.getChildCodes());
		}
	}

	public List<TermConcept> getRootConcepts() {
		return Collections.unmodifiableList(myRootConcepts);
	}

	public void validateNoCycleOrThrowInvalidRequest() {
		Set<String> codes = new HashSet<>();
		validateNoCycleOrThrowInvalidRequest(codes, getRootConcepts());
	}

	private void validateNoCycleOrThrowInvalidRequest(Set<String> theCodes, List<TermConcept> theRootConcepts) {
		for (TermConcept next : theRootConcepts) {
			validateNoCycleOrThrowInvalidRequest(theCodes, next);
		}
	}

	private void validateNoCycleOrThrowInvalidRequest(Set<String> theCodes, TermConcept next) {
		if (!theCodes.add(next.getCode())) {
			throw new InvalidRequestException(Msg.code(926) + "Cycle detected around code " + next.getCode());
		}
		validateNoCycleOrThrowInvalidRequest(theCodes, next.getChildCodes());
	}

	public Set<String> getRootConceptCodes() {
		return getRootConcepts()
			.stream()
			.map(TermConcept::getCode)
			.collect(Collectors.toSet());
	}

	@Nonnull
	public static CustomTerminologySet load(LoadedFileDescriptors theDescriptors, boolean theFlat) {

		final Map<String, TermConcept> code2concept = new LinkedHashMap<>();
		// Concepts
		IZipContentsHandlerCsv conceptHandler = new ConceptHandler(code2concept);

		TermLoaderSvcImpl.iterateOverZipFileCsv(theDescriptors, TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE, conceptHandler, ',', QuoteMode.NON_NUMERIC, false);

		if (theDescriptors.hasFile(TermLoaderSvcImpl.CUSTOM_PROPERTIES_FILE)) {
			Map<String, List<TermConceptProperty>> theCode2property = new LinkedHashMap<>();
			IZipContentsHandlerCsv propertyHandler = new PropertyHandler(theCode2property);
			TermLoaderSvcImpl.iterateOverZipFileCsv(theDescriptors, TermLoaderSvcImpl.CUSTOM_PROPERTIES_FILE, propertyHandler, ',', QuoteMode.NON_NUMERIC, false);
			for (TermConcept termConcept : code2concept.values()) {
				if (!theCode2property.isEmpty() &&  theCode2property.get(termConcept.getCode()) != null) {
					theCode2property.get(termConcept.getCode()).forEach(property -> {
						termConcept.getProperties().add(property);
					});
				}
			}
		}

		if (theFlat) {

			return new CustomTerminologySet(code2concept.size(), new ArrayList<>(code2concept.values()));

		} else {

			// Hierarchy
			if (theDescriptors.hasFile(TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE)) {
				IZipContentsHandlerCsv hierarchyHandler = new HierarchyHandler(code2concept);
				TermLoaderSvcImpl.iterateOverZipFileCsv(theDescriptors, TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE, hierarchyHandler, ',', QuoteMode.NON_NUMERIC, false);
			}

			Map<String, Integer> codesInOrder = new HashMap<>();
			for (String nextCode : code2concept.keySet()) {
				codesInOrder.put(nextCode, codesInOrder.size());
			}

			List<TermConcept> rootConcepts = new ArrayList<>();
			for (TermConcept nextConcept : code2concept.values()) {

				// Find root concepts
				if (nextConcept.getParents().isEmpty()) {
					rootConcepts.add(nextConcept);
				}

				// Sort children so they appear in the same order as they did in the concepts.csv file
				nextConcept.getChildren().sort((o1, o2) -> {
					String code1 = o1.getChild().getCode();
					String code2 = o2.getChild().getCode();
					int order1 = codesInOrder.get(code1);
					int order2 = codesInOrder.get(code2);
					return order1 - order2;
				});

			}

			return new CustomTerminologySet(code2concept.size(), rootConcepts);
		}
	}

}
