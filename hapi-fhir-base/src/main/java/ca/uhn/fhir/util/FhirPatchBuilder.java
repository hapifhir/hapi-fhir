package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;

public class FhirPatchBuilder {

	private final FhirContext myContext;
	private final IBaseParameters myPatch;

	public FhirPatchBuilder(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myContext = theFhirContext;
		myPatch = ParametersUtil.newInstance(myContext);
	}


	public AddOperationStep1 add() {
		return new AddOperationStep1();
	}



	public IBaseParameters build() {
		return myPatch;
	}

	public class AddOperationStep1 implements IAddStep1 {

		/**
		 * The path to the element which will have an element added to it.
		 */
		public AddOperationStep2 path(String thePath) {
			Validate.notBlank(thePath, "thePath must not be blank");
			return new AddOperationStep2(thePath);
		}


	}

	public class AddOperationStep2 implements IAddStep2 {

		private String myPath;
		private String myName;
		private IBase myValue;

		private AddOperationStep2(String thePath) {
			myPath = thePath;
		}

		/**
		 * The name of the element to add
		 */
		public AddOperationStep3 name(String theName) {
			Validate.notBlank(theName, "theName must not be blank");
			myName = theName;
			return new AddOperationStep3(myPath, theName);
		}

	}

	public class AddOperationStep3 implements IAddStep3 {
		private final String myPath;
		private final String myName;

		private AddOperationStep3(String thePath, String theName) {
			myPath = thePath;
			myName = theName;
		}

		public void value(@Nonnull IBase theValue) {
			Validate.notNull(theValue, "theValue must not be null");

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "add");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);
			ParametersUtil.addPartString(myContext, operation, "name", myName);
			ParametersUtil.addPart(myContext, operation, "value", theValue);
		}

	}

	private interface IAddStep1 {

	}

	private interface IAddStep2 {

	}

	private interface IAddStep3 {

	}


}
