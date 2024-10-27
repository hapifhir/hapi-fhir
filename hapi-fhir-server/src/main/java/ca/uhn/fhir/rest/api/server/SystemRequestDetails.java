/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.ElementsSupportEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * A default RequestDetails implementation that can be used for system calls to
 * Resource DAO methods when partitioning is enabled. Using a SystemRequestDetails
 * instance for system calls will ensure that any resource queries or updates will
 * use the DEFAULT partition when partitioning is enabled.
 */
public class SystemRequestDetails extends RequestDetails {
	private FhirContext myFhirContext;
	private ListMultimap<String, String> myHeaders;
	/**
	 * If a SystemRequestDetails has a RequestPartitionId, it will take precedence over the tenantId
	 */
	private RequestPartitionId myRequestPartitionId;

	private IRestfulServerDefaults myServer = new MyRestfulServerDefaults();

	public SystemRequestDetails() {
		this(new MyInterceptorBroadcaster());
	}

	public SystemRequestDetails(IInterceptorBroadcaster theInterceptorBroadcaster) {
		super(theInterceptorBroadcaster);
	}

	public SystemRequestDetails(RequestDetails theDetails) {
		super(theDetails);
		if (nonNull(theDetails.getServer())) {
			myServer = theDetails.getServer();
			myFhirContext = theDetails.getFhirContext();
		}
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public SystemRequestDetails setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
		return this;
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		return new byte[0];
	}

	@Override
	public Charset getCharset() {
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public String getHeader(String name) {
		List<String> headers = getHeaders(name);
		if (headers.isEmpty()) {
			return null;
		} else {
			return headers.get(0);
		}
	}

	@Override
	public List<String> getHeaders(String name) {
		ListMultimap<String, String> headers = myHeaders;
		if (headers == null) {
			headers = ImmutableListMultimap.of();
		}
		return headers.get(name);
	}

	@Override
	public void addHeader(String theName, String theValue) {
		initHeaderMap();
		myHeaders.put(theName, theValue);
	}

	@Override
	public void setHeaders(String theName, List<String> theValues) {
		initHeaderMap();
		myHeaders.putAll(theName, theValues);
	}

	private void initHeaderMap() {
		if (myHeaders == null) {
			// Make sure we are case-insensitive on keys
			myHeaders = MultimapBuilder.treeKeys(String.CASE_INSENSITIVE_ORDER)
					.arrayListValues()
					.build();
		}
	}

	@Override
	public Object getAttribute(String theAttributeName) {
		return null;
	}

	@Override
	public void setAttribute(String theAttributeName, Object theAttributeValue) {}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public Reader getReader() {
		return null;
	}

	@Override
	public IRestfulServerDefaults getServer() {
		return myServer;
	}

	public void setServer(RestfulServer theServer) {
		this.myServer = theServer;
	}

	@Override
	public String getServerBaseForRequest() {
		return null;
	}

	private static class MyRestfulServerDefaults implements IRestfulServerDefaults {

		@Override
		public AddProfileTagEnum getAddProfileTag() {
			return null;
		}

		@Override
		public EncodingEnum getDefaultResponseEncoding() {
			return null;
		}

		@Override
		public ETagSupportEnum getETagSupport() {
			return null;
		}

		@Override
		public ElementsSupportEnum getElementsSupport() {
			return null;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public List<IServerInterceptor> getInterceptors_() {
			return null;
		}

		@Override
		public IPagingProvider getPagingProvider() {
			return null;
		}

		@Override
		public boolean isDefaultPrettyPrint() {
			return false;
		}

		@Override
		public IInterceptorService getInterceptorService() {
			return null;
		}
	}

	private static class MyInterceptorBroadcaster implements IInterceptorBroadcaster {

		@Override
		public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
			return true;
		}

		@Override
		public Object callHooksAndReturnObject(Pointcut thePointcut, HookParams theParams) {
			return null;
		}

		@Override
		public boolean hasHooks(Pointcut thePointcut) {
			return false;
		}
	}

	public static SystemRequestDetails forAllPartitions() {
		return new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.allPartitions());
	}

	public static SystemRequestDetails newSystemRequestAllPartitions() {
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(RequestPartitionId.allPartitions());
		return systemRequestDetails;
	}
}
