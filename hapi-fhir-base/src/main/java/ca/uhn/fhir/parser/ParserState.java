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

import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeElemContainedResources;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceReferenceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
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
			resource = def.newInstance();
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
	 * Invoked after any new XML event is individually processed, containing a copy of the XML event. This is basically intended for embedded XHTML content
	 */
	public void xmlEvent(XMLEvent theNextEvent) {
		myState.xmlEvent(theNextEvent);
	}

	public static ParserState<Bundle> getPreAtomInstance(FhirContext theContext, Class<? extends IResource> theResourceType, boolean theJsonMode) throws DataFormatException {
		ParserState<Bundle> retVal = new ParserState<Bundle>(theContext, theJsonMode);
		retVal.push(retVal.new PreAtomState(theResourceType));
		return retVal;
	}

	/**
	 * @param theResourceType
	 *            May be null
	 */
	public static <T extends IResource> ParserState<T> getPreResourceInstance(Class<T> theResourceType, FhirContext theContext, boolean theJsonMode) throws DataFormatException {
		ParserState<T> retVal = new ParserState<T>(theContext, theJsonMode);
		retVal.push(retVal.new PreResourceState(theResourceType));
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
		private TagList myTagList;
		private String myTerm;
		private String myLabel;
		private String myScheme;

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
				 * This handles XML parsing, which is odd for this quasi-resource type, since the tag has three values instead of one like everything else.
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

	public class AtomDeletedEntryState extends AtomEntryState {

		public AtomDeletedEntryState(Bundle theInstance, Class<? extends IResource> theResourceType) {
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
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("by".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceURI)) {
				push(new AtomDeletedEntryByState(getEntry()));
			} else if ("comment".equals(theLocalPart)) {
				push(new AtomPrimitiveState(getEntry().getDeletedComment()));
			} else {
				super.enteringNewElement(theNamespaceURI, theLocalPart);
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			putPlacerResourceInDeletedEntry(getEntry());
			super.endingElement();
		}

	}

	public class AtomDeletedEntryByState extends BaseState {

		private BundleEntry myEntry;

		public AtomDeletedEntryByState(BundleEntry theEntry) {
			super(null);
			myEntry = theEntry;
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

		@Override
		public void endingElement() throws DataFormatException {
			pop();
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
		private Class<? extends IResource> myResourceType;

		public AtomEntryState(Bundle theInstance, Class<? extends IResource> theResourceType) {
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
				push(new PreResourceState(myEntry, myResourceType));
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
		private Class<? extends IResource> myResourceType;

		public AtomState(Bundle theInstance, Class<? extends IResource> theResourceType) {
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
				throw new DataFormatException("Type " + getCurrentElement() + " does not support undeclared extentions, and found an extension with URL: " + theUrlAttr);
			}
		}

		protected Object getCurrentElement() {
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

	private class BinaryResourceState extends BaseState {

		private static final int SUBSTATE_CONTENT = 2;
		private static final int SUBSTATE_CT = 1;
		private String myData;
		private Binary myInstance;
		private int mySubState = 0;

		public BinaryResourceState(PreResourceState thePreResourceState, Binary theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId((theValue));
				} else if (myInstance instanceof IResource) {
					((IResource) myInstance).setId(new IdDt(theValue));
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

	private class ContainedResourcesState extends PreResourceState {

		public ContainedResourcesState(PreResourceState thePreResourcesState) {
			super(thePreResourcesState);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void wereBack() {
			IResource res = getCurrentElement();
			assert res != null;
			if (res.getId() == null || res.getId().isEmpty()) {
				ourLog.debug("Discarding contained resource with no ID!");
			} else {
				getPreResourceState().getContainedResources().put(res.getId().getValueAsString(), res);
				if (!res.getId().isLocal()) {
					res.setId(new IdDt('#' + res.getId().getIdPart()));
				}
			}
			getPreResourceState().getCurrentElement().getContained().getContainedResources().add(res);
			
		}

	}

	private class DeclaredExtensionState extends BaseState {

		private IElement myChildInstance;
		private RuntimeChildDeclaredExtensionDefinition myDefinition;
		private IElement myParentInstance;
		private PreResourceState myPreResourceState;

		public DeclaredExtensionState(PreResourceState thePreResourceState, RuntimeChildDeclaredExtensionDefinition theDefinition, IElement theParentInstance) {
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
				IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
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
		protected IElement getCurrentElement() {
			return myParentInstance;
		}

	}

	private class ElementCompositeState extends BaseState {

		private BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private ICompositeElement myInstance;

		public ElementCompositeState(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, ICompositeElement theInstance) {
			super(thePreResourceState);
			myDefinition = theDef;
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId((theValue));
				} else if (myInstance instanceof IResource) {
					((IResource) myInstance).setId(new IdDt(theValue));
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
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance;
				newChildInstance = primitiveTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				getPreResourceState().getResourceReferences().add(newChildInstance);
				child.getMutator().addValue(myInstance, newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_BLOCK: {
				RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
				IResourceBlock newBlockInstance = blockTarget.newInstance();
				child.getMutator().addValue(myInstance, newBlockInstance);
				ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), blockTarget, newBlockInstance);
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
				RuntimeElemContainedResources targetElem = (RuntimeElemContainedResources) target;
				List<? extends IElement> values = child.getAccessor().getValues(myInstance);
				ContainedDt newDt;
				if (values == null || values.isEmpty() || values.get(0) == null) {
					newDt = targetElem.newInstance();
					child.getMutator().addValue(myInstance, newDt);
				} else {
					newDt = (ContainedDt) values.get(0);
				}
				ContainedResourcesState state = new ContainedResourcesState(getPreResourceState());
				push(state);
				return;
			}
			case UNDECL_EXT:
			case RESOURCE:
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
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class ExtensionState extends BaseState {

		private ExtensionDt myExtension;

		public ExtensionState(PreResourceState thePreResourceState, ExtensionDt theExtension) {
			super(thePreResourceState);
			myExtension = theExtension;
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myExtension.getValue() != null && myExtension.getUndeclaredExtensions().size() > 0) {
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
				ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
				myExtension.setValue(newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				myExtension.setValue(newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
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
		protected IElement getCurrentElement() {
			return myExtension;
		}

	}

	private class PreAtomState extends BaseState {

		private Bundle myInstance;
		private Class<? extends IResource> myResourceType;

		public PreAtomState(Class<? extends IResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
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

			myInstance = new Bundle();
			push(new AtomState(myInstance, myResourceType));

		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
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
				List<ResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(next, ResourceReferenceDt.class);
				for (ResourceReferenceDt nextRef : refs) {
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

	private class PreResourceState extends BaseState {

		private Map<String, IResource> myContainedResources = new HashMap<String, IResource>();
		private BundleEntry myEntry;
		private IResource myInstance;
		private List<ResourceReferenceDt> myResourceReferences = new ArrayList<ResourceReferenceDt>();
		private Class<? extends IResource> myResourceType;

		public PreResourceState(BundleEntry theEntry, Class<? extends IResource> theResourceType) {
			super(null);
			myEntry = theEntry;
			myResourceType = theResourceType;
		}

		/**
		 * @param theResourceType
		 *            May be null
		 */
		public PreResourceState(Class<? extends IResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
		}

		public PreResourceState(PreResourceState thePreResourcesState) {
			super(thePreResourcesState);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> definition;
			if (myResourceType == null) {
				definition = myContext.getResourceDefinition(theLocalPart);
				if ((definition == null)) {
					throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
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
			if (myEntry != null) {
				myEntry.setResource(myInstance);
			}

			if ("Binary".equals(def.getName())) {
				push(new BinaryResourceState(getRootPreResourceState(), (Binary) myInstance));
			} else {
				push(new ElementCompositeState(getRootPreResourceState(), def, myInstance));
			}
		}

		public Map<String, IResource> getContainedResources() {
			return myContainedResources;
		}

		@Override
		protected IResource getCurrentElement() {
			return myInstance;
		}

		public List<ResourceReferenceDt> getResourceReferences() {
			return myResourceReferences;
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
			if (myEntry == null) {
				myObject = (T) myInstance;
			}

			myContext.newTerser().visit(myInstance, new IModelVisitor() {

				@Override
				public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition,
						ExtensionDt theNextExt) {
					acceptElement(theNextExt.getValue(), null, null);
				}

				@Override
				public void acceptElement(IElement theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
					if (theElement instanceof ResourceReferenceDt) {
						ResourceReferenceDt nextRef = (ResourceReferenceDt) theElement;
						String ref = nextRef.getReference().getValue();
						if (isNotBlank(ref)) {
							if (ref.startsWith("#")) {
								IResource target = myContainedResources.get(ref.substring(1));
								if (target != null) {
									nextRef.setResource(target);
								} else {
									ourLog.warn("Resource contains unknown local ref: " + ref);
								}
							}
						}
					}
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
		protected TagList getCurrentElement() {
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
		private IPrimitiveDatatype<?> myInstance;

		public PrimitiveState(PreResourceState thePreResourceState, IPrimitiveDatatype<?> theInstance) {
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
				} else if (myInstance instanceof IResource) {
					((IResource) myInstance).setId(new IdDt(theValue));
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
			throw new Error("Element " + theLocalPart + " in primitive!"); // TODO:
																			// can
																			// this
																			// happen?
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceReferenceState extends BaseState {

		private ResourceReferenceDt myInstance;
		private ResourceReferenceSubState mySubState;

		public ResourceReferenceState(PreResourceState thePreResourceState, ResourceReferenceDt theInstance) {
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
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private enum ResourceReferenceSubState {
		DISPLAY, INITIAL, REFERENCE
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
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			myDepth++;
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
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
		private int mySubState = 0;
		private TagList myTagList;
		private String myTerm;
		private String myLabel;
		private String myScheme;

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
