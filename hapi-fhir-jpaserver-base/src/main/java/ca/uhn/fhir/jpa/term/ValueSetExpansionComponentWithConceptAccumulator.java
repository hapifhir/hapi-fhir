package ca.uhn.fhir.jpa.term;

/*
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Block()
public class ValueSetExpansionComponentWithConceptAccumulator extends ValueSet.ValueSetExpansionComponent implements IValueSetConceptAccumulator {
	private final int myMaxCapacity;
	private final FhirContext myContext;
	private int mySkipCountRemaining;
	private int myHardExpansionMaximumSize;
	private List<String> myMessages;
	private int myAddedConcepts;
	private Integer myTotalConcepts;
	private Map<Long, ValueSet.ValueSetExpansionContainsComponent> mySourcePidToConcept = new HashMap<>();
	private Map<ValueSet.ValueSetExpansionContainsComponent, String> myConceptToSourceDirectParentPids = new HashMap<>();
	private boolean myTrackingHierarchy;

	/**
	 * Constructor
	 *
	 * @param theMaxCapacity The maximum number of results this accumulator will accept before throwing
	 *                       an {@link InternalErrorException}
	 * @param theTrackingHierarchy
	 */
	ValueSetExpansionComponentWithConceptAccumulator(FhirContext theContext, int theMaxCapacity, boolean theTrackingHierarchy) {
		myMaxCapacity = theMaxCapacity;
		myContext = theContext;
		myTrackingHierarchy = theTrackingHierarchy;
	}

	@Nonnull
	@Override
	public Integer getCapacityRemaining() {
		return (myMaxCapacity - myAddedConcepts) + mySkipCountRemaining;
	}

	public List<String> getMessages() {
		if (myMessages == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(myMessages);
	}

	@Override
	public boolean isTrackingHierarchy() {
		return myTrackingHierarchy;
	}

	@Override
	public void addMessage(String theMessage) {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		myMessages.add(theMessage);
	}

	@Override
	public void includeConcept(String theSystem, String theCode, String theDisplay, Long theSourceConceptPid, String theSourceConceptDirectParentPids, String theCodeSystemVersion) {
		if (mySkipCountRemaining > 0) {
			mySkipCountRemaining--;
			return;
		}

		incrementConceptsCount();

		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();
		setSystemAndVersion(theSystem, contains);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
		contains.setVersion(theCodeSystemVersion);
	}

	@Override
	public void includeConceptWithDesignations(String theSystem, String theCode, String theDisplay, Collection<TermConceptDesignation> theDesignations, Long theSourceConceptPid, String theSourceConceptDirectParentPids, String theCodeSystemVersion) {
		if (mySkipCountRemaining > 0) {
			mySkipCountRemaining--;
			return;
		}

		incrementConceptsCount();

		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();

		if (theSourceConceptPid != null) {
			mySourcePidToConcept.put(theSourceConceptPid, contains);
		}
		if (theSourceConceptDirectParentPids != null) {
			myConceptToSourceDirectParentPids.put(contains, theSourceConceptDirectParentPids);
		}

		setSystemAndVersion(theSystem, contains);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);

		if (isNotBlank(theCodeSystemVersion)) {
			contains.setVersion(theCodeSystemVersion);
		}

		if (theDesignations != null) {
			for (TermConceptDesignation termConceptDesignation : theDesignations) {
				contains
					.addDesignation()
					.setValue(termConceptDesignation.getValue())
					.setLanguage(termConceptDesignation.getLanguage())
					.getUse()
					.setSystem(termConceptDesignation.getUseSystem())
					.setCode(termConceptDesignation.getUseCode())
					.setDisplay(termConceptDesignation.getUseDisplay());
			}
		}
	}

	@Override
	public void consumeSkipCount(int theSkipCountToConsume) {
		mySkipCountRemaining -= theSkipCountToConsume;
	}

	@Nullable
	@Override
	public Integer getSkipCountRemaining() {
		return mySkipCountRemaining;
	}

	@Override
	public boolean excludeConcept(String theSystem, String theCode) {
		String excludeSystem;
		String excludeSystemVersion;
		int versionSeparator = theSystem.indexOf("|");
		if (versionSeparator > -1) {
			excludeSystemVersion = theSystem.substring(versionSeparator + 1);
			excludeSystem = theSystem.substring(0, versionSeparator);
		} else {
			excludeSystem = theSystem;
			excludeSystemVersion = null;
		}
		if (excludeSystemVersion != null) {
			return this.getContains().removeIf(t ->
				excludeSystem.equals(t.getSystem()) &&
					theCode.equals(t.getCode()) &&
					excludeSystemVersion.equals(t.getVersion()));
		} else {
			return this.getContains().removeIf(t ->
				theSystem.equals(t.getSystem()) &&
					theCode.equals(t.getCode()));
		}
	}

	private void incrementConceptsCount() {
		Integer capacityRemaining = getCapacityRemaining();
		if (capacityRemaining == 0) {
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myMaxCapacity);
			msg = appendAccumulatorMessages(msg);
			throw new ExpansionTooCostlyException(Msg.code(831) + msg);
		}

		if (myHardExpansionMaximumSize > 0 && myAddedConcepts > myHardExpansionMaximumSize) {
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myHardExpansionMaximumSize);
			msg = appendAccumulatorMessages(msg);
			throw new ExpansionTooCostlyException(Msg.code(832) + msg);
		}

		myAddedConcepts++;
	}

	@Nonnull
	private String appendAccumulatorMessages(String msg) {
		msg += getMessages().stream().map(t->" - " + t).collect(Collectors.joining());
		return msg;
	}

	public Integer getTotalConcepts() {
		return myTotalConcepts;
	}

	@Override
	public void incrementOrDecrementTotalConcepts(boolean theAdd, int theDelta) {
		int delta = theDelta;
		if (!theAdd) {
			delta = -delta;
		}
		if (myTotalConcepts == null) {
			myTotalConcepts = delta;
		} else {
			myTotalConcepts = myTotalConcepts + delta;
		}
	}

	private void setSystemAndVersion(String theSystemAndVersion, ValueSet.ValueSetExpansionContainsComponent myComponent) {
		if (StringUtils.isNotEmpty((theSystemAndVersion))) {
			int versionSeparator = theSystemAndVersion.lastIndexOf('|');
			if (versionSeparator != -1) {
				myComponent.setVersion(theSystemAndVersion.substring(versionSeparator + 1));
				myComponent.setSystem(theSystemAndVersion.substring(0, versionSeparator));
			} else {
				myComponent.setSystem(theSystemAndVersion);
			}
		}
	}

	public void setSkipCountRemaining(int theSkipCountRemaining) {
		mySkipCountRemaining = theSkipCountRemaining;
	}

	public void setHardExpansionMaximumSize(int theHardExpansionMaximumSize) {
		myHardExpansionMaximumSize = theHardExpansionMaximumSize;
	}

	public void applyHierarchy() {
		for (int i = 0; i < this.getContains().size(); i++) {
			ValueSet.ValueSetExpansionContainsComponent nextContains = this.getContains().get(i);

			String directParentPidsString = myConceptToSourceDirectParentPids.get(nextContains);
			if (isNotBlank(directParentPidsString)) {
				List<Long> directParentPids = Arrays.stream(directParentPidsString.split(" ")).map(t -> Long.parseLong(t)).collect(Collectors.toList());

				boolean firstMatch = false;
				for (Long next : directParentPids) {
					ValueSet.ValueSetExpansionContainsComponent parentConcept = mySourcePidToConcept.get(next);
					if (parentConcept != null) {
						if (!firstMatch) {
							firstMatch = true;
							this.getContains().remove(i);
							i--;
						}

						parentConcept.addContains(nextContains);
					}
				}
			}
		}
	}
}
