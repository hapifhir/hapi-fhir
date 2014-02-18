package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;

class ParserState {

	private FhirContext myContext;
	private BaseState myState;

	public ParserState(FhirContext theContext) {
		myContext=theContext;
	}

	public static ParserState getResourceInstance(FhirContext theContext, String theLocalPart) throws DataFormatException {
		BaseRuntimeElementDefinition<?> definition = theContext.getNameToElementDefinition().get(theLocalPart);
		if (!(definition instanceof RuntimeResourceDefinition)) {
			throw new DataFormatException("Element '" + theLocalPart + "' is not a resource, expected a resource at this position");
		}
		
		RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
		IResource instance = def.newInstance();
		
		ParserState retVal = new ParserState(theContext);
		retVal.setState(retVal.new ResourceParserState(def, instance));
		
		return retVal;
	}

	private void setState(BaseState theState) {
		myState = theState;
	}

	private abstract class BaseState
	{
		private BaseState myStack;
		
		public abstract void enteringNewElement(String theLocalPart) throws DataFormatException;
		
	}
	
	private class ResourceParserState extends BaseState
	{

		private RuntimeResourceDefinition myResourceDefinition;
		private IResource myInstance;

		public ResourceParserState(RuntimeResourceDefinition theDef, IResource theInstance) {
			myResourceDefinition = theDef;
			myInstance = theInstance;
		}

		@Override
		public void enteringNewElement(String theChildName) throws DataFormatException {
//			BaseRuntimeChildDefinition child = myResourceDefinition.getChildByNameOrThrowDataFormatException(theChildName);
//			switch (child.getChildType()) {
//			case CHOICE:
//				break;
//			case COMPOSITE:
//				break;
//			case PRIMITIVE:
//				break;
//			case RESOURCE:
//				break;
//			default:
//				break;
//			
//			}
		}
		
	}

	public void enteringNewElement(String theLocalPart) throws DataFormatException {
		myState.enteringNewElement(theLocalPart);
	}
	
}
