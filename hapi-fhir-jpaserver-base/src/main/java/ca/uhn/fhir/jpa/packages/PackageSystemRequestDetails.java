package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;

/**
 * A RequestDetails implementation to be used when processing resources defined in a package to ensure
 * that they are always queried or updated in the DEFAULT partition if partitioning is enabled.
 */
public class PackageSystemRequestDetails extends SystemRequestDetails {
	public PackageSystemRequestDetails() {
		super(new MyInterceptorBroadcaster());
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

}
