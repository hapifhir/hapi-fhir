package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

public enum HistoryCountModeEnum {

	/**
	 * Always include an accurate count in the response
	 */
	COUNT_ACCURATE,

	/**
	 * For history invocations with no offset (i.e. no since-date specified), always include a count in the response,
	 * but cache the count so that the count may be slightly out of date (but resource usage will be much lower). For
	 * history invocations with an offset, never return a count.
	 */
	CACHED_ONLY_WITHOUT_OFFSET,

	/**
	 * Do not include a count in history responses
	 */
	COUNT_DISABLED

}
