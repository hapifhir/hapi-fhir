package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
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

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.util.ElementUtil;

public class BundleEntry extends BaseBundle {

	//@formatter:off
	/* ****************************************************
	 * NB: add any new fields to the isEmpty() method!!!
	 *****************************************************/
	//@formatter:on
	private TagList myCategories;
	private InstantDt myDeletedAt;
	private StringDt myLinkAlternate;
	private StringDt myLinkSelf;
	private InstantDt myPublished;
	private IResource myResource;
	private XhtmlDt mySummary;
	private StringDt myTitle;
	private InstantDt myUpdated;

	public Tag addCategory() {
		Tag retVal = new Tag();
		getCategories().add(retVal);
		return retVal;
	}

	public void addCategory(Tag theTag) {
		getCategories().add(theTag);
	}

	public TagList getCategories() {
		if (myCategories == null) {
			myCategories = new TagList();
		}
		return myCategories;
	}

	/**
	 * Gets the date/time that thius entry was deleted.
	 */
	public InstantDt getDeletedAt() {
		if (myDeletedAt == null) {
			myDeletedAt = new InstantDt();
		}
		return myDeletedAt;
	}

	public StringDt getLinkAlternate() {
		if (myLinkAlternate == null) {
			myLinkAlternate = new StringDt();
		}
		return myLinkAlternate;
	}

	public StringDt getLinkSelf() {
		if (myLinkSelf == null) {
			myLinkSelf = new StringDt();
		}
		return myLinkSelf;
	}

	public InstantDt getPublished() {
		if (myPublished == null) {
			myPublished = new InstantDt();
		}
		return myPublished;
	}

	public IResource getResource() {
		return myResource;
	}

	public XhtmlDt getSummary() {
		if (mySummary == null) {
			mySummary = new XhtmlDt();
		}
		return mySummary;
	}

	public StringDt getTitle() {
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	public InstantDt getUpdated() {
		if (myUpdated == null) {
			myUpdated = new InstantDt();
		}
		return myUpdated;
	}

	@Override
	public boolean isEmpty() {
		//@formatter:off
		return super.isEmpty() && 
				ElementUtil.isEmpty(myCategories, myDeletedAt, myLinkAlternate, myLinkSelf, myPublished, myResource, mySummary, myTitle, myUpdated);
		//@formatter:on
	}

	/**
	 * Sets the date/time that this entry was deleted.
	 */
	public void setDeleted(InstantDt theDeletedAt) {
		myDeletedAt = theDeletedAt;
	}

	public void setLinkAlternate(StringDt theLinkAlternate) {
		myLinkAlternate = theLinkAlternate;
	}

	public void setLinkSelf(StringDt theLinkSelf) {
		if (myLinkSelf == null) {
			myLinkSelf = new StringDt();
		}
		myLinkSelf = theLinkSelf;
	}

	public void setPublished(InstantDt thePublished) {
		Validate.notNull(thePublished, "Published may not be null");
		myPublished = thePublished;
	}

	public void setResource(IResource theResource) {
		myResource = theResource;
	}

	public void setUpdated(InstantDt theUpdated) {
		Validate.notNull(theUpdated, "Updated may not be null");
		myUpdated = theUpdated;
	}

}
