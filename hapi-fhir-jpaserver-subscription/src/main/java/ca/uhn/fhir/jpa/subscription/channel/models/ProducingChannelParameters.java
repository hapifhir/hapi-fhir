package ca.uhn.fhir.jpa.subscription.channel.models;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

public class ProducingChannelParameters extends BaseChannelParameters {

	/**
	 * Constructor
	 * <p>
	 * Producing channels are sending channels. They send data to topics/queues.
	 *
	 * @param theChannelName
	 */
	public ProducingChannelParameters(String theChannelName) {
		super(theChannelName);
	}

}
