package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

/**
 * Contract for standardizing textual primitives in the FHIR resources.
 */
public interface IStandardizer {

	/**
	 * Standardizes the specified string.
	 *
	 * @param theString String to be standardized
	 * @return Returns a standardized string.
	 */
	public String standardize(String theString);

}
