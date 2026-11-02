/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Determines the order in which {@link BaseTransactionProcessor} saves the resources of a FHIR transaction
 * that have auto-versioned references, so that the target of an auto-versioned reference is saved before
 * the resource referencing it. The target may then be a NOP, and the resource referencing it is given the
 * target's actual version.
 * <p>
 * This isn't possible for a reference cycle: one of its targets is saved after a resource that references it,
 * and that resource has already been given the target's expected (incremented) version. That target is forced
 * to get a new version, so that it can't be a NOP.
 */
// Created by Claude Opus 5.5
class AutoVersionSaveOrder {

	private enum OrderingState {
		ADDING_TARGETS,
		ADDED
	}

	private record TargetIterator(DaoMethodOutcome outcome, Iterator<DaoMethodOutcome> targets) {}

	private final FhirContext myContext;
	private final IdSubstitutionMap myIdSubstitutions;
	private final TransactionDetails myTransactionDetails;
	private final Map<DaoMethodOutcome, Set<IBaseReference>> myReferencesToAutoVersion;
	private final Map<String, DaoMethodOutcome> myOutcomesByVersionlessId = new HashMap<>();
	private final ListMultimap<String, DaoMethodOutcome> myOutcomesByResourceType = ArrayListMultimap.create();
	private final Map<DaoMethodOutcome, OrderingState> myOrderingStates = new IdentityHashMap<>();
	private final List<DaoMethodOutcome> myOrderedOutcomes = new ArrayList<>();
	private final Set<DaoMethodOutcome> myOutcomesToForceUpdateVersion =
			Collections.newSetFromMap(new IdentityHashMap<>());

	/**
	 * @param theContext                 the FHIR context
	 * @param theReferencesToAutoVersion the auto-versioned references of each resource to save
	 * @param theIdSubstitutions         the placeholder ID substitutions for the transaction
	 * @param theTransactionDetails      the transaction details, holding the match URLs resolved in the transaction
	 */
	public AutoVersionSaveOrder(
			FhirContext theContext,
			Map<DaoMethodOutcome, Set<IBaseReference>> theReferencesToAutoVersion,
			IdSubstitutionMap theIdSubstitutions,
			TransactionDetails theTransactionDetails) {
		myContext = theContext;
		myReferencesToAutoVersion = theReferencesToAutoVersion;
		myIdSubstitutions = theIdSubstitutions;
		myTransactionDetails = theTransactionDetails;

		indexOutcomes();
		orderOutcomes();
	}

	/**
	 * @return the resources to save, with targets of auto-versioned references before the resources referencing them
	 */
	public List<DaoMethodOutcome> getOrderedOutcomes() {
		return myOrderedOutcomes;
	}

	/**
	 * @return whether the resource must get a new version even if unchanged, because it is in a reference cycle
	 * and is saved after a resource that references its expected version
	 */
	public boolean isForceUpdateVersion(DaoMethodOutcome theOutcome) {
		return myOutcomesToForceUpdateVersion.contains(theOutcome);
	}

	/**
	 * Indexes the resources to save by ID and resource type, so that the targets of references can be found
	 */
	private void indexOutcomes() {
		for (DaoMethodOutcome nextOutcome : myReferencesToAutoVersion.keySet()) {
			if (nextOutcome.getId() != null) {
				myOutcomesByVersionlessId.put(
						nextOutcome.getId().toUnqualifiedVersionless().getValue(), nextOutcome);
			}
			myOutcomesByResourceType.put(myContext.getResourceType(nextOutcome.getResource()), nextOutcome);
		}
	}

	/**
	 * Orders the resources to save, with targets before the resources referencing them.
	 * All resources must be indexed first.
	 */
	private void orderOutcomes() {
		for (DaoMethodOutcome nextOutcome : myReferencesToAutoVersion.keySet()) {
			if (!myOrderingStates.containsKey(nextOutcome)) {
				processOutcomeTargets(nextOutcome);
			}
		}
	}

