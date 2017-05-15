package ca.uhn.fhir.parser;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.*;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IMutator;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor;
import ca.uhn.fhir.util.ReflectionUtil;

class ParserState<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);

	private List<String> myComments = new ArrayList<String>(2);
	private final FhirContext myContext;
	private final IParserErrorHandler myErrorHandler;
	private final boolean myJsonMode;
	private T myObject;
	private final IParser myParser;
	private IBase myPreviousElement;
	private BaseState myState;

	private ParserState(IParser theParser, FhirContext theContext, boolean theJsonMode, IParserErrorHandler theErrorHandler) {
		myParser = theParser;
		myContext = theContext;
		myJsonMode = theJsonMode;
		myErrorHandler = theErrorHandler;
	}

	public void attributeValue(String theName, String theValue) throws DataFormatException {
		myState.attributeValue(theName, theValue);
	}

	public void commentPost(String theCommentText) {
		if (myPreviousElement != null) {
			myPreviousElement.getFormatCommentsPost().add(theCommentText);
		}
	}

	public void commentPre(String theCommentText) {
		if (myState.getCurrentElement() != null) {
			IBase element = myState.getCurrentElement();
			element.getFormatCommentsPre().add(theCommentText);
		}
	}

	public boolean elementIsRepeating(String theChildName) {
		return myState.elementIsRepeating(theChildName);
	}

	public void endingElement() throws DataFormatException {
		myState.endingElement();
	}

	public void enteringNewElement(String theNamespaceUri, String theName) throws DataFormatException {
		myState.enteringNewElement(theNamespaceUri, theName);
	}

	public void enteringNewElementExtension(StartElement theElem, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
		myState.enteringNewElementExtension(theElem, theUrlAttr, theIsModifier, baseServerUrl);
	}

	public T getObject() {
		return myObject;
	}

	public boolean isPreResource() {
		return myState.isPreResource();
	}

	private Object newContainedDt(IResource theTarget) {
		return ReflectionUtil.newInstance(theTarget.getStructureFhirVersionEnum().getVersionImplementation().getContainedType());
	}

	@SuppressWarnings("unchecked")
	private void pop() {
		myPreviousElement = myState.getCurrentElement();
		if (myState.myStack != null) {
			myState = myState.myStack;
			myState.wereBack();
		} else {
			myObject = (T) myState.getCurrentElement();
			myState = null;
		}
	}

	private void push(BaseState theState) {
		theState.setStack(myState);
		myState = theState;
		if (myComments.isEmpty() == false) {
			if (myState.getCurrentElement() != null) {
				myState.getCurrentElement().getFormatCommentsPre().addAll(myComments);
				myComments.clear();
			}
		}
	}

	private void putPlacerResourceInDeletedEntry(BundleEntry entry) {
		IdDt id = null;
		if (entry.getLinkSelf() != null && entry.getLinkSelf().isEmpty() == false) {
			id = new IdDt(entry.getLinkSelf().getValue());
		} else {
			//TODO: Use of a deprecated method should be resolved.
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
			//TODO: Use of a deprecated method should be resolved.
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
		if (myState != null) {
			myState.xmlEvent(theNextEvent);
		}
	}

	static ParserState<Bundle> getPreAtomInstance(IParser theParser, FhirContext theContext, Class<? extends IBaseResource> theResourceType, boolean theJsonMode, IParserErrorHandler theErrorHandler)
			throws DataFormatException {
		ParserState<Bundle> retVal = new ParserState<Bundle>(theParser, theContext, theJsonMode, theErrorHandler);
		if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
			retVal.push(retVal.new PreAtomState(theResourceType));
		} else {
			retVal.push(retVal.new PreBundleState(theResourceType));
		}
		return retVal;
	}

	/**
	 * @param theResourceType
	 *           May be null
	 */
	static <T extends IBaseResource> ParserState<T> getPreResourceInstance(IParser theParser, Class<T> theResourceType, FhirContext theContext, boolean theJsonMode, IParserErrorHandler theErrorHandler)
			throws DataFormatException {
		ParserState<T> retVal = new ParserState<T>(theParser, theContext, theJsonMode, theErrorHandler);
		if (theResourceType == null) {
			if (theContext.getVersion().getVersion().isRi()) {
				retVal.push(retVal.new PreResourceStateHl7Org(theResourceType));
			} else {
				retVal.push(retVal.new PreResourceStateHapi(theResourceType));
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

	static ParserState<TagList> getPreTagListInstance(IParser theParser, FhirContext theContext, boolean theJsonMode, IParserErrorHandler theErrorHandler) {
		ParserState<TagList> retVal = new ParserState<TagList>(theParser, theContext, theJsonMode, theErrorHandler);
		retVal.push(retVal.new PreTagListState());
		return retVal;
	}

	private class AtomAuthorState extends BaseState {

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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
		public void enteringNewElement(String theNamespaceUri, String theName) throws DataFormatException {
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

	private class AtomDeletedEntryByState extends BaseState {

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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("name".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getDeletedByName()));
			} else if ("email".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getDeletedByEmail()));
			} else {
				throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
			}
		}

	}

	private class AtomDeletedEntryState extends AtomEntryState {

		public AtomDeletedEntryState(Bundle theInstance, Class<? extends IBaseResource> theResourceType) {
			super(theInstance, theResourceType);
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("ref".equals(theName)) {
				//TODO: Use of a deprecated method should be resolved.
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("by".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceUri)) {
				push(new AtomDeletedEntryByState(getEntry()));
			} else if ("comment".equals(theLocalPart)) {
				push(new AtomPrimitiveState(getEntry().getDeletedComment()));
			} else {
				super.enteringNewElement(theNamespaceUri, theLocalPart);
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
		}

		@Override
		protected IElement getCurrentElement() {
			return null;
		}

	}

	private class AtomEntryState extends BaseState {

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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("title".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				//TODO: Use of a deprecated method should be resolved.
				push(new AtomPrimitiveState(myEntry.getId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myEntry));
			} else if ("updated".equals(theLocalPart)) {
				//TODO: Use of a deprecated method should be resolved.
				push(new AtomPrimitiveState(myEntry.getUpdated()));
			} else if ("published".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myEntry.getPublished()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myEntry));
			} else if ("content".equals(theLocalPart)) {
				push(new PreResourceStateHapi(myEntry, myResourceType).setRequireResourceType(false));
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
			} else {
				super.attributeValue(theName, theValue);
			}
		}

		@Override
		public void endingElement() throws DataFormatException {
			myPrimitive.setValueAsString(myData);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceUri)) {
				push(new AtomEntryState(myInstance, myResourceType));
			} else if (theLocalPart.equals("title")) {
				push(new AtomPrimitiveState(myInstance.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getBundleId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myInstance));
			} else if ("totalResults".equals(theLocalPart) && (verifyNamespace(XmlParser.OPENSEARCH_NS, theNamespaceUri) || verifyNamespace(Constants.OPENSEARCH_NS_OLDER, theNamespaceUri))) {
				push(new AtomPrimitiveState(myInstance.getTotalResults()));
			} else if ("updated".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getUpdated()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myInstance));
			} else if ("category".equals(theLocalPart)) {
				push(new AtomCategoryState(myInstance.getCategories()));
			} else if ("deleted-entry".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceUri)) {
				push(new AtomDeletedEntryState(myInstance, myResourceType));
			} else {
				logAndSwallowUnexpectedElement(theLocalPart);
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
			pop();
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

		@Override
		public void wereBack() {

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

		/**
		 * @param theValue
		 *           The attribute value
		 */
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			myErrorHandler.unknownAttribute(null, theName);
		}

		public boolean elementIsRepeating(String theChildName) {
			return false;
		}

		public void endingElement() throws DataFormatException {
			// ignore by default
		}

		/**
		 * @param theNamespaceUri
		 *           The XML namespace (if XML) or null
		 */
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			myErrorHandler.unknownElement(null, theLocalPart);
		}

		/**
		 * Default implementation just handles undeclared extensions
		 */
		@SuppressWarnings("unused")
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
			if (myPreResourceState != null && getCurrentElement() instanceof ISupportsUndeclaredExtensions) {
				ExtensionDt newExtension = new ExtensionDt(theIsModifier);
				newExtension.setUrl(theUrlAttr);
				ISupportsUndeclaredExtensions elem = (ISupportsUndeclaredExtensions) getCurrentElement();
				elem.addUndeclaredExtension(newExtension);
				ExtensionState newState = new ExtensionState(myPreResourceState, newExtension);
				push(newState);
			} else {
				if (theIsModifier == false) {
					if (getCurrentElement() instanceof IBaseHasExtensions) {
						IBaseExtension<?, ?> ext = ((IBaseHasExtensions) getCurrentElement()).addExtension();
						ext.setUrl(theUrlAttr);
						ParserState<T>.ExtensionState newState = new ExtensionState(myPreResourceState, ext);
						push(newState);
					} else {
						logAndSwallowUnexpectedElement("extension");
					}
				} else {
					if (getCurrentElement() instanceof IBaseHasModifierExtensions) {
						IBaseExtension<?, ?> ext = ((IBaseHasModifierExtensions) getCurrentElement()).addModifierExtension();
						ext.setUrl(theUrlAttr);
						ParserState<T>.ExtensionState newState = new ExtensionState(myPreResourceState, ext);
						push(newState);
					} else {
						logAndSwallowUnexpectedElement("modifierExtension");
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

		protected void logAndSwallowUnexpectedElement(String theLocalPart) {
			myErrorHandler.unknownElement(null, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
		}

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		/**
		 * @param theData
		 *           The string value
		 */
		public void string(String theData) {
			// ignore by default
		}

		public void wereBack() {
			// allow an implementor to override
		}

		/**
		 * @param theNextEvent
		 *           The XML event
		 */
		public void xmlEvent(XMLEvent theNextEvent) {
			// ignore
		}

	}

	private class BinaryResourceStateForDstu1 extends BaseState {

		private static final int SUBSTATE_CONTENT = 2;
		private static final int SUBSTATE_CT = 1;
		private String myData;
		private IBaseBinary myInstance;
		private int mySubState = 0;

		public BinaryResourceStateForDstu1(PreResourceState thePreResourceState, IBaseBinary theInstance) {
			super(thePreResourceState);
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId((theValue));
				} else {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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

	private class BundleEntrySearchState extends BaseState {

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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("mode".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getSearchMode()));
			} else if ("score".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getScore()));
			} else {
				throw new DataFormatException("Unexpected element in Bundle.entry.search: " + theLocalPart);
			}
		}

	}

	private class BundleEntryState extends BaseState {

		private BundleEntry myEntry;
		private IdDt myFullUrl;
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("base".equals(theLocalPart)) {
				//TODO: Use of a deprecated method should be resolved.
				push(new PrimitiveState(getPreResourceState(), myEntry.getLinkBase()));
			} else if ("request".equals(theLocalPart)) {
				push(new BundleEntryTransactionState(myEntry));
			} else if ("search".equals(theLocalPart)) {
				push(new BundleEntrySearchState(myEntry));
			} else if ("score".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getScore()));
			} else if ("resource".equals(theLocalPart)) {
				push(new PreResourceStateHapi(myEntry, myResourceType).setRequireResourceType(false));
			} else if ("deleted".equals(theLocalPart)) {
				push(new BundleEntryDeletedState(getPreResourceState(), myEntry));
			} else if ("link".equals(theLocalPart)) {
				push(new BundleLinkState(myEntry));
			} else if ("fullUrl".equals(theLocalPart)) {
				myFullUrl = new IdDt();
				push(new PrimitiveState(getPreResourceState(), myFullUrl));
			} else if ("fhir_comments".equals(theLocalPart) && myJsonMode) {
				push(new SwallowChildrenWholeState(getPreResourceState()));
			} else {
				this.logAndSwallowUnexpectedElement(theLocalPart);
			}
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

			if (myFullUrl != null && !myFullUrl.isEmpty()) {
				myEntry.getResource().setId(myFullUrl);
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
			if (!myEntry.getTransactionMethod().isEmpty()) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(myEntry.getResource(), myEntry.getTransactionMethod().getValueAsEnum());
			}
			if (!myEntry.getScore().isEmpty()) {
				ResourceMetadataKeyEnum.ENTRY_SCORE.put(myEntry.getResource(), myEntry.getScore());
			}
		}

	}

	private class BundleEntryTransactionState extends BaseState {

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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if ("method".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getTransactionMethod()));
			} else if ("url".equals(theLocalPart)) {
				push(new PrimitiveState(getPreResourceState(), myEntry.getLinkSearch()));
			} else {
				logAndSwallowUnexpectedElement(theLocalPart);
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
			} else if ("text".equals(theLocalPart)) {
				push(new SwallowChildrenWholeState(getPreResourceState()));
			} else {
				throw new DataFormatException("Unxpected element '" + theLocalPart + "' in element 'Bundle'");
			}

			// if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceUri)) {
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
			// theNamespaceUri) || verifyNamespace(Constants.OPENSEARCH_NS_OLDER, theNamespaceUri))) {
			// push(new AtomPrimitiveState(myInstance.getTotalResults()));
			// } else if ("updated".equals(theLocalPart)) {
			// push(new AtomPrimitiveState(myInstance.getUpdated()));
			// } else if ("author".equals(theLocalPart)) {
			// push(new AtomAuthorState(myInstance));
			// } else if ("category".equals(theLocalPart)) {
			// push(new AtomCategoryState(myInstance.getCategories()));
			// } else if ("deleted-entry".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS,
			// theNamespaceUri)) {
			// push(new AtomDeletedEntryState(myInstance, myResourceType));
			// } else {
			// if (theNamespaceUri != null) {
			// throw new DataFormatException("Unexpected element: {" + theNamespaceUri + "}" + theLocalPart);
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
				if (nextResource == null) {
					continue;
				}

				String bundleBaseUrl = myInstance.getLinkBase().getValue();
				//TODO: Use of a deprecated method should be resolved.
				String entryBaseUrl = nextEntry.getLinkBase().getValue();
				String version = ResourceMetadataKeyEnum.VERSION.get(nextResource);
				String resourceName = myContext.getResourceDefinition(nextResource).getName();
				String bundleIdPart = nextResource.getId().getIdPart();
				if (isNotBlank(bundleIdPart)) {
					String baseUrl;
					if (isNotBlank(entryBaseUrl)) {
						baseUrl = entryBaseUrl;
					} else {
						baseUrl = bundleBaseUrl;
					}
					if (baseUrl == null) {
						// nothing
					} else if (!baseUrl.startsWith("cid:") && !baseUrl.startsWith("urn:")) {
						nextResource.setId(new IdDt(baseUrl, resourceName, bundleIdPart, version));
					} else {
						if (baseUrl.endsWith(":")) {
							nextResource.setId(new IdDt(baseUrl + bundleIdPart));
						} else {
							nextResource.setId(new IdDt(baseUrl + ':' + bundleIdPart));
						}
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
		protected void populateTarget() {
			// nothing
		}

		@Override
		public void wereBack() {
			super.wereBack();

			IResource res = (IResource) getCurrentElement();
			assert res != null;
			if (res.getId() == null || res.getId().isEmpty()) {
				// If there is no ID, we don't keep the resource because it's useless (contained resources
				// need an ID to be referred to)
				myErrorHandler.containedResourceWithNoId(null);
			} else {
				if (!res.getId().isLocal()) {
					res.setId(new IdDt('#' + res.getId().getIdPart()));
				}
				getPreResourceState().getContainedResources().put(res.getId().getValueAsString(), res);
			}
			IResource preResCurrentElement = (IResource) getPreResourceState().getCurrentElement();

			@SuppressWarnings("unchecked")
			List<IResource> containedResources = (List<IResource>) preResCurrentElement.getContained().getContainedResources();
			containedResources.add(res);

		}

	}

	private class ContainedResourcesStateHl7Org extends PreResourceState {

		public ContainedResourcesStateHl7Org(PreResourceState thePreResourcesState) {
			super(thePreResourcesState, thePreResourcesState.myParentVersion);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		protected void populateTarget() {
			// nothing
		}

		@Override
		public void wereBack() {
			super.wereBack();

			IBaseResource res = getCurrentElement();
			assert res != null;
			if (res.getIdElement() == null || res.getIdElement().isEmpty()) {
				// If there is no ID, we don't keep the resource because it's useless (contained resources
				// need an ID to be referred to)
				myErrorHandler.containedResourceWithNoId(null);
			} else {
				res.getIdElement().setValue('#' + res.getIdElement().getIdPart());
				getPreResourceState().getContainedResources().put(res.getIdElement().getValue(), res);
			}

			IBaseResource preResCurrentElement = getPreResourceState().getCurrentElement();
			RuntimeResourceDefinition def = myContext.getResourceDefinition(preResCurrentElement);
			def.getChildByName("contained").getMutator().addValue(preResCurrentElement, res);
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
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if (theName.equals("url")) {
				// This can be ignored
				return;
			}
			super.attributeValue(theName, theValue);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> target = myDefinition.getChildByName(theLocalPart);
			if (target == null) {
				myErrorHandler.unknownElement(null, theLocalPart);
				push(new SwallowChildrenWholeState(getPreResourceState()));
				return;
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeType newChildInstance = (ICompositeType) compositeTarget.newInstance(myDefinition.getInstanceConstructorArguments());
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				ElementCompositeState newState = new ElementCompositeState(myPreResourceState, theLocalPart, compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveType<?> newChildInstance = primitiveTarget.newInstance(myDefinition.getInstanceConstructorArguments());
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
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
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getChildExtensionForUrl(theUrlAttr);
			if (declaredExtension != null) {
				if (myChildInstance == null) {
					myChildInstance = myDefinition.newInstance();
					myDefinition.getMutator().addValue(myParentInstance, myChildInstance);
				}
				BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension, myChildInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier, baseServerUrl);
			}
		}

		@Override
		protected IBase getCurrentElement() {
			return myParentInstance;
		}

	}

	private class ElementCompositeState extends BaseState {

		private BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private IBase myInstance;
		private Set<String> myParsedNonRepeatableNames = new HashSet<String>();
		private String myElementName;

		public ElementCompositeState(PreResourceState thePreResourceState, String theElementName, BaseRuntimeElementCompositeDefinition<?> theDef, IBase theInstance) {
			super(thePreResourceState);
			myDefinition = theDef;
			myInstance = theInstance;
			myElementName = theElementName;
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
			} else {
				if (myJsonMode) {
					myErrorHandler.incorrectJsonType(null, myElementName, ValueType.OBJECT, null, ValueType.SCALAR, ScalarType.STRING);
				} else {
					myErrorHandler.unknownAttribute(null, theName);
				}
			}
		}

		@Override
		public boolean elementIsRepeating(String theChildName) {
			BaseRuntimeChildDefinition child = myDefinition.getChildByName(theChildName);
			if (child == null) {
				return false;
			}
			return child.getMax() > 1 || child.getMax() == Child.MAX_UNLIMITED;
		}

		@Override
		public void endingElement() {
			pop();
		}
		
		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			BaseRuntimeChildDefinition child = myDefinition.getChildByName(theChildName);
			if (child == null) {
				if (theChildName.equals("id")) {
					if (getCurrentElement() instanceof IIdentifiableElement) {
						push(new IdentifiableElementIdState(getPreResourceState(), (IIdentifiableElement) getCurrentElement()));
						return;
					}
				}

				/*
				 * This means we've found an element that doesn't exist on the structure. If the error handler doesn't throw
				 * an exception, swallow the element silently along with any child elements
				 */
				myErrorHandler.unknownElement(null, theChildName);
				push(new SwallowChildrenWholeState(getPreResourceState()));
				return;
			}

			if ((child.getMax() == 0 || child.getMax() == 1) && !myParsedNonRepeatableNames.add(theChildName)) {
				myErrorHandler.unexpectedRepeatingElement(null, theChildName);
				push(new SwallowChildrenWholeState(getPreResourceState()));
				return;
			}

			BaseRuntimeElementDefinition<?> target = child.getChildByName(theChildName);
			if (target == null) {
				// This is a bug with the structures and shouldn't happen..
				throw new DataFormatException("Found unexpected element '" + theChildName + "' in parent element '" + myDefinition.getName() + "'. Valid names are: " + child.getValidChildNames());
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeType newChildInstance = (ICompositeType) compositeTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				ParserState<T>.ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theChildName, compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case ID_DATATYPE:
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveType<?> newChildInstance;
				newChildInstance = primitiveTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_BLOCK: {
				RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
				IBase newBlockInstance = blockTarget.newInstance();
				child.getMutator().addValue(myInstance, newBlockInstance);
				ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theChildName, blockTarget, newBlockInstance);
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
			case PRIMITIVE_XHTML_HL7ORG: {
				RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition xhtmlTarget = (RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition) target;
				IBaseXhtml newDt = xhtmlTarget.newInstance();
				child.getMutator().addValue(myInstance, newDt);
				XhtmlStateHl7Org state = new XhtmlStateHl7Org(getPreResourceState(), newDt);
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
			case CONTAINED_RESOURCE_LIST: {
				ContainedResourcesStateHl7Org state = new ContainedResourcesStateHl7Org(getPreResourceState());
				push(state);
				return;
			}
			case RESOURCE: {
				if (myInstance instanceof IAnyResource || myInstance instanceof IBaseBackboneElement) {
					ParserState<T>.PreResourceStateHl7Org state = new PreResourceStateHl7Org(myInstance, child.getMutator(), null);
					push(state);
				} else {
					ParserState<T>.PreResourceStateHapi state = new PreResourceStateHapi(myInstance, child.getMutator(), null);
					push(state);
				}
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
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getDeclaredExtension(theUrlAttr, baseServerUrl);
			if (declaredExtension != null) {
				BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension, myInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier, baseServerUrl);
			}
		}

		@Override
		protected IBase getCurrentElement() {
			return myInstance;
		}

	}

	public class ElementIdState extends BaseState {

		private IBaseElement myElement;

		public ElementIdState(ParserState<T>.PreResourceState thePreResourceState, IBaseElement theElement) {
			super(thePreResourceState);
			myElement = theElement;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			myElement.setId(theValue);
		}

		@Override
		public void endingElement() {
			pop();
		}

	}

	private class ExtensionState extends BaseState {

		private IBaseExtension<?, ?> myExtension;

		public ExtensionState(PreResourceState thePreResourceState, IBaseExtension<?, ?> theExtension) {
			super(thePreResourceState);
			myExtension = theExtension;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("url".equals(theName)) {
				// The URL attribute is handles in the XML loop as a special case since it is "url" instead
				// of "value" like every single other place
				return;
			}
			if ("id".equals(theName)) {
				if (getCurrentElement() instanceof IBaseElement) {
					((IBaseElement) getCurrentElement()).setId(theValue);
					return;
				} else if (getCurrentElement() instanceof IIdentifiableElement) {
					((IIdentifiableElement) getCurrentElement()).setElementSpecificId(theValue);
					return;
				}
			}
			super.attributeValue(theName, theValue);
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myExtension.getValue() != null && myExtension.getExtension().size() > 0) {
				throw new DataFormatException("Extension (URL='" + myExtension.getUrl() + "') must not have both a value and other contained extensions");
			}
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if (theLocalPart.equals("id")) {
				if (getCurrentElement() instanceof IBaseElement) {
					push(new ElementIdState(getPreResourceState(), (IBaseElement)getCurrentElement()));
					return;
				} else if (getCurrentElement() instanceof IIdentifiableElement) {
					push(new IdentifiableElementIdState(getPreResourceState(), (IIdentifiableElement)getCurrentElement()));
					return;
				}
			}
			
			BaseRuntimeElementDefinition<?> target = myContext.getRuntimeChildUndeclaredExtensionDefinition().getChildByName(theLocalPart);
			
			if (target != null) {
				switch (target.getChildType()) {
				case COMPOSITE_DATATYPE: {
					BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
					ICompositeType newChildInstance = (ICompositeType) compositeTarget.newInstance();
					myExtension.setValue(newChildInstance);
					ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theLocalPart, compositeTarget, newChildInstance);
					push(newState);
					return;
				}
				case ID_DATATYPE:
				case PRIMITIVE_DATATYPE: {
					RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
					IPrimitiveType<?> newChildInstance = primitiveTarget.newInstance();
					myExtension.setValue(newChildInstance);
					PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
					push(newState);
					return;
				}
				case CONTAINED_RESOURCES:
				case CONTAINED_RESOURCE_LIST:
				case EXTENSION_DECLARED:
				case PRIMITIVE_XHTML:
				case PRIMITIVE_XHTML_HL7ORG:
				case RESOURCE:
				case RESOURCE_BLOCK:
				case UNDECL_EXT:
					break;
				}
			}

			// We hit an invalid type for the extension
			myErrorHandler.unknownElement(null, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
			return;
		}
		
		@Override
		protected IBaseExtension<?, ?> getCurrentElement() {
			return myExtension;
		}

	}

	public class IdentifiableElementIdState extends BaseState {

		private IIdentifiableElement myElement;

		public IdentifiableElementIdState(ParserState<T>.PreResourceState thePreResourceState, IIdentifiableElement theElement) {
			super(thePreResourceState);
			myElement = theElement;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			myElement.setElementSpecificId(theValue);
		}

		@Override
		public void endingElement() {
			pop();
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if (theLocalPart.equals("versionId")) {
				push(new MetaVersionElementState(getPreResourceState(), myMap));
				// } else if (theLocalPart.equals("profile")) {
				//
			} else if (theLocalPart.equals("lastUpdated")) {
				InstantDt updated = new InstantDt();
				push(new PrimitiveState(getPreResourceState(), updated));
				myMap.put(ResourceMetadataKeyEnum.UPDATED, updated);
			} else if (theLocalPart.equals("security")) {
				@SuppressWarnings("unchecked")
				List<IBase> securityLabels = (List<IBase>) myMap.get(ResourceMetadataKeyEnum.SECURITY_LABELS);
				if (securityLabels == null) {
					securityLabels = new ArrayList<IBase>();
					myMap.put(ResourceMetadataKeyEnum.SECURITY_LABELS, securityLabels);
				}
				IBase securityLabel = myContext.getVersion().newCodingDt();
				BaseRuntimeElementCompositeDefinition<?> codinfDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(securityLabel.getClass());
				push(new SecurityLabelElementStateHapi(getPreResourceState(), codinfDef, securityLabel));
				securityLabels.add(securityLabel);
			} else if (theLocalPart.equals("profile")) {
				@SuppressWarnings("unchecked")
				List<IdDt> profiles = (List<IdDt>) myMap.get(ResourceMetadataKeyEnum.PROFILES);
				List<IdDt> newProfiles;
				if (profiles != null) {
					newProfiles = new ArrayList<IdDt>(profiles.size() + 1);
					newProfiles.addAll(profiles);
				} else {
					newProfiles = new ArrayList<IdDt>(1);
				}
				IdDt profile = new IdDt();
				push(new PrimitiveState(getPreResourceState(), profile));
				newProfiles.add(profile);
				myMap.put(ResourceMetadataKeyEnum.PROFILES, Collections.unmodifiableList(newProfiles));
			} else if (theLocalPart.equals("tag")) {
				TagList tagList = (TagList) myMap.get(ResourceMetadataKeyEnum.TAG_LIST);
				if (tagList == null) {
					tagList = new TagList();
					myMap.put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
				}
				push(new TagState(tagList));
			} else {
				myErrorHandler.unknownElement(null, theLocalPart);
				push(new SwallowChildrenWholeState(getPreResourceState()));
				return;
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			myErrorHandler.unknownElement(null, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
			return;
		}

	}

	private class PreAtomState extends BasePreAtomOrBundleState {

		public PreAtomState(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if (!"Bundle".equals(theLocalPart)) {
				throw new DataFormatException("Expecting outer element called 'Bundle', found: " + theLocalPart);
			}

			setInstance(new Bundle());
			push(new BundleState(getInstance(), getResourceType()));

		}

	}

	private abstract class PreResourceState extends BaseState {

		private Map<String, IBaseResource> myContainedResources;
		private IBaseResource myInstance;
		private FhirVersionEnum myParentVersion;
		private boolean myRequireResourceType = true;
		private Class<? extends IBaseResource> myResourceType;

		public PreResourceState(Class<? extends IBaseResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
			myContainedResources = new HashMap<String, IBaseResource>();
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
			myContainedResources = thePreResourcesState.getContainedResources();
		}

		@Override
		public void endingElement() throws DataFormatException {
			stitchBundleCrossReferences();
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> definition;
			if (myResourceType == null) {
				definition = null;
				if (myParser.getPreferTypes() != null) {
					for (Class<? extends IBaseResource> next : myParser.getPreferTypes()) {
						RuntimeResourceDefinition nextDef = myContext.getResourceDefinition(next);
						if (nextDef.getName().equals(theLocalPart)) {
							definition = nextDef;
						}
					}
				}
				if (definition == null) {
					definition = myContext.getResourceDefinition(myParentVersion, theLocalPart);
				}
				if ((definition == null)) {
					throw new DataFormatException("Element '" + theLocalPart + "' is not a known resource type, expected a resource at this position");
				}
			} else {
				definition = myContext.getResourceDefinition(myResourceType);
				if (!StringUtils.equals(theLocalPart, definition.getName())) {
					if (myRequireResourceType) {
						throw new DataFormatException(myContext.getLocalizer().getMessage(ParserState.class, "wrongResourceTypeFound", definition.getName(), theLocalPart));
					}
					definition = myContext.getResourceDefinition(theLocalPart);
					if (!(definition instanceof RuntimeResourceDefinition)) {
						throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
					}
				}
			}

			RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
			if (!definition.getName().equals(theLocalPart) && definition.getName().equalsIgnoreCase(theLocalPart)) {
				throw new DataFormatException("Unknown resource type '" + theLocalPart + "': Resource names are case sensitive, found similar name: '" + definition.getName() + "'");
			}
			myInstance = def.newInstance();

			String resourceName = def.getName();
			if ("Binary".equals(resourceName) && myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				push(new BinaryResourceStateForDstu1(getRootPreResourceState(), (IBaseBinary) myInstance));
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
			}
			return this;
		}

		@Override
		public boolean isPreResource() {
			return true;
		}

		protected abstract void populateTarget();

		private void postProcess() {
			if (myContext.hasDefaultTypeForProfile()) {
				IBaseMetaType meta = myInstance.getMeta();
				Class<? extends IBaseResource> wantedProfileType = null;
				String usedProfile = null;
				for (IPrimitiveType<String> next : meta.getProfile()) {
					if (isNotBlank(next.getValue())) {
						wantedProfileType = myContext.getDefaultTypeForProfile(next.getValue());
						if (wantedProfileType != null) {
							usedProfile = next.getValue();
							break;
						}
					}
				}

				if (wantedProfileType != null && !wantedProfileType.equals(myInstance.getClass())) {
					if (myResourceType == null || myResourceType.isAssignableFrom(wantedProfileType)) {
						ourLog.debug("Converting resource of type {} to type defined for profile \"{}\": {}", new Object[] { myInstance.getClass().getName(), usedProfile, wantedProfileType });

						/*
						 * This isn't the most efficient thing really.. If we want a specific
						 * type we just re-parse into that type. The problem is that we don't know
						 * until we've parsed the resource which type we want to use because the
						 * profile declarations are in the text of the resource itself.
						 * 
						 * At some point it would be good to write code which can present a view
						 * of one type backed by another type and use that.
						 */
						IParser parser = myContext.newJsonParser();
						String asString = parser.encodeResourceToString(myInstance);
						myInstance = parser.parseResource(wantedProfileType, asString);
					}
				}
			}
			
			populateTarget();
		}

		public ParserState<T>.PreResourceState setRequireResourceType(boolean theRequireResourceType) {
			myRequireResourceType = theRequireResourceType;
			return this;
		}

		private void stitchBundleCrossReferences() {
			final boolean bundle = "Bundle".equals(myContext.getResourceDefinition(myInstance).getName());
			if (bundle) {

				FhirTerser t = myContext.newTerser();
				
				Map<String, IBaseResource> idToResource = new HashMap<String, IBaseResource>();
				if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
					List<IBase> entries = t.getValues(myInstance, "Bundle.entry", IBase.class);
					for (IBase nextEntry : entries) {
						IPrimitiveType<?> fullUrl = t.getSingleValueOrNull(nextEntry, "fullUrl", IPrimitiveType.class);
						if (fullUrl != null && isNotBlank(fullUrl.getValueAsString())) {
							IBaseResource resource = t.getSingleValueOrNull(nextEntry, "resource", IBaseResource.class);
							if (resource != null) {
								idToResource.put(fullUrl.getValueAsString(), resource);
							}
						}
					}
				}
				
				/*
				 * Stitch together resource references
				 */
				List<IBaseResource> resources = t.getAllPopulatedChildElementsOfType(myInstance, IBaseResource.class);
				for (IBaseResource next : resources) {
					IIdType id = next.getIdElement();
					if (id != null && id.isEmpty() == false) {
						String resName = myContext.getResourceDefinition(next).getName();
						IIdType idType = id.withResourceType(resName).toUnqualifiedVersionless();
						idToResource.put(idType.getValueAsString(), next);
					}
				}

				for (IBaseResource next : resources) {
					List<IBaseReference> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(next, IBaseReference.class);
					for (IBaseReference nextRef : refs) {
						if (nextRef.isEmpty() == false && nextRef.getReferenceElement() != null) {
							IIdType unqualifiedVersionless = nextRef.getReferenceElement().toUnqualifiedVersionless();
							IBaseResource target = idToResource.get(unqualifiedVersionless.getValueAsString());
							if (target != null) {
								nextRef.setResource(target);
							}
						}
					}
				}

				/*
				 * Set resource IDs based on Bundle.entry.request.url
				 */
				List<Pair<String, IBaseResource>> urlsAndResources = BundleUtil.getBundleEntryUrlsAndResources(myContext, (IBaseBundle) myInstance);
				for (Pair<String, IBaseResource> pair : urlsAndResources) {
					if (pair.getRight() != null && isNotBlank(pair.getLeft()) && pair.getRight().getIdElement().isEmpty()) {
						if (pair.getLeft().startsWith("urn:")) {
							pair.getRight().setId(pair.getLeft());
						}
					}
				}

			}
		}

		protected void weaveContainedResources() {
			FhirTerser terser = myContext.newTerser();
			terser.visit(myInstance, new IModelVisitor() {

				@Override
				public void acceptElement(IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
					if (theElement instanceof BaseResourceReferenceDt) {
						BaseResourceReferenceDt nextRef = (BaseResourceReferenceDt) theElement;
						String ref = nextRef.getReference().getValue();
						if (isNotBlank(ref)) {
							if (ref.startsWith("#")) {
								IResource target = (IResource) myContainedResources.get(ref);
								if (target != null) {
									ourLog.debug("Resource contains local ref {} in field {}", ref, thePathToElement);
									nextRef.setResource(target);
								} else {
									myErrorHandler.unknownReference(null, ref);
								}
							}
						}
					} else if (theElement instanceof IBaseReference) {
						IBaseReference nextRef = (IBaseReference) theElement;
						String ref = nextRef.getReferenceElement().getValue();
						if (isNotBlank(ref)) {
							if (ref.startsWith("#")) {
								IBaseResource target = myContainedResources.get(ref);
								if (target != null) {
									ourLog.debug("Resource contains local ref {} in field {}", ref, thePathToElement);
									nextRef.setResource(target);
								} else {
									myErrorHandler.unknownReference(null, ref);
								}
							}
						}
					}
				}
			});
		}

		@Override
		public void wereBack() {
			postProcess();
		}

	}

	private class PreResourceStateHapi extends PreResourceState {
		private BundleEntry myEntry;
		private IMutator myMutator;
		private Object myTarget;

		public PreResourceStateHapi(BundleEntry theEntry, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myEntry = theEntry;
			assert theResourceType == null || IResource.class.isAssignableFrom(theResourceType);
		}

		public PreResourceStateHapi(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			assert theResourceType == null || IResource.class.isAssignableFrom(theResourceType);
		}

		public PreResourceStateHapi(Object theTarget, IMutator theMutator, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myTarget = theTarget;
			myMutator = theMutator;
			assert theResourceType == null || IResource.class.isAssignableFrom(theResourceType);
		}

		// @Override
		// public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
		// super.enteringNewElement(theNamespaceUri, theLocalPart);
		// populateTarget();
		// }

		@Override
		protected void populateTarget() {
			weaveContainedResources();
			if (myEntry != null) {
				myEntry.setResource((IResource) getCurrentElement());
			}
			if (myMutator != null) {
				myMutator.setValue(myTarget, getCurrentElement());
			}
		}

		@Override
		public void wereBack() {
			super.wereBack();

			IResource nextResource = (IResource) getCurrentElement();
			String version = ResourceMetadataKeyEnum.VERSION.get(nextResource);
			String resourceName = myContext.getResourceDefinition(nextResource).getName();
			String bundleIdPart = nextResource.getId().getIdPart();
			if (isNotBlank(bundleIdPart)) {
				// if (isNotBlank(entryBaseUrl)) {
				// nextResource.setId(new IdDt(entryBaseUrl, resourceName, bundleIdPart, version));
				// } else {
				nextResource.setId(new IdDt(null, resourceName, bundleIdPart, version));
				// }
			}

		}

	}

	private class PreResourceStateHl7Org extends PreResourceState {

		private IMutator myMutator;
		private Object myTarget;

		public PreResourceStateHl7Org(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		public PreResourceStateHl7Org(Object theTarget, IMutator theMutator, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myMutator = theMutator;
			myTarget = theTarget;
		}

		@Override
		protected void populateTarget() {
			weaveContainedResources();
			if (myMutator != null) {
				myMutator.setValue(myTarget, getCurrentElement());
			}
		}

		@Override
		public void wereBack() {
			super.wereBack();

			if (getCurrentElement() instanceof IDomainResource) {
				IDomainResource elem = (IDomainResource) getCurrentElement();
				String resourceName = myContext.getResourceDefinition(elem).getName();
				String versionId = elem.getMeta().getVersionId();
				if (StringUtils.isBlank(elem.getIdElement().getIdPart())) {
					// Resource has no ID
				} else if (StringUtils.isNotBlank(versionId)) {
					elem.getIdElement().setValue(resourceName + "/" + elem.getIdElement().getIdPart() + "/_history/" + versionId);
				} else {
					elem.getIdElement().setValue(resourceName + "/" + elem.getIdElement().getIdPart());
				}
			}
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
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
				if ("".equals(theValue)) {
					myErrorHandler.invalidValue(null, theValue, "Attribute values must not be empty (\"\")");
				} else {
					try {
						myInstance.setValueAsString(theValue);
					} catch (DataFormatException e) {
						myErrorHandler.invalidValue(null, theValue, e.getMessage());
					} catch (IllegalArgumentException e) {
						myErrorHandler.invalidValue(null, theValue, e.getMessage());
					}
				}
			} else if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId(theValue);
				} else if (myInstance instanceof IBaseElement) {
					((IBaseElement) myInstance).setId(theValue);
				} else if (myInstance instanceof IBaseResource) {
					new IdDt(theValue).applyTo((org.hl7.fhir.instance.model.api.IBaseResource) myInstance);
				} else {
					myErrorHandler.unknownAttribute(null, theName);
				}
			} else {
				myErrorHandler.unknownAttribute(null, theName);
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			myErrorHandler.unknownElement(null, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
			return;
		}

		@Override
		protected IBase getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceStateHapi extends ElementCompositeState {

		private IResource myInstance;

		public ResourceStateHapi(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IResource theInstance) {
			super(thePreResourceState, theDef.getName(), theDef, theInstance);
			myInstance = theInstance;
		}

		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			if ("id".equals(theChildName)) {
				push(new PrimitiveState(getPreResourceState(), myInstance.getId()));
			} else if ("meta".equals(theChildName)) {
				push(new MetaElementState(getPreResourceState(), myInstance.getResourceMetadata()));
			} else {
				super.enteringNewElement(theNamespace, theChildName);
			}
		}
	}

	private class ResourceStateHl7Org extends ElementCompositeState {

		public ResourceStateHl7Org(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IBaseResource theInstance) {
			super(thePreResourceState, theDef.getName(), theDef, theInstance);
		}

	}

	private class SecurityLabelElementStateHapi extends ElementCompositeState {

		public SecurityLabelElementStateHapi(ParserState<T>.PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IBase codingDt) {
			super(thePreResourceState, theDef.getName(), theDef, codingDt);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

	}

	private class SwallowChildrenWholeState extends BaseState {

		private int myDepth;

		public SwallowChildrenWholeState(PreResourceState thePreResourceState) {
			super(thePreResourceState);
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			// ignore
		}

		@Override
		public void endingElement() throws DataFormatException {
			myDepth--;
			if (myDepth < 0) {
				pop();
			}
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			myDepth++;
		}

		@Override
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if (TagList.ATTR_CATEGORY.equals(theLocalPart)) {
				push(new TagState(myTagList));
			} else {
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}
		}

		@Override
		protected IBase getCurrentElement() {
			return myTagList;
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
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			/*
			 * We allow for both the DSTU1 and DSTU2 names here
			 */
			if (Tag.ATTR_TERM.equals(theLocalPart) || "code".equals(theLocalPart)) {
				mySubState = TERM;
			} else if (Tag.ATTR_SCHEME.equals(theLocalPart) || "system".equals(theLocalPart)) {
				mySubState = SCHEME;
			} else if (Tag.ATTR_LABEL.equals(theLocalPart) || "display".equals(theLocalPart)) {
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
			} else {				
				// IGNORE - don't handle this as an error, we process these as XML events
			}
		}

		protected void doPop() {
			pop();
		}

		@Override
		public void endingElement() throws DataFormatException {
			if (myJsonMode) {
				doPop();
				return;
			}
			super.endingElement();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			// IGNORE - don't handle this as an error, we process these as XML events
		}

		@Override
		protected IElement getCurrentElement() {
			return myDt;
		}

		public XhtmlDt getDt() {
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
					doPop();
				}
			}
		}

	}

	private class XhtmlStateHl7Org extends XhtmlState {
		private IBaseXhtml myHl7OrgDatatype;

		private XhtmlStateHl7Org(PreResourceState thePreResourceState, IBaseXhtml theHl7OrgDatatype) {
			super(thePreResourceState, new XhtmlDt(), true);
			myHl7OrgDatatype = theHl7OrgDatatype;
		}

		@Override
		public void doPop() {
			// TODO: this is not very efficient
			String value = getDt().getValueAsString();
			myHl7OrgDatatype.setValueAsString(value);

			super.doPop();
		}

	}

}
