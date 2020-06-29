package ca.uhn.fhir.empi.rules.metric.matcher;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

public class StringEncoderMatcher implements IEmpiStringMatcher {
	private static final Logger ourLog = LoggerFactory.getLogger(StringEncoderMatcher.class);

	private final StringEncoder myStringEncoder;

	public StringEncoderMatcher(StringEncoder theStringEncoder) {
		myStringEncoder = theStringEncoder;
	}

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		try {
			return myStringEncoder.encode(theLeftString).equals(myStringEncoder.encode(theRightString));
		} catch (EncoderException e) {
			ourLog.error("Failed to match strings '{}' and '{}' using encoder {}", theLeftString, theRightString, myStringEncoder.getClass().getName(), e);
		}
		return false;
	}
}
