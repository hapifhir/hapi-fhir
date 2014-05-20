package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.api.Bundle;

public interface IQuery {

	IQuery where(ICriterion theCriterion);
	
	IQuery and(ICriterion theCriterion);
	
	Bundle execute();

	IQuery include(Include theIncludeManagingorganization);

	IQuery encodedJson();

	IQuery encodedXml();

	ISort sort();

	IQuery limitTo(int theLimitTo);

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This 
	 * can be useful for debugging, but is generally not desirable in a production situation.
	 */
	IQuery andLogRequestAndResponse(boolean theLogRequestAndResponse);

	IQuery prettyPrint();
	
}
