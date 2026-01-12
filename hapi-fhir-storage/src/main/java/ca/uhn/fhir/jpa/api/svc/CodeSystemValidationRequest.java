/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
 * Request object for validating a code against a CodeSystem.
 * Use the {@link #builder()} method to construct instances.
 *
 * @param codeSystemId The CodeSystem resource ID (optional, for instance-level operation)
 * @param codeSystemUrl The CodeSystem canonical URL
 * @param version The CodeSystem version
 * @param code The code to validate
 * @param display The display value to validate
 * @param coding A Coding containing system, code, and display
 * @param codeableConcept A CodeableConcept containing one or more codings
 * @param requestDetails The request details
 */
// Created by claude-opus-4-5-20251101
public record CodeSystemValidationRequest(
		@Nullable IIdType codeSystemId,
		@Nullable IPrimitiveType<String> codeSystemUrl,
		@Nullable IPrimitiveType<String> version,
		@Nullable IPrimitiveType<String> code,
		@Nullable IPrimitiveType<String> display,
		@Nullable IBaseCoding coding,
		@Nullable IBaseDatatype codeableConcept,
		@Nullable RequestDetails requestDetails) {

	/**
	 * Creates a new builder for constructing {@link CodeSystemValidationRequest} instances.
	 *
	 * @return a new builder instance
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for {@link CodeSystemValidationRequest}.
	 */
	public static class Builder {
		private IIdType myCodeSystemId;
		private IPrimitiveType<String> myCodeSystemUrl;
		private IPrimitiveType<String> myVersion;
		private IPrimitiveType<String> myCode;
		private IPrimitiveType<String> myDisplay;
		private IBaseCoding myCoding;
		private IBaseDatatype myCodeableConcept;
		private RequestDetails myRequestDetails;

		/**
		 * Sets the CodeSystem resource ID.
		 *
		 * @param theCodeSystemId the CodeSystem ID
		 * @return this builder
		 */
		public Builder codeSystemId(IIdType theCodeSystemId) {
			myCodeSystemId = theCodeSystemId;
			return this;
		}

		/**
		 * Sets the CodeSystem canonical URL.
		 *
		 * @param theCodeSystemUrl the CodeSystem URL
		 * @return this builder
		 */
		public Builder codeSystemUrl(IPrimitiveType<String> theCodeSystemUrl) {
			myCodeSystemUrl = theCodeSystemUrl;
			return this;
		}

		/**
		 * Sets the CodeSystem version.
		 *
		 * @param theVersion the CodeSystem version
		 * @return this builder
		 */
		public Builder version(IPrimitiveType<String> theVersion) {
			myVersion = theVersion;
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
		 * Builds the {@link CodeSystemValidationRequest}.
		 *
		 * @return the request
		 */
		public CodeSystemValidationRequest build() {
			return new CodeSystemValidationRequest(
					myCodeSystemId,
					myCodeSystemUrl,
					myVersion,
					myCode,
					myDisplay,
					myCoding,
					myCodeableConcept,
					myRequestDetails);
		}
	}
}
