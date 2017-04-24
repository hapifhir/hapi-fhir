package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.UrlUtil;

public class Bundle extends BaseBundle /* implements IBase implements IElement */{

	private static final long serialVersionUID = 5811989173275366745L;
	private ResourceMetadataMap myResourceMetadata;
	private BoundCodeDt<BundleTypeEnum> myType;
	private StringDt myBundleId;
	private TagList myCategories;
	private List<BundleEntry> myEntries;
	private volatile transient Map<IdDt, IResource> myIdToEntries;
	private StringDt myLinkBase;
	private StringDt myLinkFirst;
	private StringDt myLinkLast;
	private StringDt myLinkNext;
	private StringDt myLinkPrevious;
	private StringDt myLinkSelf;
	private StringDt myTitle;
	private IntegerDt myTotalResults;

	/**
	 * @deprecated Tags wil become immutable in a future release of HAPI, so
	 *             {@link #addCategory(String, String, String)} should be used instead
	 */
	@Deprecated
	public Tag addCategory() {
		Tag retVal = new Tag();
		getCategories().add(retVal);
		return retVal;
	}

	public void addCategory(String theScheme, String theTerm, String theLabel) {
		getCategories().add(new Tag(theScheme, theTerm, theLabel));
	}

	public void addCategory(Tag theTag) {
		getCategories().add(theTag);
	}

	/**
	 * Adds and returns a new bundle entry
	 */
	public BundleEntry addEntry() {
		BundleEntry retVal = new BundleEntry();
		getEntries().add(retVal);
		return retVal;
	}

	/**
	 * Adds a new entry
	 * 
	 * @param theBundleEntry
	 *            The entry to add
	 */
	public void addEntry(BundleEntry theBundleEntry) {
		Validate.notNull(theBundleEntry, "theBundleEntry can not be null");
		getEntries().add(theBundleEntry);
	}

	/**
	 * Creates a new entry using the given resource and populates it accordingly
	 * 
	 * @param theResource
	 *            The resource to add
	 * @return Returns the newly created bundle entry that was added to the bundle
	 */
	public BundleEntry addResource(IResource theResource, FhirContext theContext, String theServerBase) {
		BundleEntry entry = addEntry();
		entry.setResource(theResource);

		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);

		String title = ResourceMetadataKeyEnum.TITLE.get(theResource);
		if (title != null) {
			entry.getTitle().setValue(title);
		} else {
			entry.getTitle().setValue(def.getName() + " " + StringUtils.defaultString(theResource.getId().getValue(), "(no ID)"));
		}

		if (theResource.getId() != null) {
			if (theResource.getId().isAbsolute()) {

				entry.getLinkSelf().setValue(theResource.getId().getValue());
				//TODO: Use of a deprecated method should be resolved.
				entry.getId().setValue(theResource.getId().toVersionless().getValue());

			} else if (StringUtils.isNotBlank(theResource.getId().getValue())) {

				StringBuilder b = new StringBuilder();
				b.append(theServerBase);
				if (b.length() > 0 && b.charAt(b.length() - 1) != '/') {
					b.append('/');
				}
				b.append(def.getName());
				b.append('/');
				String resId = theResource.getId().getIdPart();
				b.append(resId);

				//TODO: Use of a deprecated method should be resolved.
				entry.getId().setValue(b.toString());

				if (isNotBlank(theResource.getId().getVersionIdPart())) {
					b.append('/');
					b.append(Constants.PARAM_HISTORY);
					b.append('/');
					b.append(theResource.getId().getVersionIdPart());
				} else {
					//TODO: Use of a deprecated method should be resolved.
					IdDt versionId = (IdDt) ResourceMetadataKeyEnum.VERSION_ID.get(theResource);
					if (versionId != null) {
						b.append('/');
						b.append(Constants.PARAM_HISTORY);
						b.append('/');
						b.append(versionId.getValue());
					}
				}

				String qualifiedId = b.toString();
				entry.getLinkSelf().setValue(qualifiedId);

				// String resourceType = theContext.getResourceDefinition(theResource).getName();


			}
		}

