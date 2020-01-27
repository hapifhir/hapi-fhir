package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.rest.api.Constants;

import static org.apache.commons.lang3.StringUtils.left;

/*
 * #%L
 * HAPI FHIR Search Parameters
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

/**
 * Model of the _source parameter
 */
public class SourceParam {

	private static final long serialVersionUID = 1L;
	private final String myParameterValue;
	private final String mySourceUri;
	private final String myRequestId;

	public SourceParam(String theParameterValue) {
		myParameterValue = theParameterValue;
		String requestId;
		int lastHashValueIndex = theParameterValue.lastIndexOf('#');
		if (lastHashValueIndex == -1) {
			mySourceUri = theParameterValue;
			requestId = null;
		} else {
			if (lastHashValueIndex == 0) {
				mySourceUri = null;
			} else {
				mySourceUri = theParameterValue.substring(0, lastHashValueIndex);
			}
			requestId = theParameterValue.substring(lastHashValueIndex + 1);
		}
		myRequestId = left(requestId, Constants.REQUEST_ID_LENGTH);
	}

	public String getSourceUri() {
		return mySourceUri;
	}

	public String getRequestId() {
		return myRequestId;
	}
}
