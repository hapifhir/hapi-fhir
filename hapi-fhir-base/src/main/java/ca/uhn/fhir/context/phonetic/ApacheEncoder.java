package ca.uhn.fhir.context.phonetic;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;

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
			// If the string contains a space, encode alpha parts separately so, for example, numbers are preserved in address lines.
			if (theString.contains(" ")) {
				return encodeStringWithSpaces(theString);
			}
			return myStringEncoder.encode(theString);
		} catch (EncoderException e) {
			ourLog.error("Failed to encode string " + theString, e);
			return theString;
		}
	}

	private String encodeStringWithSpaces(String theString) throws EncoderException {
		StringJoiner joiner = new StringJoiner(" ");

		// This sub-stack holds the alpha parts
		StringJoiner alphaJoiner = new StringJoiner(" ");

		for (String part : theString.split("[\\s\\W]+")) {
			if (StringUtils.isAlpha(part)) {
				alphaJoiner.add(part);
			} else {
				// Once we hit a non-alpha part, encode all the alpha parts together as a single string
				// This is to allow encoders like METAPHONE to match Hans Peter to Hanspeter
				alphaJoiner = encodeAlphaParts(joiner, alphaJoiner);
				joiner.add(part);
			}
		}
		encodeAlphaParts(joiner, alphaJoiner);

		return joiner.toString();
	}

	private StringJoiner encodeAlphaParts(StringJoiner theJoiner, StringJoiner theAlphaJoiner) throws EncoderException {
		// Encode the alpha parts as a single string and then flush the alpha encoder
		if (theAlphaJoiner.length() > 0) {
			theJoiner.add(myStringEncoder.encode(theAlphaJoiner.toString()));
			theAlphaJoiner = new StringJoiner(" ");
		}
		return theAlphaJoiner;
	}
}
