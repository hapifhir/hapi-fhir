package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.IAnonymousLambdaHook;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PointcutLatch implements IAnonymousLambdaHook {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 10;
	private final String name;

	private CountDownLatch myCountdownLatch;
	private AtomicReference<String> myFailure;
	private AtomicReference<List<HookParams>> myCalledWith;

	public PointcutLatch(Pointcut thePointcut) {
		this.name = thePointcut.name();
	}

	public PointcutLatch(String theName) {
		this.name = theName;
	}

	public void setExpectedCount(int count) throws InterruptedException {
		if (myCountdownLatch != null) {
			throw new PointcutLatchException("setExpectedCount() called before previous awaitExpected() completed.");
		}
		createLatch(count);
	}

	private void createLatch(int count) {
		myFailure = new AtomicReference<>();
		myCalledWith = new AtomicReference<>(new ArrayList<>());
		myCountdownLatch = new CountDownLatch(count);
	}

	private void setFailure(String failure) {
		if (myFailure != null) {
			myFailure.set(failure);
		} else {
			throw new PointcutLatchException("trying to set failure on latch that hasn't been created: " + failure);
		}
	}

	private String getName() {
		return name + " " + this.getClass().getSimpleName();
	}

	public void awaitExpected() throws InterruptedException {
		awaitExpectedWithTimeout(DEFAULT_TIMEOUT_SECONDS);
	}

	public void awaitExpectedWithTimeout(int timeoutSecond) throws InterruptedException {
		try {
			assertNotNull(getName() + " awaitExpected() called before setExpected() called.", myCountdownLatch);
			assertTrue(getName() + " timed out waiting " + timeoutSecond + " seconds for latch to be triggered.", myCountdownLatch.await(timeoutSecond, TimeUnit.SECONDS));

			if (myFailure.get() != null) {
				String error = getName() + ": " + myFailure.get();
				error += "\nLatch called with values: " + myCalledWithString();
				throw new AssertionError(error);
			}
		} finally {
			destroyLatch();
		}
	}

	public void expectNothing() {
		destroyLatch();
	}

	private void destroyLatch() {
		myCountdownLatch = null;
	}

	private String myCalledWithString() {
		if (myCalledWith == null) {
			return "[]";
		}
		List<HookParams> calledWith = myCalledWith.get();
		if (calledWith.isEmpty()) {
			return "[]";
		}
		String retVal = "[ ";
		retVal += calledWith.stream().flatMap(hookParams -> hookParams.values().stream()).map(itemToString()).collect(Collectors.joining(", "));
		return retVal + " ]";
	}

	private static Function<Object, String> itemToString() {
		return object -> {
			if (object instanceof IBaseResource) {
				IBaseResource resource = (IBaseResource) object;
				return "Resource " + resource.getIdElement().getValue();
			} else if (object instanceof ResourceModifiedMessage) {
				ResourceModifiedMessage resourceModifiedMessage = (ResourceModifiedMessage)object;
				// FIXME KHS can we get the context from the payload?
				return "ResourceModified Message { " + resourceModifiedMessage.getOperationType() + ", " + resourceModifiedMessage.getNewPayload(FhirContext.forDstu3()).getIdElement().getValue() + "}";
			} else {
				return object.toString();
			}
		};
	}

	@Override
	public void invoke(HookParams theArgs) {
		if (myCountdownLatch == null) {
			throw new PointcutLatchException("countdown() called before setExpectedCount() called.", theArgs);
		} else if (myCountdownLatch.getCount() <= 0) {
			setFailure("countdown() called " + (1 - myCountdownLatch.getCount()) + " more times than expected.");
		}

		this.countdown();
		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
	}

	private void countdown() {
		ourLog.info("{} counting down {}", name, myCountdownLatch);
		myCountdownLatch.countDown();
	}

	private class PointcutLatchException extends IllegalStateException {
		public PointcutLatchException(String message, HookParams theArgs) {
			super(getName() + ": " + message + " called with values: " + hookParamsToString(theArgs));
		}

		public PointcutLatchException(String message) {
			super(getName() + ": " + message);
		}
	}

	private static String hookParamsToString(HookParams hookParams) {
		return hookParams.values().stream().map(itemToString()).collect(Collectors.joining(", "));
	}
}
