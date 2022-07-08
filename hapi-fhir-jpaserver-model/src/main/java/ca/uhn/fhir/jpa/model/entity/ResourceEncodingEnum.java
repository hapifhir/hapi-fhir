package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR JPA Model
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

/**
 * @see ResourceHistoryTable#ENCODING_COL_LENGTH
 */
public enum ResourceEncodingEnum {

	/*
	 * NB: Constants in this enum must be 5 chars long or less!!!
	 *
	 * See ResourceHistoryTable RES_ENCODING column
	 */

	/**
	 * Json
	 */
	JSON,

	/**
	 * Json Compressed
	 */
	JSONC,

	/**
	 * Resource was deleted - No contents expected
	 */
	DEL;

	public IParser newParser(FhirContext theContext) {
		return theContext.newJsonParser();
	}

}
