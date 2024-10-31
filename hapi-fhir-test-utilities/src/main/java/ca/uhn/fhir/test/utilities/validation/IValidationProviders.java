package ca.uhn.fhir.test.utilities.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseParameters;

public interface IValidationProviders {
	String CODE_SYSTEM = "http://code.system/url";
	String CODE_SYSTEM_VERSION = "1.0.0";
	String CODE_SYSTEM_NAME = "Test Code System";
	String CODE = "CODE";
	String VALUE_SET_URL = "http://value.set/url";
	String DISPLAY = "Explanation for code TestCode.";
	String LANGUAGE = "en";
	String ERROR_MESSAGE = "This is an error message";

	static String getInputKey(String theOperation, String theUrl, String theCode) {
		return theOperation + "-" + theUrl + "#" + theCode;
	}

	interface IMyCodeSystemProvider extends IResourceProvider {
		String getCode();
		String getSystem();
		String getDisplay();
		void setException(Exception theException);
		void addReturnParams(String theOperation, String theCodeSystemUrl, String theCode, IBaseParameters theParameters);
	}

	interface IMyLookupCodeProvider extends IResourceProvider {
		String getCode();
		String getSystem();
		void setLookupCodeResult(IValidationSupport.LookupCodeResult theLookupCodeResult);
	}

	interface IMyValueSetProvider extends IResourceProvider {
		String getCode();
		String getSystem();
		String getDisplay();
		String getValueSet();
		void setException(Exception theException);
		void addReturnParams(String theOperation, String theValueSetUrl, String theCode, IBaseParameters theReturnParams);
	}
}
