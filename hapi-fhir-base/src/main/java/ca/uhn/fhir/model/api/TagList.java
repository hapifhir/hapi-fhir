package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A collection of tags present on a single resource. TagList is backed by a {@link LinkedHashSet}, so the order of added tags will be consistent, but duplicates will not be preserved.
 * 
 * <p>
 * <b>Thread safety:</b> This class is not thread safe
 * </p>
 */
public class TagList implements Set<Tag>, Serializable {

	public static final String ATTR_CATEGORY = "category";
	public static final String ELEMENT_NAME = "TagList";

	public static final String ELEMENT_NAME_LC = ELEMENT_NAME.toLowerCase();
	private static final long serialVersionUID = 1L;
	private transient List<Tag> myOrderedTags;
	private LinkedHashSet<Tag> myTagSet = new LinkedHashSet<Tag>();

	/**
	 * Constructor
	 */
	public TagList() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("TagList[").append(size()).append(" tag(s)]");
		for (Tag next : this) {
			b.append("\n * ").append(next.toString());
		}
		return b.toString();
	}

	@Override
	public boolean add(Tag theE) {
		myOrderedTags = null;
		return myTagSet.add(theE);
	}

	@Override
	public boolean addAll(Collection<? extends Tag> theC) {
		myOrderedTags = null;
		return myTagSet.addAll(theC);
	}

	public Tag addTag() {
		myOrderedTags = null;
		return addTag(null, null, null);
	}

	public Tag addTag(String theScheme, String theTerm, String theLabel) {
		Tag retVal = new Tag(theScheme, theTerm, theLabel);
		add(retVal);
		myOrderedTags = null;
		return retVal;
	}

	@Override
	public void clear() {
		myOrderedTags = null;
		myTagSet.clear();
	}

	@Override
	public boolean contains(Object theO) {
		return myTagSet.contains(theO);
	}

	@Override
	public boolean containsAll(Collection<?> theC) {
		return myTagSet.containsAll(theC);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagList other = (TagList) obj;
		if (myTagSet == null) {
			if (other.myTagSet != null)
				return false;
		} else if (!myTagSet.equals(other.myTagSet))
			return false;
		return true;
	}

	/**
	 * Returns the tag at a given index - Note that the TagList is backed by a {@link LinkedHashSet}, so the order of added tags will be consistent, but duplicates will not be preserved.
	 */
	public Tag get(int theIndex) {
		if (myOrderedTags == null) {
			myOrderedTags = new ArrayList<Tag>();
			for (Tag next : myTagSet) {
				myOrderedTags.add(next);
			}
		}
		return myOrderedTags.get(theIndex);
	}
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public Tag set(int theIndex, Tag theElement) {
	// throw new UnsupportedOperationException();
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public void add(int theIndex, Tag theElement) {
	// myTagSet.add(theElement);
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public Tag remove(int theIndex) {
	// Tag retVal = myTagSet.remove(theIndex);
	// myTagSet.remove(retVal);
	// return retVal;
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public int indexOf(Object theO) {
	// myTagSet.remove(theO);
	// return myTagSet.indexOf(theO);
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public int lastIndexOf(Object theO) {
	// return myTagSet.lastIndexOf(theO);
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public ListIterator<Tag> listIterator() {
	// return myTagSet.listIterator();
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public ListIterator<Tag> listIterator(int theIndex) {
	// return myTagSet.listIterator(theIndex);
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public List<Tag> subList(int theFromIndex, int theToIndex) {
	// return myTagSet.subList(theFromIndex, theToIndex);
	// }

	public Tag getTag(String theScheme, String theTerm) {
		for (Tag next : this) {
			if (theScheme.equals(next.getScheme()) && theTerm.equals(next.getTerm())) {
				return next;
			}
		}
		return null;
	}

	public List<Tag> getTagsWithScheme(String theScheme) {
		ArrayList<Tag> retVal = new ArrayList<Tag>();
		for (Tag next : this) {
			if (theScheme.equals(next.getScheme())) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		return myTagSet.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return myTagSet.isEmpty();
	}

	@Override
	public Iterator<Tag> iterator() {
		return myTagSet.iterator();
	}

	@Override
	public boolean remove(Object theO) {
		myOrderedTags = null;
		return myTagSet.remove(theO);
	}

	@Override
	public boolean removeAll(Collection<?> theC) {
		myOrderedTags = null;
		return myTagSet.removeAll(theC);
	}

	@Override
	public boolean retainAll(Collection<?> theC) {
		myOrderedTags = null;
		return myTagSet.retainAll(theC);
	}

	@Override
	public int size() {
		return myTagSet.size();
	}

	@Override
	public Object[] toArray() {
		return myTagSet.toArray();
	}

	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override
	// public boolean addAll(int theIndex, Collection<? extends Tag> theC) {
	// myTagSet.addAll(theC);
	// return myTagSet.addAll(theC);
	// }
	//
	// /**
	// * @deprecated TagList will not implement the {@link List} interface in a future release, as tag order is not supposed to be meaningful
	// */
	// @Deprecated
	// @Override

	@Override
	public <T> T[] toArray(T[] theA) {
		return myTagSet.toArray(theA);
	}

}
