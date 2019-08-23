package ca.uhn.fhir.jpa.term;

/*
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

import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.util.ValidateUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;

public class ValueSetConceptAccumulator implements IValueSetConceptAccumulator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetConceptAccumulator.class);

	private TermValueSet myTermValueSet;
	private ITermValueSetConceptDao myValueSetConceptDao;
	private ITermValueSetConceptDesignationDao myValueSetConceptDesignationDao;
	private int myConceptsSaved;
	private int myDesignationsSaved;

	public ValueSetConceptAccumulator(@Nonnull TermValueSet theTermValueSet, @Nonnull ITermValueSetConceptDao theValueSetConceptDao, @Nonnull ITermValueSetConceptDesignationDao theValueSetConceptDesignationDao) {
		myTermValueSet = theTermValueSet;
		myValueSetConceptDao = theValueSetConceptDao;
		myValueSetConceptDesignationDao = theValueSetConceptDesignationDao;
		myConceptsSaved = 0;
		myDesignationsSaved = 0;
	}

	@Override
	public void includeConcept(String theSystem, String theCode, String theDisplay) {
		saveConcept(theSystem, theCode, theDisplay);
	}

	@Override
	public void includeConceptWithDesignations(String theSystem, String theCode, String theDisplay, Collection<TermConceptDesignation> theDesignations) {
		TermValueSetConcept concept = saveConcept(theSystem, theCode, theDisplay);
		for (TermConceptDesignation designation : theDesignations) {
			saveConceptDesignation(concept, designation);
		}
	}

	@Override
	public void excludeConcept(String theSystem, String theCode) {
		if (isAnyBlank(theSystem, theCode)) {
			return;
		}

		// Get existing entity so it can be deleted.
		Optional<TermValueSetConcept> optionalConcept = myValueSetConceptDao.findByTermValueSetIdSystemAndCode(myTermValueSet.getId(), theSystem, theCode);

		if (optionalConcept.isPresent()) {
			TermValueSetConcept concept = optionalConcept.get();

			ourLog.info("Excluding [{}|{}] from ValueSet[{}]", concept.getSystem(), concept.getCode(), myTermValueSet.getUrl());
			for (TermValueSetConceptDesignation designation : concept.getDesignations()) {
				myValueSetConceptDesignationDao.deleteById(designation.getId());
			}
			myValueSetConceptDao.deleteById(concept.getId());
			ourLog.info("Done excluding [{}|{}] from ValueSet[{}]", concept.getSystem(), concept.getCode(), myTermValueSet.getUrl());

			ourLog.info("Flushing...");
			myValueSetConceptDesignationDao.flush();
			myValueSetConceptDao.flush();
			ourLog.info("Done flushing.");
		}
	}

	private TermValueSetConcept saveConcept(String theSystem, String theCode, String theDisplay) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "ValueSet contains a concept with no system value");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theCode, "ValueSet contains a concept with no code value");

		TermValueSetConcept concept = new TermValueSetConcept();
		concept.setValueSet(myTermValueSet);
		concept.setSystem(theSystem);
		concept.setCode(theCode);
		if (isNotBlank(theDisplay)) {
			concept.setDisplay(theDisplay);
		}
		myValueSetConceptDao.save(concept);

		if (myConceptsSaved++ % 250 == 0) { // TODO: DM 2019-08-23 - This message never appears in the log. Fix it!
			ourLog.info("Have pre-expanded {} concepts in ValueSet[{}]", myConceptsSaved, myTermValueSet.getUrl());
		}

		return concept;
	}

	private TermValueSetConceptDesignation saveConceptDesignation(TermValueSetConcept theConcept, TermConceptDesignation theDesignation) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theDesignation.getValue(), "ValueSet contains a concept designation with no value");

		TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
		designation.setConcept(theConcept);
		designation.setValueSet(myTermValueSet);
		designation.setLanguage(theDesignation.getLanguage());
		if (isNoneBlank(theDesignation.getUseSystem(), theDesignation.getUseCode())) {
			designation.setUseSystem(theDesignation.getUseSystem());
			designation.setUseCode(theDesignation.getUseCode());
			if (isNotBlank(theDesignation.getUseDisplay())) {
				designation.setUseDisplay(theDesignation.getUseDisplay());
			}
		}
		designation.setValue(theDesignation.getValue());
		myValueSetConceptDesignationDao.save(designation);

		if (myDesignationsSaved++ % 250 == 0) { // TODO: DM 2019-08-23 - This message never appears in the log. Fix it!
			ourLog.info("Have pre-expanded {} designations for Concept[{}|{}] in ValueSet[{}]", myDesignationsSaved, theConcept.getSystem(), theConcept.getCode(), myTermValueSet.getUrl());
		}

		return designation;
	}

	// TODO: DM 2019-07-16 - We need TermValueSetConceptProperty, similar to TermConceptProperty.
	// TODO: DM 2019-07-16 - We should also populate TermValueSetConceptProperty entities here.
	// TODO: DM 2019-07-30 - Expansions don't include the properties themselves; they are needed to facilitate filters and parameterized expansions.
}
