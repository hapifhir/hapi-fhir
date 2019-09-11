package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.util.JpaConstants;

/**
 * @deprecated TerminologyUploaderProvider
 */
@Deprecated
public class BaseTerminologyUploaderProvider {

	// FIXME: remove these before 4.0.0
	public static final String UPLOAD_EXTERNAL_CODE_SYSTEM = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM;
	public static final String CONCEPT_COUNT = "conceptCount";
	public static final String TARGET = "target";
	public static final String SYSTEM = "system";
	public static final String PARENT_CODE = "parentCode";
	public static final String VALUE = "value";

}
