package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;

public abstract class RequestDetails {

	private byte[] myRequestContents;	
	private String myCompartmentName;
	private String myCompleteUrl;
	private String myFhirServerBase;
	private IdDt myId;
	private String myOperation;
	private Map<String, String[]> myParameters;
	private String myRequestPath;
	private RequestTypeEnum myRequestType;
	private String myResourceName;
	private boolean myRespondGzip;
	private RestOperationTypeEnum myRestOperationType;
	private String mySecondaryOperation;
	private Map<String, List<String>> myUnqualifiedToQualifiedNames;
	private IRestfulResponse myResponse;
	
	public String getCompartmentName() {
		return myCompartmentName;
	}

	public String getCompleteUrl() {
		return myCompleteUrl;
	}

	/**
	 * The fhir server base url, independant of the query being executed
	 * @return the fhir server base url
	 */
	public String getFhirServerBase() {
		return myFhirServerBase;
	}

	public IdDt getId() {
		return myId;
	}

	public String getOperation() {
		return myOperation;
	}

	public Map<String, String[]> getParameters() {
		return myParameters;
	}

	/**
	 * The part of the request URL that comes after the server base.
	 * <p>
	 * Will not contain a leading '/'
	 * </p>
	 */
	public String getRequestPath() {
		return myRequestPath;
	}

	public RequestTypeEnum getRequestType() {
		return myRequestType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public RestOperationTypeEnum getRestOperationType() {
		return myRestOperationType;
	}

	public String getSecondaryOperation() {
		return mySecondaryOperation;
	}

	public abstract IRestfulServerDefaults getServer();

	public Map<String, List<String>> getUnqualifiedToQualifiedNames() {
		return myUnqualifiedToQualifiedNames;
	}

	public boolean isRespondGzip() {
		return myRespondGzip;
	}

	public void setCompartmentName(String theCompartmentName) {
		myCompartmentName = theCompartmentName;
	}

	public void setCompleteUrl(String theCompleteUrl) {
		myCompleteUrl = theCompleteUrl;
	}

	public void setFhirServerBase(String theFhirServerBase) {
		myFhirServerBase = theFhirServerBase;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public void setParameters(Map<String, String[]> theParams) {
		myParameters = theParams;

		for (String next : theParams.keySet()) {
			for (int i = 0; i < next.length(); i++) {
				char nextChar = next.charAt(i);
				if (nextChar == ':' || nextChar == '.') {
					if (myUnqualifiedToQualifiedNames == null) {
						myUnqualifiedToQualifiedNames = new HashMap<String, List<String>>();
					}
					String unqualified = next.substring(0, i);
					List<String> list = myUnqualifiedToQualifiedNames.get(unqualified);
					if (list == null) {
						list = new ArrayList<String>(4);
						myUnqualifiedToQualifiedNames.put(unqualified, list);
					}
					list.add(next);
					break;
				}
			}
		}

		if (myUnqualifiedToQualifiedNames == null) {
			myUnqualifiedToQualifiedNames = Collections.emptyMap();
		}

	}

	public void setRequestPath(String theRequestPath) {
		assert theRequestPath.length() == 0 || theRequestPath.charAt(0) != '/';
		myRequestPath = theRequestPath;
	}

	public void setRequestType(RequestTypeEnum theRequestType) {
		myRequestType = theRequestType;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public void setRespondGzip(boolean theRespondGzip) {
		myRespondGzip = theRespondGzip;
	}

	public void setRestOperationType(RestOperationTypeEnum theRestOperationType) {
		myRestOperationType = theRestOperationType;
	}

	public void setSecondaryOperation(String theSecondaryOperation) {
		mySecondaryOperation = theSecondaryOperation;
	}

    public IRestfulResponse getResponse() {
        return myResponse;
    }

    public void setResponse(IRestfulResponse theResponse) {
        this.myResponse = theResponse;
    }

    public abstract String getHeader(String name);
    
	public final byte[] loadRequestContents(RequestDetails theRequest) {
		if (myRequestContents == null) {
			myRequestContents = getByteStreamRequestContents();
		}
		return myRequestContents;
	}

	protected abstract byte[] getByteStreamRequestContents();    

    public abstract List<String> getHeaders(String name);

    /**
     * Retrieves the body of the request as character data using
     * a <code>BufferedReader</code>.  The reader translates the character
     * data according to the character encoding used on the body.
     * Either this method or {@link #getInputStream} may be called to read the
     * body, not both.
     * 
     * @return a <code>Reader</code> containing the body of the request 
     *
     * @exception UnsupportedEncodingException  if the character set encoding
     * used is not supported and the text cannot be decoded
     *
     * @exception IllegalStateException if {@link #getInputStream} method
     * has been called on this request
     *
     * @exception IOException if an input or output exception occurred
     *
     * @see javax.servlet.http.HttpServletRequest#getInputStream
     */    
    public abstract Reader getReader() throws IOException;

    /**
     * Retrieves the body of the request as binary data.  
     * Either this method or {@link #getReader} may be called to 
     * read the body, not both.
     *
     * @return a {@link InputStream} object containing
     * the body of the request
     *
     * @exception IllegalStateException if the {@link #getReader} method
     * has already been called for this request
     *
     * @exception IOException if an input or output exception occurred
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Returns the server base URL (with no trailing '/') for a given request
     */    
    public abstract String getServerBaseForRequest();

}
