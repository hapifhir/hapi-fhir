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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.IBase;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.ICompositeType;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IReference;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IMutator;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseBinary;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.IModelVisitor;

class ParserState<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);
	private FhirContext myContext;
	private boolean myJsonMode;
	private T myObject;
	private BaseState myState;

	private ParserState(FhirContext theContext, boolean theJsonMode) {
		myContext = theContext;
		myJsonMode = theJsonMode;
	}

	public void attributeValue(String theName, String theValue) throws DataFormatException {
		myState.attributeValue(theName, theValue);
	}

	public void endingElement() throws DataFormatException {
		myState.endingElement();
	}

	public void enteringNewElement(String theNamespaceURI, String theName) throws DataFormatException {
		myState.enteringNewElement(theNamespaceURI, theName);
	}

	public void enteringNewElementExtension(StartElement theElem, String theUrlAttr, boolean theIsModifier) {
		myState.enteringNewElementExtension(theElem, theUrlAttr, theIsModifier);
	}

	@SuppressWarnings("unchecked")
	public T getObject() {
		return (T) myState.getCurrentElement();
	}

	public boolean isComplete() {
		return myObject != null;
	}

	public boolean isPreResource() {
		return myState.isPreResource();
	}

	private Object newContainedDt(IResource theTarget) {

		Object newChildInstance;
		try {
			newChildInstance = theTarget.getStructureFhirVersionEnum().getVersionImplementation().getContainedType().newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate " + myContext.getVersion().getResourceReferenceType(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate " + myContext.getVersion().getResourceReferenceType(), e);
		}
		return newChildInstance;
	}

	private IBase newResourceReferenceDt(IBaseResource theTarget) {

		IBase newChildInstance;
		try {
			IFhirVersion version;
			if (theTarget instanceof IResource) {
				version = ((IResource) theTarget).getStructureFhirVersionEnum().getVersionImplementation();
			} else {
				version = FhirVersionEnum.DSTU2_HL7ORG.getVersionImplementation();
			}
			newChildInstance = version.getResourceReferenceType().newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate " + myContext.getVersion().getResourceReferenceType(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate " + myContext.getVersion().getResourceReferenceType(), e);
		}
		return newChildInstance;
	}

	private void pop() {
		myState = myState.myStack;
		myState.wereBack();
	}

	private void push(BaseState theState) {
		theState.setStack(myState);
		myState = theState;
	}

	private void putPlacerResourceInDeletedEntry(BundleEntry entry) {
		IdDt id = null;
		if (entry.getLinkSelf() != null && entry.getLinkSelf().isEmpty() == false) {
			id = new IdDt(entry.getLinkSelf().getValue());
		} else {
			id = entry.getId();
		}

		IResource resource = entry.getResource();
		if (resource == null && id != null && isNotBlank(id.getResourceType())) {
			String resourceType = id.getResourceType();
			RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);
			if (def == null) {
				throw new DataFormatException("Entry references unknown resource type: " + resourceType);
			}
			resource = (IResource) def.newInstance();
			resource.setId(id);
			entry.setResource(resource);
		}

		if (resource != null) {
			resource.getResourceMetadata().put(ResourceMetadataKeyEnum.DELETED_AT, entry.getDeletedAt());
			resource.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, id);
		}
	}

	public void string(String theData) {
		myState.string(theData);
	}

	public boolean verifyNamespace(String theExpect, String theActual) {
		if (myJsonMode) {
			return true;
		}
		return StringUtils.equals(theExpect, theActual);
	}

	/**
	 * Invoked after any new XML event is individually processed, containing a copy of the XML event. This is basically
	 * intended for embedded XHTML content
	 */
	public void xmlEvent(XMLEvent theNextEvent) {
		myState.xmlEvent(theNextEvent);
	}

	public static ParserState<Bundle> getPreAtomInstance(FhirContext theContext, Class<? extends IBaseResource> theResourceType, boolean theJsonMode) throws DataFormatException {
		ParserState<Bundle> retVal = new ParserState<Bundle>(theContext, theJsonMode);
		if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
			retVal.push(retVal.new PreAtomState(theResourceType));
		} else {
			retVal.push(retVal.new PreBundleState(theResourceType));
		}
		return retVal;
	}

	/**
	 * @param theResourceType
	 *            May be null
	 */
	public static <T extends IBaseResource> ParserState<T> getPreResourceInstance(Class<T> theResourceType, FhirContext theContext, boolean theJsonMode) throws DataFormatException {
		ParserState<T> retVal = new ParserState<T>(theContext, theJsonMode);
		if (theResourceType == null) {
			if (theContext.getVersion().getVersion() != FhirVersionEnum.DSTU2_HL7ORG) {
				retVal.push(retVal.new PreResourceStateHapi(theResourceType));
			} else {
				retVal.push(retVal.new PreResourceStateHl7Org(theResourceType));
			}
		} else {
			if (IResource.class.isAssignableFrom(theResourceType)) {
				retVal.push(retVal.new PreResourceStateHapi(theResourceType));
			} else {
				retVal.push(retVal.new PreResourceStateHl7Org(theResourceType));
			}
		}
		return retVal;
	}

	public static ParserState<TagList> getPreTagListInstance(FhirContext theContext, boolean theJsonMode) {
		ParserState<TagList> retVal = new ParserState<TagList>(theContext, theJsonMode);
		retVal.push(retVal.new PreTagListState());
		return retVal;
	}

	public class AtomAuthorState extends BaseState {

		private BaseBundle myInstance;

		public AtomAuthorState(BaseBundle theEntry) {
			super(null);
			myInstance = theEntry;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("name".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getAuthorName()));
			} else if ("uri".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getAuthorUri()));
			} else {
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

	}

	public class AtomCategoryState extends BaseState {

		private static final int STATE_LABEL = 2;
		private static final int STATE_NONE = 0;
		private static final int STATE_SCHEME = 3;
		private static final int STATE_TERM = 1;

		private int myCatState = STATE_NONE;
		private String myLabel;
		private String myScheme;
		private TagList myTagList;
		private String myTerm;

		public AtomCategoryState(TagList theTagList) {
			super(null);
			myTagList = theTagList;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("term".equals(theName)) {
				myTerm = theValue;
			} else if ("label".equals(theName)) {
				myLabel = theValue;
			} else if ("scheme".equals(theName)) {
				myScheme = theValue;
			} else if ("value".equals(theName)) {
				/*
				 * This handles XML parsing, which is odd for this quasi-resource type, since the tag has three values
				 * instead of one like everything else.
				 */
				switch (myCatState) {
				case STATE_LABEL:
					myLabel = theValue;
					break;
				case STATE_TERM:
					myTerm = theValue;
					break;
				case STATE_SCHEME:
					myScheme = theValue;
					break;
				default:
					super.string(theValue);
					break;
				}

			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myCatState != STATE_NONE) {
				myCatState = STATE_NONE;
			} else {
				if (isNotEmpty(myScheme) || isNotBlank(myTerm) || isNotBlank(myLabel)) {
					myTagList.addTag(myScheme, myTerm, myLabel);
				}
				pop();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theName) throws DataFormatException {
			if (myCatState != STATE_NONE) {
				throw new DataFormatException("Unexpected element in entry: " + theName);
			}
			if ("term".equals(theName)) {
				myCatState = STATE_TERM;
			} else if ("label".equals(theName)) {
				myCatState = STATE_LABEL;
			} else if ("scheme".equals(theName)) {
				myCatState = STATE_SCHEME;
			}
		}

	}

	public class AtomDeletedEntryByState extends BaseState {

		private BundleEntry myEntry;

		public AtomDeletedEntryByState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("name".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getDeletedByName()));
			} else if ("email".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getDeletedByEmail()));
			} else {
				throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
			}
		}

	}

	public class AtomDeletedEntryState extends AtomEntryState {

		public AtomDeletedEntryState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(theInstance, theResourceType);
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("ref".equals(theName)) {
				getEntry().setId(new IdDt(theValue));
			} else if ("when".equals(theName)) {
				getEntry().setDeleted(new InstantDt(theValue));
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			putPlacerResourceInDeletedEntry(getEntry());
			super.endingElement();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("by".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceURI)) {
				push(new AtomDeletedEntryByState(getEntry()));
			} else if ("comment".equals(theLocalPart)) {
				push(new AtomPrimitiveState(getEntry().getDeletedComment()));
			} else {
				super.enteringNewElement(theNamespaceURI, theLocalPart);
			}
		}

	}

	private class AtomDeletedJsonWhenState extends BaseState {

		private String myData;
		private IPrimitiveDatatype<?> myPrimitive;

		public AtomDeletedJsonWhenState(IPrimitiveDatatype<?> thePrimitive) {
			super(null);
			Validate.notNull(thePrimitive, "thePrimitive");
			myPrimitive = thePrimitive;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			myData = theValue;
		}

		@Override
		public void endingElement() throws DataFormatException {
			myPrimitive.setValueAsString(myData);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
		}

		@Override
		protected IElement getCurrentElement() {
			return null;
		}

	}

	public class AtomEntryState extends BaseState {

		private boolean myDeleted;
		private BundleEntry myEntry;
		private Class<? extends IBaseResource> myResourceType;

		public AtomEntryState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(null);
			myEntry = new BundleEntry();
			myResourceType = theResourceType;
			theInstance.getEntries().add(myEntry);
		}

		@Override
		public void endingElement() throws DataFormatException {
			populateResourceMetadata();
			pop();

			if (myDeleted) {
				putPlacerResourceInDeletedEntry(myEntry);
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("title".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myEntry));
			} else if ("updated".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getUpdated()));
			} else if ("published".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getPublished()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myEntry));
			} else if ("content".equals(theLocalPart)) {
				push(new PreResourceStateHapi(myEntry, myResourceType));
			} else if ("summary".equals(theLocalPart)) {
				push(new XhtmlState(getPreResourceState(), myEntry.getSummary(), false));
			} else if ("category".equals(theLocalPart)) {
				push(new AtomCategoryState(myEntry.getCategories()));
			} else if ("deleted".equals(theLocalPart) && myJsonMode) {
				// JSON and XML deleted entries are completely different for some reason
				myDeleted = true;
				push(new AtomDeletedJsonWhenState(myEntry.getDeletedAt()));
			} else {
				throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
			}

			// TODO: handle category
		}

		protected BundleEntry getEntry() {
			return myEntry;
		}

		@SuppressWarnings("deprecation")
		private void populateResourceMetadata() {
			if (myEntry.getResource() == null) {
				return;
			}

			IdDt id = myEntry.getId();
			if (id != null && id.isEmpty() == false) {
				myEntry.getResource().setId(id);
			}

			Map<ResourceMetadataKeyEnum<?>, Object> metadata = myEntry.getResource().getResourceMetadata();
			if (myEntry.getPublished().isEmpty() == false) {
				ResourceMetadataKeyEnum.PUBLISHED.put(myEntry.getResource(), myEntry.getPublished());
			}
			if (myEntry.getUpdated().isEmpty() == false) {
				ResourceMetadataKeyEnum.UPDATED.put(myEntry.getResource(), myEntry.getUpdated());
			}

			ResourceMetadataKeyEnum.TITLE.put(myEntry.getResource(), myEntry.getTitle().getValue());

			if (myEntry.getCategories().isEmpty() == false) {
				TagList tagList = new TagList();
				for (Tag next : myEntry.getCategories()) {
					tagList.add(next);
				}
				ResourceMetadataKeyEnum.TAG_LIST.put(myEntry.getResource(), tagList);
			}
			if (!myEntry.getLinkSelf().isEmpty()) {
				String linkSelfValue = myEntry.getLinkSelf().getValue();
				IdDt linkSelf = new IdDt(linkSelfValue);
				myEntry.getResource().setId(linkSelf);
				if (isNotBlank(linkSelf.getVersionIdPart())) {
					metadata.put(ResourceMetadataKeyEnum.VERSION_ID, linkSelf);
				}
			}
			if (!myEntry.getLinkAlternate().isEmpty()) {
				ResourceMetadataKeyEnum.LINK_ALTERNATE.put(myEntry.getResource(), myEntry.getLinkAlternate().getValue());
			}
			if (!myEntry.getLinkSearch().isEmpty()) {
				ResourceMetadataKeyEnum.LINK_SEARCH.put(myEntry.getResource(), myEntry.getLinkSearch().getValue());
			}

		}

	}

	private class AtomLinkState extends BaseState {

		private BundleEntry myEntry;
		private String myHref;
		private Bundle myInstance;
		private String myRel;

		public AtomLinkState(Bundle theInstance) {
			super(null);
			myInstance = theInstance;
		}

		public AtomLinkState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("rel".equals(theName)) {
				myRel = theValue;
			} else if ("href".equals(theName)) {
				myHref = theValue;
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myInstance != null) {
				if ("self".equals(myRel)) {
					myInstance.getLinkSelf().setValueAsString(myHref);
				} else if ("first".equals(myRel)) {
					myInstance.getLinkFirst().setValueAsString(myHref);
				} else if ("previous".equals(myRel)) {
					myInstance.getLinkPrevious().setValueAsString(myHref);
				} else if ("next".equals(myRel)) {
					myInstance.getLinkNext().setValueAsString(myHref);
				} else if ("last".equals(myRel)) {
					myInstance.getLinkLast().setValueAsString(myHref);
				} else if ("fhir-base".equals(myRel)) {
					myInstance.getLinkBase().setValueAsString(myHref);
				}
			} else {
				if ("self".equals(myRel)) {
					myEntry.getLinkSelf().setValueAsString(myHref);
				} else if ("search".equals(myRel)) {
					myEntry.getLinkSearch().setValueAsString(myHref);
				} else if ("alternate".equals(myRel)) {
					myEntry.getLinkAlternate().setValueAsString(myHref);
				}
			}
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Found unexpected element content '" + theLocalPart + "' within <link>");
		}

	}

	private class AtomPrimitiveState extends BaseState {

		private String myData;
		private IPrimitiveDatatype<?> myPrimitive;

		public AtomPrimitiveState(IPrimitiveDatatype<?> thePrimitive) {
			super(null);
			Validate.notNull(thePrimitive, "thePrimitive");
			myPrimitive = thePrimitive;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (myJsonMode) {
				string(theValue);
			}
			super.attributeValue(theName, theValue);
		}

		@Override
		public void endingElement() throws DataFormatException {
			myPrimitive.setValueAsString(myData);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
		}

		@Override
		protected IElement getCurrentElement() {
			return null;
		}

		@Override
		public void string(String theData) {
			if (myData == null) {
				myData = theData;
			} else {
				// this shouldn't generally happen so it's ok that it's
				// inefficient
				myData = myData + theData;
			}
		}

	}

	private class AtomState extends BaseState {

		private Bundle myInstance;
		private Class<? extends IBaseResource> myResourceType;

		public AtomState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(null);
			myInstance = theInstance;
			myResourceType = theResourceType;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceURI)) {
				push(new AtomEntryState(myInstance, myResourceType));
			} else if (theLocalPart.equals("published")) {
				push(new AtomPrimitiveState(myInstance.getPublished()));
			} else if (theLocalPart.equals("title")) {
				push(new AtomPrimitiveState(myInstance.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getBundleId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myInstance));
			} else if ("totalResults".equals(theLocalPart) && (verifyNamespace(XmlParser.OPENSEARCH_NS, theNamespaceURI) || verifyNamespace(Constants.OPENSEARCH_NS_OLDER, theNamespaceURI))) {
				push(new AtomPrimitiveState(myInstance.getTotalResults()));
			} else if ("updated".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getUpdated()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myInstance));
			} else if ("category".equals(theLocalPart)) {
				push(new AtomCategoryState(myInstance.getCategories()));
			} else if ("deleted-entry".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceURI)) {
				push(new AtomDeletedEntryState(myInstance, myResourceType));
			} else {
				if (theNamespaceURI != null) {
					throw new DataFormatException("Unexpected element: {" + theNamespaceURI + "}" + theLocalPart);
				} else {
					throw new DataFormatException("Unexpected element: " + theLocalPart);
				}
			}

			// TODO: handle category and DSig
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class BasePreAtomOrBundleState extends BaseState {

		private Bundle myInstance;

		private Class<? extends IBaseResource> myResourceType;

		public BasePreAtomOrBundleState(Class<? extends IBaseResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
		}

		@Override
		public void endingElement() throws DataFormatException {
			// ignore
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

		public Bundle getInstance() {
			return myInstance;
		}

		protected Class<? extends IBaseResource> getResourceType() {
			return myResourceType;
		}

		public void setInstance(Bundle theInstance) {
			myInstance = theInstance;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myObject = (T) myInstance;

			/*
			 * Stitch together resource references
			 */

			Map<String, IResource> idToResource = new HashMap<String, IResource>();
			List<IResource> resources = myInstance.toListOfResources();
			for (IResource next : resources) {
				if (next.getId() != null && next.getId().isEmpty() == false) {
					idToResource.put(next.getId().toUnqualifiedVersionless().getValue(), next);
				}
			}

			for (IResource next : resources) {
				List<BaseResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
				for (BaseResourceReferenceDt nextRef : refs) {
					if (nextRef.isEmpty() == false && nextRef.getReference() != null) {
						IResource target = idToResource.get(nextRef.getReference().getValue());
						if (target != null) {
							nextRef.setResource(target);
						}
					}
				}
			}

		}

	}

	private abstract class BaseState {

		private PreResourceState myPreResourceState;
		private BaseState myStack;

		public BaseState(PreResourceState thePreResourceState) {
			super();
			myPreResourceState = thePreResourceState;
		}

		@SuppressWarnings("unused")
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			// ignore by default
		}

		public void endingElement() throws DataFormatException {
			// ignore by default
		}

		@SuppressWarnings("unused")
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			// ignore by default
		}

		/**
		 * Default implementation just handles undeclared extensions
		 */
		@SuppressWarnings("unused")
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			if (myPreResourceState != null && getCurrentElement() instanceof ISupportsUndeclaredExtensions) {
				ExtensionDt newExtension = new ExtensionDt(theIsModifier, theUrlAttr);
				ISupportsUndeclaredExtensions elem = (ISupportsUndeclaredExtensions) getCurrentElement();
				elem.addUndeclaredExtension(newExtension);
				ExtensionState newState = new ExtensionState(myPreResourceState, newExtension);
				push(newState);
			} else {
				if (theIsModifier == false) {
					if (getCurrentElement() instanceof IBaseHasExtensions) {
						IBaseExtension<?> ext = ((IBaseHasExtensions) getCurrentElement()).addExtension();
						ext.setUrl(theUrlAttr);
						ParserState<T>.ExtensionState newState = new ExtensionState(myPreResourceState, ext);
						push(newState);
					} else {
						throw new DataFormatException("Type " + getCurrentElement() + " does not support undeclared extentions, and found an extension with URL: " + theUrlAttr);
					}
				} else {
					if (getCurrentElement() instanceof IBaseHasModifierExtensions) {
						IBaseExtension<?> ext = ((IBaseHasModifierExtensions) getCurrentElement()).addModifierExtension();
						ext.setUrl(theUrlAttr);
						ParserState<T>.ExtensionState newState = new ExtensionState(myPreResourceState, ext);
						push(newState);
					} else {
						throw new DataFormatException("Type " + getCurrentElement() + " does not support undeclared extentions, and found an extension with URL: " + theUrlAttr);
					}
				}
			}
		}

		protected IBase getCurrentElement() {
			return null;
		}

		public PreResourceState getPreResourceState() {
			return myPreResourceState;
		}

		public boolean isPreResource() {
			return false;
		}

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		/**
		 * @param theData
		 *            The string value
		 */
		public void string(String theData) {
			// ignore by default
		}

		public void wereBack() {
			// allow an implementor to override
		}

		/**
		 * @param theNextEvent
		 *            The XML event
		 */
		public void xmlEvent(XMLEvent theNextEvent) {
			// ignore
		}

	}

	private class BinaryResourceStateForDstu1 extends BaseState {

		private static final int SUBSTATE_CONTENT = 2;
		private static final int SUBSTATE_CT = 1;
		private String myData;
		private BaseBinary myInstance;
		private int mySubState = 0;

		public BinaryResourceStateForDstu1(PreResourceState thePreResourceState, BaseBinary theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId((theValue));
				} else {
					(myInstance).setId(new IdDt(theValue));
				}
			} else if ("contentType".equals(theName)) {
				myInstance.setContentType(theValue);
			} else if (myJsonMode && "value".equals(theName)) {
				string(theValue);
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (mySubState == SUBSTATE_CT) {
				myInstance.setContentType(myData);
				mySubState = 0;
				myData = null;
				return;
			} else if (mySubState == SUBSTATE_CONTENT) {
				myInstance.setContentAsBase64(myData);
				mySubState = 0;
				myData = null;
				return;
			} else {
				if (!myJsonMode) {
					myInstance.setContentAsBase64(myData);
				}
				pop();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (myJsonMode && "contentType".equals(theLocalPart) && mySubState == 0) {
				mySubState = SUBSTATE_CT;
			} else if (myJsonMode && "content".equals(theLocalPart) && mySubState == 0) {
				mySubState = SUBSTATE_CONTENT;
			} else {
				throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
			}
		}

		@Override
		protected IElement getCurrentElement() {
			return null;
		}

		@Override
		public void string(String theData) {
			if (myData == null) {
				myData = theData;
			} else {
				// this shouldn't generally happen so it's ok that it's
				// inefficient
				myData = myData + theData;
			}
		}

	}

	private class BundleEntryDeletedState extends BaseState {

		private BundleEntry myEntry;

		public BundleEntryDeletedState(PreResourceState thePreResourceState, BundleEntry theEntry) {
			super(thePreResourceState);
			myEntry = theEntry;
		}

		@Override
		public void endingElement() throws DataFormatException {
			String resType = myEntry.getDeletedResourceType().getValue();
			String id = myEntry.getDeletedResourceId().getValue();
			String version = myEntry.getDeletedResourceVersion().getValue();
			myEntry.setLinkSelf(new StringDt(new IdDt(resType, id, version).getValue()));
			putPlacerResourceInDeletedEntry(myEntry);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("type".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedResourceType()));
			} else if ("id".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedResourceId()));
			} else if ("resourceId".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedResourceId()));
			} else if ("version".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedResourceVersion()));
			} else if ("versionId".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedResourceVersion()));
			} else if ("instant".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getDeletedAt()));
			} else {
				throw new DataFormatException("Unexpected element '" + theLocalPart + "' in element 'deleted'");
			}
		}

	}

	public class BundleEntrySearchState extends BaseState {

		private BundleEntry myEntry;

		public BundleEntrySearchState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("mode".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getSearchMode()));
			} else if ("score".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getScore()));
			} else {
				throw new DataFormatException("Unexpected element in Bundle.entry.search: " + theLocalPart);
			}
		}

	}

	public class BundleEntryState extends BaseState {

		private BundleEntry myEntry;
		private Class<? extends IBaseResource> myResourceType;

		public BundleEntryState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(null);
			myEntry = new BundleEntry();
			myResourceType = theResourceType;
			theInstance.getEntries().add(myEntry);
		}

		@Override
		public void endingElement() throws DataFormatException {
			populateResourceMetadata();
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("base".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getLinkBase()));
			} else if ("transaction".equals(theLocalPart)) {
				push(new BundleEntryTransactionState(myEntry));
			} else if ("search".equals(theLocalPart)) {
				push(new BundleEntrySearchState(myEntry));
			} else if ("score".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getScore()));
			} else if ("resource".equals(theLocalPart)) {
				push(new PreResourceStateHapi(myEntry, myResourceType));
			} else if ("deleted".equals(theLocalPart)) {
				push(new BundleEntryDeletedState(getPreResourceState(), myEntry));
			} else {
				throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
			}

			// TODO: handle category
		}

		protected BundleEntry getEntry() {
			return myEntry;
		}

		@SuppressWarnings("deprecation")
		private void populateResourceMetadata() {
			if (myEntry.getResource() == null) {
				return;
			}

			IdDt id = myEntry.getId();
			if (id != null && id.isEmpty() == false) {
				myEntry.getResource().setId(id);
			}

			Map<ResourceMetadataKeyEnum<?>, Object> metadata = myEntry.getResource().getResourceMetadata();
			if (myEntry.getPublished().isEmpty() == false) {
				ResourceMetadataKeyEnum.PUBLISHED.put(myEntry.getResource(), myEntry.getPublished());
			}
			if (myEntry.getUpdated().isEmpty() == false) {
				ResourceMetadataKeyEnum.UPDATED.put(myEntry.getResource(), myEntry.getUpdated());
			}

			ResourceMetadataKeyEnum.TITLE.put(myEntry.getResource(), myEntry.getTitle().getValue());

			if (myEntry.getCategories().isEmpty() == false) {
				TagList tagList = new TagList();
				for (Tag next : myEntry.getCategories()) {
					tagList.add(next);
				}
				ResourceMetadataKeyEnum.TAG_LIST.put(myEntry.getResource(), tagList);
			}
			if (!myEntry.getLinkSelf().isEmpty()) {
				String linkSelfValue = myEntry.getLinkSelf().getValue();
				IdDt linkSelf = new IdDt(linkSelfValue);
				myEntry.getResource().setId(linkSelf);
				if (isNotBlank(linkSelf.getVersionIdPart())) {
					metadata.put(ResourceMetadataKeyEnum.VERSION_ID, linkSelf);
				}
			}
			if (!myEntry.getLinkAlternate().isEmpty()) {
				ResourceMetadataKeyEnum.LINK_ALTERNATE.put(myEntry.getResource(), myEntry.getLinkAlternate().getValue());
			}
			if (!myEntry.getLinkSearch().isEmpty()) {
				ResourceMetadataKeyEnum.LINK_SEARCH.put(myEntry.getResource(), myEntry.getLinkSearch().getValue());
			}
			if (!myEntry.getSearchMode().isEmpty()) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(myEntry.getResource(), myEntry.getSearchMode().getValueAsEnum());
			}
			if (!myEntry.getTransactionOperation().isEmpty()) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(myEntry.getResource(), myEntry.getTransactionOperation().getValueAsEnum());
			}
			if (!myEntry.getScore().isEmpty()) {
				ResourceMetadataKeyEnum.ENTRY_SCORE.put(myEntry.getResource(), myEntry.getScore());
			}
		}

	}

	public class BundleEntryTransactionState extends BaseState {

		private BundleEntry myEntry;

		public BundleEntryTransactionState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("operation".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getTransactionOperation()));
			} else if ("url".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getLinkSearch()));
			} else {
				throw new DataFormatException("Unexpected element in Bundle.entry.search: " + theLocalPart);
			}
		}

	}

	private class BundleLinkState extends BaseState {

		private BundleEntry myEntry;
		private String myHref;
		private boolean myInRelation = false;
		private Bundle myInstance;
		private boolean myInUrl = false;
		private String myRel;

		public BundleLinkState(Bundle theInstance) {
			super(null);
			myInstance = theInstance;
		}

		public BundleLinkState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (myInRelation) {
				myRel = theValue;
			} else if (myInUrl) {
				myHref = theValue;
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (!myInRelation && !myInUrl) {
				if (myInstance != null) {
					if ("self".equals(myRel)) {
						myInstance.getLinkSelf().setValueAsString(myHref);
					} else if ("first".equals(myRel)) {
						myInstance.getLinkFirst().setValueAsString(myHref);
					} else if ("previous".equals(myRel)) {
						myInstance.getLinkPrevious().setValueAsString(myHref);
					} else if ("next".equals(myRel)) {
						myInstance.getLinkNext().setValueAsString(myHref);
					} else if ("last".equals(myRel)) {
						myInstance.getLinkLast().setValueAsString(myHref);
					} else if ("fhir-base".equals(myRel)) {
						myInstance.getLinkBase().setValueAsString(myHref);
					}
				} else {
					if ("self".equals(myRel)) {
						myEntry.getLinkSelf().setValueAsString(myHref);
					} else if ("search".equals(myRel)) {
						myEntry.getLinkSearch().setValueAsString(myHref);
					} else if ("alternate".equals(myRel)) {
						myEntry.getLinkAlternate().setValueAsString(myHref);
					}
				}
				pop();
			} else {
				myInRelation = false;
				myInUrl = false;
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (myInRelation || myInUrl) {
				throw new DataFormatException("Unexpected element '" + theLocalPart + "' in element 'link'");
			}
			if ("relation".equals(theLocalPart)) {
				myInRelation = true;
			} else if ("url".equals(theLocalPart)) {
				myInUrl = true;
			} else {
				throw new DataFormatException("Unexpected element '" + theLocalPart + "' in element 'link'");
			}
		}

	}

	private class BundleState extends BaseState {

		private Bundle myInstance;
		private Class<? extends IBaseResource> myResourceType;

		public BundleState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(null);
			myInstance = theInstance;
			myResourceType = theResourceType;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("id".equals(theLocalPart)) {
				push(new PrimitiveState(null, myInstance.getId()));
			} else if ("meta".equals(theLocalPart)) {
				push(new MetaElementState(null, myInstance.getResourceMetadata()));
			} else if ("type".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myInstance.getType()));
			} else if ("base".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myInstance.getLinkBase()));
			} else if ("total".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myInstance.getTotalResults()));
			} else if ("link".equals(theLocalPart)) {
				push(new BundleLinkState(myInstance));
			} else if ("entry".equals(theLocalPart)) {
				push(new BundleEntryState(myInstance, myResourceType));
			} else {
				throw new DataFormatException("Unxpected element '" + theLocalPart + " in element 'Bundle'");
			}

			// if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceURI)) {
			// push(new AtomEntryState(myInstance, myResourceType));
			// } else if (theLocalPart.equals("published")) {
			// push(new AtomPrimitiveState(myInstance.getPublished()));
			// } else if (theLocalPart.equals("title")) {
			// push(new AtomPrimitiveState(myInstance.getTitle()));
			// } else if ("id".equals(theLocalPart)) {
			// push(new AtomPrimitiveState(myInstance.getBundleId()));
			// } else if ("link".equals(theLocalPart)) {
			// push(new AtomLinkState(myInstance));
			// } else if ("totalResults".equals(theLocalPart) && (verifyNamespace(XmlParser.OPENSEARCH_NS,
			// theNamespaceURI) || verifyNamespace(Constants.OPENSEARCH_NS_OLDER, theNamespaceURI))) {
			// push(new AtomPrimitiveState(myInstance.getTotalResults()));
			// } else if ("updated".equals(theLocalPart)) {
			// push(new AtomPrimitiveState(myInstance.getUpdated()));
			// } else if ("author".equals(theLocalPart)) {
			// push(new AtomAuthorState(myInstance));
			// } else if ("category".equals(theLocalPart)) {
			// push(new AtomCategoryState(myInstance.getCategories()));
			// } else if ("deleted-entry".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS,
			// theNamespaceURI)) {
			// push(new AtomDeletedEntryState(myInstance, myResourceType));
			// } else {
			// if (theNamespaceURI != null) {
			// throw new DataFormatException("Unexpected element: {" + theNamespaceURI + "}" + theLocalPart);
			// } else {
			// throw new DataFormatException("Unexpected element: " + theLocalPart);
			// }
			// }

			// TODO: handle category and DSig
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

		@Override
		public void wereBack() {
			for (BundleEntry nextEntry : myInstance.getEntries()) {
				IResource nextResource = nextEntry.getResource();

				String bundleBaseUrl = myInstance.getLinkBase().getValue();
				String entryBaseUrl = nextEntry.getLinkBase().getValue();
				String version = ResourceMetadataKeyEnum.VERSION.get(nextResource);
				String resourceName = myContext.getResourceDefinition(nextResource).getName();
				String bundleIdPart = nextResource.getId().getIdPart();
				if (isNotBlank(bundleIdPart)) {
					if (isNotBlank(entryBaseUrl)) {
						nextResource.setId(new IdDt(entryBaseUrl, resourceName, bundleIdPart, version));
					} else {
						nextResource.setId(new IdDt(bundleBaseUrl, resourceName, bundleIdPart, version));
					}
				}
			}

			String bundleVersion = (String) myInstance.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION);
			String baseUrl = myInstance.getLinkBase().getValue();
			String id = myInstance.getId().getIdPart();
			if (isNotBlank(id)) {
				myInstance.setId(new IdDt(baseUrl, "Bundle", id, bundleVersion));
			}

		}

	}

	private class ContainedResourcesStateHapi extends PreResourceState {

		public ContainedResourcesStateHapi(PreResourceState thePreResourcesState) {
			super(thePreResourcesState, ((IResource) thePreResourcesState.myInstance).getStructureFhirVersionEnum());
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void wereBack() {
			IResource res = (IResource) getCurrentElement();
			assert res != null;
			if (res.getId() == null || res.getId().isEmpty()) {
				ourLog.debug("Discarding contained resource with no ID!");
			} else {
				getPreResourceState().getContainedResources().put(res.getId().getValueAsString(), res);
				if (!res.getId().isLocal()) {
					res.setId(new IdDt('#' + res.getId().getIdPart()));
				}
			}
			IResource preResCurrentElement = (IResource) getPreResourceState().getCurrentElement();

			@SuppressWarnings("unchecked")
			List<IResource> containedResources = (List<IResource>) preResCurrentElement.getContained().getContainedResources();
			containedResources.add(res);

		}

	}

	private class DeclaredExtensionState extends BaseState {

		private IBase myChildInstance;
		private RuntimeChildDeclaredExtensionDefinition myDefinition;
		private IBase myParentInstance;
		private PreResourceState myPreResourceState;

		public DeclaredExtensionState(PreResourceState thePreResourceState, RuntimeChildDeclaredExtensionDefinition theDefinition, IBase theParentInstance) {
			super(thePreResourceState);
			myPreResourceState = thePreResourceState;
			myDefinition = theDefinition;
			myParentInstance = theParentInstance;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> target = myDefinition.getChildByName(theLocalPart);
			if (target == null) {
				throw new DataFormatException("Unknown extension element name: " + theLocalPart);
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				ElementCompositeState newState = new ElementCompositeState(myPreResourceState, compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveType<?> newChildInstance = primitiveTarget.newInstance();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				IBase newChildInstance = newResourceReferenceDt(myPreResourceState.myInstance);
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				BaseState newState = createResourceReferenceState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_XHTML:
			case RESOURCE:
			case RESOURCE_BLOCK:
			case UNDECL_EXT:
			case EXTENSION_DECLARED:
			default:
				break;
			}
		}

		@Override
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getChildExtensionForUrl(theUrlAttr);
			if (declaredExtension != null) {
				if (myChildInstance == null) {
					myChildInstance = myDefinition.newInstance();
					myDefinition.getMutator().addValue(myParentInstance, myChildInstance);
				}
				BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension, myChildInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier);
			}
		}

		@Override
		protected IBase getCurrentElement() {
			return myParentInstance;
		}

	}

	private BaseState createResourceReferenceState(ParserState<T>.PreResourceState thePreResourceState, IBase newChildInstance) {
		BaseState newState;
		if (newChildInstance instanceof IReference) {
			newState = new ResourceReferenceStateHl7Org(thePreResourceState, (IReference) newChildInstance);
		} else {
			newState = new ResourceReferenceStateHapi(thePreResourceState, (BaseResourceReferenceDt) newChildInstance);
		}
		return newState;
	}

	private class ElementCompositeState<T2 extends IBase> extends BaseState {

		private BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private T2 myInstance;

		public ElementCompositeState(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, T2 theInstance) {
			super(thePreResourceState);
			myDefinition = theDef;
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId((theValue));
				} else if (myInstance instanceof IBaseElement) {
					((IBaseElement) myInstance).setId(theValue);
				} else if (myInstance instanceof IBaseResource) {
					new IdDt(theValue).applyTo((IBaseResource) myInstance);
				}
			} else if ("url".equals(theName) && myInstance instanceof ExtensionDt) {
				((ExtensionDt) myInstance).setUrl(theValue);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void endingElement() {
			pop();
			if (myState == null) {
				myObject = (T) myInstance;
			}
		}

		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			BaseRuntimeChildDefinition child;
			try {
				child = myDefinition.getChildByNameOrThrowDataFormatException(theChildName);
			} catch (DataFormatException e) {
				if (false) {// TODO: make this configurable
					throw e;
				}
				ourLog.warn(e.getMessage());
				push(new SwallowChildrenWholeState(getPreResourceState()));
				return;
			}
			BaseRuntimeElementDefinition<?> target = child.getChildByName(theChildName);
			if (target == null) {
				throw new DataFormatException("Found unexpected element '" + theChildName + "' in parent element '" + myDefinition.getName() + "'. Valid names are: " + child.getValidChildNames());
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeType newChildInstance = (ICompositeType) compositeTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				ParserState<T>.ElementCompositeState<ICompositeType> newState = new ElementCompositeState<ICompositeType>(getPreResourceState(), compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveType<?> newChildInstance;
				newChildInstance = primitiveTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				IBase newChildInstance = newResourceReferenceDt(getPreResourceState().myInstance);
				child.getMutator().addValue(myInstance, newChildInstance);
				BaseState newState = createResourceReferenceState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_BLOCK: {
				RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
				IBase newBlockInstance = blockTarget.newInstance();
				child.getMutator().addValue(myInstance, newBlockInstance);
				ElementCompositeState<IBase> newState = new ElementCompositeState<IBase>(getPreResourceState(), blockTarget, newBlockInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_XHTML: {
				RuntimePrimitiveDatatypeNarrativeDefinition xhtmlTarget = (RuntimePrimitiveDatatypeNarrativeDefinition) target;
				XhtmlDt newDt = xhtmlTarget.newInstance();
				child.getMutator().addValue(myInstance, newDt);
				XhtmlState state = new XhtmlState(getPreResourceState(), newDt, true);
				push(state);
				return;
			}
			case CONTAINED_RESOURCES: {
				List<? extends IBase> values = child.getAccessor().getValues(myInstance);
				Object newDt;
				if (values == null || values.isEmpty() || values.get(0) == null) {
					newDt = newContainedDt((IResource) getPreResourceState().myInstance);
					child.getMutator().addValue(myInstance, (IBase) newDt);
				} else {
					newDt = values.get(0);
				}
				ContainedResourcesStateHapi state = new ContainedResourcesStateHapi(getPreResourceState());
				push(state);
				return;
			}
			case RESOURCE: {
				ParserState<T>.PreResourceStateHl7Org state = new PreResourceStateHl7Org(myInstance, child.getMutator(), null);
				push(state);
				return;
			}
			case UNDECL_EXT:
			case EXTENSION_DECLARED: {
				// Throw an exception because this shouldn't happen here
				break;
			}
			}

			throw new DataFormatException("Illegal resource position: " + target.getChildType());
		}

		@Override
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getDeclaredExtension(theUrlAttr);
			if (declaredExtension != null) {
				BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension, myInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier);
			}
		}

		@Override
		protected T2 getCurrentElement() {
			return myInstance;
		}

	}

	private class ExtensionState extends BaseState {

		private IBaseExtension<?> myExtension;

		public ExtensionState(PreResourceState thePreResourceState, IBaseExtension<?> theExtension) {
			super(thePreResourceState);
			myExtension = theExtension;
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myExtension.getValue() != null && myExtension.getExtension().size() > 0) {
				throw new DataFormatException("Extension must not have both a value and other contained extensions");
			}
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> target = myContext.getRuntimeChildUndeclaredExtensionDefinition().getChildByName(theLocalPart);
			if (target == null) {
				throw new DataFormatException("Unknown extension element name: " + theLocalPart);
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
				myExtension.setValue(newChildInstance);
				ElementCompositeState<IBase> newState = new ElementCompositeState<IBase>(getPreResourceState(), compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveType<?> newChildInstance = primitiveTarget.newInstance();
				myExtension.setValue(newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				BaseResourceReferenceDt newChildInstance = (BaseResourceReferenceDt) newResourceReferenceDt(getPreResourceState().myInstance);
				myExtension.setValue(newChildInstance);
				ResourceReferenceStateHapi newState = new ResourceReferenceStateHapi(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_XHTML:
			case RESOURCE:
			case RESOURCE_BLOCK:
			case UNDECL_EXT:
			case EXTENSION_DECLARED:
			case CONTAINED_RESOURCES:
				break;
			}
		}

		@Override
		protected IBaseExtension<?> getCurrentElement() {
			return myExtension;
		}

	}


	private class CodingSubElementState extends BaseState {
		BaseCodingDt myCoding;

		String thePartWeAreAt;

		public CodingSubElementState(PreResourceState thePreResourceState, BaseCodingDt codingDt, String whichPartAreWeAt) {
			super(thePreResourceState);
			myCoding = codingDt;
			thePartWeAreAt = whichPartAreWeAt;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("value".equals(theName)) {
				if ("system".equals(thePartWeAreAt))
					myCoding.setSystem(theValue);
				else if ("code".equals(thePartWeAreAt))
					myCoding.setCode(theValue);
				else if ("display".equals(thePartWeAreAt))
					myCoding.setDisplay(theValue);
				else if ("version".equals(thePartWeAreAt)) {
					/*
					todo: handle version properly when BaseCodingDt is fixed to support version. For now, we just swallow version in order to avoid throwing a DataFormat exception.

					myCoding.setVersion(theValue);
					 */
				} else
					throw new DataFormatException("Unexpected element '" + theValue + "' found in 'security' element");
			} else {
				throw new DataFormatException("Unexpected attribute '" + theName + "' found in '" + thePartWeAreAt + "' element");
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected element '" + theLocalPart + "' found in 'system' element");

		}
	}


	private class CodingElementState extends BaseState {
		BaseCodingDt myCoding;


		public CodingElementState(ParserState<T>.PreResourceState thePreResourceState, BaseCodingDt codingDt) {
			super(thePreResourceState);
			myCoding = codingDt;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}


		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			push(new CodingSubElementState(getPreResourceState(), myCoding, theLocalPart));
		}

	}

	private class MetaElementState extends BaseState {
		private ResourceMetadataMap myMap;

		public MetaElementState(ParserState<T>.PreResourceState thePreResourceState, ResourceMetadataMap theMap) {
			super(thePreResourceState);
			myMap = theMap;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (theLocalPart.equals("versionId")) {
				push(new MetaVersionElementState(getPreResourceState(), myMap));
//			} else if (theLocalPart.equals("profile")) {
//				
			} else if (theLocalPart.equals("lastUpdated")) {
				InstantDt updated = new InstantDt();
				push(new PrimitiveState(getPreResourceState(), updated));
				myMap.put(ResourceMetadataKeyEnum.UPDATED, updated);
			} else if (theLocalPart.equals("security")) {
				List<BaseCodingDt> securityLabels = (List<BaseCodingDt>) myMap.get(ResourceMetadataKeyEnum.SECURITY_LABELS);
				if (securityLabels == null) {
					securityLabels = new ArrayList<BaseCodingDt>();
					myMap.put(ResourceMetadataKeyEnum.SECURITY_LABELS, securityLabels);
				}
				BaseCodingDt securityLabel;
				try {
					securityLabel = myContext.getCodingDtImplementation().newInstance();
				} catch (InstantiationException e) {
					throw new DataFormatException("Error parsing element 'security' ", e);
				} catch (IllegalAccessException e) {
					throw new DataFormatException("Error parsing element 'security' ", e);
				}
				push(new CodingElementState(getPreResourceState(), securityLabel));
				securityLabels.add(securityLabel);
			} else {
				throw new DataFormatException("Unexpected element '" + theLocalPart + "' found in 'meta' element");
			}
		}

	}

	private class MetaVersionElementState extends BaseState {

		private ResourceMetadataMap myMap;

		public MetaVersionElementState(ParserState<T>.PreResourceState thePreResourceState, ResourceMetadataMap theMap) {
			super(thePreResourceState);
			myMap = theMap;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			myMap.put(ResourceMetadataKeyEnum.VERSION, theValue);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected child element '" + theLocalPart + "' in element 'meta'");
		}

	}

	private class PreAtomState extends BasePreAtomOrBundleState {

		public PreAtomState(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		@Override
		public void endingElement() throws DataFormatException {
			// ignore
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (!"feed".equals(theLocalPart)) {
				throw new DataFormatException("Expecting outer element called 'feed', found: " + theLocalPart);
			}

			setInstance(new Bundle());
			push(new AtomState(getInstance(), getResourceType()));

		}

	}

	private class PreBundleState extends BasePreAtomOrBundleState {

		public PreBundleState(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		@Override
		public void endingElement() throws DataFormatException {
			// ignore
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (!"Bundle".equals(theLocalPart)) {
				throw new DataFormatException("Expecting outer element called 'Bundle', found: " + theLocalPart);
			}

			setInstance(new Bundle());
			push(new BundleState(getInstance(), getResourceType()));

		}

	}

	private class PreResourceStateHapi extends PreResourceState {
		private BundleEntry myEntry;

		public PreResourceStateHapi(BundleEntry theEntry, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myEntry = theEntry;
		}

		public PreResourceStateHapi(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			super.wereBack();
			if (myEntry == null) {
				myObject = (T) getCurrentElement();
			}

			IResource nextResource = (IResource) getCurrentElement();
			String version = ResourceMetadataKeyEnum.VERSION.get(nextResource);
			String resourceName = myContext.getResourceDefinition(nextResource).getName();
			String bundleIdPart = nextResource.getId().getIdPart();
			if (isNotBlank(bundleIdPart)) {
//				if (isNotBlank(entryBaseUrl)) {
//					nextResource.setId(new IdDt(entryBaseUrl, resourceName, bundleIdPart, version));
//				} else {
					nextResource.setId(new IdDt(null, resourceName, bundleIdPart, version));
//				}
			}

			
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			super.enteringNewElement(theNamespaceURI, theLocalPart);
			if (myEntry != null) {
				myEntry.setResource((IResource) getCurrentElement());
			}
		}

	}

	private class PreResourceStateHl7Org extends PreResourceState {

		private IMutator myMutator;
		private Object myTarget;

		public PreResourceStateHl7Org(Object theTarget, IMutator theMutator, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myMutator = theMutator;
			myTarget = theTarget;
		}

		public PreResourceStateHl7Org(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			super.wereBack();
			if (myTarget == null) {
				myObject = (T) getCurrentElement();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			super.enteringNewElement(theNamespaceURI, theLocalPart);
			if (myMutator != null) {
				myMutator.addValue(myTarget, getCurrentElement());
			}
		}

	}

	private abstract class PreResourceState extends BaseState {

		private Map<String, IBaseResource> myContainedResources = new HashMap<String, IBaseResource>();
		private IBaseResource myInstance;
		private FhirVersionEnum myParentVersion;
		private Class<? extends IBaseResource> myResourceType;

		public PreResourceState(Class<? extends IBaseResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
			if (theResourceType != null) {
				myParentVersion = myContext.getResourceDefinition(theResourceType).getStructureVersion();
			} else {
				myParentVersion = myContext.getVersion().getVersion();
			}
		}

		public PreResourceState(PreResourceState thePreResourcesState, FhirVersionEnum theParentVersion) {
			super(thePreResourcesState);
			Validate.notNull(theParentVersion);
			myParentVersion = theParentVersion;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> definition;
			if (myResourceType == null) {
				definition = myContext.getResourceDefinition(myParentVersion, theLocalPart);
				if ((definition == null)) {
					throw new DataFormatException("Element '" + theLocalPart + "' is not a known resource type, expected a resource at this position");
				}
			} else {
				definition = myContext.getResourceDefinition(myResourceType);
				if (!StringUtils.equals(theLocalPart, definition.getName())) {
					definition = myContext.getResourceDefinition(theLocalPart);
					if (!(definition instanceof RuntimeResourceDefinition)) {
						throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
					}
				}
			}

			RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
			myInstance = def.newInstance();

			String resourceName = def.getName();
			if ("Binary".equals(resourceName) && myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				push(new BinaryResourceStateForDstu1(getRootPreResourceState(), (BaseBinary) myInstance));
			} else if (myInstance instanceof IResource) {
				push(new ResourceStateHapi(getRootPreResourceState(), def, (IResource) myInstance));
			} else {
				push(new ResourceStateHl7Org(getRootPreResourceState(), def, myInstance));
			}
		}

		public Map<String, IBaseResource> getContainedResources() {
			return myContainedResources;
		}

		@Override
		protected IBaseResource getCurrentElement() {
			return myInstance;
		}

		private PreResourceState getRootPreResourceState() {
			if (getPreResourceState() != null) {
				return getPreResourceState();
			} else {
				return this;
			}
		}

		@Override
		public boolean isPreResource() {
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myContext.newTerser().visit(myInstance, new IModelVisitor() {

				@Override
				public void acceptElement(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
					if (theElement instanceof BaseResourceReferenceDt) {
						BaseResourceReferenceDt nextRef = (BaseResourceReferenceDt) theElement;
						String ref = nextRef.getReference().getValue();
						if (isNotBlank(ref)) {
							if (ref.startsWith("#")) {
								IResource target = (IResource) myContainedResources.get(ref.substring(1));
								if (target != null) {
									nextRef.setResource(target);
								} else {
									ourLog.warn("Resource contains unknown local ref: " + ref);
								}
							}
						}
					}
				}

				@Override
				public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition, ExtensionDt theNextExt) {
					acceptElement(theNextExt.getValue(), null, null);
				}
			});

		}

	}

	private class PreTagListState extends BaseState {

		private TagList myTagList;

		public PreTagListState() {
			super(null);
			myTagList = new TagList();
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (!TagList.ELEMENT_NAME_LC.equals(theLocalPart.toLowerCase())) {
				throw new DataFormatException("resourceType does not appear to be 'TagList', found: " + theLocalPart);
			}

			push(new TagListState(myTagList));
		}

		@Override
		protected IBase getCurrentElement() {
			return myTagList;
		}

		@Override
		public boolean isPreResource() {
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myObject = (T) myTagList;
		}

	}

	private class PrimitiveState extends BaseState {
		private IPrimitiveType<?> myInstance;

		public PrimitiveState(PreResourceState thePreResourceState, IPrimitiveType<?> theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("value".equals(theName)) {
				myInstance.setValueAsString(theValue);
			} else if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId(theValue);
				} else if (myInstance instanceof IBaseElement) {
					((IBaseElement) myInstance).setId(theValue);
				} else if (myInstance instanceof IBaseResource) {
					new IdDt(theValue).applyTo((org.hl7.fhir.instance.model.IBaseResource) myInstance);
				}
			}
		}

		@Override
		public void endingElement() {
			pop();
		}

		// @Override
		// public void enteringNewElementExtension(StartElement theElement,
		// String theUrlAttr) {
		// if (myInstance instanceof ISupportsUndeclaredExtensions) {
		// UndeclaredExtension ext = new UndeclaredExtension(theUrlAttr);
		// ((ISupportsUndeclaredExtensions)
		// myInstance).getUndeclaredExtensions().add(ext);
		// push(new ExtensionState(ext));
		// }
		// }

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (false) {// TODO: make this configurable
				throw new Error("Element " + theLocalPart + " in primitive!"); // TODO:
			}
			ourLog.warn("Ignoring element {} in primitive tag", theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
			return;
		}

		@Override
		protected IBase getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceReferenceStateHl7Org extends BaseState {

		private IReference myInstance;
		private ResourceReferenceSubState mySubState;

		public ResourceReferenceStateHl7Org(PreResourceState thePreResourceState, IReference theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
			mySubState = ResourceReferenceSubState.INITIAL;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (!"value".equals(theName)) {
				return;
			}

			switch (mySubState) {
			case DISPLAY:
				myInstance.setDisplay(theValue);
				break;
			case INITIAL:
				throw new DataFormatException("Unexpected attribute: " + theValue);
			case REFERENCE:
				myInstance.setReference(theValue);
				break;
			}
		}

		@Override
		public void endingElement() {
			switch (mySubState) {
			case INITIAL:
				pop();
				break;
			case DISPLAY:
			case REFERENCE:
				mySubState = ResourceReferenceSubState.INITIAL;
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			switch (mySubState) {
			case INITIAL:
				if ("display".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.DISPLAY;
					break;
				} else if ("reference".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.REFERENCE;
					break;
				} else if ("resource".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.REFERENCE;
					break;
				}
				//$FALL-THROUGH$
			case DISPLAY:
			case REFERENCE:
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

		@Override
		protected IReference getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceReferenceStateHapi extends BaseState {

		private BaseResourceReferenceDt myInstance;
		private ResourceReferenceSubState mySubState;

		public ResourceReferenceStateHapi(PreResourceState thePreResourceState, BaseResourceReferenceDt theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
			mySubState = ResourceReferenceSubState.INITIAL;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (!"value".equals(theName)) {
				return;
			}

			switch (mySubState) {
			case DISPLAY:
				myInstance.getDisplayElement().setValue(theValue);
				break;
			case INITIAL:
				throw new DataFormatException("Unexpected attribute: " + theValue);
			case REFERENCE:
				myInstance.getReference().setValue(theValue);
				break;
			}
		}

		@Override
		public void endingElement() {
			switch (mySubState) {
			case INITIAL:
				pop();
				break;
			case DISPLAY:
			case REFERENCE:
				mySubState = ResourceReferenceSubState.INITIAL;
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			switch (mySubState) {
			case INITIAL:
				if ("display".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.DISPLAY;
					break;
				} else if ("reference".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.REFERENCE;
					break;
				} else if ("resource".equals(theLocalPart)) {
					mySubState = ResourceReferenceSubState.REFERENCE;
					break;
				}
				//$FALL-THROUGH$
			case DISPLAY:
			case REFERENCE:
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private enum ResourceReferenceSubState {
		DISPLAY, INITIAL, REFERENCE
	}

	private class ResourceStateHapi extends ElementCompositeState<IResource> {

		public ResourceStateHapi(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IResource theInstance) {
			super(thePreResourceState, theDef, theInstance);
		}

		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			if ("id".equals(theChildName)) {
				push(new PrimitiveState(getPreResourceState(), getCurrentElement().getId()));
			} else if ("meta".equals(theChildName)) {
				push(new MetaElementState(getPreResourceState(), getCurrentElement().getResourceMetadata()));
			} else {
				super.enteringNewElement(theNamespace, theChildName);
			}
		}

	}

	private class ResourceStateHl7Org extends ElementCompositeState<IBaseResource> {

		public ResourceStateHl7Org(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IBaseResource theInstance) {
			super(thePreResourceState, theDef, theInstance);
		}

	}

	private class SwallowChildrenWholeState extends BaseState {

		private int myDepth;

		public SwallowChildrenWholeState(PreResourceState thePreResourceState) {
			super(thePreResourceState);
		}

		@Override
		public void endingElement() throws DataFormatException {
			myDepth--;
			if (myDepth < 0) {
				pop();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			myDepth++;
		}

		@Override
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			myDepth++;
		}

	}

	private class TagListState extends BaseState {

		private TagList myTagList;

		public TagListState(TagList theTagList) {
			super(null);
			myTagList = theTagList;
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		protected IBase getCurrentElement() {
			return myTagList;
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (TagList.ATTR_CATEGORY.equals(theLocalPart)) {
				push(new TagState(myTagList));
			} else {
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

	}

	private class TagState extends BaseState {

		private static final int LABEL = 2;
		private static final int NONE = 0;

		private static final int SCHEME = 3;
		private static final int TERM = 1;
		private String myLabel;
		private String myScheme;
		private int mySubState = 0;
		private TagList myTagList;
		private String myTerm;

		public TagState(TagList theTagList) {
			super(null);
			myTagList = theTagList;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			String value = defaultIfBlank(theValue, null);

			switch (mySubState) {
			case TERM:
				myTerm = (value);
				break;
			case LABEL:
				myLabel = (value);
				break;
			case SCHEME:
				myScheme = (value);
				break;
			case NONE:
				// This handles JSON encoding, which is a bit weird
				enteringNewElement(null, theName);
				attributeValue(null, value);
				endingElement();
				break;
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (mySubState != NONE) {
				mySubState = NONE;
			} else {
				if (isNotEmpty(myScheme) || isNotBlank(myTerm) || isNotBlank(myLabel)) {
					myTagList.addTag(myScheme, myTerm, myLabel);
				}
				pop();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (Tag.ATTR_TERM.equals(theLocalPart)) {
				mySubState = TERM;
			} else if (Tag.ATTR_SCHEME.equals(theLocalPart)) {
				mySubState = SCHEME;
			} else if (Tag.ATTR_LABEL.equals(theLocalPart)) {
				mySubState = LABEL;
			} else {
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

	}

	private class XhtmlState extends BaseState {
		private int myDepth;
		private XhtmlDt myDt;
		private List<XMLEvent> myEvents = new ArrayList<XMLEvent>();
		private boolean myIncludeOuterEvent;

		private XhtmlState(PreResourceState thePreResourceState, XhtmlDt theXhtmlDt, boolean theIncludeOuterEvent) throws DataFormatException {
			super(thePreResourceState);
			myDepth = 0;
			myDt = theXhtmlDt;
			myIncludeOuterEvent = theIncludeOuterEvent;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (myJsonMode) {
				myDt.setValueAsString(theValue);
				return;
			}
			super.attributeValue(theName, theValue);
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myJsonMode) {
				pop();
				return;
			}
			super.endingElement();
		}

		@Override
		protected IElement getCurrentElement() {
			return myDt;
		}

		@Override
		public void xmlEvent(XMLEvent theEvent) {
			if (theEvent.isEndElement()) {
				myDepth--;
			}

			if (myIncludeOuterEvent || myDepth > 0) {
				myEvents.add(theEvent);
			}

			if (theEvent.isStartElement()) {
				myDepth++;
			}

			if (theEvent.isEndElement()) {
				if (myDepth == 0) {
					myDt.setValue(myEvents);
					pop();
				}
			}
		}

	}

}
