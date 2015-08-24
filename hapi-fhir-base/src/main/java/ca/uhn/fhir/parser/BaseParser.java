package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class BaseParser implements IParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParser.class);
	private ContainedResources myContainedResources;
	private FhirContext myContext;
	private Set<String> myEncodeElements;
	private Set<String> myEncodeElementsAppliesToResourceTypes;
	private boolean myEncodeElementsIncludesStars;
	private IParserErrorHandler myErrorHandler;
	private boolean myOmitResourceId;
	private String myServerBaseUrl;
	private boolean myStripVersionsFromReferences = true;
	private boolean mySummaryMode;
	private boolean mySuppressNarratives;

	/**
	 * Constructor
	 * 
	 * @param theParserErrorHandler
	 */
	public BaseParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		myContext = theContext;
		myErrorHandler = theParserErrorHandler;
	}

	protected Iterable<CompositeChildElement> compositeChildIterator(final List<? extends BaseRuntimeChildDefinition> theChildren, final boolean theContainedResource, final CompositeChildElement theParent) {
		return new Iterable<BaseParser.CompositeChildElement>() {
			@Override
			public Iterator<CompositeChildElement> iterator() {

				return new Iterator<CompositeChildElement>() {
					private Iterator<? extends BaseRuntimeChildDefinition> myChildrenIter;
					private Boolean myHasNext = null;
					private CompositeChildElement myNext;

					/**
					 * Constructor
					 */
					{
						myChildrenIter = theChildren.iterator();
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

							myNext = new CompositeChildElement(theParent, myChildrenIter.next());

							/*
							 * There are lots of reasons we might skip encoding a particular child
							 */
							if (myNext.getDef().getElementName().equals("extension") || myNext.getDef().getElementName().equals("modifierExtension")) {
								myNext = null;
							} else if (myNext.getDef().getElementName().equals("id")) {
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
		Set<String> allIds = new HashSet<String>();
		Map<String, IBaseResource> existingIdToContainedResource = null;

		if (theTarget instanceof IResource) {
			List<? extends IResource> containedResources = ((IResource) theTarget).getContained().getContainedResources();
			for (IResource next : containedResources) {
				String nextId = next.getId().getValue();
				if (StringUtils.isNotBlank(nextId)) {
					if (!nextId.startsWith("#")) {
						nextId = '#' + nextId;
					}
					allIds.add(nextId);
					if (existingIdToContainedResource == null) {
						existingIdToContainedResource = new HashMap<String, IBaseResource>();
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
					allIds.add(nextId);
					if (existingIdToContainedResource == null) {
						existingIdToContainedResource = new HashMap<String, IBaseResource>();
					}
					existingIdToContainedResource.put(nextId, next);
				}
			}
		} else {
			// no resources to contain
		}

		{
			List<IBaseReference> allElements = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
			for (IBaseReference next : allElements) {
				IBaseResource resource = next.getResource();
				if (resource != null) {
					if (resource.getIdElement().isEmpty() || resource.getIdElement().isLocal()) {
						if (theContained.getResourceId(resource) != null) {
							// Prevent infinite recursion if there are circular loops in the contained resources
							continue;
						}
						theContained.addContained(resource);
					} else {
						continue;
					}

					containResourcesForEncoding(theContained, resource, theTarget);
				} else if (next.getReferenceElement().isLocal()) {
					if (existingIdToContainedResource != null) {
						IBaseResource potentialTarget = existingIdToContainedResource.remove(next.getReferenceElement().getValue());
						if (potentialTarget != null) {
							theContained.addContained(next.getReferenceElement(), potentialTarget);
							containResourcesForEncoding(theContained, potentialTarget, theTarget);
						}
					}
				}
			}
		}

	}

	protected void containResourcesForEncoding(IBaseResource theResource) {
		ContainedResources contained = new ContainedResources();
		containResourcesForEncoding(contained, theResource, theResource);
		myContainedResources = contained;
	}

	protected String determineReferenceText(IBaseReference theRef) {
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
							if (!refId.hasResourceType()) {
								refId = refId.withResourceType(myContext.getResourceDefinition(theRef.getResource()).getName());
							}
							if (isStripVersionsFromReferences()) {
								reference = refId.toVersionless().getValue();
							} else {
								reference = refId.getValue();
							}
						}
					}
				}
			}
			return reference;
		} else {
			if (!ref.hasResourceType() && !ref.isLocal() && theRef.getResource() != null) {
				ref = ref.withResourceType(myContext.getResourceDefinition(theRef.getResource()).getName());
			}
			if (isNotBlank(myServerBaseUrl) && StringUtils.equals(myServerBaseUrl, ref.getBaseUrl())) {
				if (isStripVersionsFromReferences()) {
					return ref.toUnqualifiedVersionless().getValue();
				} else {
					return ref.toUnqualified().getValue();
				}
			} else {
				if (isStripVersionsFromReferences()) {
					return ref.toVersionless().getValue();
				} else {
					return ref.getValue();
				}
			}
		}
	}

	protected String determineResourceBaseUrl(String bundleBaseUrl, BundleEntry theEntry) {
		IResource resource = theEntry.getResource();
		if (resource == null) {
			return null;
		}

		String resourceBaseUrl = null;
		if (resource.getId() != null && resource.getId().hasBaseUrl()) {
			if (!resource.getId().getBaseUrl().equals(bundleBaseUrl)) {
				resourceBaseUrl = resource.getId().getBaseUrl();
			}
		}
		return resourceBaseUrl;
	}

	protected abstract void doEncodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException, DataFormatException;

	protected abstract void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException, DataFormatException;

	protected abstract <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException;

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		if (theBundle == null) {
			throw new NullPointerException("Bundle can not be null");
		}
		StringWriter stringWriter = new StringWriter();
		try {
			encodeBundleToWriter(theBundle, stringWriter);
		} catch (IOException e) {
			throw new Error("Encountered IOException during write to string - This should not happen!");
		}

		return stringWriter.toString();
	}

	@Override
	public final void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException, DataFormatException {
		Validate.notNull(theBundle, "theBundle must not be null");
		Validate.notNull(theWriter, "theWriter must not be null");
		doEncodeBundleToWriter(theBundle, theWriter);
	}

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

		doEncodeResourceToWriter(theResource, theWriter);
	}

	@Override
	public String encodeTagListToString(TagList theTagList) {
		Writer stringWriter = new StringWriter();
		try {
			encodeTagListToWriter(theTagList, stringWriter);
		} catch (IOException e) {
			throw new Error("Encountered IOException during write to string - This should not happen!");
		}
		return stringWriter.toString();
	}

	protected String fixContainedResourceId(String theValue) {
		if (StringUtils.isNotBlank(theValue) && theValue.charAt(0) == '#') {
			return theValue.substring(1);
		}
		return theValue;
	}

	ContainedResources getContainedResources() {
		return myContainedResources;
	}

	/**
	 * See {@link #setEncodeElements(Set)}
	 */
	@Override
	public Set<String> getEncodeElements() {
		return myEncodeElements;
	}

	/**
	 * See {@link #setEncodeElementsAppliesToResourceTypes(Set)}
	 */
	@Override
	public Set<String> getEncodeElementsAppliesToResourceTypes() {
		return myEncodeElementsAppliesToResourceTypes;
	}

	protected IParserErrorHandler getErrorHandler() {
		return myErrorHandler;
	}

	protected TagList getMetaTagsForEncoding(IResource theIResource) {
		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(theIResource);
		if (shouldAddSubsettedTag()) {
			tags = new TagList(tags);
			tags.add(new Tag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE, "Resource encoded in summary mode"));
		}

		return tags;
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
		return (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES || childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) && getContainedResources().isEmpty() == false && theIncludedResource == false;
	}

	@Override
	public boolean isOmitResourceId() {
		return myOmitResourceId;
	}

	@Override
	public boolean isStripVersionsFromReferences() {
		return myStripVersionsFromReferences;
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
	public Bundle parseBundle(Reader theReader) {
		if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU2_HL7ORG) {
			throw new IllegalStateException("Can't parse DSTU1 (Atom) bundle in HL7.org DSTU2 mode. Use parseResource(Bundle.class, foo) instead.");
		}
		return parseBundle(null, theReader);
	}

	@Override
	public Bundle parseBundle(String theXml) throws ConfigurationException, DataFormatException {
		StringReader reader = new StringReader(theXml);
		return parseBundle(reader);
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException {
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
					List<IBase> fullUrl = fullUrlChild.getAccessor().getValues(nextEntry);
					if (fullUrl != null && !fullUrl.isEmpty()) {
						IPrimitiveType<?> value = (IPrimitiveType<?>) fullUrl.get(0);
						if (value.isEmpty() == false) {
							List<IBase> entryResources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry);
							if (entryResources != null && entryResources.size() > 0) {
								IBaseResource res = (IBaseResource) entryResources.get(0);
								res.setId(value.getValueAsString());
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

	@Override
	public TagList parseTagList(String theString) {
		return parseTagList(new StringReader(theString));
	}

	@SuppressWarnings("cast")
	protected List<? extends IBase> preProcessValues(BaseRuntimeChildDefinition metaChildUncast, List<? extends IBase> theValues) {
		if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU2_HL7ORG)) {
			if (shouldAddSubsettedTag() && metaChildUncast.getValidChildNames().contains("meta")) {
				BaseRuntimeElementDefinition<?> childByName = metaChildUncast.getChildByName("meta");
				if (childByName instanceof BaseRuntimeElementCompositeDefinition<?>) {
					BaseRuntimeElementCompositeDefinition<?> metaChildUncast1 = (BaseRuntimeElementCompositeDefinition<?>) childByName;
					if (metaChildUncast1 != null) {
						if (IBaseMetaType.class.isAssignableFrom(metaChildUncast1.getImplementingClass())) {
							IBaseMetaType metaValue;
							if (theValues != null && theValues.size() >= 1) {
								metaValue = (IBaseMetaType) theValues.iterator().next();
								try {
									metaValue = (IBaseMetaType) metaValue.getClass().getMethod("copy").invoke(metaValue);
								} catch (Exception e) {
									throw new InternalErrorException("Failed to duplicate meta", e);
								}
							} else {
								metaValue = (IBaseMetaType) metaChildUncast1.newInstance();
							}

							ArrayList<IBase> retVal = new ArrayList<IBase>();
							retVal.add(metaValue);

							BaseRuntimeChildDefinition tagChild = metaChildUncast1.getChildByName("tag");
							BaseRuntimeElementCompositeDefinition<?> codingDef = (BaseRuntimeElementCompositeDefinition<?>) ((BaseRuntimeElementCompositeDefinition<?>) tagChild.getChildByName("tag"));
							IBase coding = codingDef.newInstance();
							tagChild.getMutator().addValue(metaValue, coding);

							BaseRuntimeChildDefinition systemChild = codingDef.getChildByName("system");
							IPrimitiveType<?> system = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
							system.setValueAsString(Constants.TAG_SUBSETTED_SYSTEM);
							systemChild.getMutator().addValue(coding, system);

							BaseRuntimeChildDefinition codeChild = codingDef.getChildByName("code");
							IPrimitiveType<?> code = (IPrimitiveType<?>) myContext.getElementDefinition("code").newInstance();
							code.setValueAsString(Constants.TAG_SUBSETTED_CODE);
							codeChild.getMutator().addValue(coding, code);

							BaseRuntimeChildDefinition displayChild = codingDef.getChildByName("display");
							IPrimitiveType<?> display = (IPrimitiveType<?>) myContext.getElementDefinition("string").newInstance();
							display.setValueAsString("Resource encoded in summary mode");
							displayChild.getMutator().addValue(coding, display);

							return retVal;
						}
					}
				}
			}
		}

		return theValues;
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

	@Override
	public void setEncodeElementsAppliesToResourceTypes(Set<String> theEncodeElementsAppliesToResourceTypes) {
		if (theEncodeElementsAppliesToResourceTypes == null || theEncodeElementsAppliesToResourceTypes.isEmpty()) {
			myEncodeElementsAppliesToResourceTypes = null;
		} else {
			myEncodeElementsAppliesToResourceTypes = theEncodeElementsAppliesToResourceTypes;
		}
	}

	@Override
	public IParser setOmitResourceId(boolean theOmitResourceId) {
		myOmitResourceId = theOmitResourceId;
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
	public IParser setStripVersionsFromReferences(boolean theStripVersionsFromReferences) {
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
		return isSummaryMode() || isSuppressNarratives();
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
		List<T> securityLabels = key.get(resource);
		if (securityLabels == null) {
			securityLabels = Collections.emptyList();
		}
		return securityLabels;
	}

	protected class CompositeChildElement {
		private final BaseRuntimeChildDefinition myDef;
		private final CompositeChildElement myParent;
		private final RuntimeResourceDefinition myResDef;

		public CompositeChildElement(CompositeChildElement theParent, BaseRuntimeChildDefinition theDef) {
			myDef = theDef;
			myParent = theParent;
			myResDef = null;

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


		public CompositeChildElement(RuntimeResourceDefinition theResDef) {
			myResDef = theResDef;
			myDef = null;
			myParent = null;
		}

		private StringBuilder buildPath() {
			if (myResDef != null) {
				StringBuilder b = new StringBuilder();
				b.append(myResDef.getName());
				return b;
			} else {
				StringBuilder b = myParent.buildPath();
				if (b != null && myDef != null) {
					b.append('.');
					b.append(myDef.getElementName());
				}
				return b;
			}
		}

		private boolean checkIfParentShouldBeEncodedAndBuildPath(StringBuilder theB, boolean theStarPass) {
			if (myResDef != null) {
				if (myEncodeElementsAppliesToResourceTypes != null) {
					if (!myEncodeElementsAppliesToResourceTypes.contains(myResDef.getName())) {
						return true;
					}
				}
				if (theStarPass) {
					theB.append('*');
				} else {
					theB.append(myResDef.getName());
				}
				if (myEncodeElements.contains(theB.toString())) {
					return true;
				} else {
					return false;
				}
			} else if (myParent != null) {
				if (myParent.checkIfParentShouldBeEncodedAndBuildPath(theB, theStarPass)) {
					return true;
				}

				if (myDef != null) {
					theB.append('.');
					theB.append(myDef.getElementName());
					return myEncodeElements.contains(theB.toString());
				}
			}

			return true;
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

		public boolean shouldBeEncoded() {
			if (myEncodeElements == null) {
				return true;
			}
			boolean retVal = checkIfParentShouldBeEncodedAndBuildPath(new StringBuilder(), false);
			if (retVal == false && myEncodeElementsIncludesStars) {
				retVal = checkIfParentShouldBeEncodedAndBuildPath(new StringBuilder(), true);
			}
			return retVal;
		}
	}

	static class ContainedResources {
		private long myNextContainedId = 1;

		private List<IBaseResource> myResources = new ArrayList<IBaseResource>();
		private IdentityHashMap<IBaseResource, IIdType> myResourceToId = new IdentityHashMap<IBaseResource, IIdType>();

		public void addContained(IBaseResource theResource) {
			if (myResourceToId.containsKey(theResource)) {
				return;
			}

			IIdType newId;
			if (theResource.getIdElement().isLocal()) {
				newId = theResource.getIdElement();
			} else {
				// TODO: make this configurable between the two below (and something else?)
				// newId = new IdDt(UUID.randomUUID().toString());
				newId = new IdDt(myNextContainedId++);
			}

			myResourceToId.put(theResource, newId);
			myResources.add(theResource);
		}

		public void addContained(IIdType theId, IBaseResource theResource) {
			myResourceToId.put(theResource, theId);
			myResources.add(theResource);
		}

		public List<IBaseResource> getContainedResources() {
			return myResources;
		}

		public IIdType getResourceId(IBaseResource theNext) {
			return myResourceToId.get(theNext);
		}

		public boolean isEmpty() {
			return myResourceToId.isEmpty();
		}

	}

}
