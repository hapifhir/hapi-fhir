package ca.uhn.fhir.parser;

import java.io.StringWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceReferenceDefinition;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ResourceReference;

class ParserState {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);

	private FhirContext myContext;

	private Object myObject;
	private BaseState myState;

	public ParserState(FhirContext theContext) {
		myContext = theContext;
	}

	public void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException {
		myState.attributeValue(theAttribute, theValue);
	}

	public void endingElement(EndElement theElem) throws DataFormatException {
		myState.endingElement(theElem);
	}

	public void enteringNewElement(StartElement theElement, String theName) throws DataFormatException {
		myState.enteringNewElement(theElement, theName);
	}

	public Object getObject() {
		return myObject;
	}

	public boolean isComplete() {
		return myObject != null;
	}

	private void pop() {
		myState = myState.myStack;
	}

	private void push(BaseState theState) {
		theState.setStack(myState);
		myState = theState;
	}

	private void setState(BaseState theState) {
		myState = theState;
	}

	public static ParserState getResourceInstance(FhirContext theContext, String theLocalPart) throws DataFormatException {
		BaseRuntimeElementDefinition<?> definition = theContext.getNameToResourceDefinition().get(theLocalPart);
		if (!(definition instanceof RuntimeResourceDefinition)) {
			throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
		}

		RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
		IResource instance = def.newInstance();

		ParserState retVal = new ParserState(theContext);
		retVal.setState(retVal.new ContainerState(def, instance));

		return retVal;
	}

	private abstract class BaseState {
		private BaseState myStack;

		public abstract void attributeValue(Attribute theAttribute, String theValue) throws DataFormatException;

		public abstract void endingElement(EndElement theElem) throws DataFormatException;

		public abstract void enteringNewElement(StartElement theElement, String theLocalPart) throws DataFormatException;

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		public abstract void otherEvent(XMLEvent theEvent);

	}

	private class ContainerState extends BaseState {

		private BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private ICompositeElement myInstance;

		public ContainerState(BaseRuntimeElementCompositeDefinition<?> theDef, ICompositeElement theInstance) {
			myDefinition = theDef;
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(Attribute theAttribute, String theValue) {
			ourLog.debug("Ignoring attribute value: {}", theValue);
		}

		@Override
		public void endingElement(EndElement theElem) {
			pop();
			if (myState == null) {
				myObject = myInstance;
			}
		}

		@Override
		public void enteringNewElement(StartElement theElement, String theChildName) throws DataFormatException {
			BaseRuntimeChildDefinition child = myDefinition.getChildByNameOrThrowDataFormatException(theChildName);
			BaseRuntimeElementDefinition<?> target = child.getChildByName(theChildName);

			switch (target.getChildType()) {
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
				ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
				child.getMutator().addValue(myInstance, newChildInstance);
				ContainerState newState = new ContainerState(compositeTarget, newChildInstance);
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
				ContainerState newState = new ContainerState(blockTarget, newBlockInstance);
				push(newState);
				return;
			}
			case RESOURCE: {
				// Throw an exception because this shouldn't happen here
				break;
			}
			case PRIMITIVE_XHTML: {

			}
			}

			throw new DataFormatException("Illegal resource position: " + target.getChildType());
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
		public void enteringNewElement(StartElement theElement, String theLocalPart) throws DataFormatException {
			throw new Error("?? can this happen?"); // TODO: can this happen?
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
		public void enteringNewElement(StartElement theElem, String theLocalPart) throws DataFormatException {
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

	}

	private enum ResourceReferenceSubState {
		DISPLAY, INITIAL, REFERENCE
	}

	private class XhtmlState extends BaseState {
		private StringWriter myStringWriter;
		private XMLEventWriter myEventWriter;
		private XMLEventFactory myEventFactory;

		private XhtmlState() throws DataFormatException {
			try {
				XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();
				myStringWriter = new StringWriter();
				myEventWriter = xmlFactory.createXMLEventWriter(myStringWriter);
			} catch (XMLStreamException e) {
				throw new DataFormatException(e);
			}
		}

		@Override
		public void attributeValue(Attribute theAttr, String theValue) throws DataFormatException {
			try {
				myEventWriter.add(theAttr);
			} catch (XMLStreamException e) {
				throw new DataFormatException(e);
			}
		}

		@Override
		public void endingElement(EndElement theElement) throws DataFormatException {
			try {
				myEventWriter.add(theElement);
			} catch (XMLStreamException e) {
				throw new DataFormatException(e);
			}
		}

		@Override
		public void enteringNewElement(StartElement theElem, String theLocalPart) throws DataFormatException {
			// TODO Auto-generated method stub

		}
	}

	public void otherEvent(XMLEvent theEvent) {
		myState.otherEvent(theEvent);
	}

}