	/**
	 * Adds the resource to the save order after adding its targets.
	 * In a reference cycle, the target that can't go first gets a new version even if unchanged.
	 */
	private void processOutcomeTargets(DaoMethodOutcome theOutcome) {
		myOrderingStates.put(theOutcome, OrderingState.ADDING_TARGETS);

		Deque<TargetIterator> targetIterators = new ArrayDeque<>();
		targetIterators.push(new TargetIterator(theOutcome, findTargets(theOutcome)));

		while (!targetIterators.isEmpty()) {
			TargetIterator current = targetIterators.peek();
			if (current.targets().hasNext()) {
				processTarget(current, current.targets().next(), targetIterators);
			} else {
				DaoMethodOutcome completedOutcome = targetIterators.pop().outcome();
				myOrderingStates.put(completedOutcome, OrderingState.ADDED);
				myOrderedOutcomes.add(completedOutcome);
			}
		}
	}

	private void processTarget(
			TargetIterator theCurrentIterator, DaoMethodOutcome theTarget, Deque<TargetIterator> theTargetIterators) {
		if (theTarget == theCurrentIterator.outcome()) {
			return;
		}

		OrderingState targetState = myOrderingStates.get(theTarget);
		if (targetState == null) {
			myOrderingStates.put(theTarget, OrderingState.ADDING_TARGETS);
			theTargetIterators.push(new TargetIterator(theTarget, findTargets(theTarget)));
		} else if (targetState == OrderingState.ADDING_TARGETS) {
			// Reference cycle: the target will be added after this resource
			myOutcomesToForceUpdateVersion.add(theTarget);
		}
	}

	/**
	 * Finds the resources to save that are targets of the auto-versioned references of the given resource.
	 * A reference to the resource itself is skipped: it is given the resource's expected version, so the
	 * resource isn't a NOP anyway.
	 */
	private Iterator<DaoMethodOutcome> findTargets(DaoMethodOutcome theOutcome) {
		Iterator<IBaseReference> references =
				myReferencesToAutoVersion.get(theOutcome).iterator();
		return new Iterator<>() {
			private Iterator<DaoMethodOutcome> targets = Collections.emptyIterator();

			@Override
			public boolean hasNext() {
				while (!targets.hasNext() && references.hasNext()) {
					targets = findTargets(references.next()).iterator();
				}
				return targets.hasNext();
			}

			@Override
			public DaoMethodOutcome next() {
				return targets.next();
			}
		};
	}

	private Collection<DaoMethodOutcome> findTargets(IBaseReference theReference) {
		IIdType referenceId = theReference.getReferenceElement();
		if (isBlank(referenceId.getValue()) && theReference.getResource() != null) {
			referenceId = theReference.getResource().getIdElement();
		}
		if (isBlank(referenceId.getValue())) {
			return Collections.emptyList();
		}

		if (referenceId.getValue().contains("?")) {
			return findMatchUrlTargets(referenceId.getValue());
		}

		if (myIdSubstitutions.containsSource(referenceId)) {
			referenceId = myIdSubstitutions.getForSource(referenceId);
		}
		return findTarget(referenceId);
	}

	private Collection<DaoMethodOutcome> findMatchUrlTargets(String theMatchUrl) {
		// Normalize "Patient/?identifier=..." and "Patient?identifier=..." to "Patient?identifier=..."
		int questionMarkIndex = theMatchUrl.indexOf('?');
		String path = Strings.CS.removeEnd(theMatchUrl.substring(0, questionMarkIndex), "/");
		String resourceType = path.substring(path.lastIndexOf('/') + 1);
		String matchUrl = resourceType + theMatchUrl.substring(questionMarkIndex);

		IResourcePersistentId<?> resolvedPid =
				myTransactionDetails.getResolvedMatchUrls().get(matchUrl);
		if (resolvedPid != null && resolvedPid != TransactionDetails.NOT_FOUND) {
			IIdType resolvedId = myTransactionDetails.getReverseResolvedId(resolvedPid);
			if (resolvedId != null) {
				return findTarget(resolvedId);
			}
		}

		// The match URL wasn't resolved in the transaction, but it may still match one of these resources
		return myOutcomesByResourceType.get(resourceType);
	}

	private Collection<DaoMethodOutcome> findTarget(IIdType theTargetId) {
		if (theTargetId == null || !theTargetId.hasIdPart()) {
			return Collections.emptyList();
		}
		DaoMethodOutcome target = myOutcomesByVersionlessId.get(
				theTargetId.toUnqualifiedVersionless().getValue());
		return target != null ? List.of(target) : Collections.emptyList();
	}
}
