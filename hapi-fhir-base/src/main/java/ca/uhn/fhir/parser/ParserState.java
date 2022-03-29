package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IMutator;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

class ParserState<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);
	private final FhirContext myContext;
	private final IParserErrorHandler myErrorHandler;
	private final boolean myJsonMode;
	private final IParser myParser;
	private List<String> myComments = new ArrayList<String>(2);
	private T myObject;
	private IBase myPreviousElement;
	private BaseState myState;
	private List<IBaseResource> myGlobalResources = new ArrayList<>();
	private List<IBaseReference> myGlobalReferences = new ArrayList<>();

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

	boolean elementIsRepeating(String theChildName) {
		return myState.elementIsRepeating(theChildName);
	}

	void endingElement() throws DataFormatException {
		myState.endingElement();
	}

	void enteringNewElement(String theNamespaceUri, String theName) throws DataFormatException {
		myState.enteringNewElement(theNamespaceUri, theName);
	}

	void enteringNewElementExtension(StartElement theElem, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
		myState.enteringNewElementExtension(theElem, theUrlAttr, theIsModifier, baseServerUrl);
	}

	public T getObject() {
		return myObject;
	}

	boolean isPreResource() {
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

	public void string(String theData) {
		myState.string(theData);
	}

	/**
	 * Invoked after any new XML event is individually processed, containing a copy of the XML event. This is basically
	 * intended for embedded XHTML content
	 */
	void xmlEvent(XMLEvent theNextEvent) {
		if (myState != null) {
			myState.xmlEvent(theNextEvent);
		}
	}

	public IBase newInstance(RuntimeChildDeclaredExtensionDefinition theDefinition) {
		return theDefinition.newInstance();
	}

	public ICompositeType newCompositeInstance(PreResourceState thePreResourceState, BaseRuntimeChildDefinition theChild, BaseRuntimeElementCompositeDefinition<?> theCompositeTarget) {
		ICompositeType retVal = (ICompositeType) theCompositeTarget.newInstance(theChild.getInstanceConstructorArguments());
		if (retVal instanceof IBaseReference) {
			IBaseReference ref = (IBaseReference) retVal;
			myGlobalReferences.add(ref);
			thePreResourceState.getLocalReferences().add(ref);
		}
		return retVal;
	}

	public ICompositeType newCompositeTypeInstance(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theCompositeTarget) {
		ICompositeType retVal = (ICompositeType) theCompositeTarget.newInstance();
		if (retVal instanceof IBaseReference) {
			IBaseReference ref = (IBaseReference) retVal;
			myGlobalReferences.add(ref);
			thePreResourceState.getLocalReferences().add(ref);
		}
		return retVal;
	}

	public IPrimitiveType<?> newPrimitiveInstance(RuntimeChildDeclaredExtensionDefinition theDefinition, RuntimePrimitiveDatatypeDefinition thePrimitiveTarget) {
		return thePrimitiveTarget.newInstance(theDefinition.getInstanceConstructorArguments());
	}

	public IPrimitiveType<?> getPrimitiveInstance(BaseRuntimeChildDefinition theChild, RuntimePrimitiveDatatypeDefinition thePrimitiveTarget, String theChildName) {
		return thePrimitiveTarget.newInstance(theChild.getInstanceConstructorArguments());
	}

	public IBaseXhtml newInstance(RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition theXhtmlTarget) {
		return theXhtmlTarget.newInstance();
	}

	public XhtmlDt newInstance(RuntimePrimitiveDatatypeNarrativeDefinition theXhtmlTarget) {
		return theXhtmlTarget.newInstance();
	}

	public IPrimitiveType<?> newInstance(RuntimePrimitiveDatatypeDefinition thePrimitiveTarget) {
		return thePrimitiveTarget.newInstance();
	}

	public IBaseResource newInstance(RuntimeResourceDefinition theDef) {
		IBaseResource retVal = theDef.newInstance();
		myGlobalResources.add(retVal);
		return retVal;
	}

	public IBase newInstance(RuntimeResourceBlockDefinition theBlockTarget) {
		return theBlockTarget.newInstance();
	}

	private abstract class BaseState {

		private PreResourceState myPreResourceState;
		private BaseState myStack;

		BaseState(PreResourceState thePreResourceState) {
			super();
			myPreResourceState = thePreResourceState;
		}

		/**
		 * @param theValue The attribute value
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
		 * @param theNamespaceUri The XML namespace (if XML) or null
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

		PreResourceState getPreResourceState() {
			return myPreResourceState;
		}

		public boolean isPreResource() {
			return false;
		}

		void logAndSwallowUnexpectedElement(String theLocalPart) {
			myErrorHandler.unknownElement(null, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
		}

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		/**
		 * @param theData The string value
		 */
		public void string(String theData) {
			// ignore by default
		}

		public void wereBack() {
			// allow an implementor to override
		}

		/**
		 * @param theNextEvent The XML event
		 */
		public void xmlEvent(XMLEvent theNextEvent) {
			// ignore
		}

	}

	private class ContainedResourcesStateHapi extends PreResourceState {

		public ContainedResourcesStateHapi(PreResourceState thePreResourcesState) {
			super(thePreResourcesState, thePreResourcesState.myInstance.getStructureFhirVersionEnum());
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

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
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
					ICompositeType newChildInstance = newCompositeInstance(getPreResourceState(), myDefinition, compositeTarget);
					myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
					ElementCompositeState newState = new ElementCompositeState(myPreResourceState, theLocalPart, compositeTarget, newChildInstance);
					push(newState);
					return;
				}
				case ID_DATATYPE:
				case PRIMITIVE_DATATYPE: {
					RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
					IPrimitiveType<?> newChildInstance = newPrimitiveInstance(myDefinition, primitiveTarget);
					myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
					PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance, theLocalPart, primitiveTarget.getName());
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
					myChildInstance = newInstance(myDefinition);
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

		private final BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private final IBase myInstance;
		private final Set<String> myParsedNonRepeatableNames = new HashSet<>();
		private final String myElementName;

		ElementCompositeState(PreResourceState thePreResourceState, String theElementName, BaseRuntimeElementCompositeDefinition<?> theDef, IBase theInstance) {
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
				}
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
				throw new DataFormatException(Msg.code(1809) + "Found unexpected element '" + theChildName + "' in parent element '" + myDefinition.getName() + "'. Valid names are: " + child.getValidChildNames());
			}

			switch (target.getChildType()) {
				case COMPOSITE_DATATYPE: {
					BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
					ICompositeType newChildInstance = newCompositeInstance(getPreResourceState(), child, compositeTarget);
					child.getMutator().addValue(myInstance, newChildInstance);
					ParserState<T>.ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theChildName, compositeTarget, newChildInstance);
					push(newState);
					return;
				}
				case ID_DATATYPE:
				case PRIMITIVE_DATATYPE: {
					RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
					IPrimitiveType<?> newChildInstance;
					newChildInstance = getPrimitiveInstance(child, primitiveTarget, theChildName);
					child.getMutator().addValue(myInstance, newChildInstance);
					PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance, theChildName, primitiveTarget.getName());
					push(newState);
					return;
				}
				case RESOURCE_BLOCK: {
					RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
					IBase newBlockInstance = newInstance(blockTarget);
					child.getMutator().addValue(myInstance, newBlockInstance);
					ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theChildName, blockTarget, newBlockInstance);
					push(newState);
					return;
				}
				case PRIMITIVE_XHTML: {
					RuntimePrimitiveDatatypeNarrativeDefinition xhtmlTarget = (RuntimePrimitiveDatatypeNarrativeDefinition) target;
					XhtmlDt newDt = newInstance(xhtmlTarget);
					child.getMutator().addValue(myInstance, newDt);
					XhtmlState state = new XhtmlState(getPreResourceState(), newDt, true);
					push(state);
					return;
				}
				case PRIMITIVE_XHTML_HL7ORG: {
					RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition xhtmlTarget = (RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition) target;
					IBaseXhtml newDt = newInstance(xhtmlTarget);
					child.getMutator().addValue(myInstance, newDt);
					XhtmlStateHl7Org state = new XhtmlStateHl7Org(getPreResourceState(), newDt);
					push(state);
					return;
				}
				case CONTAINED_RESOURCES: {
					List<? extends IBase> values = child.getAccessor().getValues(myInstance);
					if (values == null || values.isEmpty() || values.get(0) == null) {
						Object newDt = newContainedDt((IResource) getPreResourceState().myInstance);
						child.getMutator().addValue(myInstance, (IBase) newDt);
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
					if (myInstance instanceof IAnyResource || myInstance instanceof IBaseBackboneElement || myInstance instanceof IBaseElement) {
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

			throw new DataFormatException(Msg.code(1810) + "Illegal resource position: " + target.getChildType());
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

		private final IBaseElement myElement;

		ElementIdState(ParserState<T>.PreResourceState thePreResourceState, IBaseElement theElement) {
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

		private final IBaseExtension<?, ?> myExtension;

		ExtensionState(PreResourceState thePreResourceState, IBaseExtension<?, ?> theExtension) {
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
				throw new DataFormatException(Msg.code(1811) + "Extension (URL='" + myExtension.getUrl() + "') must not have both a value and other contained extensions");
			}
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			if (theLocalPart.equals("id")) {
				if (getCurrentElement() instanceof IBaseElement) {
					push(new ElementIdState(getPreResourceState(), (IBaseElement) getCurrentElement()));
					return;
				} else if (getCurrentElement() instanceof IIdentifiableElement) {
					push(new IdentifiableElementIdState(getPreResourceState(), (IIdentifiableElement) getCurrentElement()));
					return;
				}
			}

			BaseRuntimeElementDefinition<?> target = myContext.getRuntimeChildUndeclaredExtensionDefinition().getChildByName(theLocalPart);

			if (target != null) {
				switch (target.getChildType()) {
					case COMPOSITE_DATATYPE: {
						BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
						ICompositeType newChildInstance = newCompositeTypeInstance(getPreResourceState(), compositeTarget);
						myExtension.setValue(newChildInstance);
						ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), theLocalPart, compositeTarget, newChildInstance);
						push(newState);
						return;
					}
					case ID_DATATYPE:
					case PRIMITIVE_DATATYPE: {
						RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
						IPrimitiveType<?> newChildInstance = newInstance(primitiveTarget);
						myExtension.setValue(newChildInstance);
						PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance, theLocalPart, primitiveTarget.getName());
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
		}

		@Override
		protected IBaseExtension<?, ?> getCurrentElement() {
			return myExtension;
		}

	}

	public class IdentifiableElementIdState extends BaseState {

		private final IIdentifiableElement myElement;

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
		private final ResourceMetadataMap myMap;

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
			switch (theLocalPart) {
				case "versionId":
					push(new MetaVersionElementState(getPreResourceState(), myMap));
					// } else if (theLocalPart.equals("profile")) {
					//
					break;
				case "lastUpdated":
					InstantDt updated = new InstantDt();
					push(new PrimitiveState(getPreResourceState(), updated, theLocalPart, "instant"));
					myMap.put(ResourceMetadataKeyEnum.UPDATED, updated);
					break;
				case "security":
					@SuppressWarnings("unchecked")
					List<IBase> securityLabels = (List<IBase>) myMap.get(ResourceMetadataKeyEnum.SECURITY_LABELS);
					if (securityLabels == null) {
						securityLabels = new ArrayList<>();
						myMap.put(ResourceMetadataKeyEnum.SECURITY_LABELS, securityLabels);
					}
					IBase securityLabel = myContext.getVersion().newCodingDt();
					BaseRuntimeElementCompositeDefinition<?> codinfDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(securityLabel.getClass());
					push(new SecurityLabelElementStateHapi(getPreResourceState(), codinfDef, securityLabel));
					securityLabels.add(securityLabel);
					break;
				case "profile":
					@SuppressWarnings("unchecked")
					List<IdDt> profiles = (List<IdDt>) myMap.get(ResourceMetadataKeyEnum.PROFILES);
					List<IdDt> newProfiles;
					if (profiles != null) {
						newProfiles = new ArrayList<>(profiles.size() + 1);
						newProfiles.addAll(profiles);
					} else {
						newProfiles = new ArrayList<>(1);
					}
					IdDt profile = new IdDt();
					push(new PrimitiveState(getPreResourceState(), profile, theLocalPart, "id"));
					newProfiles.add(profile);
					myMap.put(ResourceMetadataKeyEnum.PROFILES, Collections.unmodifiableList(newProfiles));
					break;
				case "tag":
					TagList tagList = (TagList) myMap.get(ResourceMetadataKeyEnum.TAG_LIST);
					if (tagList == null) {
						tagList = new TagList();
						myMap.put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
					}
					push(new TagState(tagList));
					break;
				default:
					myErrorHandler.unknownElement(null, theLocalPart);
					push(new SwallowChildrenWholeState(getPreResourceState()));
			}
		}

		@Override
		public void enteringNewElementExtension(StartElement theElem, String theUrlAttr, boolean theIsModifier, final String baseServerUrl) {
			ResourceMetadataKeyEnum.ExtensionResourceMetadataKey resourceMetadataKeyEnum = new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey(theUrlAttr);
			Object metadataValue = myMap.get(resourceMetadataKeyEnum);
			ExtensionDt newExtension;
			if (metadataValue == null) {
				newExtension = new ExtensionDt(theIsModifier);
			} else if (metadataValue instanceof ExtensionDt) {
				newExtension = (ExtensionDt) metadataValue;
			} else {
				throw new IllegalStateException(Msg.code(1812) + "Expected ExtensionDt as custom resource metadata type, got: " + metadataValue.getClass().getSimpleName());
			}
			newExtension.setUrl(theUrlAttr);
			myMap.put(resourceMetadataKeyEnum, newExtension);

			ExtensionState newState = new ExtensionState(getPreResourceState(), newExtension);
			push(newState);
		}

	}

	private class MetaVersionElementState extends BaseState {

		private final ResourceMetadataMap myMap;

		MetaVersionElementState(ParserState<T>.PreResourceState thePreResourceState, ResourceMetadataMap theMap) {
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
		}

	}

	private abstract class PreResourceState extends BaseState {

		private Map<String, IBaseResource> myContainedResources;
		private List<IBaseReference> myLocalReferences = new ArrayList<>();
		private IBaseResource myInstance;
		private FhirVersionEnum myParentVersion;
		private Class<? extends IBaseResource> myResourceType;
		PreResourceState(Class<? extends IBaseResource> theResourceType) {
			super(null);
			myResourceType = theResourceType;
			myContainedResources = new HashMap<>();
			if (theResourceType != null) {
				myParentVersion = myContext.getResourceDefinition(theResourceType).getStructureVersion();
			} else {
				myParentVersion = myContext.getVersion().getVersion();
			}
		}

		PreResourceState(PreResourceState thePreResourcesState, FhirVersionEnum theParentVersion) {
			super(thePreResourcesState);
			Validate.notNull(theParentVersion);
			myParentVersion = theParentVersion;
			myContainedResources = thePreResourcesState.getContainedResources();
		}

		public List<IBaseReference> getLocalReferences() {
			return myLocalReferences;
		}

		@Override
		public void endingElement() throws DataFormatException {
			stitchBundleCrossReferences();
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			RuntimeResourceDefinition definition;
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
					throw new DataFormatException(Msg.code(1813) + "Element '" + theLocalPart + "' is not a known resource type, expected a resource at this position");
				}
			} else {
				definition = myContext.getResourceDefinition(myResourceType);
				if (!StringUtils.equals(theLocalPart, definition.getName())) {
					throw new DataFormatException(Msg.code(1814) + myContext.getLocalizer().getMessage(ParserState.class, "wrongResourceTypeFound", definition.getName(), theLocalPart));
				}
			}

			RuntimeResourceDefinition def = definition;
			if (!definition.getName().equals(theLocalPart) && definition.getName().equalsIgnoreCase(theLocalPart)) {
				throw new DataFormatException(Msg.code(1815) + "Unknown resource type '" + theLocalPart + "': Resource names are case sensitive, found similar name: '" + definition.getName() + "'");
			}
			myInstance = newInstance(def);

			if (myInstance instanceof IResource) {
				push(new ResourceStateHapi(getRootPreResourceState(), def, (IResource) myInstance, myContainedResources));
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
						ourLog.debug("Converting resource of type {} to type defined for profile \"{}\": {}", myInstance.getClass().getName(), usedProfile, wantedProfileType);

						/*
						 * This isn't the most efficient thing really.. If we want a specific
						 * type we just re-parse into that type. The problem is that we don't know
						 * until we've parsed the resource which type we want to use because the
						 * profile declarations are in the text of the resource itself.
						 *
						 * At some point it would be good to write code which can present a view
						 * of one type backed by another type and use that.
						 */
						FhirTerser t = myContext.newTerser();

						// Clean up the cached resources
						myGlobalResources.remove(myInstance);
						myGlobalReferences.removeAll(t.getAllPopulatedChildElementsOfType(myInstance, IBaseReference.class));

						IParser parser = myContext.newJsonParser();
						String asString = parser.encodeResourceToString(myInstance);
						myInstance = parser.parseResource(wantedProfileType, asString);

						// Add newly created instance
						myGlobalResources.add(myInstance);
						myGlobalReferences.addAll(t.getAllPopulatedChildElementsOfType(myInstance, IBaseReference.class));
					}
				}
			}

			myInstance.setUserData(BaseParser.RESOURCE_CREATED_BY_PARSER, Boolean.TRUE);

			populateTarget();
		}

		private void stitchBundleCrossReferences() {
			final boolean bundle = "Bundle".equals(myContext.getResourceType(myInstance));
			if (bundle) {

				FhirTerser t = myContext.newTerser();

				Map<String, IBaseResource> idToResource = new HashMap<>();
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

				/*
				 * Stitch together resource references
				 */
				for (IBaseResource next : myGlobalResources) {
					IIdType id = next.getIdElement();
					if (id != null && !id.isEmpty()) {
						String resName = myContext.getResourceType(next);
						IIdType idType = id.withResourceType(resName).toUnqualifiedVersionless();
						idToResource.put(idType.getValueAsString(), next);
					}
				}

				for (IBaseReference nextRef : myGlobalReferences) {
					if (!nextRef.isEmpty() && nextRef.getReferenceElement() != null) {
						IIdType unqualifiedVersionless = nextRef.getReferenceElement().toUnqualifiedVersionless();
						IBaseResource target = idToResource.get(unqualifiedVersionless.getValueAsString());
						// resource can already be filled with local contained resource by populateTarget()
						if (target != null && nextRef.getResource() == null) {
							nextRef.setResource(target);
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

		void weaveContainedResources() {
			for (IBaseReference nextRef : myLocalReferences) {
				String ref = nextRef.getReferenceElement().getValue();
				if (isNotBlank(ref)) {
					if (ref.startsWith("#") && ref.length() > 1) {
						IBaseResource target = myContainedResources.get(ref);
						if (target != null) {
							ourLog.debug("Resource contains local ref {}", ref);
							nextRef.setResource(target);
						} else {
							myErrorHandler.unknownReference(null, ref);
						}
					}
				}
			}

		}

		@Override
		public void wereBack() {
			postProcess();
		}

	}

	private class PreResourceStateHapi extends PreResourceState {
		private IMutator myMutator;
		private IBase myTarget;


		PreResourceStateHapi(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			assert theResourceType == null || IResource.class.isAssignableFrom(theResourceType);
		}

		PreResourceStateHapi(IBase theTarget, IMutator theMutator, Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
			myTarget = theTarget;
			myMutator = theMutator;
			assert theResourceType == null || IResource.class.isAssignableFrom(theResourceType);
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

			IResource nextResource = (IResource) getCurrentElement();
			String version = ResourceMetadataKeyEnum.VERSION.get(nextResource);
			String resourceName = myContext.getResourceType(nextResource);
			String bundleIdPart = nextResource.getId().getIdPart();
			if (isNotBlank(bundleIdPart)) {
				// if (isNotBlank(entryBaseUrl)) {
				// nextResource.setId(new IdDt(entryBaseUrl, resourceName, bundleIdPart, version));
				// } else {
				IdDt previousId = nextResource.getId();
				nextResource.setId(new IdDt(null, resourceName, bundleIdPart, version));
				// Copy extensions
				if (!previousId.getAllUndeclaredExtensions().isEmpty()) {
					for (final ExtensionDt ext : previousId.getAllUndeclaredExtensions()) {
						nextResource.getId().addUndeclaredExtension(ext);
					}
				}
				// }
			}
		}

	}

	private class PreResourceStateHl7Org extends PreResourceState {

		private IMutator myMutator;
		private IBase myTarget;

		PreResourceStateHl7Org(Class<? extends IBaseResource> theResourceType) {
			super(theResourceType);
		}

		PreResourceStateHl7Org(IBase theTarget, IMutator theMutator, Class<? extends IBaseResource> theResourceType) {
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

			if (getCurrentElement() instanceof IBaseResource) {
				IBaseResource elem = (IBaseResource) getCurrentElement();
				String resourceName = myContext.getResourceType(elem);
				String versionId = elem.getMeta().getVersionId();
				if (StringUtils.isBlank(elem.getIdElement().getIdPart())) {
					// Resource has no ID
				} else if (!elem.getIdElement().getIdPart().startsWith("urn:")) {
					if (StringUtils.isNotBlank(versionId)) {
						elem.getIdElement().setValue(resourceName + "/" + elem.getIdElement().getIdPart() + "/_history/" + versionId);
					} else {
						elem.getIdElement().setValue(resourceName + "/" + elem.getIdElement().getIdPart());
					}
				}
			}
		}

	}

	private class PreTagListState extends BaseState {

		private TagList myTagList;

		PreTagListState() {
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
				throw new DataFormatException(Msg.code(1816) + "resourceType does not appear to be 'TagList', found: " + theLocalPart);
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
		private final String myChildName;
		private final String myTypeName;
		private IPrimitiveType<?> myInstance;

		PrimitiveState(PreResourceState thePreResourceState, IPrimitiveType<?> theInstance, String theChildName, String theTypeName) {
			super(thePreResourceState);
			myInstance = theInstance;
			myChildName = theChildName;
			myTypeName = theTypeName;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			String value = theValue;
			if ("value".equals(theName)) {
				if ("".equals(value)) {
					ParseLocation location = ParseLocation.fromElementName(myChildName);
					myErrorHandler.invalidValue(location, value, "Attribute value must not be empty (\"\")");
				} else {

					/*
					 * It may be possible to clean this up somewhat once the following PR is hopefully merged:
					 * https://github.com/FasterXML/jackson-core/pull/611
					 *
					 * See TolerantJsonParser
					 */
					if ("decimal".equals(myTypeName)) {
						if (value != null)
							if (value.startsWith(".") && NumberUtils.isDigits(value.substring(1))) {
								value = "0" + value;
							} else {
								while (value.startsWith("00")) {
									value = value.substring(1);
								}
							}
					}

					try {
						myInstance.setValueAsString(value);
					} catch (DataFormatException | IllegalArgumentException e) {
						ParseLocation location = ParseLocation.fromElementName(myChildName);
						myErrorHandler.invalidValue(location, value, e.getMessage());
					}
				}
			} else if ("id".equals(theName)) {
				if (myInstance instanceof IIdentifiableElement) {
					((IIdentifiableElement) myInstance).setElementSpecificId(value);
				} else if (myInstance instanceof IBaseElement) {
					((IBaseElement) myInstance).setId(value);
				} else if (myInstance instanceof IBaseResource) {
					new IdDt(value).applyTo((org.hl7.fhir.instance.model.api.IBaseResource) myInstance);
				} else {
					ParseLocation location = ParseLocation.fromElementName(myChildName);
					myErrorHandler.unknownAttribute(location, theName);
				}
			} else {
				super.attributeValue(theName, value);
			}
		}

		@Override
		public void endingElement() {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceUri, String theLocalPart) throws DataFormatException {
			super.enteringNewElement(theNamespaceUri, theLocalPart);
			push(new SwallowChildrenWholeState(getPreResourceState()));
		}

		@Override
		protected IBase getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceStateHapi extends ElementCompositeState {

		private IResource myInstance;

		public ResourceStateHapi(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IResource theInstance, Map<String, IBaseResource> theContainedResources) {
			super(thePreResourceState, theDef.getName(), theDef, theInstance);
			myInstance = theInstance;
		}

		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			if ("id".equals(theChildName)) {
				push(new PrimitiveState(getPreResourceState(), myInstance.getId(), theChildName, "id"));
			} else if ("meta".equals(theChildName)) {
				push(new MetaElementState(getPreResourceState(), myInstance.getResourceMetadata()));
			} else {
				super.enteringNewElement(theNamespace, theChildName);
			}
		}
	}

	private class ResourceStateHl7Org extends ElementCompositeState {

		ResourceStateHl7Org(PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IBaseResource theInstance) {
			super(thePreResourceState, theDef.getName(), theDef, theInstance);
		}

	}

	private class SecurityLabelElementStateHapi extends ElementCompositeState {

		SecurityLabelElementStateHapi(ParserState<T>.PreResourceState thePreResourceState, BaseRuntimeElementCompositeDefinition<?> theDef, IBase codingDt) {
			super(thePreResourceState, theDef.getName(), theDef, codingDt);
		}

		@Override
		public void endingElement() throws DataFormatException {
			pop();
		}

	}

	private class SwallowChildrenWholeState extends BaseState {

		private int myDepth;

		SwallowChildrenWholeState(PreResourceState thePreResourceState) {
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
				throw new DataFormatException(Msg.code(1817) + "Unexpected element: " + theLocalPart);
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
				throw new DataFormatException(Msg.code(1818) + "Unexpected element: " + theLocalPart);
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
					String eventsAsString = XmlUtil.encode(myEvents);
					myDt.setValue(eventsAsString);
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

	/**
	 * @param theResourceType May be null
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

}
