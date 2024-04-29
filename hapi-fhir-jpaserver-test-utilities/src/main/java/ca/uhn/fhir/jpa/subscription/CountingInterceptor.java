/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.ArrayList;
import java.util.List;

public class CountingInterceptor implements ChannelInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(CountingInterceptor.class);
	private List<String> mySent = new ArrayList<>();

	public int getSentCount(String theContainingKeyword) {
		return (int)
				mySent.stream().filter(t -> t.contains(theContainingKeyword)).count();
	}

	@Override
	public void afterSendCompletion(
			Message<?> theMessage, MessageChannel theChannel, boolean theSent, Exception theException) {
		ourLog.info("Send complete for message: {}", theMessage);
		if (theSent) {
			mySent.add(theMessage.toString());
		}
	}

	@Override
	public String toString() {
		return "[" + String.join("\n", mySent) + "]";
	}
}
