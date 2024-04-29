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

public class BlockListRuleJson implements IModelJson {
	/**
	 * The resource type that this block list rule applies to.
	 */
	@JsonProperty(value = "resourceType", required = true)
	private String myResourceType;

	/**
	 * The list of blocked fields that this rule applies to.
	 */
	@JsonProperty(value = "fields", required = true)
	private List<BlockedFieldJson> myBlockedFields;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public List<BlockedFieldJson> getBlockedFields() {
		if (myBlockedFields == null) {
			myBlockedFields = new ArrayList<>();
		}
		return myBlockedFields;
	}

	public BlockedFieldJson addBlockListField() {
		BlockedFieldJson rule = new BlockedFieldJson();
		getBlockedFields().add(rule);
		return rule;
	}
}
