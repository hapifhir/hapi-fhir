/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * JPA implementation of IResourcePersistentId.  JPA uses a Long as the primary key.  This class should be used in any
 * context where the pid is known to be a Long.
 */
public class JpaPid extends BaseResourcePersistentId<Long> {
	private final Long myId;

	private JpaPid(Long theId) {
		super(null);
		myId = theId;
	}

	private JpaPid(Long theId, Long theVersion) {
		super(theVersion, null);
		myId = theId;
	}

	private JpaPid(Long theId, String theResourceType) {
		super(theResourceType);
		myId = theId;
	}

	private JpaPid(Long theId, Long theVersion, String theResourceType) {
		super(theVersion, theResourceType);
		myId = theId;
	}

	public static List<Long> toLongList(Collection<JpaPid> thePids) {
		List<Long> retVal = new ArrayList<>(thePids.size());
		for (JpaPid next : thePids) {
			retVal.add(next.getId());
		}
		return retVal;
	}

	public static Set<Long> toLongSet(Collection<JpaPid> thePids) {
		Set<Long> retVal = new HashSet<>(thePids.size());
		for (JpaPid next : thePids) {
			retVal.add(next.getId());
		}
		return retVal;
	}

	public static List<JpaPid> fromLongList(Collection<Long> theResultList) {
		List<JpaPid> retVal = new ArrayList<>(theResultList.size());
		for (Long next : theResultList) {
			retVal.add(fromId(next));
		}
		return retVal;
	}

	public static JpaPid fromId(Long theId) {
		return new JpaPid(theId);
	}

	public static JpaPid fromIdAndVersion(Long theId, Long theVersion) {
		return new JpaPid(theId, theVersion);
	}

	public static JpaPid fromIdAndResourceType(Long theId, String theResourceType) {
		return new JpaPid(theId, theResourceType);
	}

	public static JpaPid fromIdAndVersionAndResourceType(Long theId, Long theVersion, String theResourceType) {
		return new JpaPid(theId, theVersion, theResourceType);
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		if (!super.equals(theO)) return false;
		JpaPid jpaPid = (JpaPid) theO;
		return myId.equals(jpaPid.myId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), myId);
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public String toString() {
		return myId.toString();
	}
}
