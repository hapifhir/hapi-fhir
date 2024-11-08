package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.IValidationProviders;
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

import java.util.List;

public interface IValidateCodeProvidersDstu3 {
	@SuppressWarnings("unused")
	class MyCodeSystemProviderDstu3 implements IValidationProviders.IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private StringType myDisplay;
		private Exception myException;
		private Parameters myReturnParams;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = org.hl7.fhir.dstu3.model.BooleanType.class, min = 1),
				@OperationParam(name = "message", type = org.hl7.fhir.dstu3.model.StringType.class),
				@OperationParam(name = "display", type = org.hl7.fhir.dstu3.model.StringType.class)
		})
		public org.hl7.fhir.dstu3.model.Parameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) org.hl7.fhir.dstu3.model.IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) org.hl7.fhir.dstu3.model.UriType theCodeSystemUrl,
				@OperationParam(name = "code", min = 0, max = 1) org.hl7.fhir.dstu3.model.CodeType theCode,
				@OperationParam(name = "display", min = 0, max = 1) org.hl7.fhir.dstu3.model.StringType theDisplay
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
				@OperationParam(name = "name", type = org.hl7.fhir.dstu3.model.StringType.class, min = 1),
				@OperationParam(name = "version", type = org.hl7.fhir.dstu3.model.StringType.class),
				@OperationParam(name = "display", type = org.hl7.fhir.dstu3.model.StringType.class, min = 1),
				@OperationParam(name = "abstract", type = org.hl7.fhir.dstu3.model.BooleanType.class, min = 1),
				@OperationParam(name = "property", type = org.hl7.fhir.dstu3.model.StringType.class, min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
				HttpServletRequest theServletRequest,
				@OperationParam(name = "code", max = 1) org.hl7.fhir.dstu3.model.CodeType theCode,
				@OperationParam(name = "system",max = 1) org.hl7.fhir.dstu3.model.UriType theSystem,
				@OperationParam(name = "coding", max = 1) Coding theCoding,
				@OperationParam(name = "version", max = 1) org.hl7.fhir.dstu3.model.StringType theVersion,
				@OperationParam(name = "displayLanguage", max = 1) org.hl7.fhir.dstu3.model.CodeType theDisplayLanguage,
				@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<org.hl7.fhir.dstu3.model.CodeType> thePropertyNames,
				RequestDetails theRequestDetails
		) {
			myCode = theCode;
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
	class MyValueSetProviderDstu3 implements IValidationProviders.IMyValueSetProvider {
		private Exception myException;
		private Parameters myReturnParams;
		private UriType mySystemUrl;
		private UriType myValueSetUrl;
		private CodeType myCode;
		private StringType myDisplay;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = BooleanType.class, min = 1),
				@OperationParam(name = "message", type = org.hl7.fhir.dstu3.model.StringType.class),
				@OperationParam(name = "display", type = org.hl7.fhir.dstu3.model.StringType.class)
		})
		public Parameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) org.hl7.fhir.dstu3.model.UriType theValueSetUrl,
				@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
				@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
				@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
				@OperationParam(name = "valueSet") org.hl7.fhir.dstu3.model.ValueSet theValueSet
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
