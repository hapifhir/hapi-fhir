/*
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public interface IFhirResourceDaoValueSet<T extends IBaseResource> extends IFhirResourceDao<T> {

	/**
	 * Expands the stored value set with the given id.
	 *
	 * @param theId the id of the value set to expand
	 * @param theOptions the expansion options
	 * @param theRequestDetails the request
	 * @return the value set, with its expansion
	 * @throws ca.uhn.fhir.rest.server.exceptions.InternalErrorException if no validation support module can expand
	 *                                                                    the value set
	 * @throws ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException if the expansion fails
	 */
	T expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequestDetails);

	/**
	 * Expands the given value set, which need not be stored.
	 *
	 * @param theSource the value set to expand
	 * @param theOptions the expansion options
	 * @return the value set, with its expansion
	 * @throws ca.uhn.fhir.rest.server.exceptions.InternalErrorException if no validation support module can expand
	 *                                                                    the value set
	 * @throws ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException if the expansion fails
	 */
	T expand(T theSource, ValueSetExpansionOptions theOptions);

	/**
	 * Expands a value set, as the <code>ValueSet/$expand</code> operation does. Exactly one of {@literal theId},
	 * {@literal theValueSet} and {@literal theUrl} identifies the value set.
	 *
	 * @param theId the id of the value set to expand, or null
	 * @param theValueSet the value set to expand, or null
	 * @param theUrl the canonical url of the value set to expand, or null
	 * @param theValueSetVersion the version of the value set named by {@literal theUrl}, or null for whichever version
	 *                           is current
	 * @param theFilter the text filter to apply to the concepts, or null
	 * @param theContext the context of the autocomplete extension, or null
	 * @param theContextDirection the context direction of the autocomplete extension, or null; with
	 *                            {@literal theContext}, <code>existing</code> asks for the autocomplete extension
	 * @param theOffset the index of the first concept to return, or null
	 * @param theCount the number of concepts to return, or null
	 * @param theDisplayLanguage the language for the displays, or null
	 * @param theIncludeHierarchy whether to return the concepts as a hierarchy, or null
	 * @param theRequestDetails the request
	 * @return the value set, with its expansion
	 * @throws ca.uhn.fhir.rest.server.exceptions.InvalidRequestException if not exactly one of {@literal theId},
	 *                                                                     {@literal theValueSet} and {@literal theUrl}
	 *                                                                     is given, or the autocomplete extension is
	 *                                                                     asked for without fulltext search
	 * @throws ca.uhn.fhir.rest.server.exceptions.InternalErrorException if no validation support module can expand
	 *                                                                    the value set
	 * @throws ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException if the expansion fails
	 */
	T expand(
			IIdType theId,
			T theValueSet,
			IPrimitiveType<String> theUrl,
			IPrimitiveType<String> theValueSetVersion,
			IPrimitiveType<String> theFilter,
			IPrimitiveType<String> theContext,
			IPrimitiveType<String> theContextDirection,
			IPrimitiveType<Integer> theOffset,
			IPrimitiveType<Integer> theCount,
			IPrimitiveType<String> theDisplayLanguage,
			IPrimitiveType<Boolean> theIncludeHierarchy,
			RequestDetails theRequestDetails);

	/**
	 * Expands the value set with the given canonical url.
	 *
	 * @param theUri the canonical url of the value set, which may carry a version as <code>url|version</code>
	 * @param theOptions the expansion options
	 * @return the value set, with its expansion
	 * @throws ca.uhn.fhir.rest.server.exceptions.InternalErrorException if no validation support module can expand
	 *                                                                    the value set
	 * @throws ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException if the expansion fails
	 */
	T expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions);

	/**
	 * Validates a code against a value set, as the <code>ValueSet/$validate-code</code> operation does. Exactly one
	 * of a code with {@literal theSystem}, {@literal theCoding} and {@literal theCodeableConcept} is validated.
	 *
	 * @param theValueSetIdentifier the canonical url of the value set, which may carry a version as
	 *                              <code>url|version</code>, or null when {@literal theId} is given
	 * @param theId the id of the value set to validate against, or null when {@literal theValueSetIdentifier} is
	 *              given
	 * @param theCode the code to validate, given together with {@literal theSystem}
	 * @param theSystem the code system url, which may carry a version as <code>url|version</code>; that version also
	 *                  applies to {@literal theCoding}, and to the codings in {@literal theCodeableConcept}, of that
	 *                  code system which have none
	 * @param theDisplay the display to validate, or null
	 * @param theCoding the coding to validate, given instead of {@literal theCode} and {@literal theSystem}
	 * @param theCodeableConcept the concept to validate, given instead of a code or coding; it is valid if any of its
	 *                           codings is
	 * @param theRequestDetails the request
	 * @return the outcome of the validation
	 * @throws ca.uhn.fhir.rest.server.exceptions.InvalidRequestException if not exactly one of a code, coding and
	 *                                                                     codeable concept is given, no value set is
	 *                                                                     identified, or a coding of the code system in
	 *                                                                     {@literal theSystem} names another version
	 */
	IValidationSupport.CodeValidationResult validateCode(
			IPrimitiveType<String> theValueSetIdentifier,
			IIdType theId,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails);
}
