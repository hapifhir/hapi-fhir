package ca.uhn.fhir.rest.client.api;

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

/**
 * Represents an HTTP header field.
 */
public class Header {
	
	public final String myName;
	public final String myValue;

    public Header(String myName, String myValue) {
		this.myName = myName;
		this.myValue = myValue;
	}

	/**
     * Get the name of the Header.
     *
     * @return the name of the Header,  never {@code null}
     */
    public String getName() {
    	return myName;
    }

    /**
     * Get the value of the Header.
     *
     * @return the value of the Header,  may be {@code null}
     */
    public String getValue() {
    	return myValue;
    }

}
