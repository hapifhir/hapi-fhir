package ca.uhn.fhir.rest.gclient;

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

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;

public interface IUpdateExecutable extends IClientExecutable<IUpdateExecutable, MethodOutcome>{

	/**
	 * Add a <code>Prefer</code> header to the request, which requests that the server include 
	 * or suppress the resource body as a part of the result. If a resource is returned by the server
	 * it will be parsed an accessible to the client via {@link MethodOutcome#getResource()}
	 * 
	 * @since HAPI 1.1
	 */
	IUpdateExecutable prefer(PreferReturnEnum theReturn);

}
