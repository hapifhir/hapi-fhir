package ca.uhn.fhir.rest.client.interceptor;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import java.io.IOException;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * HTTP interceptor to be used for adding HTTP headers containing user identifying info for auditing purposes to the request
 */
public class UserInfoInterceptor implements IClientInterceptor {
	
	public static final String HEADER_USER_ID = "fhir-user-id";
	public static final String HEADER_USER_NAME = "fhir-user-name";	
	public static final String HEADER_APPLICATION_NAME = "fhir-app-name";
	
	private String myUserId;
	private String myUserName;		
	private String myAppName;
	
    public UserInfoInterceptor(String theUserId, String theUserName, String theAppName) {
		super();
		myUserId = theUserId;
		myUserName = theUserName;		
		myAppName = theAppName;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		if(myUserId != null) theRequest.addHeader(HEADER_USER_ID, myUserId);
		if(myUserName != null) theRequest.addHeader(HEADER_USER_NAME, myUserName);		
		if(myAppName != null) theRequest.addHeader(HEADER_APPLICATION_NAME, myAppName);
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

	

}
