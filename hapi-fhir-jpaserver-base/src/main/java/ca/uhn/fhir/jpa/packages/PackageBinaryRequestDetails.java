package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;

public class PackageBinaryRequestDetails extends SystemRequestDetails {
	/**
	 * Constructor
	 *
	 */
	public PackageBinaryRequestDetails() {
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