		InstantDt published = ResourceMetadataKeyEnum.PUBLISHED.get(theResource);
		if (published == null) {
			entry.getPublished().setToCurrentTimeInLocalTimeZone();
		} else {
			entry.setPublished(published);
		}

		InstantDt updated = ResourceMetadataKeyEnum.UPDATED.get(theResource);
		if (updated != null) {
			//TODO: Use of a deprecated method should be resolved.
			entry.setUpdated(updated);
		}

		InstantDt deleted = ResourceMetadataKeyEnum.DELETED_AT.get(theResource);
		if (deleted != null) {
			entry.setDeleted(deleted);
		}

		IdDt previous = ResourceMetadataKeyEnum.PREVIOUS_ID.get(theResource);
		if (previous != null) {
			entry.getLinkAlternate().setValue(previous.withServerBase(theServerBase, def.getName()).getValue());
		}

		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag nextTag : tagList) {
				entry.addCategory(nextTag);
			}
		}

		String linkSearch = ResourceMetadataKeyEnum.LINK_SEARCH.get(theResource);
		if (isNotBlank(linkSearch)) {
			if (!UrlUtil.isAbsolute(linkSearch)) {
				linkSearch = (theServerBase + "/" + linkSearch);
			}
			entry.getLinkSearch().setValue(linkSearch);
		}

		String linkAlternate = ResourceMetadataKeyEnum.LINK_ALTERNATE.get(theResource);
		if (isNotBlank(linkAlternate)) {
			if (!UrlUtil.isAbsolute(linkAlternate)) {
				linkSearch = (theServerBase + "/" + linkAlternate);
			}
			entry.getLinkAlternate().setValue(linkSearch);
		}
		
		BundleEntrySearchModeEnum entryStatus = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(theResource);
		if (entryStatus != null) {
			entry.getSearchMode().setValueAsEnum(entryStatus);
		}

		BundleEntryTransactionMethodEnum entryTransactionOperation = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(theResource);
		if (entryTransactionOperation != null) {
			entry.getTransactionMethod().setValueAsEnum(entryTransactionOperation);
		}

		DecimalDt entryScore = ResourceMetadataKeyEnum.ENTRY_SCORE.get(theResource);
		if (entryScore != null) {
			entry.setScore(entryScore);
		}

		return entry;
	}

	public StringDt getBundleId() {
		if (myBundleId == null) {
			myBundleId = new StringDt();
		}
		return myBundleId;
	}

	public TagList getCategories() {
		if (myCategories == null) {
			myCategories = new TagList();
		}
		return myCategories;
	}

	public List<BundleEntry> getEntries() {
		if (myEntries == null) {
			myEntries = new ArrayList<BundleEntry>();
		}
		return myEntries;
	}

	public StringDt getLinkBase() {
		if (myLinkBase == null) {
			myLinkBase = new StringDt();
		}
		return myLinkBase;
	}

	public StringDt getLinkFirst() {
		if (myLinkFirst == null) {
			myLinkFirst = new StringDt();
		}
		return myLinkFirst;
	}

	public StringDt getLinkLast() {
		if (myLinkLast == null) {
			myLinkLast = new StringDt();
		}
		return myLinkLast;
	}

	public StringDt getLinkNext() {
		if (myLinkNext == null) {
			myLinkNext = new StringDt();
		}
		return myLinkNext;
	}

	public StringDt getLinkPrevious() {
		if (myLinkPrevious == null) {
			myLinkPrevious = new StringDt();
		}
		return myLinkPrevious;
	}

	public StringDt getLinkSelf() {
		if (myLinkSelf == null) {
			myLinkSelf = new StringDt();
		}
		return myLinkSelf;
	}

	/*
	public InstantDt getPublished() {
		InstantDt retVal = (InstantDt) getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		if (retVal == null) {
			retVal= new InstantDt();
			getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, retVal);
		}
		return retVal;
	}
	*/

	/**
	 * Retrieves a resource from a bundle given its logical ID.
	 * <p>
	 * <b>Important usage notes</b>: This method ignores base URLs (so passing in an ID of
	 * <code>http://foo/Patient/123</code> will return a resource if it has the logical ID of
	 * <code>http://bar/Patient/123</code>. Also, this method is intended to be used for bundles which have already been
	 * populated. It will cache its results for fast performance, but that means that modifications to the bundle after
	 * this method is called may not be accurately reflected.
	 * </p>
	 * 
	 * @param theId
	 *            The resource ID
	 * @return Returns the resource with the given ID, or <code>null</code> if none is found
	 */
	public IResource getResourceById(IdDt theId) {
		Map<IdDt, IResource> map = myIdToEntries;
		if (map == null) {
			map = new HashMap<IdDt, IResource>();
			for (BundleEntry next : this.getEntries()) {
				//TODO: Use of a deprecated method should be resolved.
				if (next.getId().isEmpty() == false) {
					map.put(next.getId().toUnqualified(), next.getResource());
				}
			}
			myIdToEntries = map;
		}
		return map.get(theId.toUnqualified());
	}

	public ResourceMetadataMap getResourceMetadata() {
		if (myResourceMetadata == null) {
			myResourceMetadata = new ResourceMetadataMap();
		}
		return myResourceMetadata;
	}

	/**
	 * Returns a list containing all resources of the given type from this bundle
	 */
	public <T extends IResource> List<T> getResources(Class<T> theClass) {
		ArrayList<T> retVal = new ArrayList<T>();
		for (BundleEntry next : getEntries()) {
			if (next.getResource() != null && theClass.isAssignableFrom(next.getResource().getClass())) {
				@SuppressWarnings("unchecked")
				T resource = (T) next.getResource();
				retVal.add(resource);
			}
		}
		return retVal;
	}

	public StringDt getTitle() {
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	public IntegerDt getTotalResults() {
		if (myTotalResults == null) {
			myTotalResults = new IntegerDt();
		}
		return myTotalResults;
	}

	public BoundCodeDt<BundleTypeEnum> getType() {
		if (myType == null) {
			myType = new BoundCodeDt<BundleTypeEnum>(BundleTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	public InstantDt getUpdated() {
		InstantDt retVal = (InstantDt) getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		if (retVal == null) {
			retVal= new InstantDt();
			getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, retVal);
		}
		return retVal;
	}

	/**
	 * Returns true if this bundle contains zero entries
	 */
	@Override
	public boolean isEmpty() {
		return getEntries().isEmpty();
	}

	public void setCategories(TagList theCategories) {
		myCategories = theCategories;
	}

	/*
	public void setPublished(InstantDt thePublished) {
		getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, thePublished);
	}
	*/

	public void setType(BoundCodeDt<BundleTypeEnum> theType) {
		myType = theType;
	}

	/**
	 * Returns the number of entries in this bundle
	 */
	public int size() {
		return getEntries().size();
	}

	public List<IResource> toListOfResources() {
		ArrayList<IResource> retVal = new ArrayList<IResource>();
		for (BundleEntry next : getEntries()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			}
		}
		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append(getEntries().size() + " entries");
		b.append("id", getId());
		return b.toString();
	}

	public static Bundle withResources(List<IResource> theResources, FhirContext theContext, String theServerBase) {
		Bundle retVal = new Bundle();
		for (IResource next : theResources) {
			retVal.addResource(next, theContext, theServerBase);
		}
		return retVal;
	}

	public static Bundle withSingleResource(IResource theResource) {
		Bundle retVal = new Bundle();
		retVal.addEntry().setResource(theResource);
		return retVal;
	}

}
