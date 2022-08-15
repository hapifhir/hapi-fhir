package ca.uhn.fhir.rest.server.interceptor.validation.address;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.hl7.fhir.instance.model.api.IBase;

import java.util.HashMap;
import java.util.Map;

public class AddressValidationResult {

	private boolean myIsValid;
	private String myValidatedAddressString;
	private Map<String, String> myValidationResults = new HashMap<>();
	private String myRawResponse;
	private IBase myValidatedAddress;

	public boolean isValid() {
		return myIsValid;
	}

	public void setValid(boolean theIsValid) {
		this.myIsValid = theIsValid;
	}

	public Map<String, String> getValidationResults() {
		return myValidationResults;
	}

	public void setValidationResults(Map<String, String> myValidationResults) {
		this.myValidationResults = myValidationResults;
	}

	public String getValidatedAddressString() {
		return myValidatedAddressString;
	}

	public void setValidatedAddressString(String theValidatedAddressString) {
		this.myValidatedAddressString = theValidatedAddressString;
	}

	public IBase getValidatedAddress() {
		return myValidatedAddress;
	}

	public void setValidatedAddress(IBase theValidatedAddress) {
		this.myValidatedAddress = theValidatedAddress;
	}

	public String getRawResponse() {
		return myRawResponse;
	}

	public void setRawResponse(String theRawResponse) {
		this.myRawResponse = theRawResponse;
	}

	@Override
	public String toString() {
		return
			"  isValid=" + myIsValid +
			", validatedAddressString='" + myValidatedAddressString + '\'' +
			", validationResults=" + myValidationResults + '\'' +
			", rawResponse='" + myRawResponse + '\'' +
			", myValidatedAddress='" + myValidatedAddress + '\'';
	}
}
