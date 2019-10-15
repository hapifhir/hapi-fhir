package ca.uhn.fhir.jpa.term.custom;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.LoadedFileDescriptors;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.*;

public class CustomTerminologySet {

	private final int mySize;
	private final ListMultimap<TermConcept, String> myUnanchoredChildConceptsToParentCodes;
	private final List<TermConcept> myRootConcepts;

	/**
	 * Constructor for an empty object
	 */
	public CustomTerminologySet() {
		this(0, ArrayListMultimap.create(), new ArrayList<>());
	}

	/**
	 * Constructor
	 */
	private CustomTerminologySet(int theSize, ListMultimap<TermConcept, String> theUnanchoredChildConceptsToParentCodes, Collection<TermConcept> theRootConcepts) {
		this(theSize, theUnanchoredChildConceptsToParentCodes, new ArrayList<>(theRootConcepts));
	}

	/**
	 * Constructor
	 */
	private CustomTerminologySet(int theSize, ListMultimap<TermConcept, String> theUnanchoredChildConceptsToParentCodes, List<TermConcept> theRootConcepts) {
		mySize = theSize;
		myUnanchoredChildConceptsToParentCodes = theUnanchoredChildConceptsToParentCodes;
		myRootConcepts = theRootConcepts;
	}

	public void addRootConcept(String theCode) {
		addRootConcept(theCode, null);
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


	public ListMultimap<TermConcept, String> getUnanchoredChildConceptsToParentCodes() {
		return Multimaps.unmodifiableListMultimap(myUnanchoredChildConceptsToParentCodes);
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

	public void addUnanchoredChildConcept(String theParentCode, String theCode, String theDisplay) {
		Validate.notBlank(theParentCode);
		Validate.notBlank(theCode);

		TermConcept code = new TermConcept()
			.setCode(theCode)
			.setDisplay(theDisplay);
		myUnanchoredChildConceptsToParentCodes.put(code, theParentCode);
	}

	public void validateNoCycleOrThrowInvalidRequest() {
		Set<String> codes = new HashSet<>();
		validateNoCycleOrThrowInvalidRequest(codes, getRootConcepts());
		for (TermConcept next : myUnanchoredChildConceptsToParentCodes.keySet()) {
			validateNoCycleOrThrowInvalidRequest(codes, next);
		}
	}

	private void validateNoCycleOrThrowInvalidRequest(Set<String> theCodes, List<TermConcept> theRootConcepts) {
		for (TermConcept next : theRootConcepts) {
			validateNoCycleOrThrowInvalidRequest(theCodes, next);
		}
	}

	private void validateNoCycleOrThrowInvalidRequest(Set<String> theCodes, TermConcept next) {
		if (!theCodes.add(next.getCode())) {
			throw new InvalidRequestException("Cycle detected around code " + next.getCode());
		}
		validateNoCycleOrThrowInvalidRequest(theCodes, next.getChildCodes());
	}


	@Nonnull
	public static CustomTerminologySet load(LoadedFileDescriptors theDescriptors, boolean theFlat) {

		final Map<String, TermConcept> code2concept = new LinkedHashMap<>();
		ArrayListMultimap<TermConcept, String> unanchoredChildConceptsToParentCodes = ArrayListMultimap.create();

		// Concepts
		IRecordHandler conceptHandler = new ConceptHandler(code2concept);
		TermLoaderSvcImpl.iterateOverZipFile(theDescriptors, TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE, conceptHandler, ',', QuoteMode.NON_NUMERIC, false);
		if (theFlat) {

			return new CustomTerminologySet(code2concept.size(), ArrayListMultimap.create(), code2concept.values());

		} else {

			// Hierarchy
			if (theDescriptors.hasFile(TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE)) {
				IRecordHandler hierarchyHandler = new HierarchyHandler(code2concept, unanchoredChildConceptsToParentCodes);
				TermLoaderSvcImpl.iterateOverZipFile(theDescriptors, TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE, hierarchyHandler, ',', QuoteMode.NON_NUMERIC, false);
			}

			// Find root concepts
			List<TermConcept> rootConcepts = new ArrayList<>();
			for (TermConcept nextConcept : code2concept.values()) {
				if (nextConcept.getParents().isEmpty()) {
					rootConcepts.add(nextConcept);
				}
			}

			return new CustomTerminologySet(code2concept.size(), unanchoredChildConceptsToParentCodes, rootConcepts);
		}
	}

}
