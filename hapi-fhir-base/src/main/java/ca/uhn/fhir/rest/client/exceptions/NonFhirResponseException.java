package ca.uhn.fhir.rest.client.exceptions;

/*
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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.apache.commons.lang3.StringUtils.isBlank;

@CoverageIgnore
public class NonFhirResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param theMessage    The message
	 * @param theStatusCode The HTTP status code
	 */
	NonFhirResponseException(int theStatusCode, String theMessage) {
		super(theStatusCode, theMessage);
	}

	public static NonFhirResponseException newInstance(int theStatusCode, String theContentType, InputStream theInputStream) {
		return newInstance(theStatusCode, theContentType, new InputStreamReader(theInputStream, Charsets.UTF_8));
	}

	public static NonFhirResponseException newInstance(int theStatusCode, String theContentType, Reader theReader) {
		String responseBody = "";
		try {
			responseBody = IOUtils.toString(theReader);
		} catch (IOException e) {
			// ignore
		} finally {
			try {
				theReader.close();
			} catch (IOException theE) {
				// ignore
			}
		}

		NonFhirResponseException retVal;
		if (isBlank(theContentType)) {
			retVal = new NonFhirResponseException(theStatusCode, "Response contains no Content-Type");
		} else if (theContentType.contains("text")) {
			retVal = new NonFhirResponseException(theStatusCode, "Response contains non FHIR Content-Type '" + theContentType + "' : " + responseBody);
		} else {
			retVal = new NonFhirResponseException(theStatusCode, "Response contains non FHIR Content-Type '" + theContentType + "'");
		}

		retVal.setResponseBody(responseBody);
		return retVal;
	}

}
