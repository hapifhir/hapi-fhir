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
package ca.uhn.fhir.mdm.blocklist.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BlockListJson implements IModelJson {

	/**
	 * List of blocklistrules.
	 * Each item can be thought of as a 'ruleset'.
	 * These rulesets are applicable to a resource type.
	 * Each ruleset is applied as an 'or' to the resource being processed.
	 */
	@JsonProperty(value = "blocklist", required = true)
	private List<BlockListRuleJson> myBlockListItemJsonList;

	public List<BlockListRuleJson> getBlockListItemJsonList() {
		if (myBlockListItemJsonList == null) {
			myBlockListItemJsonList = new ArrayList<>();
		}
		return myBlockListItemJsonList;
	}

	public BlockListJson addBlockListRule(BlockListRuleJson theRule) {
		getBlockListItemJsonList().add(theRule);
		return this;
	}
}
