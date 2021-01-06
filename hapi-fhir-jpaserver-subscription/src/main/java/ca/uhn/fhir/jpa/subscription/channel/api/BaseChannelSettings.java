package ca.uhn.fhir.jpa.subscription.channel.api;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

public abstract class BaseChannelSettings implements IChannelSettings {
	private boolean myQualifyChannelName = true;

	/**
	 * Default true.  Used by IChannelNamer to decide how to qualify the channel name.
	 */
	@Override
	public boolean isQualifyChannelName() {
		return myQualifyChannelName;
	}

	/**
	 * Default true.  Used by IChannelNamer to decide how to qualify the channel name.
	 */
	public void setQualifyChannelName(boolean theQualifyChannelName) {
		myQualifyChannelName = theQualifyChannelName;
	}
}
