package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class DeleteConflictList implements Iterable<DeleteConflict> {
	private final List<DeleteConflict> myList = new ArrayList<>();
	private final Set<String> myResourceIdsMarkedForDeletion;
	private final Set<String> myResourceIdsToIgnoreConflict;
	private int myRemoveModCount;

	/**
	 * Constructor
	 */
	public DeleteConflictList() {
		myResourceIdsMarkedForDeletion = new HashSet<>();
		myResourceIdsToIgnoreConflict = new HashSet<>();
	}

	/**
	 * Constructor that shares (i.e. uses the same list, as opposed to cloning it)
	 * of {@link #isResourceIdMarkedForDeletion(IIdType) resources marked for deletion}
	 */
	public DeleteConflictList(DeleteConflictList theParentList) {
		myResourceIdsMarkedForDeletion = theParentList.myResourceIdsMarkedForDeletion;
		myResourceIdsToIgnoreConflict = theParentList.myResourceIdsToIgnoreConflict;
	}


	public boolean isResourceIdMarkedForDeletion(IIdType theIdType) {
		Validate.notNull(theIdType);
		Validate.notBlank(theIdType.toUnqualifiedVersionless().getValue());
		return myResourceIdsMarkedForDeletion.contains(theIdType.toUnqualifiedVersionless().getValue());
	}

	public void setResourceIdMarkedForDeletion(IIdType theIdType) {
		Validate.notNull(theIdType);
		Validate.notBlank(theIdType.toUnqualifiedVersionless().getValue());
		myResourceIdsMarkedForDeletion.add(theIdType.toUnqualifiedVersionless().getValue());
	}

	public boolean isResourceIdToIgnoreConflict(IIdType theIdType) {
		Validate.notNull(theIdType);
		Validate.notBlank(theIdType.toUnqualifiedVersionless().getValue());
		return myResourceIdsToIgnoreConflict.contains(theIdType.toUnqualifiedVersionless().getValue());
	}

	public void setResourceIdToIgnoreConflict(IIdType theIdType) {
		Validate.notNull(theIdType);
		Validate.notBlank(theIdType.toUnqualifiedVersionless().getValue());
		myResourceIdsToIgnoreConflict.add(theIdType.toUnqualifiedVersionless().getValue());
	}

	public void add(DeleteConflict theDeleteConflict) {
		myList.add(theDeleteConflict);
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	@Override
	public Iterator<DeleteConflict> iterator() {
		// Note that handlers may add items to this list, so we're using a special iterator
		// that is ok with this. Only removals from the list should trigger a concurrent modification
		// issue
		return new Iterator<DeleteConflict>() {

			private final int myOriginalRemoveModCont = myRemoveModCount;
			private int myNextIndex = 0;
			private boolean myLastOperationWasNext;

			@Override
			public boolean hasNext() {
				checkForCoModification();
				myLastOperationWasNext = false;
				return myNextIndex < myList.size();
			}

			@Override
			public DeleteConflict next() {
				checkForCoModification();
				myLastOperationWasNext = true;
				return myList.get(myNextIndex++);
			}

			@Override
			public void remove() {
				Assert.isTrue(myLastOperationWasNext);
				myNextIndex--;
				myList.remove(myNextIndex);
				myLastOperationWasNext = false;
			}

			private void checkForCoModification() {
				Validate.isTrue(myOriginalRemoveModCont == myRemoveModCount);
			}
		};
	}

	public boolean removeIf(Predicate<DeleteConflict> theFilter) {
		boolean retVal = myList.removeIf(theFilter);
		if (retVal) {
			myRemoveModCount++;
		}
		return retVal;
	}

	public void addAll(DeleteConflictList theNewConflicts) {
		myList.addAll(theNewConflicts.myList);
	}

	public int size() {
		return myList.size();
	}

	public void removeAll() {
		this.removeIf(x -> true);
	}

	@Override
	public String toString() {
		return myList.toString();
	}
}
