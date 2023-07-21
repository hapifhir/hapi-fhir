/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.cr.repo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.ElementsSupportEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

import java.util.List;

public class CrSystemRequestDetails extends SystemRequestDetails {

	private final FhirContext fhirContext;
	private final IPagingProvider pagingProvider;

	public CrSystemRequestDetails(IInterceptorBroadcaster iInterceptorBroadcaster, IPagingProvider pagingProvider, FhirContext fhirContext) {
		super(iInterceptorBroadcaster);
		this.pagingProvider = pagingProvider;
		this.fhirContext = fhirContext;
		this.setFhirContext(fhirContext);
	}

	@Override
	public IRestfulServerDefaults getServer() {
		return new CrRestfulServerDefaults(this.fhirContext, this.pagingProvider);
	}

	private static class CrRestfulServerDefaults implements IRestfulServerDefaults {

		private final FhirContext fhirContext;
		private final IPagingProvider pagingProvider;

		public CrRestfulServerDefaults(FhirContext fhirContext, IPagingProvider pagingProvider) {
			this.fhirContext = fhirContext;
			this.pagingProvider = pagingProvider;
		}

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
			return fhirContext;
		}

		@Override
		public List<IServerInterceptor> getInterceptors_() {
			return null;
		}

		@Override
		public IPagingProvider getPagingProvider() {
			return pagingProvider;
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
}
