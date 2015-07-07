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

import static org.apache.commons.lang3.StringUtils.*;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ObjectUtil;

public abstract class BaseParser implements IParser {

	private ContainedResources myContainedResources;
	private FhirContext myContext;
	private IParserErrorHandler myErrorHandler;
	private boolean myOmitResourceId;
	private String myServerBaseUrl;
	private boolean myStripVersionsFromReferences = true;
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
				} else if (theRef.getResource().getIdElement() != null && theRef.getResource().getIdElement().hasIdPart()) {
					if (isStripVersionsFromReferences()) {
						reference = theRef.getResource().getIdElement().toVersionless().getValue();
					} else {
						reference = theRef.getResource().getIdElement().getValue();
					}
				}
			}
			return reference;
		} else {
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

		if (theResource instanceof IBaseBundle) {
			fixBaseLinksForBundle((IBaseBundle) theResource);
		}

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


	/**
	 * If individual resources in the bundle have an ID that has the base set, we make sure that Bundle.entry.base gets set as needed.
	 */
	private void fixBaseLinksForBundle(IBaseBundle theBundle) {
		/*
		 * ATTENTION IF YOU ARE EDITING THIS:
		 * There are two versions of this method, one for DSTU1/atom bundle and
		 * one for DSTU2/resource bundle. If you edit one, edit both and also
		 * update unit tests for both.
		 */
		FhirTerser t = myContext.newTerser();
		IPrimitiveType<?> element = t.getSingleValueOrNull(theBundle, "base", IPrimitiveType.class);
		String bundleBase = element != null ? element.getValueAsString() : null;

		for (IBase nextEntry : t.getValues(theBundle, "Bundle.entry", IBase.class)) {
			IBaseResource resource = t.getSingleValueOrNull(nextEntry, "resource", IBaseResource.class);
			if (resource == null) {
				continue;
			}

			IPrimitiveType<?> baseElement = t.getSingleValueOrNull(nextEntry, "base", IPrimitiveType.class);
			String entryBase = baseElement != null ? baseElement.getValueAsString() : null;
			if (isNotBlank(entryBase)) {
				continue;
			}

			IIdType resourceId = resource.getIdElement();
			String resourceIdBase = resourceId.getBaseUrl();
			if (isNotBlank(resourceIdBase)) {
				if (!ObjectUtil.equals(bundleBase, resourceIdBase)) {
					if (baseElement == null) {
						baseElement = (IPrimitiveType<?>) myContext.getElementDefinition("uri").newInstance();
						BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextEntry.getClass());
						entryDef.getChildByNameOrThrowDataFormatException("base").getMutator().setValue(nextEntry, baseElement);
					}

					baseElement.setValueAsString(resourceIdBase);
				}
			}
		}
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

	protected IParserErrorHandler getErrorHandler() {
		return myErrorHandler;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded values.
	 */
	public boolean getSuppressNarratives() {
		return mySuppressNarratives;
	}

	protected boolean isChildContained(BaseRuntimeElementDefinition<?> childDef, boolean theIncludedResource) {
		return (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES || childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) && getContainedResources().isEmpty() == false
				&& theIncludedResource == false;
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
			List<IBase> base = def.getChildByName("base").getAccessor().getValues(retVal);
			if (base != null && base.size() > 0) {
				IPrimitiveType<?> baseType = (IPrimitiveType<?>) base.get(0);
				IBaseResource res = (retVal);
				res.setId(new IdDt(baseType.getValueAsString(), def.getName(), res.getIdElement().getIdPart(), res.getIdElement().getVersionIdPart()));
			}

			BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
			BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
			List<IBase> entries = entryChild.getAccessor().getValues(retVal);
			if (entries != null) {
				for (IBase nextEntry : entries) {
					List<IBase> entryBase = entryDef.getChildByName("base").getAccessor().getValues(nextEntry);

					if (entryBase == null || entryBase.isEmpty()) {
						entryBase = base;
					}

					if (entryBase != null && entryBase.size() > 0) {
						IPrimitiveType<?> baseType = (IPrimitiveType<?>) entryBase.get(0);

						List<IBase> entryResources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry);
						if (entryResources != null && entryResources.size() > 0) {
							IBaseResource res = (IBaseResource) entryResources.get(0);
							RuntimeResourceDefinition resDef = myContext.getResourceDefinition(res);
							String versionIdPart = res.getIdElement().getVersionIdPart();
							if (isBlank(versionIdPart) && res instanceof IResource) {
								versionIdPart = ResourceMetadataKeyEnum.VERSION.get((IResource) res);
							}

							String baseUrl = baseType.getValueAsString();
							String idPart = res.getIdElement().getIdPart();

							String resourceName = resDef.getName();
							if (!baseUrl.startsWith("cid:") && !baseUrl.startsWith("urn:")) {
								res.setId(new IdDt(baseUrl, resourceName, idPart, versionIdPart));
							} else {
								if (baseUrl.endsWith(":")) {
									res.setId(new IdDt(baseUrl + idPart));
								} else {
									res.setId(new IdDt(baseUrl + ':' + idPart));
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

	@Override
	public TagList parseTagList(String theString) {
		return parseTagList(new StringReader(theString));
	}

	@Override
	public BaseParser setOmitResourceId(boolean theOmitResourceId) {
		myOmitResourceId = theOmitResourceId;
		return this;
	}

	@Override
	public BaseParser setParserErrorHandler(IParserErrorHandler theErrorHandler) {
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
	public IParser setSuppressNarratives(boolean theSuppressNarratives) {
		mySuppressNarratives = theSuppressNarratives;
		return this;
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
