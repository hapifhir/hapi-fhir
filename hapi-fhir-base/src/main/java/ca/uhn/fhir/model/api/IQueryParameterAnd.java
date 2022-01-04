package ca.uhn.fhir.model.api;

import java.io.Serializable;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IQueryParameterAnd<T extends IQueryParameterOr<?>> extends Serializable {

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 * @param theContext TODO
	 * @param theParamName TODO
	 */
	void setValuesAsQueryTokens(FhirContext theContext, String theParamName, List<QualifiedParamList> theParameters) throws InvalidRequestException;

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	List<T> getValuesAsQueryTokens();

	
}
