/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

/**
 * Request object for validating a code against a ValueSet.
 * Use the {@link #builder()} method to construct instances.
 *
 * @param valueSetId The ValueSet resource ID (optional, for instance-level operation)
 * @param valueSetUrl The ValueSet canonical URL
 * @param valueSetVersion The ValueSet version
 * @param code The code to validate
 * @param system The code system URL
 * @param systemVersion The code system version
 * @param display The display value to validate
 * @param coding A Coding containing system, code, and display
 * @param codeableConcept A CodeableConcept containing one or more codings
 * @param requestDetails The request details
 */
// Created by claude-opus-4-5-20251101
public record ValueSetValidationRequest(
		@Nullable IIdType valueSetId,
		@Nullable IPrimitiveType<String> valueSetUrl,
		@Nullable IPrimitiveType<String> valueSetVersion,
		@Nullable IPrimitiveType<String> code,
		@Nullable IPrimitiveType<String> system,
		@Nullable IPrimitiveType<String> systemVersion,
		@Nullable IPrimitiveType<String> display,
		@Nullable IBaseCoding coding,
		@Nullable IBaseDatatype codeableConcept,
		@Nullable RequestDetails requestDetails) {

	/**
	 * Creates a new builder for constructing {@link ValueSetValidationRequest} instances.
	 *
	 * @return a new builder instance
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for {@link ValueSetValidationRequest}.
	 */
	public static class Builder {
		private IIdType myValueSetId;
		private IPrimitiveType<String> myValueSetUrl;
		private IPrimitiveType<String> myValueSetVersion;
		private IPrimitiveType<String> myCode;
		private IPrimitiveType<String> mySystem;
		private IPrimitiveType<String> mySystemVersion;
		private IPrimitiveType<String> myDisplay;
		private IBaseCoding myCoding;
		private IBaseDatatype myCodeableConcept;
		private RequestDetails myRequestDetails;

		/**
		 * Sets the ValueSet resource ID.
		 *
		 * @param theValueSetId the ValueSet ID
		 * @return this builder
		 */
		public Builder valueSetId(IIdType theValueSetId) {
			myValueSetId = theValueSetId;
			return this;
		}

		/**
		 * Sets the ValueSet canonical URL.
		 *
		 * @param theValueSetUrl the ValueSet URL
		 * @return this builder
		 */
		public Builder valueSetUrl(IPrimitiveType<String> theValueSetUrl) {
			myValueSetUrl = theValueSetUrl;
			return this;
		}

		/**
		 * Sets the ValueSet version.
		 *
		 * @param theValueSetVersion the ValueSet version
		 * @return this builder
		 */
		public Builder valueSetVersion(IPrimitiveType<String> theValueSetVersion) {
			myValueSetVersion = theValueSetVersion;
			return this;
		}

		/**
		 * Sets the code to validate.
		 *
		 * @param theCode the code
		 * @return this builder
		 */
		public Builder code(IPrimitiveType<String> theCode) {
			myCode = theCode;
			return this;
		}

		/**
		 * Sets the code system URL.
		 *
		 * @param theSystem the system URL
		 * @return this builder
		 */
		public Builder system(IPrimitiveType<String> theSystem) {
			mySystem = theSystem;
			return this;
		}

		/**
		 * Sets the code system version.
		 *
		 * @param theSystemVersion the system version
		 * @return this builder
		 */
		public Builder systemVersion(IPrimitiveType<String> theSystemVersion) {
			mySystemVersion = theSystemVersion;
			return this;
		}

		/**
		 * Sets the display value to validate.
		 *
		 * @param theDisplay the display
		 * @return this builder
		 */
		public Builder display(IPrimitiveType<String> theDisplay) {
			myDisplay = theDisplay;
			return this;
		}

		/**
		 * Sets the Coding containing system, code, and display.
		 *
		 * @param theCoding the coding
		 * @return this builder
		 */
		public Builder coding(IBaseCoding theCoding) {
			myCoding = theCoding;
			return this;
		}

		/**
		 * Sets the CodeableConcept containing one or more codings.
		 *
		 * @param theCodeableConcept the codeableConcept
		 * @return this builder
		 */
		public Builder codeableConcept(IBaseDatatype theCodeableConcept) {
			myCodeableConcept = theCodeableConcept;
			return this;
		}

		/**
		 * Sets the request details.
		 *
		 * @param theRequestDetails the request details
		 * @return this builder
		 */
		public Builder requestDetails(RequestDetails theRequestDetails) {
			myRequestDetails = theRequestDetails;
			return this;
		}

		/**
		 * Builds the {@link ValueSetValidationRequest}.
		 *
		 * @return the request
		 */
		public ValueSetValidationRequest build() {
			return new ValueSetValidationRequest(
					myValueSetId,
					myValueSetUrl,
					myValueSetVersion,
					myCode,
					mySystem,
					mySystemVersion,
					myDisplay,
					myCoding,
					myCodeableConcept,
					myRequestDetails);
		}
	}
}
