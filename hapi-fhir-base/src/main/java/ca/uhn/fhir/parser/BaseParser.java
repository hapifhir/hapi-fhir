/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.CollectionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import jakarta.annotation.Nullable;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.startsWith;

@SuppressWarnings("WeakerAccess")
public abstract class BaseParser implements IParser {

	/**
	 * Any resources that were created by the parser (i.e. by parsing a serialized resource) will have
	 * a {@link IBaseResource#getUserData(String) user data} property with this key.
	 *
	 * @since 5.0.0
	 */
	public static final String RESOURCE_CREATED_BY_PARSER =
			BaseParser.class.getName() + "_" + "RESOURCE_CREATED_BY_PARSER";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParser.class);

	private static final Set<String> notEncodeForContainedResource =
			new HashSet<>(Arrays.asList("security", "versionId", "lastUpdated"));

	private FhirTerser.ContainedResources myContainedResources;
	private boolean myEncodeElementsAppliesToChildResourcesOnly;
	private final FhirContext myContext;
	private Collection<String> myDontEncodeElements;
	private Collection<String> myEncodeElements;
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
	 */
	public BaseParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		myContext = theContext;
		myErrorHandler = theParserErrorHandler;
	}

	protected FhirContext getContext() {
		return myContext;
	}

	@Override
	public IParser setDontEncodeElements(Collection<String> theDontEncodeElements) {
		myDontEncodeElements = theDontEncodeElements;
		return this;
	}

	@Override
	public IParser setEncodeElements(Set<String> theEncodeElements) {
		myEncodeElements = theEncodeElements;
		return this;
	}

	protected Iterable<CompositeChildElement> compositeChildIterator(
			IBase theCompositeElement,
			final boolean theContainedResource,
			final CompositeChildElement theParent,
			EncodeContext theEncodeContext) {
		BaseRuntimeElementCompositeDefinition<?> elementDef = (BaseRuntimeElementCompositeDefinition<?>)
				myContext.getElementDefinition(theCompositeElement.getClass());
		return theEncodeContext
				.getCompositeChildrenCache()
				.computeIfAbsent(new Key(elementDef, theContainedResource, theParent, theEncodeContext), (k) -> {
					final List<BaseRuntimeChildDefinition> children = elementDef.getChildrenAndExtension();
					final List<CompositeChildElement> result = new ArrayList<>(children.size());

					for (final BaseRuntimeChildDefinition child : children) {
						CompositeChildElement myNext = new CompositeChildElement(theParent, child, theEncodeContext);

						/*
						 * There are lots of reasons we might skip encoding a particular child
						 */
						if (myNext.getDef().getElementName().equals("id")) {
							continue;
						} else if (!myNext.shouldBeEncoded(theContainedResource)) {
							continue;
						} else if (myNext.getDef() instanceof RuntimeChildNarrativeDefinition) {
							if (isSuppressNarratives() || isSummaryMode()) {
								continue;
							}
						} else if (myNext.getDef() instanceof RuntimeChildContainedResources) {
							if (theContainedResource) {
								continue;
							}
						}
						result.add(myNext);
					}
					return result;
				});
	}

	private String determineReferenceText(
			IBaseReference theRef, CompositeChildElement theCompositeChildElement, IBaseResource theResource) {
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
									refId = refId.withResourceType(myContext
											.getResourceDefinition(theRef.getResource())
											.getName());
								}
								if (isStripVersionsFromReferences(theCompositeChildElement, theResource)) {
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
			ref = ref.withResourceType(
					myContext.getResourceDefinition(theRef.getResource()).getName());
		}
		if (isNotBlank(myServerBaseUrl) && StringUtils.equals(myServerBaseUrl, ref.getBaseUrl())) {
			if (isStripVersionsFromReferences(theCompositeChildElement, theResource)) {
				return ref.toUnqualifiedVersionless().getValue();
			}
			return ref.toUnqualified().getValue();
		}
		if (isStripVersionsFromReferences(theCompositeChildElement, theResource)) {
			return ref.toVersionless().getValue();
		}
		return ref.getValue();
	}

	protected abstract void doEncodeResourceToWriter(
			IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext)
			throws IOException, DataFormatException;

	protected void doEncodeToWriter(IBase theElement, Writer theWriter, EncodeContext theEncodeContext)
			throws IOException, DataFormatException {
		throw new InternalErrorException(Msg.code(2363) + "This parser does not support encoding non-resource values");
	}

	protected abstract <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader)
			throws DataFormatException;

	@Override
	public String encodeResourceToString(IBaseResource theResource) throws DataFormatException {
		Writer stringWriter = new StringBuilderWriter();
		try {
			encodeResourceToWriter(theResource, stringWriter);
		} catch (IOException e) {
			throw new Error(
					Msg.code(1828) + "Encountered IOException during write to string - This should not happen!", e);
		}
		return stringWriter.toString();
	}

	@Override
	public final void encodeResourceToWriter(IBaseResource theResource, Writer theWriter)
			throws IOException, DataFormatException {
		EncodeContext encodeContext = new EncodeContext(this, myContext.getParserOptions());
		encodeResourceToWriter(theResource, theWriter, encodeContext);
	}

	@Override
	public String encodeToString(IBase theElement) throws DataFormatException {
		Writer stringWriter = new StringBuilderWriter();
		try {
			encodeToWriter(theElement, stringWriter);
		} catch (IOException e) {
			throw new Error(
					Msg.code(2364) + "Encountered IOException during write to string - This should not happen!", e);
		}
		return stringWriter.toString();
	}

	@Override
	public void encodeToWriter(IBase theElement, Writer theWriter) throws DataFormatException, IOException {
		if (theElement instanceof IBaseResource) {
			encodeResourceToWriter((IBaseResource) theElement, theWriter);
		} else if (theElement instanceof IPrimitiveType) {
			theWriter.write(((IPrimitiveType<?>) theElement).getValueAsString());
		} else {
			EncodeContext encodeContext = new EncodeContext(this, myContext.getParserOptions());
			encodeToWriter(theElement, theWriter, encodeContext);
		}
	}

	protected void encodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext)
			throws IOException {
		Validate.notNull(theResource, "theResource can not be null");
		Validate.notNull(theWriter, "theWriter can not be null");
		Validate.notNull(theEncodeContext, "theEncodeContext can not be null");

		if (theResource.getStructureFhirVersionEnum() != myContext.getVersion().getVersion()) {
			throw new IllegalArgumentException(Msg.code(1829) + "This parser is for FHIR version "
					+ myContext.getVersion().getVersion() + " - Can not encode a structure for version "
					+ theResource.getStructureFhirVersionEnum());
		}

		String resourceName =
				myContext.getElementDefinition(theResource.getClass()).getName();
		theEncodeContext.pushPath(resourceName, true);

		doEncodeResourceToWriter(theResource, theWriter, theEncodeContext);

		theEncodeContext.popPath();
	}

	protected void encodeToWriter(IBase theElement, Writer theWriter, EncodeContext theEncodeContext)
			throws IOException {
		Validate.notNull(theElement, "theElement can not be null");
		Validate.notNull(theWriter, "theWriter can not be null");
		Validate.notNull(theEncodeContext, "theEncodeContext can not be null");

		BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theElement.getClass());
		String elementName = def.getName();
		theEncodeContext.pushPath(elementName, true);

		doEncodeToWriter(theElement, theWriter, theEncodeContext);

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
				Class<? extends IBase> type2 =
						myContext.getElementDefinition("code").getImplementingClass();
				childDef = theChild.getChildElementDefinitionByDatatype(type2);
				childName = theChild.getChildNameByDatatype(type2);
			}

			// See possibly the user has extended a built-in type without
			// declaring it anywhere, as in XmlParserDstu3Test#testEncodeUndeclaredBlock
			if (childDef == null) {
				Class<?> nextSuperType = theValue.getClass();
				while (IBase.class.isAssignableFrom(nextSuperType) && childDef == null) {
					if (Modifier.isAbstract(nextSuperType.getModifiers()) == false) {
						BaseRuntimeElementDefinition<?> def =
								myContext.getElementDefinition((Class<? extends IBase>) nextSuperType);
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

	FhirTerser.ContainedResources getContainedResources() {
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
		for (Map.Entry<ResourceMetadataKeyEnum<?>, Object> entry :
				resource.getResourceMetadata().entrySet()) {
			if (entry.getKey() instanceof ResourceMetadataKeyEnum.ExtensionResourceMetadataKey) {
				extensionMetadataKeys.add(entry);
			}
		}

		return extensionMetadataKeys;
	}

	protected String getExtensionUrl(final String extensionUrl) {
		String url = extensionUrl;
		if (StringUtils.isNotBlank(extensionUrl) && StringUtils.isNotBlank(myServerBaseUrl)) {
			url = !UrlUtil.isValid(extensionUrl) && extensionUrl.startsWith("/")
					? myServerBaseUrl + extensionUrl
					: extensionUrl;
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
	protected <T extends IPrimitiveType<String>> List<T> getProfileTagsForEncoding(
			IBaseResource theResource, List<T> theProfiles) {
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
				&& getContainedResources().isEmpty() == false
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

	private boolean isStripVersionsFromReferences(
			CompositeChildElement theCompositeChildElement, IBaseResource theResource) {

		if (theResource != null) {
			Set<String> autoVersionReferencesAtPathExtensions = MetaUtil.getAutoVersionReferencesAtPath(
					theResource.getMeta(), myContext.getResourceType(theResource));

			if (!autoVersionReferencesAtPathExtensions.isEmpty()
					&& theCompositeChildElement.anyPathMatches(autoVersionReferencesAtPathExtensions)) {
				return false;
			}
		}

		Boolean stripVersionsFromReferences = myStripVersionsFromReferences;
		if (stripVersionsFromReferences != null) {
			return stripVersionsFromReferences;
		}

		if (!myContext.getParserOptions().isStripVersionsFromReferences()) {
			return false;
		}

		Set<String> dontStripVersionsFromReferencesAtPaths = getDontStripVersionsFromReferencesAtPaths();
		if (dontStripVersionsFromReferencesAtPaths != null
				&& !dontStripVersionsFromReferencesAtPaths.isEmpty()
				&& theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths)) {
			return false;
		}

		dontStripVersionsFromReferencesAtPaths =
				myContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths();
		return dontStripVersionsFromReferencesAtPaths.isEmpty()
				|| !theCompositeChildElement.anyPathMatches(dontStripVersionsFromReferencesAtPaths);
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
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, InputStream theInputStream)
			throws DataFormatException {
		return parseResource(theResourceType, new InputStreamReader(theInputStream, Constants.CHARSET_UTF8));
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, Reader theReader)
			throws DataFormatException {

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

			if (isOverrideResourceIdWithBundleEntryFullUrl()) {
				BundleUtil.processEntries(myContext, (IBaseBundle) retVal, t -> {
					String fullUrl = t.getFullUrl();
					if (fullUrl != null) {
						IBaseResource resource = t.getResource();
						if (resource != null) {
							IIdType resourceId = resource.getIdElement();
							if (isBlank(resourceId.getValue())) {
								resourceId.setValue(fullUrl);
							} else {
								if (fullUrl.startsWith("urn:")
										&& fullUrl.length()
												> resourceId.getIdPart().length()
										&& fullUrl.charAt(fullUrl.length()
														- resourceId.getIdPart().length()
														- 1)
												== ':'
										&& fullUrl.endsWith(resourceId.getIdPart())) {
									resourceId.setValue(fullUrl);
								} else {
									IIdType fullUrlId = myContext.getVersion().newIdType();
									fullUrlId.setValue(fullUrl);
									if (myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
										IIdType newId = fullUrlId;
										if (!newId.hasVersionIdPart() && resourceId.hasVersionIdPart()) {
											newId = newId.withVersion(resourceId.getVersionIdPart());
										}
										resourceId.setValue(newId.getValue());
									} else if (StringUtils.equals(fullUrlId.getIdPart(), resourceId.getIdPart())) {
										if (fullUrlId.hasBaseUrl()) {
											IIdType newResourceId = resourceId.withServerBase(
													fullUrlId.getBaseUrl(), resourceId.getResourceType());
											resourceId.setValue(newResourceId.getValue());
										}
									}
								}
							}
						}
					}
				});
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

	protected List<? extends IBase> preProcessValues(
			BaseRuntimeChildDefinition theMetaChildUncast,
			IBaseResource theResource,
			List<? extends IBase> theValues,
			CompositeChildElement theCompositeChildElement,
			EncodeContext theEncodeContext) {
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
					metaValue = (IBaseMetaType)
							metaValue.getClass().getMethod("copy").invoke(metaValue);
				} catch (Exception e) {
					throw new InternalErrorException(Msg.code(1830) + "Failed to duplicate meta", e);
				}

				if (isBlank(metaValue.getVersionId())) {
					if (theResource.getIdElement().hasVersionIdPart()) {
						metaValue.setVersionId(theResource.getIdElement().getVersionIdPart());
					}
				}

				filterCodingsWithNoCodeOrSystem(metaValue.getTag());
				filterCodingsWithNoCodeOrSystem(metaValue.getSecurity());

				List<? extends IPrimitiveType<String>> newProfileList =
						getProfileTagsForEncoding(theResource, metaValue.getProfile());
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
				String refText = determineReferenceText(nextRef, theCompositeChildElement, theResource);
				if (!StringUtils.equals(refText, nextRef.getReferenceElement().getValue())) {

					if (retVal == theValues) {
						retVal = new ArrayList<>(theValues);
					}
					IBaseReference newRef = (IBaseReference)
							myContext.getElementDefinition(nextRef.getClass()).newInstance();
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
		if (theEncodeContext.myEncodeElementPaths != null) {
			if (isEncodeElementsAppliesToChildResourcesOnly()
					&& theEncodeContext.getResourcePath().size() < 2) {
				return false;
			}

			String currentResourceName = theEncodeContext
					.getResourcePath()
					.get(theEncodeContext.getResourcePath().size() - 1)
					.getName();
			return theEncodeContext.myEncodeElementsAppliesToResourceTypes == null
					|| theEncodeContext.myEncodeElementsAppliesToResourceTypes.contains(currentResourceName);
		}

		return false;
	}

	protected boolean shouldEncodeResourceId(IBaseResource theResource, EncodeContext theEncodeContext) {
		boolean retVal = true;
		if (isOmitResourceId() && theEncodeContext.getPath().size() == 1) {
			retVal = false;
		} else {
			if (theEncodeContext.myDontEncodeElementPaths != null) {
				String resourceName = myContext.getResourceType(theResource);
				if (theEncodeContext.myDontEncodeElementPaths.stream()
						.anyMatch(t -> t.equalsPath(resourceName + ".id"))) {
					retVal = false;
				} else if (theEncodeContext.myDontEncodeElementPaths.stream().anyMatch(t -> t.equalsPath("*.id"))) {
					retVal = false;
				} else if (theEncodeContext.getResourcePath().size() == 1
						&& theEncodeContext.myDontEncodeElementPaths.stream().anyMatch(t -> t.equalsPath("id"))) {
					retVal = false;
				}
			}
		}
		return retVal;
	}

	/**
	 * Used for DSTU2 only
	 */
	protected boolean shouldEncodeResourceMeta(IResource theResource, EncodeContext theEncodeContext) {
		return shouldEncodePath(theResource, "meta", theEncodeContext);
	}

	/**
	 * Used for DSTU2 only
	 */
	protected boolean shouldEncodePath(IResource theResource, String thePath, EncodeContext theEncodeContext) {
		if (theEncodeContext.myDontEncodeElementPaths != null) {
			String resourceName = myContext.getResourceType(theResource);
			if (theEncodeContext.myDontEncodeElementPaths.stream()
					.anyMatch(t -> t.equalsPath(resourceName + "." + thePath))) {
				return false;
			} else {
				return theEncodeContext.myDontEncodeElementPaths.stream().noneMatch(t -> t.equalsPath("*." + thePath));
			}
		}
		return true;
	}

	private String subsetDescription() {
		return "Resource encoded in summary mode";
	}

	protected void throwExceptionForUnknownChildType(
			BaseRuntimeChildDefinition nextChild, Class<? extends IBase> theType) {
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
			throw new DataFormatException(Msg.code(1831) + b);
		}
		throw new DataFormatException(Msg.code(1832) + nextChild + " has no child of type " + theType);
	}

	protected boolean shouldEncodeResource(String theName, EncodeContext theEncodeContext) {
		if (theEncodeContext.myDontEncodeElementPaths != null) {
			for (EncodeContextPath next : theEncodeContext.myDontEncodeElementPaths) {
				if (next.equalsPath(theName)) {
					return false;
				}
			}
		}
		return true;
	}

	protected void containResourcesInReferences(IBaseResource theResource) {

		/*
		 * If a UUID is present in Bundle.entry.fullUrl but no value is present
		 * in Bundle.entry.resource.id, the resource has a discrete identity which
		 * should be copied into the resource ID. It will not be serialized in
		 * Resource.id because it's a placeholder/UUID value, but its presence there
		 * informs the serializer that we don't need to contain this resource.
		 */
		if (theResource instanceof IBaseBundle) {
			List<Pair<String, IBaseResource>> entries =
					BundleUtil.getBundleEntryFullUrlsAndResources(getContext(), (IBaseBundle) theResource);
			for (Pair<String, IBaseResource> nextEntry : entries) {
				String fullUrl = nextEntry.getKey();
				IBaseResource resource = nextEntry.getValue();
				if (startsWith(fullUrl, "urn:")) {
					if (resource != null && resource.getIdElement().getValue() == null) {
						resource.getIdElement().setValue(fullUrl);
					}
				}
			}
		}

		myContainedResources = getContext().newTerser().containResources(theResource);
	}

	static class ChildNameAndDef {

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

	/**
	 * EncodeContext is a shared state object that is passed around the
	 * encode process
	 */
	class EncodeContext extends EncodeContextPath {
		private final Map<Key, List<BaseParser.CompositeChildElement>> myCompositeChildrenCache = new HashMap<>();
		private final List<EncodeContextPath> myEncodeElementPaths;
		private final Set<String> myEncodeElementsAppliesToResourceTypes;
		private final List<EncodeContextPath> myDontEncodeElementPaths;

		public EncodeContext(BaseParser theParser, ParserOptions theParserOptions) {
			Collection<String> encodeElements = theParser.myEncodeElements;
			Collection<String> dontEncodeElements = theParser.myDontEncodeElements;
			if (isSummaryMode()) {
				encodeElements = CollectionUtil.nullSafeUnion(
						encodeElements, theParserOptions.getEncodeElementsForSummaryMode());
				dontEncodeElements = CollectionUtil.nullSafeUnion(
						dontEncodeElements, theParserOptions.getDontEncodeElementsForSummaryMode());
			}

			if (encodeElements == null || encodeElements.isEmpty()) {
				myEncodeElementPaths = null;
			} else {
				myEncodeElementPaths =
						encodeElements.stream().map(EncodeContextPath::new).collect(Collectors.toList());
			}
			if (dontEncodeElements == null || dontEncodeElements.isEmpty()) {
				myDontEncodeElementPaths = null;
			} else {
				myDontEncodeElementPaths =
						dontEncodeElements.stream().map(EncodeContextPath::new).collect(Collectors.toList());
			}

			myEncodeElementsAppliesToResourceTypes =
					ParserUtil.determineApplicableResourceTypesForTerserPaths(myEncodeElementPaths);
		}

		private Map<Key, List<BaseParser.CompositeChildElement>> getCompositeChildrenCache() {
			return myCompositeChildrenCache;
		}
	}

	protected class CompositeChildElement {
		private final BaseRuntimeChildDefinition myDef;
		private final CompositeChildElement myParent;
		private final RuntimeResourceDefinition myResDef;
		private final EncodeContext myEncodeContext;

		public CompositeChildElement(
				CompositeChildElement theParent,
				@Nullable BaseRuntimeChildDefinition theDef,
				EncodeContext theEncodeContext) {
			myDef = theDef;
			myParent = theParent;
			myResDef = null;
			myEncodeContext = theEncodeContext;

			if (ourLog.isTraceEnabled()) {
				if (theParent != null) {
					StringBuilder path = theParent.buildPath();
					if (path != null) {
						path.append('.');
						if (myDef != null) {
							path.append(myDef.getElementName());
						}
						ourLog.trace(" * Next path: {}", path);
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

		@Override
		public String toString() {
			return myDef.getElementName();
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
			List<EncodeContextPath> encodeElements = myEncodeContext.myEncodeElementPaths;

			String currentResourceName = myEncodeContext
					.getResourcePath()
					.get(myEncodeContext.getResourcePath().size() - 1)
					.getName();
			if (myEncodeContext.myEncodeElementsAppliesToResourceTypes != null
					&& !myEncodeContext.myEncodeElementsAppliesToResourceTypes.contains(currentResourceName)) {
				encodeElements = null;
			}

			boolean retVal = checkIfPathMatchesForEncoding(encodeElements, true);

			/*
			 * We force the meta tag to be encoded even if it's not specified as an element in the
			 * elements filter, specifically because we'll need it in order to automatically add
			 * the SUBSETTED tag
			 */
			if (!retVal) {
				if ("meta".equals(myEncodeContext.getLeafResourcePathFirstField())
						&& shouldAddSubsettedTag(myEncodeContext)) {
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
			return checkIfPathMatchesForEncoding(myEncodeContext.myDontEncodeElementPaths, false);
		}

		private boolean checkIfPathMatchesForEncoding(
				List<EncodeContextPath> theElements, boolean theCheckingForEncodeElements) {

			boolean retVal = false;
			if (myDef != null) {
				myEncodeContext.pushPath(myDef.getElementName(), false);
			}

			if (theCheckingForEncodeElements
					&& isEncodeElementsAppliesToChildResourcesOnly()
					&& myEncodeContext.getResourcePath().size() < 2) {
				retVal = true;
			} else if (theElements == null) {
				retVal = true;
			} else {
				EncodeContextPath currentResourcePath = myEncodeContext.getCurrentResourcePath();
				ourLog.trace("Current resource path: {}", currentResourcePath);
				for (EncodeContextPath next : theElements) {

					if (next.startsWith(currentResourcePath, true)) {
						if (theCheckingForEncodeElements
								|| next.getPath().size()
										== currentResourcePath.getPath().size()) {
							retVal = true;
							break;
						}
					}

					if (next.getPath().get(next.getPath().size() - 1).getName().equals("(mandatory)")) {
						if (myDef.getMin() > 0) {
							retVal = true;
							break;
						}
						if (currentResourcePath.getPath().size()
								> next.getPath().size()) {
							retVal = true;
							break;
						}
					}
				}
			}

			if (myDef != null) {
				myEncodeContext.popPath();
			}

			return retVal;
		}

		public BaseRuntimeChildDefinition getDef() {
			return myDef;
		}

		public CompositeChildElement getParent() {
			return myParent;
		}

		public boolean shouldBeEncoded(boolean theContainedResource) {
			boolean retVal = true;
			if (isSummaryMode() && (getDef() == null || !getDef().isSummary())) {
				String resourceName = myEncodeContext.getLeafResourceName();
				// Technically the spec says we shouldn't include extensions in CapabilityStatement
				// but we will do so because there are people who depend on this behaviour, at least
				// as of 2019-07. See
				// https://github.com/smart-on-fhir/Swift-FHIR/issues/26
				// for example.
				if (("Conformance".equals(resourceName) || "CapabilityStatement".equals(resourceName))
						&& ("extension".equals(myDef.getElementName())
								|| "extension".equals(myEncodeContext.getLeafElementName()))) {
					// skip
				} else {
					retVal = false;
				}
			}
			if (myEncodeContext.myEncodeElementPaths != null) {
				retVal = checkIfParentShouldBeEncodedAndBuildPath();
			}
			if (retVal && myEncodeContext.myDontEncodeElementPaths != null) {
				retVal = !checkIfParentShouldNotBeEncodedAndBuildPath();
			}
			if (theContainedResource) {
				retVal = !notEncodeForContainedResource.contains(myDef.getElementName());
			}

			return retVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((myDef == null) ? 0 : myDef.hashCode());
			result = prime * result + ((myParent == null) ? 0 : myParent.hashCode());
			result = prime * result + ((myResDef == null) ? 0 : myResDef.hashCode());
			result = prime * result + ((myEncodeContext == null) ? 0 : myEncodeContext.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;

			if (obj instanceof CompositeChildElement) {
				final CompositeChildElement that = (CompositeChildElement) obj;
				return Objects.equals(this.getEnclosingInstance(), that.getEnclosingInstance())
						&& Objects.equals(this.myDef, that.myDef)
						&& Objects.equals(this.myParent, that.myParent)
						&& Objects.equals(this.myResDef, that.myResDef)
						&& Objects.equals(this.myEncodeContext, that.myEncodeContext);
			}
			return false;
		}

		private BaseParser getEnclosingInstance() {
			return BaseParser.this;
		}
	}

	private static class Key {
		private final BaseRuntimeElementCompositeDefinition<?> resDef;
		private final boolean theContainedResource;
		private final BaseParser.CompositeChildElement theParent;
		private final BaseParser.EncodeContext theEncodeContext;

		public Key(
				BaseRuntimeElementCompositeDefinition<?> resDef,
				final boolean theContainedResource,
				final BaseParser.CompositeChildElement theParent,
				BaseParser.EncodeContext theEncodeContext) {
			this.resDef = resDef;
			this.theContainedResource = theContainedResource;
			this.theParent = theParent;
			this.theEncodeContext = theEncodeContext;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((resDef == null) ? 0 : resDef.hashCode());
			result = prime * result + (theContainedResource ? 1231 : 1237);
			result = prime * result + ((theParent == null) ? 0 : theParent.hashCode());
			result = prime * result + ((theEncodeContext == null) ? 0 : theEncodeContext.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Key) {
				final Key that = (Key) obj;
				return Objects.equals(this.resDef, that.resDef)
						&& this.theContainedResource == that.theContainedResource
						&& Objects.equals(this.theParent, that.theParent)
						&& Objects.equals(this.theEncodeContext, that.theEncodeContext);
			}
			return false;
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
			if (res.getUndeclaredExtensions().size() > 0
					|| res.getUndeclaredModifierExtensions().size() > 0) {
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
