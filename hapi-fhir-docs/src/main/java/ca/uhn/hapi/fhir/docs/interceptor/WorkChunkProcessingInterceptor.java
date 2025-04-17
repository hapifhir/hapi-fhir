package ca.uhn.hapi.fhir.docs.interceptor;

// START SNIPPET: interceptor
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;
import ca.uhn.fhir.interceptor.api.Pointcut;

public class WorkChunkProcessingInterceptor {

	@Hook(Pointcut.BATCH2_CHUNK_PROCESS_FILTER)
	public IInterceptorFilterHook batch2ProcessFilter(JobInstance theJobInstance, WorkChunk theWorkChunk) {
		return theContinuation -> {
			try {
				// Perform pre-processing logic before the work chunk is processed

				// Process the work chunk (Note: If the continuation is not ran, an IllegalStateException will be
				// thrown)
				theContinuation.run();
			} catch (Exception e) {
				// Handle any exceptions that occur during work chunk processing

				// rethrow the exception
				throw e;
			} finally {
				// Perform any necessary cleanup or final operations
			}
		};
	}
}
// END SNIPPET: interceptor
