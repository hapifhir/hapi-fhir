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

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IReadExecutable<T extends IBaseResource> extends IClientExecutable<IReadExecutable<T>, T>{

	/**
	 * Send an "If-None-Match" header containing <code>theVersion</code>, which requests
	 * that the server return an "HTTP 301 Not Modified" if the newest version of the resource
	 * on the server has the same version as the version ID specified by <code>theVersion</code>.
	 * In this case, the client operation will perform the linked operation.
	 *
	 * @param theVersion The version ID (e.g. "123")
	 */
	IReadIfNoneMatch<T> ifVersionMatches(String theVersion);

}
