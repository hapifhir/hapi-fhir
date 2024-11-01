package ca.uhn.fhir.test.utilities.validation;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.UriParam;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.getInputKey;

public interface IValidationProvidersDstu3 {
	@SuppressWarnings("unused")
	class MyCodeSystemProviderDstu3 implements IValidationProviders.IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private StringType myDisplay;
		private Exception myException;
		private final Map<String, Parameters> myReturnParamMap = new HashMap<>();
		private final Map<String, CodeSystem> myReturnCodeSystemMap = new HashMap<>();

		@Operation(name = "$validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = BooleanType.class, min = 1),
				@OperationParam(name = "message", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
				@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
				@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) throws Exception {
			mySystemUrl = theCodeSystemUrl;
			myCode = theCode;
			myDisplay = theDisplay;
			if (myException != null) {
				throw myException;
			}
			String codeSystemUrl = theCodeSystemUrl != null ? theCodeSystemUrl.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			String inputKey = getInputKey("$validate-code", codeSystemUrl, code);
			Parameters params = myReturnParamMap.get(inputKey);
			if (params == null) {
				throw new IllegalStateException("Test setup incomplete. Missing return params for " + inputKey);
			}
			return params;
		}

		@Operation(name = "$lookup", idempotent = true, returnParameters= {
				@OperationParam(name = "name", type = StringType.class, min = 1),
				@OperationParam(name = "version", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class, min = 1),
				@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
				@OperationParam(name = "property", type = StringType.class, min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
				HttpServletRequest theServletRequest,
				@OperationParam(name = "code", max = 1) CodeType theCode,
				@OperationParam(name = "system",max = 1) UriType theSystem,
				@OperationParam(name = "coding", max = 1) Coding theCoding,
				@OperationParam(name = "version", max = 1) StringType theVersion,
				@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
				@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
				RequestDetails theRequestDetails
		) throws Exception {
			mySystemUrl = theSystem;
			myCode = theCode;
			if (myException != null) {
				throw myException;
			}
			String codeSystemUrl = theSystem != null ? theSystem.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			String inputKey = getInputKey("$lookup", codeSystemUrl, code);
			Parameters params = myReturnParamMap.get(inputKey);
			if (params == null) {
				throw new IllegalStateException("Test setup incomplete. Missing return params for " + inputKey);
			}
			return params;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			if (theUrlParam.isEmpty()) {
				throw new IllegalStateException("CodeSystem url should not be null.");
			}
			String urlValue = theUrlParam.getValue();
			if (!myReturnCodeSystemMap.containsKey(urlValue)) {
				throw new IllegalStateException("Test setup incomplete. CodeSystem not found " + urlValue);
			}
			return List.of(myReturnCodeSystemMap.get(urlValue));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		public void setException(Exception theException) {
			myException = theException;
		}
		@Override
		public void addReturnParams(String theOperation, String theUrl, String theCode, IBaseParameters theParameters) {
			myReturnParamMap.put(getInputKey(theOperation, theUrl, theCode), (Parameters) theParameters);
		}
		public void addReturnCodeSystem(CodeSystem theCodeSystem) {
			myReturnCodeSystemMap.put(theCodeSystem.getUrl(), theCodeSystem);
		}
		@Override
		public String getCode() {
			return myCode != null ? myCode.getValueAsString() : null;
		}
		@Override
		public String getSystem() {
			return mySystemUrl != null ? mySystemUrl.getValueAsString() : null;
		}
		public String getDisplay() {
			return myDisplay != null ? myDisplay.getValue() : null;
		}
	}

	@SuppressWarnings("unused")
	class MyValueSetProviderDstu3 implements IValidationProviders.IMyValueSetProvider {
		private UriType mySystemUrl;
		private UriType myValueSetUrl;
		private CodeType myCode;
		private StringType myDisplay;
		private Exception myException;
		private final Map<String, Parameters> myReturnParamMap = new HashMap<>();
		private final Map<String, ValueSet> myReturnValueSetMap = new HashMap<>();

		@Operation(name = "$validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = BooleanType.class, min = 1),
				@OperationParam(name = "message", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
				@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
				@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
				@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
				@OperationParam(name = "valueSet") ValueSet theValueSet
		) throws Exception {
			mySystemUrl = theSystem;
			myValueSetUrl = theValueSetUrl;
			myCode = theCode;
			myDisplay = theDisplay;
			if (myException != null) {
				throw myException;
			}
			String valueSetUrl = theValueSetUrl != null ? theValueSetUrl.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			String inputKey = getInputKey("$validate-code", valueSetUrl, code);
			Parameters params = myReturnParamMap.get(inputKey);
			if (params == null) {
				throw new IllegalStateException("Test setup incomplete. Missing return params for " + inputKey);
			}
			return params;
		}

		@Search
		public List<ValueSet> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			if (theUrlParam.isEmpty()) {
				throw new IllegalStateException("ValueSet url should not be null.");
			}
			String urlValue = theUrlParam.getValue();
			if (!myReturnValueSetMap.containsKey(urlValue)) {
				throw new IllegalStateException("Test setup incomplete. ValueSet not found " + urlValue);
			}
			return List.of(myReturnValueSetMap.get(urlValue));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}
		public void setException(Exception theException) {
			myException = theException;
		}
		@Override
		public void addReturnParams(String theOperation, String theUrl, String theCode, IBaseParameters theReturnParams) {
			myReturnParamMap.put(getInputKey(theOperation, theUrl, theCode), (Parameters) theReturnParams);
		}
		public void addReturnValueSet(ValueSet theValueSet) {
			myReturnValueSetMap.put(theValueSet.getUrl(), theValueSet);
		}
		@Override
		public String getCode() {
			return myCode != null ? myCode.getValueAsString() : null;
		}
		@Override
		public String getSystem() {
			return mySystemUrl != null ? mySystemUrl.getValueAsString() : null;
		}
		@Override
		public String getValueSet() {
			return myValueSetUrl != null ? myValueSetUrl.getValueAsString() : null;
		}
		public String getDisplay() {
			return myDisplay != null ? myDisplay.getValue() : null;
		}
	}
}
