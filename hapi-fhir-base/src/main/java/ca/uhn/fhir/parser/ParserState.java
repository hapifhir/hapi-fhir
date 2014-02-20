package ca.uhn.fhir.parser;

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

	private enum ResourceReferenceSubState {
		INITIAL, REFERENCE, DISPLAY
	}

	private class ResourceReferenceState extends BaseState {

		private ResourceReferenceSubState mySubState;
		private RuntimeResourceReferenceDefinition myDefinition;
		private ResourceReference myInstance;

		public ResourceReferenceState(RuntimeResourceReferenceDefinition theDefinition, ResourceReference theInstance) {
			myDefinition = theDefinition;
			myInstance = theInstance;
			mySubState = ResourceReferenceSubState.INITIAL;
		}

		@Override
		public void attributeValue(String theValue) throws DataFormatException {
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
		public void enteringNewElement(String theLocalPart) throws DataFormatException {
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
		public void endingElement(String theLocalPart) {
			switch (mySubState) {
			case INITIAL:
				pop();
				break;
			case DISPLAY:
			case REFERENCE:
				mySubState = ResourceReferenceSubState.INITIAL;
			}
		}

	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);
	private FhirContext myContext;

	private BaseState myState;
	private Object myObject;

	public ParserState(FhirContext theContext) {
		myContext = theContext;
	}

	public void attributeValue(String theValue) throws DataFormatException {
		myState.attributeValue(theValue);
	}

	public void enteringNewElement(String theLocalPart) throws DataFormatException {
		myState.enteringNewElement(theLocalPart);
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

		public abstract void attributeValue(String theValue) throws DataFormatException;

		public abstract void enteringNewElement(String theLocalPart) throws DataFormatException;

		public void setStack(BaseState theState) {
			myStack = theState;
		}

		public abstract void endingElement(String theLocalPart);

	}

	private class ContainerState extends BaseState {

		private BaseRuntimeElementCompositeDefinition<?> myDefinition;
		private ICompositeElement myInstance;

		public ContainerState(BaseRuntimeElementCompositeDefinition<?> theDef, ICompositeElement theInstance) {
			myDefinition = theDef;
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theValue) {
			ourLog.debug("Ignoring attribute value: {}", theValue);
		}

		@Override
		public void enteringNewElement(String theChildName) throws DataFormatException {
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
			case RESOURCE:
				// Throw an exception because this shouldn't happen here
				break;
			}

			throw new DataFormatException("Illegal resource position: " + target.getChildType());
		}

		@Override
		public void endingElement(String theLocalPart) {
			pop();
			if (myState == null) {
				myObject = myInstance;
			}
		}

	}

	private class PrimitiveState extends BaseState {
		private IPrimitiveDatatype<?> myInstance;

		public PrimitiveState(IPrimitiveDatatype<?> theInstance) {
			super();
			myInstance = theInstance;
		}

		@Override
		public void attributeValue(String theValue) throws DataFormatException {
			myInstance.setValueAsString(theValue);
		}

		@Override
		public void enteringNewElement(String theLocalPart) throws DataFormatException {
			throw new Error("?? can this happen?"); // TODO: can this happen?
		}

		@Override
		public void endingElement(String theLocalPart) {
			pop();
		}

	}

	public Object getObject() {
		return myObject;
	}

	private void pop() {
		myState = myState.myStack;
	}

	public boolean isComplete() {
		return myObject != null;
	}

	public void endingElement(String theLocalPart) {
		myState.endingElement(theLocalPart);
	}

}
