package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.util.ValidateUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValueSetConceptAccumulator implements IValueSetConceptAccumulator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetConceptAccumulator.class);

	private TermValueSet myTermValueSet;
	private ITermValueSetDao myValueSetDao;
	private ITermValueSetConceptDao myValueSetConceptDao;
	private ITermValueSetConceptDesignationDao myValueSetConceptDesignationDao;
	private int myConceptsSaved;
	private int myDesignationsSaved;
	private int myConceptsExcluded;

	public ValueSetConceptAccumulator(@Nonnull TermValueSet theTermValueSet, @Nonnull ITermValueSetDao theValueSetDao, @Nonnull ITermValueSetConceptDao theValueSetConceptDao, @Nonnull ITermValueSetConceptDesignationDao theValueSetConceptDesignationDao) {
		myTermValueSet = theTermValueSet;
		myValueSetDao = theValueSetDao;
		myValueSetConceptDao = theValueSetConceptDao;
		myValueSetConceptDesignationDao = theValueSetConceptDesignationDao;
		myConceptsSaved = 0;
		myDesignationsSaved = 0;
		myConceptsExcluded = 0;
	}

	@Override
	public void addMessage(String theMessage) {
		// ignore for now

	}

	@Override
	public void includeConcept(String theSystem, String theCode, String theDisplay) {
		saveConcept(theSystem, theCode, theDisplay);
	}

	@Override
	public void includeConceptWithDesignations(String theSystem, String theCode, String theDisplay, Collection<TermConceptDesignation> theDesignations) {
		TermValueSetConcept concept = saveConcept(theSystem, theCode, theDisplay);
		if (theDesignations != null) {
			for (TermConceptDesignation designation : theDesignations) {
				saveConceptDesignation(concept, designation);
			}
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

			ourLog.debug("Excluding [{}|{}] from ValueSet[{}]", concept.getSystem(), concept.getCode(), myTermValueSet.getUrl());
			for (TermValueSetConceptDesignation designation : concept.getDesignations()) {
				myValueSetConceptDesignationDao.deleteById(designation.getId());
				myTermValueSet.decrementTotalConceptDesignations();
			}
			myValueSetConceptDao.deleteById(concept.getId());
			myTermValueSet.decrementTotalConcepts();
			myValueSetDao.save(myTermValueSet);
			ourLog.debug("Done excluding [{}|{}] from ValueSet[{}]", concept.getSystem(), concept.getCode(), myTermValueSet.getUrl());

			if (++myConceptsExcluded % 250 == 0) {
				ourLog.info("Have excluded {} concepts from ValueSet[{}]", myConceptsExcluded, myTermValueSet.getUrl());
			}
		}
	}

	private TermValueSetConcept saveConcept(String theSystem, String theCode, String theDisplay) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "ValueSet contains a concept with no system value");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theCode, "ValueSet contains a concept with no code value");

		TermValueSetConcept concept = new TermValueSetConcept();
		concept.setValueSet(myTermValueSet);
		concept.setOrder(myConceptsSaved);
		concept.setSystem(theSystem);
		concept.setCode(theCode);
		if (isNotBlank(theDisplay)) {
			concept.setDisplay(theDisplay);
		}
		myValueSetConceptDao.save(concept);
		myValueSetDao.save(myTermValueSet.incrementTotalConcepts());

		if (++myConceptsSaved % 250 == 0) {
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
		myValueSetDao.save(myTermValueSet.incrementTotalConceptDesignations());

		if (++myDesignationsSaved % 250 == 0) {
			ourLog.debug("Have pre-expanded {} designations for Concept[{}|{}] in ValueSet[{}]", myDesignationsSaved, theConcept.getSystem(), theConcept.getCode(), myTermValueSet.getUrl());
		}

		return designation;
	}

	public Boolean removeGapsFromConceptOrder() {
		if (myConceptsExcluded <= 0) {
			return false;
		}

		ourLog.info("Removing gaps from concept order for ValueSet[{}]", myTermValueSet.getUrl());
		int order = 0;
		List<Long> conceptIds = myValueSetConceptDao.findIdsByTermValueSetId(myTermValueSet.getId());
		for (Long conceptId : conceptIds) {
			myValueSetConceptDao.updateOrderById(conceptId, order++);
		}
		ourLog.info("Have removed gaps from concept order for {} concepts in ValueSet[{}]", conceptIds.size(), myTermValueSet.getUrl());

		return true;
	}

	// TODO: DM 2019-07-16 - We may need TermValueSetConceptProperty, similar to TermConceptProperty.
	// TODO: DM 2019-07-16 - If so, we should also populate TermValueSetConceptProperty entities here.
	// TODO: DM 2019-07-30 - Expansions don't include the properties themselves; they may be needed to facilitate filters and parameterized expansions.
}
