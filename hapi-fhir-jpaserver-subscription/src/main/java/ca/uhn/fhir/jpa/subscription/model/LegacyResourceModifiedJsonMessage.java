package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LegacyResourceModifiedJsonMessage extends BaseJsonMessage<LegacyResourceModifiedMessage> {

	@JsonProperty("payload")
	private LegacyResourceModifiedMessage myPayload;

	/**
	 * Constructor
	 */
	public LegacyResourceModifiedJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public LegacyResourceModifiedJsonMessage(LegacyResourceModifiedMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public LegacyResourceModifiedMessage getPayload() {
		return myPayload;
	}

	public void setPayload(LegacyResourceModifiedMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}
}
