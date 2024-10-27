/*
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

	T expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequestDetails);

	T expand(T theSource, ValueSetExpansionOptions theOptions);

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

	T expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions);

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
