/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.api;

/**
 * Which of the two MDM rules documents a match is scored with. See {@link IMdmSettings#getMdmRules(MdmRuleSetEnum)}.
 */
public enum MdmRuleSetEnum {
	/**
	 * The rules from {@link IMdmSettings#getMdmRules()}, used wherever the result decides identity: it creates or
	 * changes MDM links, or merges resources that are already stored.
	 */
	MATCH_AND_LINK,
	/**
	 * The rules from {@link IMdmSettings#getMatchOnlyMdmRules()}, used by callers whose results never create or
	 * change MDM links: the read-only {@code $match} and {@code $mdm-match} operations, and callers that drop or merge an
	 * incoming resource that has not been stored yet.
	 */
	MATCH_ONLY
}
