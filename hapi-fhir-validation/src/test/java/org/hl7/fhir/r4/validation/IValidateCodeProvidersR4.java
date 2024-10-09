package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.IValidationProviders;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

public interface IValidateCodeProvidersR4 {

	@SuppressWarnings("unused")
	class MyCodeSystemProviderR4 implements IValidationProviders.IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private StringType myDisplay;
		private Exception myException;
		private Parameters myReturnParams;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
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
			return myReturnParams;
		}

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
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
				@OperationParam(name = "version", max = 1) StringType ignoredTheVersion,
				@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
				@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
				RequestDetails theRequestDetails
		) throws Exception {
			mySystemUrl = theSystem;
			myCode = theCode;
			if (myException != null) {
				throw myException;
			}
			return myReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		public void setException(Exception theException) {
			myException = theException;
		}
		@Override
		public void setReturnParams(IBaseParameters theParameters) {
			myReturnParams = (Parameters) theParameters;
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
	class MyValueSetProviderR4 implements IValidationProviders.IMyValueSetProvider {
		private Exception myException;
		private Parameters myReturnParams;
		private UriType mySystemUrl;
		private UriType myValueSetUrl;
		private CodeType myCode;
		private StringType myDisplay;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
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
			return myReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}
		public void setException(Exception theException) {
			myException = theException;
		}
		@Override
		public void setReturnParams(IBaseParameters theParameters) {
			myReturnParams = (Parameters) theParameters;
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
