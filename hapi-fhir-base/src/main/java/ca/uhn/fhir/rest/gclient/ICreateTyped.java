package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

public interface ICreateTyped extends IClientExecutable<ICreateTyped, MethodOutcome> {
	
	/**
	 * If you want the explicitly state an ID for your created resource, put that ID here. You generally do not
	 * need to invoke this method, so that the server will assign the ID itself.
	 */
	ICreateTyped withId(String theId);

	/**
	 * If you want the explicitly state an ID for your created resource, put that ID here. You generally do not
	 * need to invoke this method, so that the server will assign the ID itself.
	 */
	ICreateTyped withId(IdDt theId);

}
