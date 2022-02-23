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

import org.hl7.fhir.instance.model.api.IBaseParameters;

public interface IPatch {

	/**
	 * The body of the patch document serialized in either XML or JSON which conforms to
	 * http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261
	 *
	 * @param thePatchBody The body of the patch
	 */
	IPatchWithBody withBody(String thePatchBody);

	/**
	 * The body of the patch document using FHIR Patch syntax as described at
	 * http://hl7.org/fhir/fhirpatch.html
	 *
	 * @since 5.1.0
	 */
	IPatchWithBody withFhirPatch(IBaseParameters thePatchBody);
}
