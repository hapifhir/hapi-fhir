package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("WeakerAccess")
public abstract class BaseParser implements IParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParser.class);

	private ContainedResources myContainedResources;
	private boolean myEncodeElementsAppliesToChildResourcesOnly;
	private FhirContext myContext;
	private Set<String> myDontEncodeElements;
	private boolean myDontEncodeElementsIncludesStars;
	private Set<String> myEncodeElements;
	private Set<String> myEncodeElementsAppliesToResourceTypes;
	private boolean myEncodeElementsIncludesStars;
	private IIdType myEncodeForceResourceId;
	private IParserErrorHandler myErrorHandler;
	private boolean myOmitResourceId;
	private List<Class<? extends IBaseResource>> myPreferTypes;
	private String myServerBaseUrl;
	private Boolean myStripVersionsFromReferences;
	private Boolean myOverrideResourceIdWithBundleEntryFullUrl;
	private boolean mySummaryMode;
	private boolean mySuppressNarratives;
	private Set<String> myDontStripVersionsFromReferencesAtPaths;

	/**
	 * Constructor
	 *
	 * @param theParserErrorHandler
	 */
	public BaseParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		myContext = theContext;
		myErrorHandler = theParserErrorHandler;
	}

	protected Iterable<CompositeChildElement> compositeChildIterator(IBase theCompositeElement, final boolean theContainedResource, final boolean theSubResource, final CompositeChildElement theParent) {

		BaseRuntimeElementCompositeDefinition<?> elementDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theCompositeElement.getClass());
		final List<BaseRuntimeChildDefinition> children = elementDef.getChildrenAndExtension();

		return new Iterable<BaseParser.CompositeChildElement>() {
			@Override
			public Iterator<CompositeChildElement> iterator() {

				return new Iterator<CompositeChildElement>() {
					private Iterator<? extends BaseRuntimeChildDefinition> myChildrenIter;
					private Boolean myHasNext = null;
					private CompositeChildElement myNext;

					/**
					 * Constructor
					 */ {
						myChildrenIter = children.iterator();
					}

					@Override
					public boolean hasNext() {
						if (myHasNext != null) {
							return myHasNext;
						}

						myNext = null;
						do {
							if (myChildrenIter.hasNext() == false) {
								myHasNext = Boolean.FALSE;
								return false;
							}

							myNext = new CompositeChildElement(theParent, myChildrenIter.next(), theSubResource);

							/*
							 * There are lots of reasons we might skip encoding a particular child
							 */
							if (myNext.getDef().getElementName().equals("id")) {
								myNext = null;
							} else if (!myNext.shouldBeEncoded()) {
								myNext = null;
							} else if (isSummaryMode() && !myNext.getDef().isSummary()) {
								myNext = null;
							} else if (myNext.getDef() instanceof RuntimeChildNarrativeDefinition) {
								if (isSuppressNarratives() || isSummaryMode()) {
									myNext = null;
								} else if (theContainedResource) {
									myNext = null;
								}
							} else if (myNext.getDef() instanceof RuntimeChildContainedResources) {
								if (theContainedResource) {
									myNext = null;
								}
							}

						} while (myNext == null);

						myHasNext = true;
						return true;
					}

					@Override
					public CompositeChildElement next() {
						if (myHasNext == null) {
							if (!hasNext()) {
								throw new IllegalStateException();
							}
						}
						CompositeChildElement retVal = myNext;
						myNext = null;
						myHasNext = null;
						return retVal;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	private void containResourcesForEncoding(ContainedResources theContained, IBaseResource theResource, IBaseResource theTarget) {
		Map<String, IBaseResource> existingIdToContainedResource = null;

		if (theTarget instanceof IResource) {
			List<? extends IResource> containedResources = ((IResource) theTarget).getContained().getContainedResources();
			for (IResource next : containedResources) {
				String nextId = next.getId().getValue();
				if (StringUtils.isNotBlank(nextId)) {
					if (!nextId.startsWith("#")) {
						nextId = '#' + nextId;
					}
					if (existingIdToContainedResource == null) {
						existingIdToContainedResource = new HashMap<>();
					}
					existingIdToContainedResource.put(nextId, next);
				}
			}
		} else if (theTarget instanceof IDomainResource) {
			List<? extends IAnyResource> containedResources = ((IDomainResource) theTarget).getContained();
			for (IAnyResource next : containedResources) {
				String nextId = next.getIdElement().getValue();
				if (StringUtils.isNotBlank(nextId)) {
					if (!nextId.startsWith("#")) {
						nextId = '#' + nextId;
					}
					if (existingIdToContainedResource == null) {
						existingIdToContainedResource = new HashMap<>();
					}
					existingIdToContainedResource.put(nextId, next);
				}
			}
		} else {
			// no resources to contain
		}

		// Make sure we don't reuse local IDs
		if (existingIdToContainedResource != null) {
			theContained.addPreExistingLocalIds(existingIdToContainedResource.keySet());
		}

		{
			List<IBaseReference> allElements = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
			for (IBaseReference next : allElements) {
				IBaseResource resource = next.getResource();
				if (resource == null && next.getReferenceElement().isLocal()) {
					if (existingIdToContainedResource != null) {
						IBaseResource potentialTarget = existingIdToContainedResource.remove(next.getReferenceElement().getValue());
						if (potentialTarget != null) {
							theContained.addContained(next.getReferenceElement(), potentialTarget);
							containResourcesForEncoding(theContained, potentialTarget, theTarget);
						}
					}
				}
			}

			for (IBaseReference next : allElements) {
				IBaseResource resource = next.getResource();
				if (resource != null) {
					if (resource.getIdElement().isEmpty() || resource.getIdElement().isLocal()) {
						if (theContained.getResourceId(resource) != null) {
							// Prevent infinite recursion if there are circular loops in the contained resources
							continue;
						}
						theContained.addContained(resource);
						if (resource.getIdElement().isLocal() && existingIdToContainedResource != null) {
							existingIdToContainedResource.remove(resource.getIdElement().getValue());
						}
					} else {
						continue;
					}

					containResourcesForEncoding(theContained, resource, theTarget);
				}

			}
		}

	}

	protected void containResourcesForEncoding(IBaseResource theResource) {
		ContainedResources contained = new ContainedResources();
		containResourcesForEncoding(contained, theResource, theResource);
		myContainedResources = contained;
	}

	private String determineReferenceText(IBaseReference theRef, CompositeChildElement theCompositeChildElement) {
		IIdType ref = theRef.getReferenceElement();
		if (isBlank(ref.getIdPart())) {
			String reference = ref.getValue();
			if (theRef.getResource() != null) {
				IIdType containedId = getContainedResources().getResourceId(theRef.getResource());
				if (containedId != null && !containedId.isEmpty()) {
					if (containedId.isLocal()) {
						reference = containedId.getValue();
					} else {
						reference = "#" + containedId.getValue();
					}
				} else {
					IIdType refId = theRef.getResource().getIdElement();
					if (refId != null) {
						if (refId.hasIdPart()) {
							if (refId.getValue().startsWith("urn:")) {
								reference = refId.getValue();
							} else {
								if (!refId.hasResourceType()) {
									refId = refId.withResourceType(myContext.getResourceDefinition(theRef.getResource()).getName());
								}
								if (isStripVersionsFromReferences(theCompositeChildElement)) {
									reference = refId.toVersionless().getValue();
								} else {
									reference = refId.getValue();
								}
							}
						}
					}
				}
			}
			return reference;
		}
		if (!ref.hasResourceType() && !ref.isLocal() && theRef.getResource() != null) {
			ref = ref.withResourceType(myContext.getResourceDefinition(theRef.getResource()).getName());
		}
		if (isNotBlank(myServerBaseUrl) && StringUtils.equals(myServerBaseUrl, ref.getBaseUrl())) {
			if (isStripVersionsFromReferences(theCompositeChildElement)) {
				return ref.toUnqualifiedVersionless().getValue();
			}
			return ref.toUnqualified().getValue();
		}
		if (isStripVersionsFromReferences(theCompositeChildElement)) {
			return ref.toVersionless().getValue();
		}
		return ref.getValue();
	}

	protected abstract void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException, DataFormatException;

	protected abstract <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException;

	@Override
	public String encodeResourceToString(IBaseResource theResource) throws DataFormatException {
		Writer stringWriter = new StringWriter();
		try {
			encodeResourceToWriter(theResource, stringWriter);
		} catch (IOException e) {
			throw new Error("Encountered IOException during write to string - This should not happen!");
		}
		return stringWriter.toString();
	}

	@Override
	public final void encodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException, DataFormatException {
		Validate.notNull(theResource, "theResource can not be null");
		Validate.notNull(theWriter, "theWriter can not be null");

		if (theResource.getStructureFhirVersionEnum() != myContext.getVersion().getVersion()) {
			throw new IllegalArgumentException(
				"This parser is for FHIR version " + myContext.getVersion().getVersion() + " - Can not encode a structure for version " + theResource.getStructureFhirVersionEnum());
		}

		doEncodeResourceToWriter(theResource, theWriter);
	}

	private void filterCodingsWithNoCodeOrSystem(List<? extends IBaseCoding> tagList) {
		for (int i = 0; i < tagList.size(); i++) {
			if (isBlank(tagList.get(i).getCode()) && isBlank(tagList.get(i).getSystem())) {
				tagList.remove(i);
				i--;
			}
		}
	}

	protected IIdType fixContainedResourceId(String theValue) {
		IIdType retVal = (IIdType) myContext.getElementDefinition("id").newInstance();
		if (StringUtils.isNotBlank(theValue) && theValue.charAt(0) == '#') {
			retVal.setValue(theValue.substring(1));
		} else {
			retVal.setValue(theValue);
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	ChildNameAndDef getChildNameAndDef(BaseRuntimeChildDefinition theChild, IBase theValue) {
		Class<? extends IBase> type = theValue.getClass();
		String childName = theChild.getChildNameByDatatype(type);
		BaseRuntimeElementDefinition<?> childDef = theChild.getChildElementDefinitionByDatatype(type);
		if (childDef == null) {
			// if (theValue instanceof IBaseExtension) {
			// return null;
			// }

			/*
			 * For RI structures Enumeration class, this replaces the child def
			 * with the "code" one. This is messy, and presumably there is a better
			 * way..
			 */
			BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(type);
			if (elementDef.getName().equals("code")) {
				Class<? extends IBase> type2 = myContext.getElementDefinition("code").getImplementingClass();
				childDef = theChild.getChildElementDefinitionByDatatype(type2);
				childName = theChild.getChildNameByDatatype(type2);
			}

			// See possibly the user has extended a built-in type without
			// declaring it anywhere, as in XmlParserDstu3Test#testEncodeUndeclaredBlock
			if (childDef == null) {
				Class<?> nextSuperType = theValue.getClass();
				while (IBase.class.isAssignableFrom(nextSuperType) && childDef == null) {
					if (Modifier.isAbstract(nextSuperType.getModifiers()) == false) {
						BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition((Class<? extends IBase>) nextSuperType);
						Class<?> nextChildType = def.getImplementingClass();
						childDef = theChild.getChildElementDefinitionByDatatype((Class<? extends IBase>) nextChildType);
						childName = theChild.getChildNameByDatatype((Class<? extends IBase>) nextChildType);
					}
					nextSuperType = nextSuperType.getSuperclass();
				}
			}

			if (childDef == null) {
				throwExceptionForUnknownChildType(theChild, type);
			}
		}

		return new ChildNameAndDef(childName, childDef);
	}

	protected String getCompositeElementId(IBase theElement) {
		String elementId = null;
		if (!(theElement instanceof IBaseResource)) {
			if (theElement instanceof IBaseElement) {
				elementId = ((IBaseElement) theElement).getId();
			} else if (theElement instanceof IIdentifiableElement) {
				elementId = ((IIdentifiableElement) theElement).getElementSpecificId();
			}
		}
		return elementId;
	}

	ContainedResources getContainedResources() {
		return myContainedResources;
	}

	@Override
	public Set<String> getDontStripVersionsFromReferencesAtPaths() {
		return myDontStripVersionsFromReferencesAtPaths;
	}

	/**
	 * See {@link #setEncodeElements(Set)}
	 */
	@Override
	public Set<String> getEncodeElements() {
		return myEncodeElements;
	}

	@Override
	public void setEncodeElements(Set<String> theEncodeElements) {
		myEncodeElementsIncludesStars = false;
		if (theEncodeElements == null || theEncodeElements.isEmpty()) {
			myEncodeElements = null;
		} else {
			myEncodeElements = theEncodeElements;
			for (String next : theEncodeElements) {
				if (next.startsWith("*.")) {
					myEncodeElementsIncludesStars = true;
				}
			}
		}
	}

	/**
	 * See {@link #setEncodeElementsAppliesToResourceTypes(Set)}
	 */
	@Override
	public Set<String> getEncodeElementsAppliesToResourceTypes() {
		return myEncodeElementsAppliesToResourceTypes;
	}

	@Override
	public void setEncodeElementsAppliesToResourceTypes(Set<String> theEncodeElementsAppliesToResourceTypes) {
		if (theEncodeElementsAppliesToResourceTypes == null || theEncodeElementsAppliesToResourceTypes.isEmpty()) {
			myEncodeElementsAppliesToResourceTypes = null;
		} else {
			myEncodeElementsAppliesToResourceTypes = theEncodeElementsAppliesToResourceTypes;
		}
	}

	@Override
	public IIdType getEncodeForceResourceId() {
		return myEncodeForceResourceId;
	}

	@Override
	public BaseParser setEncodeForceResourceId(IIdType theEncodeForceResourceId) {
		myEncodeForceResourceId = theEncodeForceResourceId;
		return this;
	}

	protected IParserErrorHandler getErrorHandler() {
		return myErrorHandler;
	}

	protected List<Map.Entry<ResourceMetadataKeyEnum<?>, Object>> getExtensionMetadataKeys(IResource resource) {
		List<Map.Entry<ResourceMetadataKeyEnum<?>, Object>> extensionMetadataKeys = new ArrayList<Map.Entry<ResourceMetadataKeyEnum<?>, Object>>();
		for (Map.Entry<ResourceMetadataKeyEnum<?>, Object> entry : resource.getResourceMetadata().entrySet()) {
			if (entry.getKey() instanceof ResourceMetadataKeyEnum.ExtensionResourceMetadataKey) {
				extensionMetadataKeys.add(entry);
			}
		}

		return extensionMetadataKeys;
	}

	protected String getExtensionUrl(final String extensionUrl) {
		String url = extensionUrl;
		if (StringUtils.isNotBlank(extensionUrl) && StringUtils.isNotBlank(myServerBaseUrl)) {
			url = !UrlUtil.isValid(extensionUrl) && extensionUrl.startsWith("/") ? myServerBaseUrl + extensionUrl : extensionUrl;
		}
		return url;
	}

	protected TagList getMetaTagsForEncoding(IResource theIResource) {
		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(theIResource);
		if (shouldAddSubsettedTag()) {
			tags = new TagList(tags);
			tags.add(new Tag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE, subsetDescription()));
		}

		return tags;
	}

	@Override
	public Boolean getOverrideResourceIdWithBundleEntryFullUrl() {
		return myOverrideResourceIdWithBundleEntryFullUrl;
	}

	@Override
	public List<Class<? extends IBaseResource>> getPreferTypes() {
		return myPreferTypes;
	}

	@Override
	public void setPreferTypes(List<Class<? extends IBaseResource>> thePreferTypes) {
		if (thePreferTypes != null) {
			ArrayList<Class<? extends IBaseResource>> types = new ArrayList<>();
			for (Class<? extends IBaseResource> next : thePreferTypes) {
				if (Modifier.isAbstract(next.getModifiers()) == false) {
					types.add(next);
				}
			}
			myPreferTypes = Collections.unmodifiableList(types);
		} else {
			myPreferTypes = thePreferTypes;
		}
	}

	@SuppressWarnings("deprecation")
	protected <T extends IPrimitiveType<String>> List<T> getProfileTagsForEncoding(IBaseResource theResource, List<T> theProfiles) {
		switch (myContext.getAddProfileTagWhenEncoding()) {
			case NEVER:
				return theProfiles;
			case ONLY_FOR_CUSTOM:
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
				if (resDef.isStandardType()) {
					return theProfiles;
				}
				break;
			case ALWAYS:
				break;
		}

		RuntimeResourceDefinition nextDef = myContext.getResourceDefinition(theResource);
		String profile = nextDef.getResourceProfile(myServerBaseUrl);
		if (isNotBlank(profile)) {
			for (T next : theProfiles) {
				if (profile.equals(next.getValue())) {
					return theProfiles;
				}
			}

			List<T> newList = new ArrayList<>(theProfiles);

			BaseRuntimeElementDefinition<?> idElement = myContext.getElementDefinition("id");
			@SuppressWarnings("unchecked")
			T newId = (T) idElement.newInstance();
			newId.setValue(profile);

			newList.add(newId);
			return newList;
		}

		return theProfiles;
	}

	protected String getServerBaseUrl() {
		return myServerBaseUrl;
	}

	@Override
	public Boolean getStripVersionsFromReferences() {
		return myStripVersionsFromReferences;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded
	 * values.
	 *
	 * @deprecated Use {@link #isSuppressNarratives()}
	 */
	@Deprecated
	public boolean getSuppressNarratives() {
		return mySuppressNarratives;
	}

	protected boolean isChildContained(BaseRuntimeElementDefinition<?> childDef, boolean theIncludedResource) {
		return (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES || childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) && getContainedResources().isEmpty() == false
			&& theIncludedResource == false;
	}

	@Override
	public boolean isEncodeElementsAppliesToChildResourcesOnly() {
		return myEncodeElementsAppliesToChildResourcesOnly;
	}

	@Override
	public void setEncodeElementsAppliesToChildResourcesOnly(boolean theEncodeElementsAppliesToChildResourcesOnly) {
		myEncodeElementsAppliesToChildResourcesOnly = theEncodeElementsAppliesToChildResourcesOnly;
	}

	@Override
	public boolean isOmitResourceId() {
		return myOmitResourceId;
	}

	private boolean isOverrideResourceIdWithBundleEntryFullUrl() {
		Boolean overrideResourceIdWithBundleEntryFullUrl = myOverrideResourceIdWithBundleEntryFullUrl;
		if (overrideResourceIdWithBundleEntryFullUrl != null) {
			return overrideResourceIdWithBundleEntryFullUrl;
		}

		return myContext.getParserOptions().isOverrideResourceIdWithBundleEntryFullUrl();
	}

	private boolean isStripVersionsFromReferences(CompositeChildElement theCompositeChildElement) {
		Boolean stripVersionsFromReferences = myStripVersionsFromReferences;
		if (stripVersionsFromReferences != null) {
			return stripVersionsFromReferences;
		}

		if (myContext.getParserOptions().isStripVersionsFromReferences() == false) {
			return false;
		}

		Set<String> dontStripVersionsFromReferencesAtPaths = myDontStripVersionsFromReferencesAtPaths;
		if (dontStripVersionsFromReferencesAtPaths != null) {
			if (dontStripVersionsFromReferencesAtPaths.isEmpty() == false && theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths)) {
				return false;
			}
		}

		dontStripVersionsFromReferencesAtPaths = myContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths();
		if (dontStripVersionsFromReferencesAtPaths.isEmpty() == false && theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isSummaryMode() {
		return mySummaryMode;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded
	 * values.
	 *
	 * @since 1.2
	 */
	public boolean isSuppressNarratives() {
		return mySuppressNarratives;
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException {

		/*
		 * We do this so that the context can verify that the structure is for
		 * the correct FHIR version
		 */
		if (theResourceType != null) {
			myContext.getResourceDefinition(theResourceType);
		}

		// Actually do the parse
		T retVal = doParseResource(theResourceType, theReader);

		RuntimeResourceDefinition def = myContext.getResourceDefinition(retVal);
		if ("Bundle".equals(def.getName())) {

			BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
			BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
			List<IBase> entries = entryChild.getAccessor().getValues(retVal);
			if (entries != null) {
				for (IBase nextEntry : entries) {

					/**
					 * If Bundle.entry.fullUrl is populated, set the resource ID to that
					 */
					// TODO: should emit a warning and maybe notify the error handler if the resource ID doesn't match the
					// fullUrl idPart
					BaseRuntimeChildDefinition fullUrlChild = entryDef.getChildByName("fullUrl");
					if (fullUrlChild == null) {
						continue; // TODO: remove this once the data model in tinder plugin catches up to 1.2
					}
					if (isOverrideResourceIdWithBundleEntryFullUrl()) {
						List<IBase> fullUrl = fullUrlChild.getAccessor().getValues(nextEntry);
						if (fullUrl != null && !fullUrl.isEmpty()) {
							IPrimitiveType<?> value = (IPrimitiveType<?>) fullUrl.get(0);
							if (value.isEmpty() == false) {
								List<IBase> entryResources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry);
								if (entryResources != null && entryResources.size() > 0) {
									IBaseResource res = (IBaseResource) entryResources.get(0);
									String versionId = res.getIdElement().getVersionIdPart();
									res.setId(value.getValueAsString());
									if (isNotBlank(versionId) && res.getIdElement().hasVersionIdPart() == false) {
										res.setId(res.getIdElement().withVersion(versionId));
									}
								}
							}
						}
					}
				}
			}

		}

		return retVal;
	}

	@SuppressWarnings("cast")
	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		StringReader reader = new StringReader(theMessageString);
		return (T) parseResource(theResourceType, reader);
	}

	@Override
	public IBaseResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
		return parseResource(null, theReader);
	}

	@Override
	public IBaseResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
		return parseResource(null, theMessageString);
	}

	protected List<? extends IBase> preProcessValues(BaseRuntimeChildDefinition theMetaChildUncast, IBaseResource theResource, List<? extends IBase> theValues,
																	 CompositeChildElement theCompositeChildElement) {
		if (myContext.getVersion().getVersion().isRi()) {

			/*
			 * If we're encoding the meta tag, we do some massaging of the meta values before
			 * encoding. But if there is no meta element at all, we create one since we're possibly going to be
			 * adding things to it
			 */
			if (theValues.isEmpty() && theMetaChildUncast.getElementName().equals("meta")) {
				BaseRuntimeElementDefinition<?> metaChild = theMetaChildUncast.getChildByName("meta");
				if (IBaseMetaType.class.isAssignableFrom(metaChild.getImplementingClass())) {
					IBaseMetaType newType = (IBaseMetaType) metaChild.newInstance();
					theValues = Collections.singletonList(newType);
				}
			}

			if (theValues.size() == 1 && theValues.get(0) instanceof IBaseMetaType) {

				IBaseMetaType metaValue = (IBaseMetaType) theValues.get(0);
				try {
					metaValue = (IBaseMetaType) metaValue.getClass().getMethod("copy").invoke(metaValue);
				} catch (Exception e) {
					throw new InternalErrorException("Failed to duplicate meta", e);
				}

				if (isBlank(metaValue.getVersionId())) {
					if (theResource.getIdElement().hasVersionIdPart()) {
						metaValue.setVersionId(theResource.getIdElement().getVersionIdPart());
					}
				}

				filterCodingsWithNoCodeOrSystem(metaValue.getTag());
				filterCodingsWithNoCodeOrSystem(metaValue.getSecurity());

				List<? extends IPrimitiveType<String>> newProfileList = getProfileTagsForEncoding(theResource, metaValue.getProfile());
				List<? extends IPrimitiveType<String>> oldProfileList = metaValue.getProfile();
				if (oldProfileList != newProfileList) {
					oldProfileList.clear();
					for (IPrimitiveType<String> next : newProfileList) {
						if (isNotBlank(next.getValue())) {
							metaValue.addProfile(next.getValue());
						}
					}
				}

				if (shouldAddSubsettedTag()) {
					IBaseCoding coding = metaValue.addTag();
					coding.setCode(Constants.TAG_SUBSETTED_CODE);
					coding.setSystem(Constants.TAG_SUBSETTED_SYSTEM);
					coding.setDisplay(subsetDescription());
				}

				return Collections.singletonList(metaValue);
			}
		}

		@SuppressWarnings("unchecked")
		List<IBase> retVal = (List<IBase>) theValues;

		for (int i = 0; i < retVal.size(); i++) {
			IBase next = retVal.get(i);

			/*
			 * If we have automatically contained any resources via
			 * their references, this ensures that we output the new
			 * local reference
			 */
			if (next instanceof IBaseReference) {
				IBaseReference nextRef = (IBaseReference) next;
				String refText = determineReferenceText(nextRef, theCompositeChildElement);
				if (!StringUtils.equals(refText, nextRef.getReferenceElement().getValue())) {

					if (retVal == theValues) {
						retVal = new ArrayList<IBase>(theValues);
					}
					IBaseReference newRef = (IBaseReference) myContext.getElementDefinition(nextRef.getClass()).newInstance();
					myContext.newTerser().cloneInto(nextRef, newRef, true);
					newRef.setReference(refText);
					retVal.set(i, newRef);

				}
			}
		}

		return retVal;
	}

	@Override
	public void setDontEncodeElements(Set<String> theDontEncodeElements) {
		myDontEncodeElementsIncludesStars = false;
		if (theDontEncodeElements == null || theDontEncodeElements.isEmpty()) {
			myDontEncodeElements = null;
		} else {
			myDontEncodeElements = theDontEncodeElements;
			for (String next : theDontEncodeElements) {
				if (next.startsWith("*.")) {
					myDontEncodeElementsIncludesStars = true;
				}
			}
		}
	}

	@Override
	public IParser setDontStripVersionsFromReferencesAtPaths(String... thePaths) {
		if (thePaths == null) {
			setDontStripVersionsFromReferencesAtPaths((List<String>) null);
		} else {
			setDontStripVersionsFromReferencesAtPaths(Arrays.asList(thePaths));
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IParser setDontStripVersionsFromReferencesAtPaths(Collection<String> thePaths) {
		if (thePaths == null) {
			myDontStripVersionsFromReferencesAtPaths = Collections.emptySet();
		} else if (thePaths instanceof HashSet) {
			myDontStripVersionsFromReferencesAtPaths = (Set<String>) ((HashSet<String>) thePaths).clone();
		} else {
			myDontStripVersionsFromReferencesAtPaths = new HashSet<String>(thePaths);
		}
		return this;
	}

	@Override
	public IParser setOmitResourceId(boolean theOmitResourceId) {
		myOmitResourceId = theOmitResourceId;
		return this;
	}

	@Override
	public IParser setOverrideResourceIdWithBundleEntryFullUrl(Boolean theOverrideResourceIdWithBundleEntryFullUrl) {
		myOverrideResourceIdWithBundleEntryFullUrl = theOverrideResourceIdWithBundleEntryFullUrl;
		return this;
	}

	@Override
	public IParser setParserErrorHandler(IParserErrorHandler theErrorHandler) {
		Validate.notNull(theErrorHandler, "theErrorHandler must not be null");
		myErrorHandler = theErrorHandler;
		return this;
	}

	@Override
	public IParser setServerBaseUrl(String theUrl) {
		myServerBaseUrl = isNotBlank(theUrl) ? theUrl : null;
		return this;
	}

	@Override
	public IParser setStripVersionsFromReferences(Boolean theStripVersionsFromReferences) {
		myStripVersionsFromReferences = theStripVersionsFromReferences;
		return this;
	}

	@Override
	public IParser setSummaryMode(boolean theSummaryMode) {
		mySummaryMode = theSummaryMode;
		return this;
	}

	@Override
	public IParser setSuppressNarratives(boolean theSuppressNarratives) {
		mySuppressNarratives = theSuppressNarratives;
		return this;
	}

	protected boolean shouldAddSubsettedTag() {
		return isSummaryMode() || isSuppressNarratives() || getEncodeElements() != null;
	}

	protected boolean shouldEncodeResourceId(IBaseResource theResource, boolean theSubResource) {
		boolean retVal = true;
		if (isOmitResourceId()) {
			retVal = false;
		} else {
			if (myDontEncodeElements != null) {
				String resourceName = myContext.getResourceDefinition(theResource).getName();
				if (myDontEncodeElements.contains(resourceName + ".id")) {
					retVal = false;
				} else if (myDontEncodeElements.contains("*.id")) {
					retVal = false;
				} else if (theSubResource == false && myDontEncodeElements.contains("id")) {
					retVal = false;
				}
			}
		}
		return retVal;
	}

	/**
	 * Used for DSTU2 only
	 */
	protected boolean shouldEncodeResourceMeta(IResource theResource) {
		if (myDontEncodeElements != null) {
			String resourceName = myContext.getResourceDefinition(theResource).getName();
			if (myDontEncodeElements.contains(resourceName + ".meta")) {
				return false;
			} else if (myDontEncodeElements.contains("*.meta")) {
				return false;
			}
		}
		return true;
	}

	private String subsetDescription() {
		return "Resource encoded in summary mode";
	}

	protected void throwExceptionForUnknownChildType(BaseRuntimeChildDefinition nextChild, Class<? extends IBase> theType) {
		if (nextChild instanceof BaseRuntimeDeclaredChildDefinition) {
			StringBuilder b = new StringBuilder();
			b.append(((BaseRuntimeDeclaredChildDefinition) nextChild).getElementName());
			b.append(" has type ");
			b.append(theType.getName());
			b.append(" but this is not a valid type for this element");
			if (nextChild instanceof RuntimeChildChoiceDefinition) {
				RuntimeChildChoiceDefinition choice = (RuntimeChildChoiceDefinition) nextChild;
				b.append(" - Expected one of: " + choice.getValidChildTypes());
			}
			throw new DataFormatException(b.toString());
		}
		throw new DataFormatException(nextChild + " has no child of type " + theType);
	}

	protected static <T> List<T> extractMetadataListNotNull(IResource resource, ResourceMetadataKeyEnum<List<T>> key) {
		List<? extends T> securityLabels = key.get(resource);
		if (securityLabels == null) {
			securityLabels = Collections.emptyList();
		}
		return new ArrayList<T>(securityLabels);
	}

	static boolean hasExtensions(IBase theElement) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theElement;
			if (res.getUndeclaredExtensions().size() > 0 || res.getUndeclaredModifierExtensions().size() > 0) {
				return true;
			}
		}
		if (theElement instanceof IBaseHasExtensions) {
			IBaseHasExtensions res = (IBaseHasExtensions) theElement;
			if (res.hasExtension()) {
				return true;
			}
		}
		if (theElement instanceof IBaseHasModifierExtensions) {
			IBaseHasModifierExtensions res = (IBaseHasModifierExtensions) theElement;
			if (res.hasModifierExtension()) {
				return true;
			}
		}
		return false;
	}

	class ChildNameAndDef {

		private final BaseRuntimeElementDefinition<?> myChildDef;
		private final String myChildName;

		public ChildNameAndDef(String theChildName, BaseRuntimeElementDefinition<?> theChildDef) {
			myChildName = theChildName;
			myChildDef = theChildDef;
		}

		public BaseRuntimeElementDefinition<?> getChildDef() {
			return myChildDef;
		}

		public String getChildName() {
			return myChildName;
		}

	}

	protected class CompositeChildElement {
		private final BaseRuntimeChildDefinition myDef;
		private final CompositeChildElement myParent;
		private final RuntimeResourceDefinition myResDef;
		private final boolean mySubResource;

		public CompositeChildElement(CompositeChildElement theParent, BaseRuntimeChildDefinition theDef, boolean theSubResource) {
			myDef = theDef;
			myParent = theParent;
			myResDef = null;
			mySubResource = theSubResource;

			if (ourLog.isTraceEnabled()) {
				if (theParent != null) {
					StringBuilder path = theParent.buildPath();
					if (path != null) {
						path.append('.');
						path.append(myDef.getElementName());
						ourLog.trace(" * Next path: {}", path.toString());
					}
				}
			}

		}

		public CompositeChildElement(RuntimeResourceDefinition theResDef, boolean theSubResource) {
			myResDef = theResDef;
			myDef = null;
			myParent = null;
			mySubResource = theSubResource;
		}

		private void addParent(CompositeChildElement theParent, StringBuilder theB) {
			if (theParent != null) {
				if (theParent.myResDef != null) {
					theB.append(theParent.myResDef.getName());
					return;
				}

				if (theParent.myParent != null) {
					addParent(theParent.myParent, theB);
				}

				if (theParent.myDef != null) {
					if (theB.length() > 0) {
						theB.append('.');
					}
					theB.append(theParent.myDef.getElementName());
				}
			}
		}

		public boolean anyPathMatches(Set<String> thePaths) {
			StringBuilder b = new StringBuilder();
			addParent(this, b);

			String path = b.toString();
			return thePaths.contains(path);
		}

		private StringBuilder buildPath() {
			if (myResDef != null) {
				StringBuilder b = new StringBuilder();
				b.append(myResDef.getName());
				return b;
			} else if (myParent != null) {
				StringBuilder b = myParent.buildPath();
				if (b != null && myDef != null) {
					b.append('.');
					b.append(myDef.getElementName());
				}
				return b;
			} else {
				return null;
			}
		}

		private boolean checkIfParentShouldBeEncodedAndBuildPath(StringBuilder thePathBuilder, boolean theStarPass) {
			Set<String> encodeElements = myEncodeElements;
			if (encodeElements != null && encodeElements.isEmpty() == false) {
				if (isEncodeElementsAppliesToChildResourcesOnly() && !mySubResource) {
					encodeElements = null;
				}
			}
			return checkIfPathMatchesForEncoding(thePathBuilder, theStarPass, myEncodeElementsAppliesToResourceTypes, encodeElements, true);
		}

		private boolean checkIfParentShouldNotBeEncodedAndBuildPath(StringBuilder thePathBuilder, boolean theStarPass) {
			return checkIfPathMatchesForEncoding(thePathBuilder, theStarPass, null, myDontEncodeElements, false);
		}

		private boolean checkIfPathMatchesForEncoding(StringBuilder thePathBuilder, boolean theStarPass, Set<String> theResourceTypes, Set<String> theElements, boolean theCheckingForWhitelist) {
			if (myResDef != null) {
				if (theResourceTypes != null) {
					if (!theResourceTypes.contains(myResDef.getName())) {
						return true;
					}
				}
				if (theStarPass) {
					thePathBuilder.append('*');
				} else {
					thePathBuilder.append(myResDef.getName());
				}
				if (theElements == null) {
					return true;
				}
				if (theElements.contains(thePathBuilder.toString())) {
					return true;
				}
				return false;
			} else if (myParent != null) {
				boolean parentCheck;
				if (theCheckingForWhitelist) {
					parentCheck = myParent.checkIfParentShouldBeEncodedAndBuildPath(thePathBuilder, theStarPass);
				} else {
					parentCheck = myParent.checkIfParentShouldNotBeEncodedAndBuildPath(thePathBuilder, theStarPass);
				}
				if (parentCheck) {
					return true;
				}

				if (myDef != null) {
					if (myDef.getMin() > 0) {
						if (theElements.contains("*.(mandatory)")) {
							return true;
						}
					}

					thePathBuilder.append('.');
					thePathBuilder.append(myDef.getElementName());
					String currentPath = thePathBuilder.toString();
					boolean retVal = theElements.contains(currentPath);
					int dotIdx = currentPath.indexOf('.');
					if (!retVal) {
						if (dotIdx != -1 && theElements.contains(currentPath.substring(dotIdx + 1))) {
							if (!myParent.isSubResource()) {
								return true;
							}
						}
					}
					return retVal;
				}
			}

			return false;
		}

		public BaseRuntimeChildDefinition getDef() {
			return myDef;
		}

		public CompositeChildElement getParent() {
			return myParent;
		}

		public RuntimeResourceDefinition getResDef() {
			return myResDef;
		}

		private boolean isSubResource() {
			return mySubResource;
		}

		public boolean shouldBeEncoded() {
			boolean retVal = true;
			if (myEncodeElements != null) {
				retVal = checkIfParentShouldBeEncodedAndBuildPath(new StringBuilder(), false);
				if (retVal == false && myEncodeElementsIncludesStars) {
					retVal = checkIfParentShouldBeEncodedAndBuildPath(new StringBuilder(), true);
				}
			}
			if (retVal && myDontEncodeElements != null) {
				retVal = !checkIfParentShouldNotBeEncodedAndBuildPath(new StringBuilder(), false);
				if (retVal && myDontEncodeElementsIncludesStars) {
					retVal = !checkIfParentShouldNotBeEncodedAndBuildPath(new StringBuilder(), true);
				}
			}
			// if (retVal == false && myEncodeElements.contains("*.(mandatory)")) {
			// if (myDef.getMin() > 0) {
			// retVal = true;
			// }
			// }

			return retVal;
		}
	}

	static class ContainedResources {
		private long myNextContainedId = 1;

		private List<IBaseResource> myResources;
		private IdentityHashMap<IBaseResource, IIdType> myResourceToId;
		private Set<String> myPreExistingLocalIds;

		public void addContained(IBaseResource theResource) {
			initializeMapsIfNeeded();
			if (myResourceToId.containsKey(theResource)) {
				return;
			}

			IIdType newId;
			if (theResource.getIdElement().isLocal()) {
				newId = theResource.getIdElement();
			} else {
				// TODO: make this configurable between the two below (and something else?)
				// newId = new IdDt(UUID.randomUUID().toString());

				// Generally we won't even go into this loop once since
				// our next ID shouldn't already exist, but there is
				// always a chance that it does if someone is mixing
				// manually assigned local IDs with HAPI assigned ones
				// See JsonParser#testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalFirst
				// and JsonParser#testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast
				// for examples of where this is needed...
				while (true) {
					String nextCandidate = "#" + myNextContainedId;
					if (myPreExistingLocalIds.add(nextCandidate)) {
							break;
					}
					myNextContainedId++;
				}
				newId = new IdDt(myNextContainedId);

				myNextContainedId++;
			}

			myResourceToId.put(theResource, newId);
			myResources.add(theResource);
		}

		public void addContained(IIdType theId, IBaseResource theResource) {
			initializeMapsIfNeeded();
			if (!myResourceToId.containsKey(theResource)) {
				myResourceToId.put(theResource, theId);
				myResources.add(theResource);
			}
		}

		public void addPreExistingLocalIds(Set<String> theIds) {
			initializeMapsIfNeeded();
			myPreExistingLocalIds.addAll(theIds);
		}

		public List<IBaseResource> getContainedResources() {
			if (myResources == null) {
				return Collections.emptyList();
			}
			return myResources;
		}

		public IIdType getResourceId(IBaseResource theNext) {
			if (myResourceToId == null) {
				return null;
			}
			return myResourceToId.get(theNext);
		}

		private void initializeMapsIfNeeded() {
			if (myResources == null) {
				myResources = new ArrayList<>();
				myResourceToId = new IdentityHashMap<>();
				myPreExistingLocalIds = new HashSet<>();
			}
		}

		public boolean isEmpty() {
			if (myResourceToId == null) {
				return true;
			}
			return myResourceToId.isEmpty();
		}

	}

}
