package ca.uhn.fhir.jpa.subscription.module;


import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;

import java.util.List;

public interface IPointcutLatch {
	void clear();

	void setExpectedCount(int count);

	List<HookParams> awaitExpected() throws InterruptedException;
}
