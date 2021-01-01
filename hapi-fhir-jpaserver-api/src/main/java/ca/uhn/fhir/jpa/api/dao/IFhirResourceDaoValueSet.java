package ca.uhn.fhir.jpa.api.dao;

/*
 * #%L
 * HAPI FHIR JPA API
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public interface IFhirResourceDaoValueSet<T extends IBaseResource, CD, CC> extends IFhirResourceDao<T> {

	T expand(IIdType theId, String theFilter, RequestDetails theRequestDetails);

	T expand(IIdType theId, String theFilter, int theOffset, int theCount, RequestDetails theRequestDetails);

	T expand(T theSource, String theFilter);

	T expand(T theSource, String theFilter, int theOffset, int theCount);

	T expandByIdentifier(String theUri, String theFilter);

	T expandByIdentifier(String theUri, String theFilter, int theOffset, int theCount);

	void purgeCaches();

	IValidationSupport.CodeValidationResult validateCode(IPrimitiveType<String> theValueSetIdentifier, IIdType theId, IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, IPrimitiveType<String> theDisplay, CD theCoding, CC theCodeableConcept, RequestDetails theRequestDetails);

}
