/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.support.GenericMessage;

import java.util.Map;

/**
 * A RawMessage is used to represent messages received from outside the system. The payload of these messages
 * is available as a string. There are no constraints on the contents of the String. It could be an HL7v2 message,
 * JSON, XML, CSV, etc.
 */
public class RawMessage extends GenericMessage<String> implements IMessage<String> {
	public RawMessage(String thePayload) {
		super(thePayload);
	}

	public RawMessage(String thePayload, Map<String, Object> theHeaders) {
		super(thePayload, theHeaders);
	}
}
