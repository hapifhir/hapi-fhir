package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import com.ctc.wstx.sw.BaseStreamWriter;

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
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

class ParserState<T extends IElement> {

	public class AtomAuthorState extends BaseState {

		private Bundle myInstance;

		public AtomAuthorState(Bundle theInstance) {
			myInstance = theInstance;
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

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);

	private FhirContext myContext;
	private T myObject;
	private BaseState myState;

	private ParserState(FhirContext theContext) {
		myContext = theContext;
	}

	public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
		myState.attributeValue(theAttribute, theValue);
	}

	public void endingElement(EndElement theElem) throws DataFormatException {
		myState.endingElement(theElem);
	}

	public void enteringNewElement(String theNamespaceURI, String theName) throws DataFormatException {
		myState.enteringNewElement(theNamespaceURI, theName);
	}

	public void enteringNewElementExtension(StartElement theElem, String theUrlAttr) {
		myState.enteringNewElementExtension(theElem, theUrlAttr);
	}

	public T getObject() {
		return myObject;
	}

	public boolean isComplete() {
		return myObject != null;
	}

	private void pop() {
		myState = myState.myStack;
		myState.wereBack();
	}

	private void push(BaseState theState) {
		theState.setStack(myState);
		myState = theState;
	}

	public static ParserState<IResource> getPreResourceInstance(FhirContext theContext) throws DataFormatException {
		ParserState<IResource> retVal = new ParserState<IResource>(theContext);
		retVal.push(retVal.new PreResourceState());
		return retVal;
	}

	public static ParserState<Bundle> getPreAtomInstance(FhirContext theContext) throws DataFormatException {
		ParserState<Bundle> retVal = new ParserState<Bundle>(theContext);
		retVal.push(retVal.new PreAtomState());
		return retVal;
	}

	private class AtomPrimitiveState extends BaseState {

		private IPrimitiveDatatype<?> myPrimitive;
		private String myData;

