package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.util.CoverageIgnore;
import org.hl7.fhir.instance.model.api.IBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A collection of tags present on a single resource. TagList is backed by a {@link LinkedHashSet}, so the order of
 * added tags will be consistent, but duplicates will not be preserved.
 * 
 * <p>
 * <b>Thread safety:</b> This class is not thread safe
 * </p>
 */
public class TagList implements Set<Tag>, Serializable, IBase {

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

	/**
	 * Copy constructor
	 */
	public TagList(TagList theTags) {
		if (theTags != null) {
			for (Tag next : theTags) {
				add(next);
			}
		}
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

	/**
	 * @deprecated Tags wil become immutable in a future release of HAPI, so {@link #addTag(String, String, String)}
	 *             should be used instead
	 */
	@Deprecated
	public Tag addTag() {
		myOrderedTags = null;
		return addTag(null, null, null);
	}

	/**
	 * Add a new tag instance
	 * 
	 * @param theScheme
	 *           The tag scheme (the system)
	 * @param theTerm
	 *           The tag term (the code)
	 * @return Returns the newly created tag instance. Note that the tag is added to the list by this method, so you
	 *         generally do not need to interact directly with the added tag.
	 */
	public Tag addTag(String theScheme, String theTerm) {
		Tag retVal = new Tag(theScheme, theTerm);
		add(retVal);
		myOrderedTags = null;
		return retVal;
	}

	/**
	 * Add a new tag instance
	 * 
	 * @param theScheme
	 *           The tag scheme
	 * @param theTerm
	 *           The tag term
	 * @param theLabel
	 *           The tag label
	 * @return Returns the newly created tag instance. Note that the tag is added to the list by this method, so you
	 *         generally do not need to interact directly with the added tag.
	 */
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
	 * Returns the tag at a given index - Note that the TagList is backed by a {@link LinkedHashSet}, so the order of
	 * added tags will be consistent, but duplicates will not be preserved.
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
		for (Tag next : myTagSet) {
			if (next.isEmpty() == false) {
				return false;
			}
		}
		return true;
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

	@Override
	public <T> T[] toArray(T[] theA) {
		return myTagSet.toArray(theA);
	}

	/**
	 * Returns false
	 */
	@Override
	@CoverageIgnore
	public boolean hasFormatComment() {
		return false;
	}

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPre() {
		throw new UnsupportedOperationException(Msg.code(1895));
	}

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPost() {
		throw new UnsupportedOperationException(Msg.code(1896));
	}

	@Override
	public Object getUserData(String theName) {
		throw new UnsupportedOperationException(Msg.code(1897));
	}

	@Override
	public void setUserData(String theName, Object theValue) {
		throw new UnsupportedOperationException(Msg.code(1898));
	}

}
