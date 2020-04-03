package ca.uhn.fhir.jpa.api;

import com.google.common.annotations.VisibleForTesting;

public interface IEmpiInterceptor {
	void start();

	@VisibleForTesting
	void stopForUnitTest();
}
