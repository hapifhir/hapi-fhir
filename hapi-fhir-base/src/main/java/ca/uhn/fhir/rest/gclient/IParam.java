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

public interface IParam {

	/**
	 * Returns the name of this parameter
	 */
	String getParamName();

	/**
	 * Sets the <code>:missing</code> qualifier for this parameter. Set this to <code>true</code>
	 * to indicate that the server should return resources with this value <p>populated</p>. Set this to
	 * <code>false</code> to indicate that the server should return resources with this value <b>missing</b>.
	 */
	ICriterion<?> isMissing(boolean theMissing);
	
}
