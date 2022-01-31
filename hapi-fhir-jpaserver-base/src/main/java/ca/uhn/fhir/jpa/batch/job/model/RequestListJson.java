package ca.uhn.fhir.jpa.batch.job.model;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Serialize a list of URLs and partition ids so Spring Batch can store it as a String
 */
public class RequestListJson implements IModelJson {
	static final ObjectMapper ourObjectMapper = new ObjectMapper();

	@JsonProperty("partitionedUrls")
	private List<PartitionedUrl> myPartitionedUrls;

	public static RequestListJson fromUrlStringsAndRequestPartitionIds(List<String> theUrls, List<RequestPartitionId> theRequestPartitionIds) {
		assert theUrls.size() == theRequestPartitionIds.size();

		RequestListJson retval = new RequestListJson();
		List<PartitionedUrl> partitionedUrls = new ArrayList<>();
		for (int i = 0; i < theUrls.size(); ++i) {
			partitionedUrls.add(new PartitionedUrl(theUrls.get(i), theRequestPartitionIds.get(i)));
		}
		retval.setPartitionedUrls(partitionedUrls);
		return retval;
	}

	public static RequestListJson fromJson(String theJson) {
		try {
			return ourObjectMapper.readValue(theJson, RequestListJson.class);
		} catch (JsonProcessingException e) {
			throw new InternalErrorException(Msg.code(1283) + "Failed to decode " + RequestListJson.class);
		}
	}

	public String toJson() {
		return JsonUtil.serializeOrInvalidRequest(this);
	}

	@Override
	public String toString() {
		return "RequestListJson{" +
			"myPartitionedUrls=" + myPartitionedUrls +
			'}';
	}

	public List<PartitionedUrl> getPartitionedUrls() {
		return myPartitionedUrls;
	}

	public void setPartitionedUrls(List<PartitionedUrl> thePartitionedUrls) {
		myPartitionedUrls = thePartitionedUrls;
	}
}
