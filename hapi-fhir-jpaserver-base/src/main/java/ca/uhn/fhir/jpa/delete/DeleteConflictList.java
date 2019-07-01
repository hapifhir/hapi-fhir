package ca.uhn.fhir.jpa.delete;

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

import ca.uhn.fhir.jpa.util.DeleteConflict;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class DeleteConflictList implements Iterable<DeleteConflict> {
	private final List<DeleteConflict> myList = new ArrayList<>();

	public void add(DeleteConflict theDeleteConflict) {
		myList.add(theDeleteConflict);
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	@Override
	public Iterator<DeleteConflict> iterator() {
		return myList.iterator();
	}

	public boolean removeIf(Predicate<DeleteConflict> theFilter) {
		return myList.removeIf(theFilter);
	}

	public void addAll(DeleteConflictList theNewConflicts) {
		myList.addAll(theNewConflicts.myList);
	}

	public int size() {
		return myList.size();
	}

	@Override
	public String toString() {
		return myList.toString();
	}
}
