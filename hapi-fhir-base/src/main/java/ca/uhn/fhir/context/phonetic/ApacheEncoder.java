package ca.uhn.fhir.context.phonetic;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheEncoder implements IPhoneticEncoder {
	private static final Logger ourLog = LoggerFactory.getLogger(ApacheEncoder.class);

	private final String myName;
	private final StringEncoder myStringEncoder;

	public ApacheEncoder(String theName, StringEncoder theStringEncoder) {
		myName = theName;
		myStringEncoder = theStringEncoder;
	}

	@Override
	public String name() {
		return myName;
	}

	@Override
	public String encode(String theString) {
		try {
			return myStringEncoder.encode(theString);
		} catch (EncoderException e) {
			ourLog.error("Failed to encode string " + theString, e);
			return theString;
		}
	}
}
