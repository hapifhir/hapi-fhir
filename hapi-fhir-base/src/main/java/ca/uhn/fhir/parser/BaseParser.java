package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("WeakerAccess")
public abstract class BaseParser implements IParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParser.class);

	private ContainedResources myContainedResources;
	private boolean myEncodeElementsAppliesToChildResourcesOnly;
	private FhirContext myContext;
	private List<ElementsPath> myDontEncodeElements;
	private List<ElementsPath> myEncodeElements;
	private Set<String> myEncodeElementsAppliesToResourceTypes;
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

	/*
	 * Constructor
	 */
	public BaseParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		myContext = theContext;
		myErrorHandler = theParserErrorHandler;
	}

	List<ElementsPath> getDontEncodeElements() {
		return myDontEncodeElements;
	}

	@Override
	public void setDontEncodeElements(Set<String> theDontEncodeElements) {
		if (theDontEncodeElements == null || theDontEncodeElements.isEmpty()) {
			myDontEncodeElements = null;
		} else {
			myDontEncodeElements = theDontEncodeElements
				.stream()
				.map(ElementsPath::new)
				.collect(Collectors.toList());
		}
	}

	List<ElementsPath> getEncodeElements() {
		return myEncodeElements;
	}

	@Override
	public void setEncodeElements(Set<String> theEncodeElements) {

		if (theEncodeElements == null || theEncodeElements.isEmpty()) {
			myEncodeElements = null;
			myEncodeElementsAppliesToResourceTypes = null;
		} else {
			myEncodeElements = theEncodeElements
				.stream()
				.map(ElementsPath::new)
				.collect(Collectors.toList());

			myEncodeElementsAppliesToResourceTypes = new HashSet<>();
			for (String next : myEncodeElements.stream().map(t -> t.getPath().get(0).getName()).collect(Collectors.toList())) {
				if (next.startsWith("*")) {
					myEncodeElementsAppliesToResourceTypes = null;
					break;
				}
				int dotIdx = next.indexOf('.');
				if (dotIdx == -1) {
					myEncodeElementsAppliesToResourceTypes.add(next);
				} else {
					myEncodeElementsAppliesToResourceTypes.add(next.substring(0, dotIdx));
				}
			}

		}
	}

	protected Iterable<CompositeChildElement> compositeChildIterator(IBase theCompositeElement,
																						  final boolean theContainedResource,
																						  final CompositeChildElement theParent,
																						  EncodeContext theEncodeContext) {

		BaseRuntimeElementCompositeDefinition<?> elementDef =
			(BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theCompositeElement.getClass());
		final List<BaseRuntimeChildDefinition> children = elementDef.getChildrenAndExtension();

		return new Iterable<BaseParser.CompositeChildElement>() {

			@Override
			public Iterator<CompositeChildElement> iterator() {

				return new Iterator<CompositeChildElement>() {
					private Iterator<? extends BaseRuntimeChildDefinition> myChildrenIter;
					private Boolean myHasNext = null;
					private CompositeChildElement myNext;

					/*
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
							if (!myChildrenIter.hasNext()) {
								myHasNext = Boolean.FALSE;
								return false;
							}

							myNext = new CompositeChildElement(theParent, myChildrenIter.next(), theEncodeContext);

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

		if (theTarget instanceof IResource) {
			List<? extends IResource> containedResources = ((IResource) theTarget).getContained().getContainedResources();
			for (IResource next : containedResources) {
				String nextId = next.getId().getValue();
				if (StringUtils.isNotBlank(nextId)) {
					if (!nextId.startsWith("#")) {
						nextId = '#' + nextId;
					}
					theContained.getExistingIdToContainedResource().put(nextId, next);
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
					theContained.getExistingIdToContainedResource().put(nextId, next);
				}
			}
		}

		List<IBaseReference> allReferences = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
		for (IBaseReference next : allReferences) {
			IBaseResource resource = next.getResource();
			if (resource == null && next.getReferenceElement().isLocal()) {
				if (theContained.hasExistingIdToContainedResource()) {
					IBaseResource potentialTarget = theContained.getExistingIdToContainedResource().remove(next.getReferenceElement().getValue());
					if (potentialTarget != null) {
						theContained.addContained(next.getReferenceElement(), potentialTarget);
						containResourcesForEncoding(theContained, potentialTarget, theTarget);
					}
				}
			}
		}

		for (IBaseReference next : allReferences) {
			IBaseResource resource = next.getResource();
			if (resource != null) {
				if (resource.getIdElement().isEmpty() || resource.getIdElement().isLocal()) {
					if (theContained.getResourceId(resource) != null) {
						// Prevent infinite recursion if there are circular loops in the contained resources
						continue;
					}
					theContained.addContained(resource);
					if (resource.getIdElement().isLocal() && theContained.hasExistingIdToContainedResource()) {
						theContained.getExistingIdToContainedResource().remove(resource.getIdElement().getValue());
					}
				} else {
					continue;
				}

				containResourcesForEncoding(theContained, resource, theTarget);
			}

		}

	}

	protected void containResourcesForEncoding(IBaseResource theResource) {
		ContainedResources contained = new ContainedResources();
		containResourcesForEncoding(contained, theResource, theResource);
		contained.assignIdsToContainedResources();
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

	protected abstract void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext) throws IOException, DataFormatException;

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
		EncodeContext encodeContext = new EncodeContext();

		encodeResourceToWriter(theResource, theWriter, encodeContext);
	}

	protected void encodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext) throws IOException {
		Validate.notNull(theResource, "theResource can not be null");
		Validate.notNull(theWriter, "theWriter can not be null");
		Validate.notNull(theEncodeContext, "theEncodeContext can not be null");

		if (theResource.getStructureFhirVersionEnum() != myContext.getVersion().getVersion()) {
			throw new IllegalArgumentException(
				"This parser is for FHIR version " + myContext.getVersion().getVersion() + " - Can not encode a structure for version " + theResource.getStructureFhirVersionEnum());
		}

		String resourceName = myContext.getResourceDefinition(theResource).getName();
		theEncodeContext.pushPath(resourceName, true);

		doEncodeResourceToWriter(theResource, theWriter, theEncodeContext);

		theEncodeContext.popPath();
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
					if (!Modifier.isAbstract(nextSuperType.getModifiers())) {
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
		List<Map.Entry<ResourceMetadataKeyEnum<?>, Object>> extensionMetadataKeys = new ArrayList<>();
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

	protected TagList getMetaTagsForEncoding(IResource theIResource, EncodeContext theEncodeContext) {
		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(theIResource);
		if (shouldAddSubsettedTag(theEncodeContext)) {
			tags = new TagList(tags);
			tags.add(new Tag(getSubsettedCodeSystem(), Constants.TAG_SUBSETTED_CODE, subsetDescription()));
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
				if (!Modifier.isAbstract(next.getModifiers())) {
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
		return (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES
			|| childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST)
			&& !getContainedResources().isEmpty()
			&& !theIncludedResource;
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

		if (!myContext.getParserOptions().isStripVersionsFromReferences()) {
			return false;
		}

		Set<String> dontStripVersionsFromReferencesAtPaths = myDontStripVersionsFromReferencesAtPaths;
		if (dontStripVersionsFromReferencesAtPaths != null) {
			if (!dontStripVersionsFromReferencesAtPaths.isEmpty() && theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths)) {
				return false;
			}
		}

		dontStripVersionsFromReferencesAtPaths = myContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths();
		return dontStripVersionsFromReferencesAtPaths.isEmpty() || !theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths);
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
	public IBaseResource parseResource(InputStream theInputStream) throws DataFormatException {
		return parseResource(new InputStreamReader(theInputStream, Charsets.UTF_8));
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, InputStream theInputStream) throws DataFormatException {
		return parseResource(theResourceType, new InputStreamReader(theInputStream, Constants.CHARSET_UTF8));
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

					/*
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
							if (!value.isEmpty()) {
								List<IBase> entryResources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry);
								if (entryResources != null && entryResources.size() > 0) {
									IBaseResource res = (IBaseResource) entryResources.get(0);
									String versionId = res.getIdElement().getVersionIdPart();
									res.setId(value.getValueAsString());
									if (isNotBlank(versionId) && !res.getIdElement().hasVersionIdPart()) {
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
		return parseResource(theResourceType, reader);
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
																	 CompositeChildElement theCompositeChildElement, EncodeContext theEncodeContext) {
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

				if (shouldAddSubsettedTag(theEncodeContext)) {
					IBaseCoding coding = metaValue.addTag();
					coding.setCode(Constants.TAG_SUBSETTED_CODE);
					coding.setSystem(getSubsettedCodeSystem());
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
						retVal = new ArrayList<>(theValues);
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

	private String getSubsettedCodeSystem() {
		if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			return Constants.TAG_SUBSETTED_SYSTEM_R4;
		} else {
			return Constants.TAG_SUBSETTED_SYSTEM_DSTU3;
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
			myDontStripVersionsFromReferencesAtPaths = new HashSet<>(thePaths);
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

	protected boolean shouldAddSubsettedTag(EncodeContext theEncodeContext) {
		if (isSummaryMode()) {
			return true;
		}
		if (isSuppressNarratives()) {
			return true;
		}
		if (myEncodeElements != null) {
			if (isEncodeElementsAppliesToChildResourcesOnly() && theEncodeContext.getResourcePath().size() < 2) {
				return false;
			}

			String currentResourceName = theEncodeContext.getResourcePath().get(theEncodeContext.getResourcePath().size() - 1).getName();
			return myEncodeElementsAppliesToResourceTypes == null || myEncodeElementsAppliesToResourceTypes.contains(currentResourceName);
		}

		return false;
	}

	protected boolean shouldEncodeResourceId(IBaseResource theResource, EncodeContext theEncodeContext) {
		boolean retVal = true;
		if (isOmitResourceId()) {
			retVal = false;
		} else {
			if (myDontEncodeElements != null) {
				String resourceName = myContext.getResourceDefinition(theResource).getName();
				if (myDontEncodeElements.stream().anyMatch(t -> t.equalsPath(resourceName + ".id"))) {
					retVal = false;
				} else if (myDontEncodeElements.stream().anyMatch(t -> t.equalsPath("*.id"))) {
					retVal = false;
				} else if (theEncodeContext.getResourcePath().size() == 1 && myDontEncodeElements.stream().anyMatch(t -> t.equalsPath("id"))) {
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
		return shouldEncodePath(theResource, "meta");
	}

	/**
	 * Used for DSTU2 only
	 */
	protected boolean shouldEncodePath(IResource theResource, String thePath) {
		if (myDontEncodeElements != null) {
			String resourceName = myContext.getResourceDefinition(theResource).getName();
			if (myDontEncodeElements.stream().anyMatch(t -> t.equalsPath(resourceName + "." + thePath))) {
				return false;
			} else return myDontEncodeElements.stream().noneMatch(t -> t.equalsPath("*." + thePath));
		}
		return true;
	}

	private String subsetDescription() {
		return "Resource encoded in summary mode";
	}

	protected void throwExceptionForUnknownChildType(BaseRuntimeChildDefinition nextChild, Class<? extends IBase> theType) {
		if (nextChild instanceof BaseRuntimeDeclaredChildDefinition) {
			StringBuilder b = new StringBuilder();
			b.append(nextChild.getElementName());
			b.append(" has type ");
			b.append(theType.getName());
			b.append(" but this is not a valid type for this element");
			if (nextChild instanceof RuntimeChildChoiceDefinition) {
				RuntimeChildChoiceDefinition choice = (RuntimeChildChoiceDefinition) nextChild;
				b.append(" - Expected one of: ").append(choice.getValidChildTypes());
			}
			throw new DataFormatException(b.toString());
		}
		throw new DataFormatException(nextChild + " has no child of type " + theType);
	}

	protected boolean shouldEncodeResource(String theName) {
		if (myDontEncodeElements != null) {
			for (ElementsPath next : myDontEncodeElements) {
				if (next.equalsPath(theName)) {
					return false;
				}
			}
		}
		return true;
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
		private final EncodeContext myEncodeContext;

		public CompositeChildElement(CompositeChildElement theParent, BaseRuntimeChildDefinition theDef, EncodeContext theEncodeContext) {
			myDef = theDef;
			myParent = theParent;
			myResDef = null;
			myEncodeContext = theEncodeContext;

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

		public CompositeChildElement(RuntimeResourceDefinition theResDef, EncodeContext theEncodeContext) {
			myResDef = theResDef;
			myDef = null;
			myParent = null;
			myEncodeContext = theEncodeContext;
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

		private boolean checkIfParentShouldBeEncodedAndBuildPath() {
			List<ElementsPath> encodeElements = myEncodeElements;

			String currentResourceName = myEncodeContext.getResourcePath().get(myEncodeContext.getResourcePath().size() - 1).getName();
			if (myEncodeElementsAppliesToResourceTypes != null && !myEncodeElementsAppliesToResourceTypes.contains(currentResourceName)) {
				encodeElements = null;
			}

			boolean retVal = checkIfPathMatchesForEncoding(encodeElements, true);

			/*
			 * We force the meta tag to be encoded even if it's not specified as an element in the
			 * elements filter, specifically because we'll need it in order to automatically add
			 * the SUBSETTED tag
			 */
			if (!retVal) {
				if ("meta".equals(myEncodeContext.getLeafResourcePathFirstField()) && shouldAddSubsettedTag(myEncodeContext)) {
					// The next element is a child of the <meta> element
					retVal = true;
				} else if ("meta".equals(myDef.getElementName()) && shouldAddSubsettedTag(myEncodeContext)) {
					// The next element is the <meta> element
					retVal = true;
				}
			}

			return retVal;
		}

		private boolean checkIfParentShouldNotBeEncodedAndBuildPath() {
			return checkIfPathMatchesForEncoding(myDontEncodeElements, false);
		}

		private boolean checkIfPathMatchesForEncoding(List<ElementsPath> theElements, boolean theCheckingForEncodeElements) {

			boolean retVal = false;
			myEncodeContext.pushPath(myDef.getElementName(), false);

			if (theCheckingForEncodeElements && isEncodeElementsAppliesToChildResourcesOnly() && myEncodeContext.getResourcePath().size() < 2) {
				retVal = true;
			} else if (theElements == null) {
				retVal = true;
			} else {
				EncodeContextPath currentResourcePath = myEncodeContext.getCurrentResourcePath();
				ourLog.trace("Current resource path: {}", currentResourcePath);
				for (ElementsPath next : theElements) {

					if (next.startsWith(currentResourcePath)) {
						if (theCheckingForEncodeElements || next.getPath().size() == currentResourcePath.getPath().size()) {
							retVal = true;
							break;
						}
					}

					if (next.getPath().get(next.getPath().size() - 1).getName().equals("(mandatory)")) {
						if (myDef.getMin() > 0) {
							retVal = true;
							break;
						}
						if (currentResourcePath.getPath().size() > next.getPath().size()) {
							retVal = true;
							break;
						}
					}

				}
			}

			myEncodeContext.popPath();
			return retVal;
		}

		public BaseRuntimeChildDefinition getDef() {
			return myDef;
		}

		public CompositeChildElement getParent() {
			return myParent;
		}

		public boolean shouldBeEncoded() {
			boolean retVal = true;
			if (myEncodeElements != null) {
				retVal = checkIfParentShouldBeEncodedAndBuildPath();
			}
			if (retVal && myDontEncodeElements != null) {
				retVal = !checkIfParentShouldNotBeEncodedAndBuildPath();
			}

			return retVal;
		}
	}

	protected class EncodeContextPath {
		private final List<EncodeContextPathElement> myPath;

		public EncodeContextPath() {
			myPath = new ArrayList<>(10);
		}

		public EncodeContextPath(List<EncodeContextPathElement> thePath) {
			myPath = thePath;
		}

		@Override
		public String toString() {
			return myPath.toString();
		}

		protected List<EncodeContextPathElement> getPath() {
			return myPath;
		}

		public EncodeContextPath getCurrentResourcePath() {
			EncodeContextPath retVal = null;
			for (int i = myPath.size() - 1; i >= 0; i--) {
				if (myPath.get(i).isResource()) {
					retVal = new EncodeContextPath(myPath.subList(i, myPath.size()));
					break;
				}
			}
			Validate.isTrue(retVal != null);
			return retVal;
		}
	}

	protected class ElementsPath extends EncodeContextPath {

		protected ElementsPath(String thePath) {
			StringTokenizer tok = new StringTokenizer(thePath, ".");
			boolean first = true;
			while (tok.hasMoreTokens()) {
				String next = tok.nextToken();
				if (first && next.equals("*")) {
					getPath().add(new EncodeContextPathElement("*", true));
				} else if (isNotBlank(next)) {
					getPath().add(new EncodeContextPathElement(next, Character.isUpperCase(next.charAt(0))));
				}
				first = false;
			}
		}

		public boolean startsWith(EncodeContextPath theCurrentResourcePath) {
			for (int i = 0; i < getPath().size(); i++) {
				if (theCurrentResourcePath.getPath().size() == i) {
					return true;
				}
				EncodeContextPathElement expected = getPath().get(i);
				EncodeContextPathElement actual = theCurrentResourcePath.getPath().get(i);
				if (!expected.matches(actual)) {
					return false;
				}
			}
			return true;
		}

		public boolean equalsPath(String thePath) {
			ElementsPath parsedPath = new ElementsPath(thePath);
			return getPath().equals(parsedPath.getPath());
		}
	}

	/**
	 * EncodeContext is a shared state object that is passed around the
	 * encode process
	 */
	protected class EncodeContext extends EncodeContextPath {
		private final ArrayList<EncodeContextPathElement> myResourcePath = new ArrayList<>(10);

		protected ArrayList<EncodeContextPathElement> getResourcePath() {
			return myResourcePath;
		}

		public String getLeafResourcePathFirstField() {
			String retVal = null;
			for (int i = getPath().size() - 1; i >= 0; i--) {
				if (getPath().get(i).isResource()) {
					break;
				} else {
					retVal = getPath().get(i).getName();
				}
			}
			return retVal;
		}

		/**
		 * Add an element at the end of the path
		 */
		protected void pushPath(String thePathElement, boolean theResource) {
			assert isNotBlank(thePathElement);
			assert !thePathElement.contains(".");
			assert theResource ^ Character.isLowerCase(thePathElement.charAt(0));

			EncodeContextPathElement element = new EncodeContextPathElement(thePathElement, theResource);
			getPath().add(element);
			if (theResource) {
				myResourcePath.add(element);
			}
		}

		/**
		 * Remove the element at the end of the path
		 */
		public void popPath() {
			EncodeContextPathElement removed = getPath().remove(getPath().size() - 1);
			if (removed.isResource()) {
				myResourcePath.remove(myResourcePath.size() - 1);
			}
		}

	}

	protected class EncodeContextPathElement {
		private final String myName;
		private final boolean myResource;

		public EncodeContextPathElement(String theName, boolean theResource) {
			Validate.notBlank(theName);
			myName = theName;
			myResource = theResource;
		}

		public boolean matches(EncodeContextPathElement theOther) {
			if (myResource != theOther.isResource()) {
				return false;
			}
			String otherName = theOther.getName();
			if (myName.equals(otherName)) {
				return true;
			}
			/*
			 * This is here to handle situations where a path like
			 *    Observation.valueQuantity has been specified as an include/exclude path,
			 * since we only know that path as
			 *    Observation.value
			 * until we get to actually looking at the values there.
			 */
			if (myName.length() > otherName.length() && myName.startsWith(otherName)) {
				char ch = myName.charAt(otherName.length());
				if (Character.isUpperCase(ch)) {
					return true;
				}
			}
			return myName.equals("*");
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}

			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}

			EncodeContextPathElement that = (EncodeContextPathElement) theO;

			return new EqualsBuilder()
				.append(myResource, that.myResource)
				.append(myName, that.myName)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(myName)
				.append(myResource)
				.toHashCode();
		}

		@Override
		public String toString() {
			if (myResource) {
				return myName + "(res)";
			}
			return myName;
		}

		public String getName() {
			return myName;
		}

		public boolean isResource() {
			return myResource;
		}
	}

	static class ContainedResources {
		private long myNextContainedId = 1;

		private List<IBaseResource> myResourceList;
		private IdentityHashMap<IBaseResource, IIdType> myResourceToIdMap;
		private Map<String, IBaseResource> myExistingIdToContainedResourceMap;

		public Map<String, IBaseResource> getExistingIdToContainedResource() {
			if (myExistingIdToContainedResourceMap == null) {
				myExistingIdToContainedResourceMap = new HashMap<>();
			}
			return myExistingIdToContainedResourceMap;
		}

		public void addContained(IBaseResource theResource) {
			if (getResourceToIdMap().containsKey(theResource)) {
				return;
			}

			IIdType newId;
			if (theResource.getIdElement().isLocal()) {
				newId = theResource.getIdElement();
			} else {
				newId = null;
			}

			getResourceToIdMap().put(theResource, newId);
			getResourceList().add(theResource);
		}

		public void addContained(IIdType theId, IBaseResource theResource) {
			if (!getResourceToIdMap().containsKey(theResource)) {
				getResourceToIdMap().put(theResource, theId);
				getResourceList().add(theResource);
			}
		}

		public List<IBaseResource> getContainedResources() {
			if (getResourceToIdMap() == null) {
				return Collections.emptyList();
			}
			return getResourceList();
		}

		public IIdType getResourceId(IBaseResource theNext) {
			if (getResourceToIdMap() == null) {
				return null;
			}
			return getResourceToIdMap().get(theNext);
		}

		private List<IBaseResource> getResourceList() {
			if (myResourceList == null) {
				myResourceList = new ArrayList<>();
			}
			return myResourceList;
		}

		private IdentityHashMap<IBaseResource, IIdType> getResourceToIdMap() {
			if (myResourceToIdMap == null) {
				myResourceToIdMap = new IdentityHashMap<>();
			}
			return myResourceToIdMap;
		}

		public boolean isEmpty() {
			if (myResourceToIdMap == null) {
				return true;
			}
			return myResourceToIdMap.isEmpty();
		}

		public boolean hasExistingIdToContainedResource() {
			return myExistingIdToContainedResourceMap != null;
		}

		public void assignIdsToContainedResources() {

			if (getResourceList() != null) {

				/*
				 * The idea with the code block below:
				 *
				 * We want to preserve any IDs that were user-assigned, so that if it's really
				 * important to someone that their contained resource have the ID of #FOO
				 * or #1 we will keep that.
				 *
				 * For any contained resources where no ID was assigned by the user, we
				 * want to manually create an ID but make sure we don't reuse an existing ID.
				 */

				Set<String> ids = new HashSet<>();

				// Gather any user assigned IDs
				for (IBaseResource nextResource : getResourceList()) {
					if (getResourceToIdMap().get(nextResource) != null) {
						ids.add(getResourceToIdMap().get(nextResource).getValue());
					}
				}

				// Automatically assign IDs to the rest
				for (IBaseResource nextResource : getResourceList()) {

					while (getResourceToIdMap().get(nextResource) == null) {
						String nextCandidate = "#" + myNextContainedId;
						myNextContainedId++;
						if (!ids.add(nextCandidate)) {
							continue;
						}

						getResourceToIdMap().put(nextResource, new IdDt(nextCandidate));
					}

				}

			}

		}
	}

	protected static <T> List<T> extractMetadataListNotNull(IResource resource, ResourceMetadataKeyEnum<List<T>> key) {
		List<? extends T> securityLabels = key.get(resource);
		if (securityLabels == null) {
			securityLabels = Collections.emptyList();
		}
		return new ArrayList<>(securityLabels);
	}

	static boolean hasNoExtensions(IBase theElement) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theElement;
			if (res.getUndeclaredExtensions().size() > 0 || res.getUndeclaredModifierExtensions().size() > 0) {
				return false;
			}
		}
		if (theElement instanceof IBaseHasExtensions) {
			IBaseHasExtensions res = (IBaseHasExtensions) theElement;
			if (res.hasExtension()) {
				return false;
			}
		}
		if (theElement instanceof IBaseHasModifierExtensions) {
			IBaseHasModifierExtensions res = (IBaseHasModifierExtensions) theElement;
			return !res.hasModifierExtension();
		}
		return true;
	}

}
