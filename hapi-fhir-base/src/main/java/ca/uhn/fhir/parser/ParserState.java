package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceReferenceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleCategory;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.BaseResourceReference;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

class ParserState<T extends IElement> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);
	private FhirContext myContext;
	private T myObject;

	private BaseState myState;

	private ParserState(FhirContext theContext) {
		myContext = theContext;
	}

	public void attributeValue(String theName, String theValue) throws DataFormatException {
		myState.attributeValue(theName, theValue);
	}

	public void endingElement(EndElement theElem) throws DataFormatException {
		myState.endingElement(theElem);
	}

	public void enteringNewElement(String theNamespaceURI, String theName) throws DataFormatException {
		myState.enteringNewElement(theNamespaceURI, theName);
	}

	public void enteringNewElementExtension(StartElement theElem, String theUrlAttr, boolean theIsModifier) {
		myState.enteringNewElementExtension(theElem, theUrlAttr, theIsModifier);
	}

	public T getObject() {
		return myObject;
	}

	public boolean isComplete() {
		return myObject != null;
	}

	public void string(String theData) {
		myState.string(theData);
	}

	public boolean verifyNamespace(String theExpect, String theActual) {
		return StringUtils.equals(theExpect, theActual);
	}

	/**
	 * Invoked after any new XML event is individually processed, containing a copy of the XML event. This is basically intended for embedded XHTML content
	 */
	public void xmlEvent(XMLEvent theNextEvent) {
		myState.xmlEvent(theNextEvent);
	}

	private void pop() {
		myState = myState.myStack;
		myState.wereBack();
	}

	private void push(BaseState theState) {
		theState.setStack(myState);
		myState = theState;
	}

	public static ParserState<Bundle> getPreAtomInstance(FhirContext theContext) throws DataFormatException {
		ParserState<Bundle> retVal = new ParserState<Bundle>(theContext);
		retVal.push(retVal.new PreAtomState());
		return retVal;
	}

	/**
	 * @param theResourceType
	 *            May be null
	 */
	public static ParserState<IResource> getPreResourceInstance(Class<? extends IResource> theResourceType, FhirContext theContext) throws DataFormatException {
		ParserState<IResource> retVal = new ParserState<IResource>(theContext);
		retVal.push(retVal.new PreResourceState(theResourceType));
		return retVal;
	}

	public class AtomAuthorState extends BaseState {

		private BaseBundle myInstance;

		public AtomAuthorState(BaseBundle theEntry) {
			myInstance = theEntry;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
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

		private BundleCategory myInstance;

		public AtomCategoryState(BundleCategory theEntry) {
			myInstance = theEntry;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected element: " + theLocalPart);
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("term".equals(theName)) {
				myInstance.setTerm(theValue);
			} else if ("label".equals(theName)) {
				myInstance.setLabel(theValue);
			} else if ("scheme".equals(theName)) {
				myInstance.setScheme(theValue);
			}
		}

	}

	public class AtomEntryState extends BaseState {

		private BundleEntry myEntry;

		public AtomEntryState(Bundle theInstance) {
			myEntry = new BundleEntry();
			theInstance.getEntries().add(myEntry);
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			pop();
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
				push(new PreResourceState(myEntry));
			} else if ("summary".equals(theLocalPart)) {
				push(new XhtmlState(myEntry.getSummary(), false));
			} else if ("category".equals(theLocalPart)) {
				push(new AtomCategoryState(myEntry.addCategory()));
			} else {
				throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
			}

			// TODO: handle category
		}

	}

	private class AtomLinkState extends BaseState {

		private BundleEntry myEntry;
		private String myHref;
		private Bundle myInstance;
		private String myRel;

		public AtomLinkState(Bundle theInstance) {
			myInstance = theInstance;
		}

		public AtomLinkState(BundleEntry theEntry) {
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
		public void endingElement(EndElement theElem) throws DataFormatException {
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
				myEntry.getLinkSelf().setValueAsString(myHref);
			}
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Found unexpected element content");
		}

	}

	private class AtomPrimitiveState extends BaseState {

		private String myData;
		private IPrimitiveDatatype<?> myPrimitive;

		public AtomPrimitiveState(IPrimitiveDatatype<?> thePrimitive) {
			assert thePrimitive != null;
			myPrimitive = thePrimitive;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			myPrimitive.setValueAsString(myData);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
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

		@Override
		protected IElement getCurrentElement() {
			return null;
		}

	}

	private class AtomState extends BaseState {

		private Bundle myInstance;

		public AtomState(Bundle theInstance) {
			myInstance = theInstance;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceURI)) {
				push(new AtomEntryState(myInstance));
			} else if (theLocalPart.equals("published")) {
				push(new AtomPrimitiveState(myInstance.getPublished()));
			} else if (theLocalPart.equals("title")) {
				push(new AtomPrimitiveState(myInstance.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myInstance));
			} else if ("totalResults".equals(theLocalPart) && verifyNamespace(XmlParser.OPENSEARCH_NS, theNamespaceURI)) {
				push(new AtomPrimitiveState(myInstance.getTotalResults()));
			} else if ("updated".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getUpdated()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myInstance));
			} else {
				throw new DataFormatException("Unexpected element: " + theLocalPart);
			}

			// TODO: handle category and DSig
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private abstract class BaseState {

		private BaseState myStack;

		@SuppressWarnings("unused")
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			// ignore by default
		}

		@SuppressWarnings("unused")
		public void endingElement(EndElement theElem) throws DataFormatException {
			// ignore by default
		}

		@SuppressWarnings("unused")
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			// ignore by default
		}

		/**
		 * Default implementation just handles undeclared extensions
		 */
		public void enteringNewElementExtension(@SuppressWarnings("unused") StartElement theElement, String theUrlAttr, boolean theIsModifier) {
			if (getCurrentElement() instanceof ISupportsUndeclaredExtensions) {
				UndeclaredExtension newExtension = new UndeclaredExtension(theIsModifier, theUrlAttr);
				ISupportsUndeclaredExtensions elem = (ISupportsUndeclaredExtensions) getCurrentElement();
				if (theIsModifier) {
					elem.getUndeclaredModifierExtensions().add(newExtension);
				} else {
					elem.getUndeclaredExtensions().add(newExtension);
				}
				ExtensionState newState = new ExtensionState(newExtension);
				push(newState);
			} else {
				throw new DataFormatException("Type " + getCurrentElement() + " does not support undeclared extentions, and found an extension with URL: " + theUrlAttr);
			}
		}

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		public void string(@SuppressWarnings("unused") String theData) {
			// ignore by default
		}

		public void wereBack() {
			// allow an implementor to override
		}

		public void xmlEvent(@SuppressWarnings("unused") XMLEvent theNextEvent) {
			// ignore
		}

		protected IElement getCurrentElement() {
			return null;
		}

	}

	private class DeclaredExtensionState extends BaseState {

		private IElement myChildInstance;
		private RuntimeChildDeclaredExtensionDefinition myDefinition;
		private IElement myParentInstance;

		public DeclaredExtensionState(RuntimeChildDeclaredExtensionDefinition theDefinition, IElement theParentInstance) {
			myDefinition = theDefinition;
			myParentInstance = theParentInstance;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
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
				ElementCompositeState newState = new ElementCompositeState(compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				myDefinition.getMutator().addValue(myParentInstance, newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(resourceRefTarget, newChildInstance);
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
				BaseState newState = new DeclaredExtensionState(declaredExtension, myChildInstance);
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

		public ElementCompositeState(BaseRuntimeElementCompositeDefinition<?> theDef, ICompositeElement theInstance) {
			myDefinition = theDef;
			myInstance = theInstance;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void endingElement(EndElement theElem) {
			pop();
			if (myState == null) {
				myObject = (T) myInstance;
			}
		}

		@Override
		public void enteringNewElement(String theNamespace, String theChildName) throws DataFormatException {
			BaseRuntimeChildDefinition child = myDefinition.getChildByNameOrThrowDataFormatException(theChildName);
			BaseRuntimeElementDefinition<?> target = child.getChildByName(theChildName);
			if (target == null) {
				throw new DataFormatException("Found unexpected element '" + theChildName + "' in parent element '" + myDefinition.getName() + "'. Valid names are: " + child.getValidChildNames());
			}

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				ElementCompositeState newState = new ElementCompositeState(compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance;
				newChildInstance = primitiveTarget.newInstance(child.getInstanceConstructorArguments());
				child.getMutator().addValue(myInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				child.getMutator().addValue(myInstance, newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(resourceRefTarget, newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_BLOCK: {
				RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
				IResourceBlock newBlockInstance = blockTarget.newInstance();
				child.getMutator().addValue(myInstance, newBlockInstance);
				ElementCompositeState newState = new ElementCompositeState(blockTarget, newBlockInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_XHTML: {
				RuntimePrimitiveDatatypeNarrativeDefinition xhtmlTarget = (RuntimePrimitiveDatatypeNarrativeDefinition) target;
				XhtmlDt newDt = xhtmlTarget.newInstance();
				child.getMutator().addValue(myInstance, newDt);
				XhtmlState state = new XhtmlState(newDt, true);
				push(state);
				return;
			}
			case UNDECL_EXT:
			case RESOURCE: {
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
				BaseState newState = new DeclaredExtensionState(declaredExtension, myInstance);
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

		private UndeclaredExtension myExtension;

		public ExtensionState(UndeclaredExtension theExtension) {
			myExtension = theExtension;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
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
				ElementCompositeState newState = new ElementCompositeState(compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
				myExtension.setValue(newChildInstance);
				PrimitiveState newState = new PrimitiveState(newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
				ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
				myExtension.setValue(newChildInstance);
				ResourceReferenceState newState = new ResourceReferenceState(resourceRefTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_XHTML:
			case RESOURCE:
			case RESOURCE_BLOCK:
			case UNDECL_EXT:
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

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			// ignore
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if (!"feed".equals(theLocalPart)) {
				throw new DataFormatException("Expecting outer element called 'feed', found: " + theLocalPart);
			}

			myInstance = new Bundle();
			push(new AtomState(myInstance));

		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myObject = (T) myInstance;
		}

		@Override
		protected IElement getCurrentElement() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private class PreResourceState extends BaseState {

		private IResource myInstance;
		private BundleEntry myEntry;
		private Class<? extends IResource> myResourceType;

		/**
		 * @param theResourceType
		 *            May be null
		 */
		public PreResourceState(Class<? extends IResource> theResourceType) {
			myResourceType = theResourceType;
		}

		public PreResourceState(BundleEntry theEntry) {
			myEntry = theEntry;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> definition;
			if (myResourceType == null) {
				definition = myContext.getResourceDefinition(theLocalPart);
				if (!(definition instanceof RuntimeResourceDefinition)) {
					throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
				}
			} else {
				definition = myContext.getResourceDefinition(myResourceType);
				if (!StringUtils.equals(theLocalPart, definition.getName())) {
					throw new DataFormatException("Incorrect resource root element '" + theLocalPart + "', expected: '" + definition.getName() + "'");
				}
			}

			RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
			myInstance = def.newInstance();
			if (myEntry != null) {
				myEntry.setResource(myInstance);
			}

			push(new ElementCompositeState(def, myInstance));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			if (myEntry == null) {
				myObject = (T) myInstance;
			}
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class PrimitiveState extends BaseState {
		private IPrimitiveDatatype<?> myInstance;

		public PrimitiveState(IPrimitiveDatatype<?> theInstance) {
			super();
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theName, String theValue) throws DataFormatException {
			if ("value".equals(theName)) {
				myInstance.setValueAsString(theValue);
			}
		}

		@Override
		public void endingElement(EndElement theElem) {
			pop();
		}

		// @Override
		// public void enteringNewElementExtension(StartElement theElement, String theUrlAttr) {
		// if (myInstance instanceof ISupportsUndeclaredExtensions) {
		// UndeclaredExtension ext = new UndeclaredExtension(theUrlAttr);
		// ((ISupportsUndeclaredExtensions) myInstance).getUndeclaredExtensions().add(ext);
		// push(new ExtensionState(ext));
		// }
		// }

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new Error("?? can this happen?"); // TODO: can this happen?
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class ResourceReferenceState extends BaseState {

		private RuntimeResourceReferenceDefinition myDefinition;
		private ResourceReferenceDt myInstance;
		private ResourceReferenceSubState mySubState;

		public ResourceReferenceState(RuntimeResourceReferenceDefinition theDefinition, ResourceReferenceDt theInstance) {
			myDefinition = theDefinition;
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
		public void endingElement(EndElement theElement) {
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

	private class XhtmlState extends BaseState {
		private int myDepth;
		private XhtmlDt myDt;
		private List<XMLEvent> myEvents = new ArrayList<XMLEvent>();
		private boolean myIncludeOuterEvent;

		private XhtmlState(XhtmlDt theXhtmlDt, boolean theIncludeOuterEvent) throws DataFormatException {
			myDepth = 0;
			myDt = theXhtmlDt;
			myIncludeOuterEvent = theIncludeOuterEvent;
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

		@Override
		protected IElement getCurrentElement() {
			return myDt;
		}

	}

}
