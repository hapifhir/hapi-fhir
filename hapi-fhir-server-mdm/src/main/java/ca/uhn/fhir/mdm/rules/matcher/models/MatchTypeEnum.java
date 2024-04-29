/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.matcher.models;

/**
 * Enum for holding all the known FHIR Element matchers that we support in HAPI.  The string matchers first
 * encode the string using an Apache Encoder before comparing them.
 * https://commons.apache.org/proper/commons-codec/userguide.html
 */
public enum MatchTypeEnum {
	CAVERPHONE1,
	CAVERPHONE2,
	COLOGNE,
	DOUBLE_METAPHONE,
	MATCH_RATING_APPROACH,
	METAPHONE,
	NYSIIS,
	REFINED_SOUNDEX,
	SOUNDEX,
	NICKNAME,

	STRING,
	SUBSTRING,

	DATE,
	NAME_ANY_ORDER,
	NAME_FIRST_AND_LAST,

	IDENTIFIER,

	EMPTY_FIELD,
	EXTENSION_ANY_ORDER,
	NUMERIC;
}
