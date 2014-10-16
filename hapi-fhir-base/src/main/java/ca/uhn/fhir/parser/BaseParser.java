package ca.uhn.fhir.parser;

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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;

public abstract class BaseParser implements IParser {

	private ContainedResources myContainedResources;
	private FhirContext myContext;
	private boolean mySuppressNarratives;

	public BaseParser(FhirContext theContext) {
		myContext = theContext;
	}

	protected String fixContainedResourceId(String theValue) {
		if (StringUtils.isNotBlank(theValue)&&theValue.charAt(0)=='#') {
			return theValue.substring(1);
		}
		return theValue;
	}

	
	private void containResourcesForEncoding(ContainedResources theContained, IResource theResource, IResource theTarget) {
		List<ResourceReferenceDt> allElements = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, ResourceReferenceDt.class);

		Set<String> allIds = new HashSet<String>();

		for (IResource next : theTarget.getContained().getContainedResources()) {
			String nextId = next.getId().getValue();
			if (StringUtils.isNotBlank(nextId)) {
				allIds.add(nextId);
			}
		}

		for (ResourceReferenceDt next : allElements) {
			IResource resource = next.getResource();
			if (resource != null) {
				if (resource.getId().isEmpty() || resource.getId().isLocal()) {
					theContained.addContained(resource);
				} else {
					continue;
				}

				containResourcesForEncoding(theContained, resource, theTarget);
			}
		}

	}

	public void containResourcesForEncoding(IResource theResource) {
		ContainedResources contained = new ContainedResources();
		containResourcesForEncoding(contained, theResource, theResource);
		myContainedResources = contained;
	}

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
	public String encodeResourceToString(IResource theResource) throws DataFormatException {
		Writer stringWriter = new StringWriter();
		try {
			encodeResourceToWriter(theResource, stringWriter);
		} catch (IOException e) {
			throw new Error("Encountered IOException during write to string - This should not happen!");
		}
		return stringWriter.toString();
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

	ContainedResources getContainedResources() {
		return myContainedResources;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded values.
	 */
	public boolean getSuppressNarratives() {
		return mySuppressNarratives;
	}

	@Override
	public Bundle parseBundle(Reader theReader) {
		return parseBundle(null, theReader);
	}

	@Override
	public Bundle parseBundle(String theXml) throws ConfigurationException, DataFormatException {
		StringReader reader = new StringReader(theXml);
		return parseBundle(reader);
	}

	@SuppressWarnings("cast")
	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		StringReader reader = new StringReader(theMessageString);
		return (T) parseResource(theResourceType, reader);
	}

	@Override
	public IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
		return parseResource(null, theReader);
	}

	@Override
	public IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
		return parseResource(null, theMessageString);
	}

	@Override
	public TagList parseTagList(String theString) {
		return parseTagList(new StringReader(theString));
	}

	@Override
	public IParser setSuppressNarratives(boolean theSuppressNarratives) {
		mySuppressNarratives = theSuppressNarratives;
		return this;
	}

	protected void throwExceptionForUnknownChildType(BaseRuntimeChildDefinition nextChild, Class<? extends IElement> type) {
		if (nextChild instanceof BaseRuntimeDeclaredChildDefinition) {
			StringBuilder b = new StringBuilder();
			b.append(((BaseRuntimeDeclaredChildDefinition) nextChild).getElementName());
			b.append(" has type ");
			b.append(type);
			b.append(" but this is not a valid type for this element");
			if (nextChild instanceof RuntimeChildChoiceDefinition) {
				RuntimeChildChoiceDefinition choice = (RuntimeChildChoiceDefinition) nextChild;
				b.append(" - Expected one of: " + choice.getValidChildTypes());
			}
			throw new DataFormatException(b.toString());
		}
		throw new DataFormatException(nextChild + " has no child of type " + type);
	}

	protected String determineReferenceText(ResourceReferenceDt theRef) {
		String reference = theRef.getReference().getValue();
		if (isBlank(reference)) {
			if (theRef.getResource() != null) {
				IdDt containedId = getContainedResources().getResourceId(theRef.getResource());
				if (containedId != null && !containedId.isEmpty()) {
					if (containedId.isLocal()) {
						reference = containedId.getValue();
					} else {
						reference = "#" + containedId.getValue();
					}
				} else if (theRef.getResource().getId() != null && theRef.getResource().getId().hasIdPart()) {
					reference = theRef.getResource().getId().getValue();
				}
			}
		}
		return reference;
	}

	
	static class ContainedResources {
		private long myNextContainedId = 1;

		private IdentityHashMap<IResource, IdDt> myResourceToId = new IdentityHashMap<IResource, IdDt>();
		private List<IResource> myResources = new ArrayList<IResource>();

		public void addContained(IResource theResource) {
			if (myResourceToId.containsKey(theResource)) {
				return;
			}
			
			IdDt newId;
			if (theResource.getId().isLocal()) {
				newId = theResource.getId();
			} else {
				// TODO: make this configurable between the two below (and something else?)
				// newId = new IdDt(UUID.randomUUID().toString());
				newId = new IdDt(myNextContainedId++);
			}
			
			myResourceToId.put(theResource, newId);
			myResources.add(theResource);
		}

		public List<IResource> getContainedResources() {
			return myResources;
		}
		
		public IdDt getResourceId(IResource theResource) {
			return myResourceToId.get(theResource);
		}

		public boolean isEmpty() {
			return myResourceToId.isEmpty();
		}

	}

}