		public AtomPrimitiveState(IPrimitiveDatatype<?> thePrimitive) {
			myPrimitive = thePrimitive;
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			myPrimitive.setValueAsString(myData);
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Unexpected nested element in atom tag ");
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
				// this shouldn't generally happen so it's ok that it's inefficient
				myData = myData + theData;
			}
		}

	}

	private class AtomLinkState extends BaseState {

		private String myRel;
		private String myHref;
		private Bundle myInstance;

		public AtomLinkState(Bundle theInstance) {
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			String name = theAttribute.getName().getLocalPart();
			if ("rel".equals(name)) {
				myRel = theValue;
			} else if ("href".equals(name)) {
				myHref = theValue;
			}
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
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

			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			throw new DataFormatException("Found unexpected element content");
		}

	}

	private static final QName ATOM_LINK_REL_ATTRIBUTE = new QName("rel");
	private static final QName ATOM_LINK_HREF_ATTRIBUTE = new QName("href");

	private class AtomState extends BaseState {

		private Bundle myInstance;

		public AtomState(Bundle theInstance) {
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			// TODO Auto-generated method stub

		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			pop();
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceURI)) {
				
			} else if (theLocalPart.equals("title")) {
				push(new AtomPrimitiveState(myInstance.getTitle()));
			} else if ("id".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getId()));
			} else if ("link".equals(theLocalPart)) {
				push(new AtomLinkState(myInstance));
			} else if ("totalresults".equals(theLocalPart) && verifyNamespace(XmlParser.OPENSEARCH_NS, theNamespaceURI)) {
				push(new AtomPrimitiveState(myInstance.getTotalResults()));
			} else if ("updated".equals(theLocalPart)) {
				push(new AtomPrimitiveState(myInstance.getUpdated()));
			} else if ("author".equals(theLocalPart)) {
				push(new AtomAuthorState(myInstance));
			}

			// TODO: handle category and DSig
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

	}

	private class PreAtomState extends BaseState {

		private Bundle myInstance;

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			// ignore
		}

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

		@Override
		protected IElement getCurrentElement() {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myObject = (T) myInstance;
		}

	}

	private abstract class BaseState {

		private BaseState myStack;

		@SuppressWarnings("unused")
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
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
		public void enteringNewElementExtension(@SuppressWarnings("unused") StartElement theElement, String theUrlAttr) {
			if (getCurrentElement() instanceof ISupportsUndeclaredExtensions) {
				UndeclaredExtension newExtension = new UndeclaredExtension();
				newExtension.setUrl(theUrlAttr);
				// TODO: fail if we don't support undeclared extensions
				((ISupportsUndeclaredExtensions) getCurrentElement()).getUndeclaredExtensions().add(newExtension);
				ExtensionState newState = new ExtensionState(newExtension);
				push(newState);
			}
		}

		protected IElement getCurrentElement() {
			return null;
		}

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		public void wereBack() {
			// allow an implementor to override
		}

		public void string(@SuppressWarnings("unused") String theData) {
			// ignore by default
		}

		public void xmlEvent(@SuppressWarnings("unused") XMLEvent theNextEvent) {
			// ignore
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
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			throw new DataFormatException("'value' attribute is invalid in 'extension' element");
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
				ResourceReference newChildInstance = new ResourceReference();
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
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getChildExtensionForUrl(theUrlAttr);
			if (declaredExtension != null) {
				if (myChildInstance == null) {
					myChildInstance = myDefinition.newInstance();
					myDefinition.getMutator().addValue(myParentInstance, myChildInstance);
				}
				BaseState newState = new DeclaredExtensionState(declaredExtension, myChildInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr);
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

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) {
			ourLog.debug("Ignoring attribute value: {}", theValue);
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
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
				child.getMutator().addValue(myInstance, newChildInstance);
				ElementCompositeState newState = new ElementCompositeState(compositeTarget, newChildInstance);
				push(newState);
				return;
			}
			case PRIMITIVE_DATATYPE: {
				RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
				IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
				child.getMutator().addValue(myInstance, newChildInstance);
				PrimitiveState newState = new PrimitiveState(newChildInstance);
				push(newState);
				return;
			}
			case RESOURCE_REF: {
				RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
				ResourceReference newChildInstance = new ResourceReference();
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
				XhtmlState state = new XhtmlState(newDt);
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
		public void enteringNewElementExtension(StartElement theElement, String theUrlAttr) {
			RuntimeChildDeclaredExtensionDefinition declaredExtension = myDefinition.getDeclaredExtension(theUrlAttr);
			if (declaredExtension != null) {
				BaseState newState = new DeclaredExtensionState(declaredExtension, myInstance);
				push(newState);
			} else {
				super.enteringNewElementExtension(theElement, theUrlAttr);
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
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			throw new DataFormatException("'value' attribute is invalid in 'extension' element");
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
				ResourceReference newChildInstance = new ResourceReference();
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

	private class PreResourceState extends BaseState {

		private ICompositeElement myInstance;

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			// ignore
		}

		@Override
		public void endingElement(EndElement theElem) throws DataFormatException {
			// ignore
		}

		@Override
		public void enteringNewElement(String theNamespaceURI, String theLocalPart) throws DataFormatException {
			BaseRuntimeElementDefinition<?> definition = myContext.getNameToResourceDefinition().get(theLocalPart);
			if (!(definition instanceof RuntimeResourceDefinition)) {
				throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
			}

			RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
			myInstance = def.newInstance();

			push(new ElementCompositeState(def, myInstance));
		}

		@Override
		protected IElement getCurrentElement() {
			return myInstance;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void wereBack() {
			myObject = (T) myInstance;
		}

	}

	private class PrimitiveState extends BaseState {
		private IPrimitiveDatatype<?> myInstance;

		public PrimitiveState(IPrimitiveDatatype<?> theInstance) {
			super();
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
			myInstance.setValueAsString(theValue);
		}

		@Override
		public void endingElement(EndElement theElem) {
			pop();
		}

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
		private ResourceReference myInstance;
		private ResourceReferenceSubState mySubState;

		public ResourceReferenceState(RuntimeResourceReferenceDefinition theDefinition, ResourceReference theInstance) {
			myDefinition = theDefinition;
			myInstance = theInstance;
			mySubState = ResourceReferenceSubState.INITIAL;
		}

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
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

		private XhtmlState(XhtmlDt theXhtmlDt) throws DataFormatException {
			myDepth = 1;
			myDt = theXhtmlDt;
		}

		@Override
		protected IElement getCurrentElement() {
			return myDt;
		}

		@Override
		public void xmlEvent(XMLEvent theEvent) {
			myEvents.add(theEvent);

			if (theEvent.isStartElement()) {
				myDepth++;
			}
			if (theEvent.isEndElement()) {
				myDepth--;
				if (myDepth == 0) {
					myDt.setValue(myEvents);
					pop();
				}
			}
		}

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

}
